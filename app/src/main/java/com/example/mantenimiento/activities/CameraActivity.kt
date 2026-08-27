package com.example.mantenimiento.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.mantenimiento.databinding.ActivityCameraBinding
import java.io.File

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private var photoFile: File? = null
    private var photoUri: Uri? = null

    // Lanzador para solicitar permiso de cámara
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            abrirCamara()
        } else {
            Toast.makeText(this, "Permiso de cámara denegado. No se puede capturar la foto.", Toast.LENGTH_LONG).show()
        }
    }

    // Lanzador para tomar la foto en alta resolución
    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            binding.imgPreview.setImageURI(photoUri)
            finalizarConResultado()
        } else {
            Toast.makeText(this, "Captura cancelada", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCapture.setOnClickListener {
            checkCameraPermission()
        }
    }

    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                abrirCamara()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun abrirCamara() {
        photoFile = crearArchivoImagen()
        photoFile?.let { file ->
            photoUri = FileProvider.getUriForFile(
                this,
                "com.example.mantenimiento.fileprovider",
                file
            )
            photoUri?.let { takePictureLauncher.launch(it) }
        }
    }

    private fun crearArchivoImagen(): File? {
        val fileName = "evidencia_${System.currentTimeMillis()}.jpg"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return try {
            File(storageDir, fileName)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun finalizarConResultado() {
        photoFile?.let {
            val resultIntent = Intent().apply {
                putExtra("FILE_PATH", it.absolutePath)
            }
            setResult(RESULT_OK, resultIntent)
            Toast.makeText(this, "Foto guardada correctamente", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
