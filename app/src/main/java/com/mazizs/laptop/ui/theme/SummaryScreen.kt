package com.mazizs.laptop.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mazizs.laptop.R
import com.mazizs.laptop.data.OrderUiState
import com.mazizs.laptop.ui.components.FormattedPriceLabel

//Fungsi komponen Composable dalam Jetpack Compose di bawah ini merupakan fungsi OrderSummaryScreen yaitu untuk membuat, mengatur, dan menampilkan ringkasan pesanan
@Composable
fun OrderSummaryScreen(
    orderUiState: OrderUiState,
    onCancelButtonClicked: () -> Unit,
    onSendButtonClicked: (String, String) -> Unit,
    modifier: Modifier = Modifier
){
    val resources = LocalContext.current.resources

    val numberOfLaptops = resources.getQuantityString(
        R.plurals.laptops,
        orderUiState.kuantitas,
        orderUiState.kuantitas
    )
    val orderSummary = stringResource( //Memuat dan memformat string dengan parameter tertentu
        R.string.detail_pesanan,
        numberOfLaptops,
        orderUiState.warna,
        orderUiState.tanggal,
        orderUiState.kuantitas
    )
    val newOrder = stringResource(R.string.pesanan_laptop_baru) //Untuk mendefinisikan string untuk pesanan baru
    val items = listOf( //Untuk membuat daftar ringkasan pesanan untuk ditampilkan
        //Ringkasan pada baris 1 untuk menampilkan kuantitas yang dipilih
        Pair(stringResource(R.string.kuantitas), numberOfLaptops),
        //Ringkasan pada baris 2 untuk menampilkan warna yang dipilih
        Pair(stringResource(R.string.warna), orderUiState.warna),
        //Ringkasan pada baris 3 untuk menampilkan tanggal pengambilan yang dipilih
        Pair(stringResource(R.string.tanggal_pengambilan), orderUiState.tanggal)
    )

    Column( //Merupakan omponen utama berupa tata letak kolom dan baris secara vrtikal
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column( //Komponen kolom untuk menampilkan ringkasan pesanan
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            items.forEach { item -> //Untuk menampilkan setiap item ringkasan pesanan
                Text(item.first.uppercase())
                Text(text = item.second, fontWeight = FontWeight.Bold)
                Divider(thickness = dimensionResource(R.dimen.thickness_divider))
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            FormattedPriceLabel( //Untuk menampilkan label harga
                subtotal = orderUiState.harga,
                modifier = Modifier.align(Alignment.End)
            )
        }
        Row( //Merupakan komponen baris untuk menampilkan tombol kirim dan batalkan
            modifier = Modifier
                .weight(1f, false)
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            Column( //Komponen kolom untuk menampilkan tombol kirim dan batalkan
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
            ) {
                Button( //Merupakan tombol kirim
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onSendButtonClicked(newOrder, orderSummary) }
                ) {
                    Text(stringResource(R.string.kirim))
                }
                OutlinedButton( //Merupakan tombol batalkan
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCancelButtonClicked
                ) {
                    Text(stringResource(R.string.batal))
                }
            }
        }
    }
}

//Fungsi di bawah ini adalah komponen Composable yang digunakan untuk menampilkan preview atau pratinjau dari layar Ringkasan Pesanan
@Preview
@Composable
fun OrderSummaryPreview(){
    OrderSummaryScreen(
        orderUiState = OrderUiState(0, "Test", "Test", "$300.00"),
        onSendButtonClicked = { subject: String, summary: String -> },
        onCancelButtonClicked = {},
        modifier = Modifier.fillMaxHeight()
    )
}