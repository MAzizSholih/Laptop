package com.mazizs.laptop.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mazizs.laptop.R
import com.mazizs.laptop.data.DataSource

//Fungsi komponen Composable dalam Jetpack Compose di bawah ini merupakan fungsi StartOrderScreen yaitu untuk menampilkan layar awal saat melakukan proses pemesanan
@Composable
fun StartOrderScreen(
    quantityOptions: List<Pair<Int, Int>>,
    onNextButtonClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    Column( //Merupakan komponen utama berupa tata letak kolom dan baris secara vertikal
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column( //Untuk menyusun elemen-elemen tata letak di tengah dengan vertikal
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
            Image( //Untuk menampilkan gambar laptop yang dipesan
                painter = painterResource(R.drawable.laptop),
                contentDescription = null,
                modifier = Modifier.width(300.dp)
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
            Text( //Untuk memberi terks informasi bahwa pengguna sedang memesan laptop
                text = stringResource(R.string.memesan_laptop),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
        }
        Row(modifier = Modifier.weight(1f, false)) {//Untuk menampilkan tombol kuantitas yang dapat dipilih
            Column( //Untuk membuat dan menyusun tombol kuantitas
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    dimensionResource(id = R.dimen.padding_medium)
                )
            ) {
                quantityOptions.forEach { item -> //Untuk mennampilkan tombol kuantitas untuk setiap opsi yang diberikan
                    SelectQuantityButton(
                        labelResourceId = item.first,
                        onClick = { onNextButtonClicked(item.second) }
                    )
                }
            }
        }
    }
}

//Fungsi komponen Composable dalam Jetpack Compose di bawah ini merupakan fungsi SelectQuantityButton yaitu untuk membuat tombol kuantitas yang dapat dipilih
@Composable
fun SelectQuantityButton(
    @StringRes labelResourceId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Button( //Untuk tombol dengan teks yang berasal dari string
        onClick = onClick,
        modifier = modifier.widthIn(min = 250.dp)
    ) {
        Text(stringResource(labelResourceId))
    }
}

//Fungsi di bawah ini adalah komponen Composable yang digunakan untuk menampilkan preview atau pratinjau dari layar awal saat melakukan proses pemesanan
@Preview
@Composable
fun StartOrderPreview(){
    StartOrderScreen(
        quantityOptions = DataSource.quantityOptions,
        onNextButtonClicked = {},
        modifier = Modifier.fillMaxSize().padding(dimensionResource(R.dimen.padding_medium))
    )
}