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
import com.example.mantenimiento.adapters.EquipoAdapter
import com.example.mantenimiento.models.Equipo
import com.example.mantenimiento.repository.EquipoRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListaEquiposActivity : AppCompatActivity() {

    private lateinit var repo: EquipoRepository
    private lateinit var adapter: EquipoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_equipos)

        repo = EquipoRepository(this)
        setupRecyclerView()

        findViewById<FloatingActionButton>(R.id.fabAddEquipo).setOnClickListener {
            val intent = Intent(this, FormEquipoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        val rv = findViewById<RecyclerView>(R.id.rvEquipos)
        rv.layoutManager = LinearLayoutManager(this)
        
        adapter = EquipoAdapter(emptyList(), 
            onItemClick = { equipo ->
                // Futuro: Ir a historial o detalles
            },
            onOptionsClick = { equipo, view ->
                showOptionsMenu(equipo, view)
            }
        )
        rv.adapter = adapter
    }

    private fun showOptionsMenu(equipo: Equipo, view: View) {
        val popup = PopupMenu(this, view)
        popup.menu.add("Registrar Mantenimiento")
        popup.menu.add("Editar")
        popup.menu.add("Eliminar")

        popup.setOnMenuItemClickListener { item ->
            when (item.title) {
                "Registrar Mantenimiento" -> {
                    val intent = Intent(this, RegistroMantenimientoActivity::class.java)
                    intent.putExtra("EQUIPO_ID", equipo.id)
                    intent.putExtra("EQUIPO_NOMBRE", equipo.nombre)
                    startActivity(intent)
                }
                "Editar" -> {
                    val intent = Intent(this, FormEquipoActivity::class.java)
                    intent.putExtra("EQUIPO_ID", equipo.id)
                    startActivity(intent)
                }
                "Eliminar" -> {
                    confirmDelete(equipo)
                }
            }
            true
        }
        popup.show()
    }

    private fun confirmDelete(equipo: Equipo) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Equipo")
            .setMessage("¿Estás seguro de que deseas eliminar el equipo ${equipo.nombre}?")
            .setPositiveButton("Eliminar") { _, _ ->
                val result = repo.deleteEquipo(equipo.id ?: 0)
                if (result > 0) {
                    Toast.makeText(this, "Equipo eliminado", Toast.LENGTH_SHORT).show()
                    loadEquipos()
                } else {
                    Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun loadEquipos() {
        val lista = repo.getAllEquipos()
        adapter.updateData(lista)
    }

    override fun onResume() {
        super.onResume()
        loadEquipos()
    }
}
