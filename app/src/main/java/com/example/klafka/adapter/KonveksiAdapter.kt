package com.example.klafka.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.klafka.R
import com.example.klafka.model.KonveksiBatch

class KonveksiAdapter(
    private var list: List<KonveksiBatch>,
    private var onDeleteClick: (KonveksiBatch) -> Unit
) : RecyclerView.Adapter<KonveksiAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama = itemView.findViewById<TextView>(R.id.tvNamaJenisKain)
        val tvTanggal = itemView.findViewById<TextView>(R.id.tvTanggal)
        val tvAkurasi = itemView.findViewById<TextView>(R.id.tvAkurasi)
        val tvJumlahSesuai = itemView.findViewById<TextView>(R.id.tvJumlahSesuai)
        val tvJumlahTidakSesuai = itemView.findViewById<TextView>(R.id.tvJumlahTidakSesuai)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_batch_konveksi, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvNama.text = item.namaJenisKain
        holder.tvTanggal.text = item.tanggal
        holder.tvAkurasi.text = "Akurasi: ${item.akurasi}"
        holder.tvJumlahSesuai.text = "✔ ${item.jumlahSesuai} Sesuai"
        holder.tvJumlahTidakSesuai.text = "✖ ${item.jumlahTidakSesuai} Tidak Sesuai"
        holder.btnDelete.setOnClickListener {
            onDeleteClick(item)
        }

    }

    fun update(newList: List<KonveksiBatch>) {
        list = newList
        notifyDataSetChanged()
    }
}



//class KonveksiAdapter(
//    private var list: List<KonveksiBatch>
//) : RecyclerView.Adapter<KonveksiAdapter.ViewHolder>() {
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val tvNama = itemView.findViewById<TextView>(R.id.tvNamaJenisKain)
//        val tvTanggal = itemView.findViewById<TextView>(R.id.tvTanggal)
//        val tvAkurasi = itemView.findViewById<TextView>(R.id.tvAkurasi)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_batch_konveksi, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun getItemCount(): Int = list.size
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = list[position]
//        holder.tvNama.text = item.namaJenisKain
//        holder.tvTanggal.text = item.tanggal
//        holder.tvAkurasi.text = "Akurasi: ${item.akurasi}"
//    }
//
//    fun update(newList: List<KonveksiBatch>) {
//        list = newList
//        notifyDataSetChanged()
//    }
//}
