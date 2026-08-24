package com.example.mantenimiento.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mantenimiento.R
import com.example.mantenimiento.activities.FormEquipoActivity
import com.example.mantenimiento.activities.RegistroMantenimientoActivity
import com.example.mantenimiento.adapters.EquipoAdapter
import com.example.mantenimiento.models.Equipo
import com.example.mantenimiento.repository.EquipoRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EquiposFragment : Fragment() {

    private lateinit var repo: EquipoRepository
    private lateinit var adapter: EquipoAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_equipos, container, false)
        
        repo = EquipoRepository(requireContext())
        setupRecyclerView(view)

        view.findViewById<FloatingActionButton>(R.id.fabAddEquipo).setOnClickListener {
            val intent = Intent(requireContext(), FormEquipoActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun setupRecyclerView(view: View) {
        val rv = view.findViewById<RecyclerView>(R.id.rvEquipos)
        rv.layoutManager = LinearLayoutManager(requireContext())
        
        adapter = EquipoAdapter(emptyList(), 
            onItemClick = { equipo ->
                // Futuro: Ir a historial o detalles
            },
            onOptionsClick = { equipo, v ->
                showOptionsMenu(equipo, v)
            }
        )
        rv.adapter = adapter
    }

    private fun showOptionsMenu(equipo: Equipo, view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.menu.add("Registrar Mantenimiento")
        popup.menu.add("Editar")
        popup.menu.add("Eliminar")

        popup.setOnMenuItemClickListener { item ->
            when (item.title) {
                "Registrar Mantenimiento" -> {
                    val intent = Intent(requireContext(), RegistroMantenimientoActivity::class.java)
                    intent.putExtra("EQUIPO_ID", equipo.id)
                    intent.putExtra("EQUIPO_NOMBRE", equipo.nombre)
                    startActivity(intent)
                }
                "Editar" -> {
                    val intent = Intent(requireContext(), FormEquipoActivity::class.java)
                    intent.putExtra("EQUIPO_ID", equipo.id)
                    startActivity(intent)
                }
                "Eliminar" -> confirmDelete(equipo)
            }
            true
        }
        popup.show()
    }

    private fun confirmDelete(equipo: Equipo) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar Equipo")
            .setMessage("¿Deseas eliminar ${equipo.nombre}?")
            .setPositiveButton("Eliminar") { _, _ ->
                val result = repo.deleteEquipo(equipo.id ?: 0)
                if (result > 0) {
                    Toast.makeText(requireContext(), "Equipo eliminado", Toast.LENGTH_SHORT).show()
                    loadEquipos()
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
