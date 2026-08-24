package com.example.mantenimiento.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mantenimiento.R
import com.example.mantenimiento.adapters.RepuestoAdapter
import com.example.mantenimiento.models.Repuesto
import com.example.mantenimiento.repository.RepuestoRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListaRepuestosActivity : AppCompatActivity() {

    private lateinit var repo: RepuestoRepository
    private lateinit var adapter: RepuestoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_repuestos)

        repo = RepuestoRepository(this)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbarRepuestos)
        toolbar.setNavigationOnClickListener { finish() }

        setupRecyclerView()

        findViewById<FloatingActionButton>(R.id.fabAddRepuesto).setOnClickListener {
            val intent = Intent(this, FormRepuestoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        val rv = findViewById<RecyclerView>(R.id.rvRepuestos)
        rv.layoutManager = LinearLayoutManager(this)
        
        adapter = RepuestoAdapter(emptyList()) { repuesto, view ->
            showOptionsMenu(repuesto, view)
        }
        rv.adapter = adapter
    }

    private fun showOptionsMenu(repuesto: Repuesto, view: View) {
        val popup = PopupMenu(this, view)
        popup.menu.add("Editar")
        popup.menu.add("Eliminar")

        popup.setOnMenuItemClickListener { item ->
            when (item.title) {
                "Editar" -> {
                    val intent = Intent(this, FormRepuestoActivity::class.java)
                    intent.putExtra("REPUESTO_ID", repuesto.id)
                    startActivity(intent)
                }
                "Eliminar" -> confirmDelete(repuesto)
            }
            true
        }
        popup.show()
    }

    private fun confirmDelete(repuesto: Repuesto) {
        AlertDialog.Builder(this)
            .setTitle(R.string.menu_inventario_repuestos)
            .setMessage("¿Deseas eliminar ${repuesto.nombre}?")
            .setPositiveButton("Eliminar") { _, _ ->
                val res = repo.deleteRepuesto(repuesto.id ?: 0)
                if (res > 0) {
                    Toast.makeText(this, getString(R.string.msg_repuesto_eliminado), Toast.LENGTH_SHORT).show()
                    loadRepuestos()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun loadRepuestos() {
        val list = repo.getAllRepuestos()
        adapter.updateData(list)
    }

    override fun onResume() {
        super.onResume()
        loadRepuestos()
    }
}
