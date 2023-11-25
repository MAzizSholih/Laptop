package com.mazizs.laptop.ui

import androidx.lifecycle.ViewModel
import com.mazizs.laptop.data.OrderUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val PRICE_PER_LAPTOP = 65_999.000 //Harga untuk satu laptop
private const val PRICE_FOR_SAME_DAY_PICKUP = 501.000 //Jika mengambil pesanan di hari yang sama, maka dikenakan biaya tambahan

//Merupakan kelas untuk menyimpan informasi tentang pesanan laptop baik dari segi jumlah,
//warna, dan tanggal pengambilan dan menghitung harga total berdasarkan detail pesanan
class OrderViewModel : ViewModel() {

    //Status pesanan laptop akan disimpan di MutableStateFlow
    private val _uiState = MutableStateFlow(OrderUiState(opsiPengambilan = pickupOptions()))
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()

    //Untuk mengatur kuantitas laptop untuk status pesanan dan memperbarui harganya
    fun setQuantity(numberLaptops: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                kuantitas = numberLaptops,
                harga = calculatePrice(quantity = numberLaptops)
            )
        }
    }
    //Untuk engatur warna yang diinginkan untuk laptop pada status pesanan dan warna yang dipilih hanya satu saja dari berbagai warna
    fun setColor(desiredColor: String) {
        _uiState.update { currentState ->
            currentState.copy(warna = desiredColor)
        }
    }
    //Untuk mengatur tanggal pengambilan pesanan pada status pesanan dan memperbarui harganya
    fun setDate(pickupDate: String) {
        _uiState.update { currentState ->
            currentState.copy(
                tanggal = pickupDate,
                harga = calculatePrice(pickupDate = pickupDate)
            )
        }
    }
    //Untuk mereset status pesanan ke nilai awal
    fun resetOrder() {
        _uiState.value = OrderUiState(opsiPengambilan = pickupOptions())
    }
    //Untuk menghitung harga berdasarkan detail pesanan seperti kuantitas dan tanggal pengambilan
    //Jika pengambilan dilakukan di hari yang sama, maka dikenakanan biaya tambahan
    private fun calculatePrice(
        quantity: Int = _uiState.value.kuantitas,
        pickupDate: String = _uiState.value.tanggal
    ): String {
        var calculatedPrice = quantity * PRICE_PER_LAPTOP
        if (pickupOptions()[0] == pickupDate) { //Jika pengguna memilih opsi pertama atau saat ini juga, maka dikenakan biaya tambahan
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        }
        val formattedPrice = formatToRupiah(calculatedPrice)
        return formattedPrice
    }
    //Mengembalikan daftar opsi tanggal pengambilan, kemudian dimulai dari tanggal hari ini dan tiga tanggal berikutnya
    private fun pickupOptions(): List<String> {
        val dateOptions = mutableListOf<String>()
        //Untuk membuat format tanggal menggunakan bahasa Indonesia
        val formatter = SimpleDateFormat("d MMMM yyyy", Locale("id", "ID"))
        val calendar = Calendar.getInstance()
        repeat(4) { //Untuk menambahkan tanggal hari ini dan tiga tanggal berikutnya
            dateOptions.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return dateOptions
    }
    //Untuk mengubah harga menjadi format mata uang Indonesia atau Rupiah
    private fun formatToRupiah(price: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return format.format(price)
    }
}