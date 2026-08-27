package com.example.mantenimiento

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mantenimiento.fragments.DashboardFragment
import com.example.mantenimiento.fragments.EquiposFragment
import com.example.mantenimiento.fragments.HistorialFragment
import com.example.mantenimiento.fragments.OrdenesFragment
import com.example.mantenimiento.fragments.RepuestosFragment
import com.example.mantenimiento.models.Mantenimiento
import com.example.mantenimiento.repository.MantenimientoRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        generarDatosDePruebaConImagenes()

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        
        // Manejar navegación desde Intent (Dashboard)
        val fragmentToLoad = intent.getStringExtra("FRAGMENT_TO_LOAD")
        
        if (savedInstanceState == null) {
            when (fragmentToLoad) {
                "ordenes" -> {
                    loadFragment(OrdenesFragment())
                    bottomNav.selectedItemId = R.id.nav_ordenes
                }
                "equipos" -> {
                    loadFragment(EquiposFragment())
                    bottomNav.selectedItemId = R.id.nav_equipos
                }
                "historial" -> {
                    loadFragment(HistorialFragment())
                    bottomNav.selectedItemId = R.id.nav_historial
                }
                else -> {
                    loadFragment(DashboardFragment()) // Ahora el Dashboard es el fragmento inicial
                    bottomNav.selectedItemId = R.id.nav_inicio
                }
            }
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> loadFragment(DashboardFragment())
                R.id.nav_ordenes -> loadFragment(OrdenesFragment())
                R.id.nav_equipos -> loadFragment(EquiposFragment())
                R.id.nav_historial -> loadFragment(HistorialFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    /**
     * Genera datos de prueba incluyendo archivos de imagen reales para visualizar en el historial.
     * Solo se ejecuta una vez.
     */
    private fun generarDatosDePruebaConImagenes() {
        val prefs = getSharedPreferences("ClimaTrackDataGen", MODE_PRIVATE)
        if (prefs.getBoolean("samples_generated", false)) return

        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        
        // 1. Crear Foto de Ejemplo (Cuadro azul)
        val fotoFile = File(storageDir, "sample_evidencia_blue.jpg")
        val fotoBitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
        val canvasFoto = Canvas(fotoBitmap)
        canvasFoto.drawColor(Color.BLUE)
        val paintText = Paint().apply {
            color = Color.WHITE
            textSize = 40f
            textAlign = Paint.Align.CENTER
        }
        canvasFoto.drawText("EVIDENCIA DE PRUEBA", 250f, 250f, paintText)
        
        // 2. Crear Firma de Ejemplo (Trazo rojo)
        val firmaFile = File(storageDir, "sample_firma_red.png")
        val firmaBitmap = Bitmap.createBitmap(400, 200, Bitmap.Config.ARGB_8888)
        val canvasFirma = Canvas(firmaBitmap)
        canvasFirma.drawColor(Color.WHITE)
        val paintFirma = Paint().apply {
            color = Color.RED
            strokeWidth = 10f
            style = Paint.Style.STROKE
        }
        canvasFirma.drawCircle(200f, 100f, 50f, paintFirma)
        canvasFirma.drawLine(50f, 150f, 350f, 50f, paintFirma)

        try {
            FileOutputStream(fotoFile).use { fotoBitmap.compress(Bitmap.CompressFormat.JPEG, 90, it) }
            FileOutputStream(firmaFile).use { firmaBitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }

            // 3. Guardar en Base de Datos
            val repo = MantenimientoRepository(this)
            val sampleMnt = Mantenimiento(
                equipoId = 1, // Asumiendo que el equipo con ID 1 existe (creado por DatabaseHelper)
                fecha = "27/08/2026",
                tipo = "INSPECCIÓN",
                descripcion = "Mantenimiento de prueba con evidencias generadas",
                observaciones = "Generado automáticamente por el sistema",
                estadoFinal = "OPERATIVO",
                fotoEvidencia = fotoFile.absolutePath,
                firmaCliente = firmaFile.absolutePath
            )
            repo.addMantenimiento(sampleMnt)

            prefs.edit().putBoolean("samples_generated", true).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}