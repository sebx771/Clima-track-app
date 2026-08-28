package com.example.mantenimiento.activities

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mantenimiento.databinding.ActivitySignatureBinding
import com.example.mantenimiento.models.Mantenimiento
import com.example.mantenimiento.repository.MantenimientoRepository
import java.io.File
import java.io.FileOutputStream

class SignatureActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignatureBinding
    private lateinit var mantenimiento: Mantenimiento
    private lateinit var repo: MantenimientoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignatureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repo = MantenimientoRepository(this)
        mantenimiento = intent.getSerializableExtra("MANTENIMIENTO") as Mantenimiento

        binding.btnClear.setOnClickListener {
            binding.signatureView.clear()
        }

        binding.btnSave.setOnClickListener {
            val bitmap = binding.signatureView.getSignatureBitmap()
            saveSignatureAndFinish(bitmap)
        }
    }

    private fun saveSignatureAndFinish(bitmap: Bitmap) {
        val fileName = "firma_${System.currentTimeMillis()}.png"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File(storageDir, fileName)

        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
            
            // Actualizar objeto mantenimiento con la ruta de la firma
            val mntFinal = mantenimiento.copy(firmaCliente = file.absolutePath)
            
            // GUARDAR TODO EN DB
            val id = repo.addMantenimiento(mntFinal)
            
            if (id > 0) {
                Toast.makeText(this, "Mantenimiento y Firma guardados con éxito", Toast.LENGTH_LONG).show()
                // Regresar al inicio o historial
                val intent = Intent(this, com.example.mantenimiento.MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                Toast.makeText(this, "Error al guardar en la base de datos", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al guardar firma", Toast.LENGTH_SHORT).show()
        }
    }
}
