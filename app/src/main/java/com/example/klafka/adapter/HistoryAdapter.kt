package com.example.klafka.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.klafka.R
import com.example.klafka.model.History
import com.squareup.picasso.Picasso

class HistoryAdapter(
    private val context: Context,
    private var historyList: List<History>,
    private val onItemClick: (History) -> Unit,
    private val onDeleteClick: (History) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount(): Int = historyList.size

    fun updateData(newList: List<History>) {
        historyList = newList
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageHistory)
        private val tvJenisKlasifikasi: TextView = itemView.findViewById(R.id.tvJenisKlasifikasi)
        private val tvNamaJenisKain: TextView = itemView.findViewById(R.id.tvNamaJenisKain)
        private val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)

        fun bind(history: History) {
            tvJenisKlasifikasi.text = history.jenisKlasifikasi
            tvNamaJenisKain.text = history.namaJenisKain
            tvTanggal.text = history.tanggal

            // Gunakan Picasso agar lebih ringan dan handling error
            if (history.imagePath.isNotEmpty()) {
                Picasso.get()
                    .load(Uri.parse(history.imagePath))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(imageView)
            } else {
                imageView.setImageResource(R.drawable.placeholder)
            }

            itemView.setOnClickListener {
                onItemClick(history)
            }

            btnDelete.setOnClickListener {
                onDeleteClick(history)
            }

            // Tambahkan contentDescription untuk aksesibilitas
            btnDelete.contentDescription = "Hapus riwayat ${history.namaJenisKain} tanggal ${history.tanggal}"
        }
    }
}
