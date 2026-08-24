package com.example.mantenimiento.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mantenimiento.R
import com.example.mantenimiento.adapters.EquipoAdapter
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
        
        adapter = EquipoAdapter(emptyList()) { equipo ->
            // Acción al hacer clic en un equipo (podría ir a detalles o edición)
        }
        rv.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        // Recargar la lista cada vez que volvemos a la pantalla
        val lista = repo.getAllEquipos()
        adapter.updateData(lista)
    }
}
