package com.example.klafka.ui.clasification

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.klafka.R
import com.example.klafka.databinding.FragmentKlasifikasiLangsungBinding
import com.example.klafka.helper.HistoryDatabaseHelper
import com.example.klafka.model.History
import com.example.klafka.ui.home.MainActivity
import org.tensorflow.lite.Interpreter
import java.io.*
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.*

class KlasifikasiLangsungFragment : Fragment() {

    private var _binding: FragmentKlasifikasiLangsungBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageUri: Uri
    private lateinit var photoFile: File
    private val classLabels = arrayOf(
        "Kain Cotton Stretch (CS)",        // CS
        "Kain Katun Combed",               // Combed
        "Kain CVC (Chief Value Cotton)",   // CVC
        "Kain TC (Tetoron Cotton)",        // TC
        "Kain Polyester"                   // Polyester
    )
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val inputStream = requireContext().contentResolver.openInputStream(it)
            val tempFile = File.createTempFile("gallery_", ".jpg", requireContext().cacheDir)
            inputStream?.copyTo(FileOutputStream(tempFile))
            inputStream?.close()
            imageUri = Uri.fromFile(tempFile)
            binding.imageView.setImageURI(imageUri)
        }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            binding.imageView.setImageURI(imageUri)
        }
    }

    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) openCamera()
        else Toast.makeText(requireContext(), "Izin kamera diperlukan", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentKlasifikasiLangsungBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.showBottomNav(false)

        binding.btnKamera.setOnClickListener {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        binding.btnGaleri.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.btnPindai.setOnClickListener {
            if (::imageUri.isInitialized && imageUri.toString().isNotEmpty()) {
                runLocalModel(imageUri, requireContext())
            } else {
                Toast.makeText(requireContext(), "Silakan pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun openCamera() {
        try {
            photoFile = File.createTempFile("photo_", ".jpg", requireContext().cacheDir)
            imageUri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.fileprovider", photoFile)
            cameraLauncher.launch(imageUri)
        } catch (e: IOException) {
            Toast.makeText(requireContext(), "Gagal membuka kamera", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun runLocalModel(uri: Uri, context: Context) {
        val model = Interpreter(loadModelFile("klafka_resnet_model.tflite", context))
        val inputStream = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)

        val input = Array(1) { Array(224) { Array(224) { FloatArray(3) } } }
        for (y in 0 until 224) {
            for (x in 0 until 224) {
                val pixel = resizedBitmap.getPixel(x, y)
                input[0][y][x][0] = Color.red(pixel).toFloat()
                input[0][y][x][1] = Color.green(pixel).toFloat()
                input[0][y][x][2] = Color.blue(pixel).toFloat()
            }
        }

        val output = Array(1) { FloatArray(classLabels.size) }
        model.run(input, output)

        val predictedIndex = output[0].indices.maxByOrNull { output[0][it] } ?: -1
        val prediction = classLabels[predictedIndex]
        val tanggal = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())

        // Simpan ke SQLite
        val dbHelper = HistoryDatabaseHelper(context)
        val success = dbHelper.insertHistory(
            History(
                id = 0,
                jenisKlasifikasi = "Langsung",
                namaJenisKain = prediction,
                tanggal = tanggal,
                imagePath = uri.toString()
            )
        )

        if (success) {
            Toast.makeText(context, "Klasifikasi berhasil: $prediction", Toast.LENGTH_LONG).show()

            val bundle = Bundle().apply {
                putString("jenisKlasifikasi", "Langsung")
                putString("namaJenisKain", prediction)
                putString("tanggal", tanggal)
                putString("imagePath", uri.toString())
            }

            findNavController().navigate(R.id.historyDetailFragment, bundle)
        } else {
            Toast.makeText(context, "Gagal menyimpan hasil klasifikasi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadModelFile(modelName: String, context: Context): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelName)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, fileDescriptor.startOffset, fileDescriptor.declaredLength)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.showBottomNav(true)
        _binding = null
    }
}
