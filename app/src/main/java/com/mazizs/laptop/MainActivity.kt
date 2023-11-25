package com.mazizs.laptop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.mazizs.laptop.ui.theme.LaptopTheme

//Fungsi onCreate ini digunakan untuk mengedit tampilan aktivitas utama dengan menggunakan komponen UI yang telah didefinisikan dalam LaptopApp
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            LaptopTheme {
                LaptopApp()
            }
        }
    }
}