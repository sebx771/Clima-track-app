package com.example.mantenimiento.activities

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mantenimiento.databinding.ActivitySignatureBinding
import com.example.mantenimiento.models.Mantenimiento
import com.example.mantenimiento.repository.MantenimientoRepository
import com.example.mantenimiento.repository.OrdenRepository
import com.example.mantenimiento.utils.LocationUtils
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class SignatureActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignatureBinding
    private lateinit var mantenimiento: Mantenimiento
    private lateinit var repo: MantenimientoRepository
    private lateinit var ordenRepo: OrdenRepository
    
    private var pathFoto: String? = null
    private var repuestosSeleccionados: HashMap<Int, Int>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignatureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repo = MantenimientoRepository(this)
        ordenRepo = OrdenRepository(this)
        
        mantenimiento = intent.getSerializableExtra("MANTENIMIENTO") as Mantenimiento
        pathFoto = intent.getStringExtra("FOTO_PATH")
        repuestosSeleccionados = intent.getSerializableExtra("REPUESTOS") as? HashMap<Int, Int>

        binding.btnClear.setOnClickListener {
            binding.signatureView.clear()
        }

        binding.btnSave.setOnClickListener {
            val bitmap = binding.signatureView.getSignatureBitmap()
            capturarUbicacionYGuardar(bitmap)
        }
    }

    private fun capturarUbicacionYGuardar(bitmap: Bitmap) {
        LocationUtils.getCurrentLocation(this) { location ->
            saveAll(bitmap, location?.latitude ?: 0.0, location?.longitude ?: 0.0)
        }
    }

    private fun saveAll(bitmap: Bitmap, lat: Double, lon: Double) {
        // Ejecutar guardado en segundo plano (Módulo 11: Manejo de hilos)
        Thread {
            val fileName = "firma_${System.currentTimeMillis()}.png"
            val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file = File(storageDir, fileName)

            try {
                val out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.flush()
                out.close()
                
                val fechaActual = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
                
                // 1. Guardar Mantenimiento
                val mntId = repo.addMantenimiento(mantenimiento)
                
                if (mntId > 0) {
                    // 2. Guardar Repuestos Detalle
                    repuestosSeleccionados?.forEach { (repId, cant) ->
                        repo.addDetalleRepuesto(mntId.toInt(), repId, cant)
                    }
                    
                    // 3. Guardar Evidencia (Foto)
                    pathFoto?.let {
                        repo.addEvidencia(mantenimiento.ordenId, it, fechaActual)
                    }
                    
                    // 4. Guardar Ubicación
                    repo.addUbicacion(mantenimiento.ordenId, lat, lon, fechaActual)
                    
                    // 5. Guardar Aprobación (Firma)
                    repo.addAprobacion(mantenimiento.ordenId, "Cliente", file.absolutePath, true, fechaActual)
                    
                    // 6. Finalizar Orden
                    ordenRepo.actualizarEstadoOrden(mantenimiento.ordenId, "FINALIZADA")
                    
                    runOnUiThread {
                        Toast.makeText(this, "Servicio finalizado y guardado con éxito", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, com.example.mantenimiento.MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "Error al guardar mantenimiento", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Error al guardar datos", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}
