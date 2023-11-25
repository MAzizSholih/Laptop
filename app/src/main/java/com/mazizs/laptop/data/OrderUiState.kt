package com.mazizs.laptop.data

//Merupakan kelas data yang mewakili status UI saat ini dalam hal kuantitas,
//warna, pemilihan tanggal pengambilan dan harga
data class OrderUiState(
    val kuantitas: Int = 0, //Jumlah laptop yang dipilih (1, 6, 12)
    val warna: String = "", //Warna laptop dalam pesanan seperti Hitam, Putih, Silver, Merah, Biru
    val tanggal: String = "", //Tanggal yang dipilih untuk mengambil pesanan
    val harga: String = "", //Total harga pesanan
    val opsiPengambilan: List<String> = listOf() //Pemilihan tanggal pengambilan yang tersedia untuk pesanan
)