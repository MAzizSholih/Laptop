package com.mazizs.laptop.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mazizs.laptop.R

//Fungsi komponen Composable dalam Jetpack Compose di bawah ini merupakan fungsi FormattedPriceLabel
//yaitu untuk menampilkan harga di layar
@Composable
fun FormattedPriceLabel(subtotal: String, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.subtotal_harga, subtotal),
        modifier = modifier,
        style = MaterialTheme.typography.headlineSmall
    )
}