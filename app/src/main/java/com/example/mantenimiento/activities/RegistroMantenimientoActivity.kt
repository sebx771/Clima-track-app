package com.example.mantenimiento.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.mantenimiento.R
import com.example.mantenimiento.models.Mantenimiento
import com.example.mantenimiento.repository.MantenimientoRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar
import java.util.Locale

class RegistroMantenimientoActivity : AppCompatActivity() {

    private lateinit var repo: MantenimientoRepository
    private var equipoId: Int = -1
    private var pathFoto: String? = null
    private var pathFirma: String? = null

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

    private val signatureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            pathFirma = result.data?.getStringExtra("FILE_PATH")
            pathFirma?.let {
                val ivPreview = findViewById<ImageView>(R.id.ivPreviewFirma)
                ivPreview.visibility = View.VISIBLE
                ivPreview.setImageBitmap(BitmapFactory.decodeFile(it))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_mantenimiento)

        repo = MantenimientoRepository(this)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbarRegistroMnt)
        toolbar.setNavigationOnClickListener { finish() }

        // Recibir datos del equipo
        equipoId = intent.getIntExtra("EQUIPO_ID", -1)
        val equipoNombre = intent.getStringExtra("EQUIPO_NOMBRE") ?: "Desconocido"
        findViewById<TextView>(R.id.tvEquipoInfo).text = getString(R.string.equipo_label, equipoNombre)

        setupForm()

        findViewById<MaterialButton>(R.id.btnCapturarEvidencia).setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            cameraLauncher.launch(intent)
        }

        findViewById<MaterialButton>(R.id.btnRegistrarFirma).setOnClickListener {
            val intent = Intent(this, SignatureActivity::class.java)
            signatureLauncher.launch(intent)
        }

        findViewById<MaterialButton>(R.id.btnFinalizarMnt).setOnClickListener {
            saveMantenimiento()
        }
    }

    private fun setupForm() {
        // Configurar selector de fecha
        val etFecha = findViewById<TextInputEditText>(R.id.etFechaMnt)
        etFecha.setOnClickListener { showDatePicker() }

        // Configurar dropdown de tipos
        val tipos = arrayOf("PREVENTIVO", "CORRECTIVO", "INSPECCIÓN")
        val adapterTipos = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, tipos)
        findViewById<AutoCompleteTextView>(R.id.spinnerTipoMnt).setAdapter(adapterTipos)

        // Configurar dropdown de estado final
        val estados = arrayOf("OPERATIVO", "FUERA DE SERVICIO")
        val adapterEstados = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, estados)
        findViewById<AutoCompleteTextView>(R.id.spinnerEstadoFinal).setAdapter(adapterEstados)
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, y, m, d ->
            val fechaFormateada = String.format(Locale.getDefault(), "%02d/%02d/%d", d, m + 1, y)
            findViewById<TextInputEditText>(R.id.etFechaMnt).setText(fechaFormateada)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun saveMantenimiento() {
        val fecha = findViewById<TextInputEditText>(R.id.etFechaMnt).text.toString()
        val tipo = findViewById<AutoCompleteTextView>(R.id.spinnerTipoMnt).text.toString()
        val desc = findViewById<TextInputEditText>(R.id.etDescripcionMnt).text.toString()
        val obs = findViewById<TextInputEditText>(R.id.etObservacionesMnt).text.toString()
        val estado = findViewById<AutoCompleteTextView>(R.id.spinnerEstadoFinal).text.toString()

        if (fecha.isEmpty() || tipo.isEmpty() || desc.isEmpty()) {
            Toast.makeText(this, getString(R.string.msg_campos_obligatorios), Toast.LENGTH_SHORT).show()
            return
        }

        val mnt = Mantenimiento(
            equipoId = equipoId,
            fecha = fecha,
            tipo = tipo,
            descripcion = desc,
            observaciones = obs,
            estadoFinal = estado,
            fotoEvidencia = pathFoto,
            firmaCliente = pathFirma
        )

        val id = repo.addMantenimiento(mnt)
        if (id > 0) {
            Toast.makeText(this, getString(R.string.msg_mantenimiento_exito), Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, getString(R.string.msg_mantenimiento_error), Toast.LENGTH_SHORT).show()
        }
    }
}
