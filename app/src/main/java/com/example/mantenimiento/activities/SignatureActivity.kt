package com.example.mantenimiento.activities

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mantenimiento.databinding.ActivitySignatureBinding
import java.io.File
import java.io.FileOutputStream

class SignatureActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignatureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignatureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnClear.setOnClickListener {
            binding.signatureView.clear()
        }

        binding.btnSave.setOnClickListener {
            val bitmap = binding.signatureView.getSignatureBitmap()
            saveSignature(bitmap)
        }
    }

    private fun saveSignature(bitmap: Bitmap) {
        val fileName = "firma_${System.currentTimeMillis()}.png"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File(storageDir, fileName)

        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
            
            val resultIntent = Intent().apply {
                putExtra("FILE_PATH", file.absolutePath)
            }
            setResult(RESULT_OK, resultIntent)
            Toast.makeText(this, "Firma guardada", Toast.LENGTH_SHORT).show()
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al guardar firma", Toast.LENGTH_SHORT).show()
        }
    }
}
