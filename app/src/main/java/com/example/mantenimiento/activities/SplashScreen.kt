package com.example.mantenimiento.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mantenimiento.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Aquí conectamos el código con tu archivo XML
        setContentView(R.layout.splash_screen)
    }
}