package com.mazizs.laptop

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mazizs.laptop.data.DataSource
import com.mazizs.laptop.data.OrderUiState
import com.mazizs.laptop.ui.OrderSummaryScreen
import com.mazizs.laptop.ui.OrderViewModel
import com.mazizs.laptop.ui.SelectOptionScreen
import com.mazizs.laptop.ui.StartOrderScreen

//Untuk membuat enumerasi dengan konstanta yang masing-masing memiliki ID string resource
enum class LaptopScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name), //Nilai enum untuk screen awal Judul Laptop
    Warna(title = R.string.pilih_warna), //Nilai enum untuk screen pemilihan warna dengan judul Pilih Warna
    Pengambilan(title = R.string.pilih_tanggal_pengambilan), //Nilai enum untuk screen pemilihan tanggal pengambilan dengan judul Pilih Tanggal Pengambilan
    Ringkasan(title = R.string.ringkasan_pesanan) //Nilai enum untuk screen ringkasan pesanan dengan judul Ringkasan Pesanan

}

//Fungsi komponen Composable dalam Jetpack Compose di bawah ini merupakan fungsi LaptopAppBar yaitu untuk menampilkan topBar dan menampilkan tombol kembali
@Composable
fun LaptopAppBar(
    currentScreen: LaptopScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar( //Untuk membuat dan menampilkan judul yaitu Laptop di bagian atas layar
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) { //Untuk mengecek apakah dapat melakukan navigasi ke layar sebelumnya
                //Jika dapat, maka menampilkan IconButton dengan ikon navigasi Kembali
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack, //Untuk menampilkan ikon panah kembali
                        contentDescription = stringResource(R.string.tombol_kembali)
                    )
                }
            }
        }
    )
}

//Fungsi komponen Composable dalam Jetpack Compose di bawah ini merupakan fungsi LaptopApp yaitu untuk mengatasi navigasi antar layar dan menampilkan UI yang sesuai berdasarkan status pesanan
@Composable
fun LaptopApp(
    viewModel: OrderViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    //Untuk mendapatkan tumpukan entri belakang saat ini
    val backStackEntry by navController.currentBackStackEntryAsState()
    //Untuk mendapatkan nama layar saat ini
    val currentScreen = LaptopScreen.valueOf(
        backStackEntry?.destination?.route ?: LaptopScreen.Start.name
    )

    Scaffold( //Untuk membuat tata letak dengan menggunakan komponen Scaffold dari Jetpack Compose
        topBar = {
            LaptopAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        //Untuk mengumpulkan status UI dari ViewModel
        val uiState by viewModel.uiState.collectAsState()

        NavHost( //Umtuk menyiapkan navigasi untuk setiap layar
            navController = navController,
            startDestination = LaptopScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            //Tampilan awal untuk melakukan pemesanan laptop
            composable(route = LaptopScreen.Start.name) {
                StartOrderScreen(
                    quantityOptions = DataSource.quantityOptions,
                    onNextButtonClicked = {
                        viewModel.setQuantity(it)
                        //Untuk menavigasi ke layar pemilihan warna
                        navController.navigate(LaptopScreen.Warna.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
            //Untuk layar Pemilihan Warna untuk memilih warna laptop
            composable(route = LaptopScreen.Warna.name) {
                val context = LocalContext.current
                SelectOptionScreen(
                    subtotal = uiState.harga,
                    onNextButtonClicked = { navController.navigate(LaptopScreen.Pengambilan.name) },
                    onCancelButtonClicked = {
                        //Umtuk membatalkan pesanan dan menavigasi kembali ke layar awal
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    },
                    options = DataSource.warna.map { id -> context.resources.getString(id) },
                    onSelectionChanged = { viewModel.setColor(it) },
                    modifier = Modifier.fillMaxHeight()
                )
            }
            //Unrtuk layar Pemilihan Tanggal Pengambilan
            composable(route = LaptopScreen.Pengambilan.name) {
                SelectOptionScreen(
                    subtotal = uiState.harga,
                    onNextButtonClicked = { navController.navigate(LaptopScreen.Ringkasan.name) },
                    onCancelButtonClicked = {
                        //Umtuk membatalkan pesanan dan menavigasi kembali ke layar awal
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    },
                    options = uiState.opsiPengambilan,
                    onSelectionChanged = { viewModel.setDate(it) },
                    modifier = Modifier.fillMaxHeight()
                )
            }
            //Untuk layar Ringkasan Pesanan laptop yang akan dipesan
            composable(route = LaptopScreen.Ringkasan.name) {
                val context = LocalContext.current
                OrderSummaryScreen(
                    orderUiState = uiState,
                    onCancelButtonClicked = {
                        //Umtuk membatalkan pesanan dan menavigasi kembali ke layar awal
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    },
                    onSendButtonClicked = { subject: String, summary: String ->
                        //Membagikan pesanan melalui berbagai aplikasi
                        shareOrder(context, subject = subject, summary = summary)
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}

//Di bawah ini berfungsi untuk membatalkan pesanan dan kembali ke tampilan awal atau Start Screen
private fun cancelOrderAndNavigateToStart(
    viewModel: OrderViewModel,
    navController: NavHostController
) {
    viewModel.resetOrder() //Untuk mengatur ulang status pesanan di ViewModel
    navController.popBackStack(LaptopScreen.Start.name, inclusive = false) //Kembali ke tampilan awal tanpa menyertakan tampilan saat ini dalam tumpukan
}

//Di bawah ini berfungsi untuk berbagi detail pesanan melalui aplikasi
private fun shareOrder(context: Context, subject: String, summary: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, summary)
    }
    context.startActivity( //Untuk memulai aktivitas memilih aplikasi untuk dibagikan
        Intent.createChooser(
            intent,
            context.getString(R.string.pesanan_laptop_baru)
        )
    )
}