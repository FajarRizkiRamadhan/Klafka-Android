package com.example.klafka.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.klafka.R
import com.example.klafka.model.JenisKain

class JenisKainAdapter(
    private val listKain: List<JenisKain>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<JenisKainAdapter.KainViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(kain: JenisKain)
    }

    inner class KainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgKain: ImageView = itemView.findViewById(R.id.imgKain)
        private val tvNama: TextView = itemView.findViewById(R.id.tvNamaKain)
        private val tvDeskripsi: TextView = itemView.findViewById(R.id.tvDeskripsiKain)

        fun bind(kain: JenisKain) {
            imgKain.setImageResource(kain.gambarResId)
            tvNama.text = kain.nama
            tvDeskripsi.text = kain.deskripsi

            itemView.setOnClickListener {
                listener.onItemClick(kain)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KainViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jenis_kain, parent, false)
        return KainViewHolder(view)
    }

    override fun onBindViewHolder(holder: KainViewHolder, position: Int) {
        holder.bind(listKain[position])
    }

    override fun getItemCount(): Int = listKain.size
}
