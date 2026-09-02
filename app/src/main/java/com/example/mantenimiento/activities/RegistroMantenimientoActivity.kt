package com.example.mantenimiento.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.mantenimiento.R
import com.example.mantenimiento.models.Mantenimiento
import com.example.mantenimiento.repository.MantenimientoRepository
import com.example.mantenimiento.repository.RepuestoRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class RegistroMantenimientoActivity : AppCompatActivity() {

    private lateinit var repo: MantenimientoRepository
    private lateinit var repuestoRepo: RepuestoRepository
    
    private var ordenId: Int = -1
    private var numeroOrden: String = ""
    private var equipoId: Int = -1
    private var equipoNombre: String = ""
    private var pathFoto: String? = null
    
    // Lista temporal de repuestos seleccionados (ID a Cantidad)
    private val repuestosSeleccionados = mutableMapOf<Int, Int>()

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            pathFoto = result.data?.getStringExtra("FILE_PATH")
            pathFoto?.let {
                val ivPreview = findViewById<ImageView>(R.id.ivPreviewFoto)
                ivPreview.visibility = View.VISIBLE
                ivPreview.setImageBitmap(BitmapFactory.decodeFile(it))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_mantenimiento)

        repo = MantenimientoRepository(this)
        repuestoRepo = RepuestoRepository(this)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbarRegistroMnt)
        toolbar.setNavigationOnClickListener { finish() }

        // Recibir datos de la orden
        ordenId = intent.getIntExtra("ORDEN_ID", -1)
        numeroOrden = intent.getStringExtra("ORDEN_NUMERO") ?: "OT-00000"
        equipoId = intent.getIntExtra("EQUIPO_ID", -1)
        equipoNombre = intent.getStringExtra("EQUIPO_NOMBRE") ?: "Desconocido"
        
        findViewById<TextView>(R.id.tvEquipoInfo).text = "Orden: $numeroOrden\nEquipo: $equipoNombre"

        setupForm()

        findViewById<MaterialButton>(R.id.btnGestionarRepuestos).setOnClickListener {
            mostrarDialogoRepuestos()
        }

        findViewById<MaterialButton>(R.id.btnCapturarEvidencia).setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            cameraLauncher.launch(intent)
        }

        findViewById<MaterialButton>(R.id.btnFinalizarMnt).setOnClickListener {
            saveMantenimiento()
        }
    }

    private fun setupForm() {
        val etFecha = findViewById<TextInputEditText>(R.id.etFechaMnt)
        etFecha.setOnClickListener { showDatePicker() }
        
        // Fecha actual por defecto
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        etFecha.setText(sdf.format(Date()))

        val tipos = arrayOf("PREVENTIVO", "CORRECTIVO", "INSPECCIÓN")
        val adapterTipos = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, tipos)
        findViewById<AutoCompleteTextView>(R.id.spinnerTipoMnt).setAdapter(adapterTipos)

        val estados = arrayOf("OPERATIVO", "FUERA DE SERVICIO")
        val adapterEstados = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, estados)
        findViewById<AutoCompleteTextView>(R.id.spinnerEstadoFinal).setAdapter(adapterEstados)
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, y, m, d ->
            val fechaFormateada = String.format(Locale.getDefault(), "%02d/%02d/%d", d, m + 1, y)
            findViewById<TextInputEditText>(R.id.etFechaMnt).setText(fechaFormateada)
        }, year, month, day).show()
    }

    private fun mostrarDialogoRepuestos() {
        val repuestosDisponibles = repuestoRepo.getAllRepuestos()
        val nombres = repuestosDisponibles.map { "${it.nombre} (${it.codigo})" }.toTypedArray()
        
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Seleccionar Repuesto")
        
        var selectedIdx = -1
        builder.setSingleChoiceItems(nombres, -1) { _, which ->
            selectedIdx = which
        }
        
        builder.setPositiveButton("Agregar") { dialog, _ ->
            if (selectedIdx != -1) {
                val repuesto = repuestosDisponibles[selectedIdx]
                pedirCantidad(repuesto.id!!, repuesto.nombre)
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    private fun pedirCantidad(repuestoId: Int, nombre: String) {
        val input = EditText(this)
        input.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        input.hint = "Cantidad"
        
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Cantidad para $nombre")
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                val cant = input.text.toString().toIntOrNull() ?: 1
                repuestosSeleccionados[repuestoId] = (repuestosSeleccionados[repuestoId] ?: 0) + cant
                Toast.makeText(this, "Agregado: $nombre x$cant", Toast.LENGTH_SHORT).show()
                findViewById<MaterialButton>(R.id.btnGestionarRepuestos).text = "Repuestos (${repuestosSeleccionados.size})"
            }
            .show()
    }

    private fun saveMantenimiento() {
        val fecha = findViewById<TextInputEditText>(R.id.etFechaMnt).text.toString()
        val diag = findViewById<TextInputEditText>(R.id.etDiagnosticoMnt).text.toString()
        val trabajo = findViewById<TextInputEditText>(R.id.etDescripcionMnt).text.toString()
        val obs = findViewById<TextInputEditText>(R.id.etObservacionesMnt).text.toString()
        val recom = findViewById<TextInputEditText>(R.id.etRecomendacionesMnt).text.toString()

        if (fecha.isEmpty() || trabajo.isEmpty()) {
            Toast.makeText(this, "Complete los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val mnt = Mantenimiento(
            ordenId = ordenId,
            fecha = fecha,
            diagnostico = diag,
            trabajoRealizado = trabajo,
            observaciones = obs,
            recomendaciones = recom,
            equipoNombre = equipoNombre,
            numeroOrden = numeroOrden
        )

        // Pasar al resumen
        val intent = Intent(this, ResumenServicioActivity::class.java)
        intent.putExtra("MANTENIMIENTO", mnt)
        intent.putExtra("FOTO_PATH", pathFoto)
        intent.putExtra("REPUESTOS", HashMap(repuestosSeleccionados))
        startActivity(intent)
        finish()
    }
}
