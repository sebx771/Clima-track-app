package com.example.mantenimiento.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mantenimiento.R
import com.example.mantenimiento.models.Mantenimiento
import com.google.android.material.button.MaterialButton

class ResumenServicioActivity : AppCompatActivity() {

    private lateinit var mantenimiento: Mantenimiento
    private var pathFoto: String? = null
    private var repuestos: HashMap<Int, Int>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resumen_servicio)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbarResumen)
        toolbar.setNavigationOnClickListener { finish() }

        mantenimiento = intent.getSerializableExtra("MANTENIMIENTO") as Mantenimiento
        pathFoto = intent.getStringExtra("FOTO_PATH")
        repuestos = intent.getSerializableExtra("REPUESTOS") as? HashMap<Int, Int>

        findViewById<TextView>(R.id.tvResumenEquipo).text = mantenimiento.equipoNombre ?: "Equipo Desconocido"
        findViewById<TextView>(R.id.tvResumenTrabajo).text = mantenimiento.trabajoRealizado
        findViewById<TextView>(R.id.tvResumenDiag).text = mantenimiento.diagnostico.ifEmpty { "N/A" }
        findViewById<TextView>(R.id.tvResumenRecom).text = mantenimiento.recomendaciones.ifEmpty { "N/A" }

        findViewById<MaterialButton>(R.id.btnIrAFirma).setOnClickListener {
            val intent = Intent(this, SignatureActivity::class.java)
            intent.putExtra("MANTENIMIENTO", mantenimiento)
            intent.putExtra("FOTO_PATH", pathFoto)
            intent.putExtra("REPUESTOS", repuestos)
            startActivity(intent)
            finish()
        }
    }
}
