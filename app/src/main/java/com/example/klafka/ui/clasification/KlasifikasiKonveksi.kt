package com.example.klafka.ui.clasification

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.klafka.R
import com.example.klafka.databinding.FragmentKlasifikasiKonveksiBinding
import com.example.klafka.helper.KonveksiDatabaseHelper
import com.example.klafka.model.KonveksiBatch
import com.example.klafka.ui.home.MainActivity
import kotlinx.coroutines.*
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.*

class KlasifikasiKonveksiFragment : Fragment() {

    private var _binding: FragmentKlasifikasiKonveksiBinding? = null
    private val binding get() = _binding!!

    private val selectedImageUris = mutableListOf<Uri>()
    private val maxImages = 100

    private val pickImagesLauncher = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        if (uris != null) {
            if (uris.size != getInputJumlah()) {
                Toast.makeText(requireContext(), "Jumlah gambar tidak sesuai dengan input!", Toast.LENGTH_SHORT).show()
                return@registerForActivityResult
            }
            selectedImageUris.clear()
            selectedImageUris.addAll(uris)
            binding.imagePreviewKonveksi.setImageURI(selectedImageUris.first())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentKlasifikasiKonveksiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as? MainActivity)?.showBottomNav(false)
        setupSpinner()

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnAmbilGambar.setOnClickListener {
            val jumlah = getInputJumlah()
            if (jumlah <= 0 || jumlah > maxImages) {
                Toast.makeText(requireContext(), "Masukkan jumlah (1-$maxImages)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            pickImagesLauncher.launch("image/*")
        }

        binding.btnProsesKonveksi.setOnClickListener {
            val jumlah = getInputJumlah()
            if (selectedImageUris.size != jumlah) {
                Toast.makeText(requireContext(), "Jumlah gambar belum sesuai!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            showLoading(true)

            lifecycleScope.launch(Dispatchers.IO) {
                val result = runKlasifikasiBatch()
                withContext(Dispatchers.Main) {
                    showLoading(false)
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
                    findNavController().navigate(
                        R.id.konveksiFragment,
                        null,
                        androidx.navigation.NavOptions.Builder()
                            .setPopUpTo(R.id.klasifikasiFragment, true)
                            .build()
                    )
                }
            }
        }
    }

    private fun setupSpinner() {
        val jenisKainList = listOf("Cotton Combed", "TC", "CVC", "Cotton Stretch", "Polyester")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, jenisKainList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerJenisKain.adapter = adapter
    }

    private fun getInputJumlah(): Int {
        val input = binding.inputJumlahGambar.text.toString()
        return input.toIntOrNull() ?: 0
    }

    private fun showLoading(show: Boolean) {
        binding.loadingOverlay.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnProsesKonveksi.isEnabled = !show
        binding.btnAmbilGambar.isEnabled = !show
        binding.inputJumlahGambar.isEnabled = !show
        binding.spinnerJenisKain.isEnabled = !show
    }

    private suspend fun runKlasifikasiBatch(): KlasifikasiResult {
        val context = requireContext()

        val expectedLabel = when (binding.spinnerJenisKain.selectedItem.toString().lowercase(Locale.getDefault())) {
            "cotton combed" -> "combed"
            "tc" -> "tc"
            "cvc" -> "cvc"
            "cotton stretch" -> "cs"
            "polyester" -> "polyester"
            else -> ""
        }

        val interpreter = Interpreter(loadModelFile("klafka_resnet_model.tflite", context))
        var matchCount = 0

        for (uri in selectedImageUris) {
            val predicted = runModelOnImage(uri, interpreter)
            if (predicted.equals(expectedLabel, ignoreCase = true)) {
                matchCount++
            }
        }

        val total = selectedImageUris.size
        val percentage = (matchCount * 100) / total
        val batch = KonveksiBatch(
            id = 0,
            namaJenisKain = binding.spinnerJenisKain.selectedItem.toString(),
            jumlahTotal = total,
            jumlahSesuai = matchCount,
            jumlahTidakSesuai = total - matchCount,
            akurasi = "$percentage%",
            tanggal = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
        )

        KonveksiDatabaseHelper(context).insertBatch(batch)

        return KlasifikasiResult(matchCount, total, percentage)
    }

    private fun runModelOnImage(uri: Uri, interpreter: Interpreter): String {
        val bitmap = BitmapFactory.decodeStream(requireContext().contentResolver.openInputStream(uri))
        val resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true)

        val input = Array(1) { Array(224) { Array(224) { FloatArray(3) } } }
        for (y in 0 until 224) {
            for (x in 0 until 224) {
                val pixel = resized.getPixel(x, y)
                input[0][y][x][0] = Color.red(pixel).toFloat()
                input[0][y][x][1] = Color.green(pixel).toFloat()
                input[0][y][x][2] = Color.blue(pixel).toFloat()
            }
        }

        val output = Array(1) { FloatArray(5) }
        interpreter.run(input, output)
        val labels = arrayOf("cs", "combed", "cvc", "tc", "polyester")
        val maxIdx = output[0].indices.maxByOrNull { output[0][it] } ?: 0
        return labels[maxIdx]
    }

    private fun loadModelFile(name: String, context: Context): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(name)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, fileDescriptor.startOffset, fileDescriptor.declaredLength)
    }

    override fun onDestroyView() {
        _binding = null
        (activity as? MainActivity)?.showBottomNav(true)
        super.onDestroyView()
    }

    data class KlasifikasiResult(val cocok: Int, val total: Int, val persentase: Int) {
        val message: String
            get() = "Klasifikasi selesai!\nCocok: $cocok dari $total gambar ($persentase%)"
    }
}

