package com.example.klafka.repository

import com.example.klafka.R
import com.example.klafka.model.JenisKain

object JenisKainRepository {
    val classList = listOf(
        JenisKain(
            nama = "Kain Katun Combed",
            deskripsi = "Katun combed adalah kain berbahan 100% kapas alami yang disisir secara khusus menggunakan mesin combing untuk menghilangkan serat-serat pendek. Proses ini menghasilkan kain yang lebih halus, lembut, kuat, dan memiliki permukaan yang rata serta nyaman di kulit.",
            kegunaan = "Cocok digunakan untuk kaos distro, baju casual, dan pakaian anak-anak.",
            karakteristik = "Lembut, adem, menyerap keringat dengan baik, tidak mudah berbulu, dan terasa nyaman dipakai seharian.",
            perawatan = "Cuci dengan air dingin, hindari pemutih, setrika suhu sedang, dan keringkan di tempat teduh.",
            gambarResId = R.drawable.kain_katuncombed
        ),
        JenisKain(
            nama = "Kain TC (Tetoron Cotton)",
            deskripsi = "Kain TC adalah hasil campuran antara serat polyester (Tetoron) dan cotton dengan komposisi umum 65% polyester dan 35% cotton. Campuran ini menghasilkan kain yang kuat dan tidak mudah kusut, meski sedikit kurang dalam menyerap keringat.",
            kegunaan = "Biasa digunakan untuk seragam sekolah, kemeja kerja, dan pakaian promosi karena harganya yang ekonomis dan awet.",
            karakteristik = "Agak panas, tidak mudah kusut, tidak menyusut saat dicuci, dan tahan lama.",
            perawatan = "Cuci dengan air biasa, tidak perlu disetrika terlalu panas, jemur di tempat teduh untuk menjaga warna.",
            gambarResId = R.drawable.kain_tc
        ),
        JenisKain(
            nama = "Kain Cotton Stretch (CS)",
            deskripsi = "Cotton Stretch adalah jenis kain katun yang dicampur dengan sedikit bahan elastane (spandex/lycra), membuat kain ini lentur dan fleksibel. Teksturnya tetap halus seperti katun murni namun lebih elastis.",
            kegunaan = "Ideal untuk pakaian aktif seperti legging, celana olahraga, atau pakaian wanita yang membutuhkan elastisitas.",
            karakteristik = "Melar, fleksibel, adem, nyaman digunakan untuk aktivitas harian maupun olahraga ringan.",
            perawatan = "Cuci manual agar tidak merusak elastisitas, hindari air panas dan pemutih, serta jemur tidak langsung di bawah sinar matahari.",
            gambarResId = R.drawable.kain_cs
        ),
        JenisKain(
            nama = "Kain CVC (Chief Value Cotton)",
            deskripsi = "CVC adalah campuran antara cotton dan polyester dengan kandungan cotton lebih tinggi (umumnya 55% cotton dan 45% polyester). Kombinasi ini menjaga kenyamanan katun dan kekuatan polyester.",
            kegunaan = "Sering digunakan untuk kaos polo, kaos promosi, dan seragam kerja karena nyaman namun tetap tahan lama.",
            karakteristik = "Lebih adem dari TC, menyerap keringat lebih baik, tidak mudah kusut, dan awet.",
            perawatan = "Gunakan deterjen ringan, setrika suhu rendah, dan jemur di tempat teduh.",
            gambarResId = R.drawable.kain_cvc
        ),
        JenisKain(
            nama = "Kain Polyester",
            deskripsi = "Polyester adalah kain berbahan dasar serat sintetis buatan manusia dari minyak bumi. Kain ini terkenal karena kekuatannya, daya tahannya terhadap air, dan tidak mudah kusut meskipun terasa panas saat dikenakan.",
            kegunaan = "Digunakan untuk jas hujan, tas, seragam lapangan, jaket outdoor, dan tekstil rumah tangga.",
            karakteristik = "Tahan air, kuat, tidak mudah menyusut atau melar, namun kurang menyerap keringat dan terasa panas.",
            perawatan = "Cuci menggunakan mesin dengan suhu sedang, tidak perlu disetrika kecuali sangat kusut, dan hindari pemutih.",
            gambarResId = R.drawable.kain_polyester
        )
    )

    fun getData(): List<JenisKain> = classList
    fun findKainByName(nama: String): JenisKain? {
        return classList.find { it.nama.equals(nama, ignoreCase = true) }
    }

}
