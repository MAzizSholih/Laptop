package com.mazizs.laptop.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mazizs.laptop.R
import com.mazizs.laptop.ui.components.FormattedPriceLabel

//Fungsi komponen Composable dalam Jetpack Compose di bawah ini merupakan fungsi SelectOptionScreen yaitu untuk menampilkan layar pemilihan opsi dengan RadioButton
@Composable
fun SelectOptionScreen(
    subtotal: String,
    options: List<String>,
    onSelectionChanged: (String) -> Unit = {},
    onCancelButtonClicked: () -> Unit = {},
    onNextButtonClicked: () -> Unit = {},
    modifier: Modifier = Modifier
){
    var selectedValue by rememberSaveable { mutableStateOf("") }

    Column( //Merupakan komponen utama berupa tata letak kolom dan baris secara vertikal
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        //Untuk menyusun elemen-elemen tata letak di tengah
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))){
            options.forEach { item ->
                Row(
                    modifier = Modifier.selectable(
                        selected = selectedValue == item,
                        onClick = {
                            selectedValue = item
                            onSelectionChanged(item)
                        }
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    RadioButton( //Tombol RadioButton untuk setiap opsi pilihan
                        selected = selectedValue == item,
                        onClick = {
                            selectedValue = item
                            onSelectionChanged(item)
                        }
                    )
                    Text(item) //Untuk menampilkan teks opsi
                }
            }
            Divider( //Merupakan pembatas untuk memberi jarak di antara opsi dan label harga
                thickness = dimensionResource(R.dimen.thickness_divider),
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_medium))
            )
            FormattedPriceLabel( //Format label harga
                subtotal = subtotal,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(
                        top = dimensionResource(R.dimen.padding_medium),
                        bottom = dimensionResource(R.dimen.padding_medium)
                    )
            )
        }
        Row( //Untuk mengelompokkan dua tombol dan menampilkan tombol Batal dan Selanjutnya
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium))
                .weight(1f, false),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
            verticalAlignment = Alignment.Bottom
        ){
            //Merupakan tombol Batal dengan panggilan balik berupa onCancelButtonClicked
            OutlinedButton(modifier = Modifier.weight(1f), onClick = onCancelButtonClicked) {
                Text(stringResource(R.string.batal))
            }
            Button( //Merupakan tombol Selanjutnya dengan panggilan balik berupa onNextButtonClicked
                modifier = Modifier.weight(1f),
                enabled = selectedValue.isNotEmpty(), //Tombol hanya dapat diaktifkan jika pengguna membuat pemilihan
                onClick = onNextButtonClicked
            ) {
                Text(stringResource(R.string.selanjutnya))
            }
        }
    }
}

//Fungsi di bawah ini adalah komponen Composable yang digunakan untuk menampilkan preview atau pratinjau dari layar pemilihan opsi dengan RadioButton
@Preview
@Composable
fun SelectOptionPreview(){
    SelectOptionScreen(
        subtotal = "299.99",
        options = listOf("Option 1", "Option 2", "Option 3", "Option 4"),
        modifier = Modifier.fillMaxHeight()
    )
}