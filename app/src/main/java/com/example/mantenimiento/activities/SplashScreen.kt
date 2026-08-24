package com.example.mantenimiento.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.mantenimiento.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        // Navegar a la lista de equipos después de 2 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, ListaEquiposActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}