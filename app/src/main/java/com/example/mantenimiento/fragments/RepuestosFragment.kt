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
import com.example.mantenimiento.activities.FormRepuestoActivity
import com.example.mantenimiento.adapters.RepuestoAdapter
import com.example.mantenimiento.models.Repuesto
import com.example.mantenimiento.repository.RepuestoRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RepuestosFragment : Fragment() {

    private lateinit var repo: RepuestoRepository
    private lateinit var adapter: RepuestoAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_repuestos, container, false)

        repo = RepuestoRepository(requireContext())
        setupRecyclerView(view)

        view.findViewById<FloatingActionButton>(R.id.fabAddRepuesto).setOnClickListener {
            val intent = Intent(requireContext(), FormRepuestoActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun setupRecyclerView(view: View) {
        val rv = view.findViewById<RecyclerView>(R.id.rvRepuestos)
        rv.layoutManager = LinearLayoutManager(requireContext())

        adapter = RepuestoAdapter(emptyList()) { repuesto, v ->
            showOptionsMenu(repuesto, v)
        }
        rv.adapter = adapter
    }

    private fun showOptionsMenu(repuesto: Repuesto, view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.menu.add("Editar")
        popup.menu.add("Eliminar")

        popup.setOnMenuItemClickListener { item ->
            when (item.title) {
                "Editar" -> {
                    val intent = Intent(requireContext(), FormRepuestoActivity::class.java)
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
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.menu_inventario_repuestos)
            .setMessage("¿Deseas eliminar ${repuesto.nombre}?")
            .setPositiveButton("Eliminar") { _, _ ->
                val res = repo.deleteRepuesto(repuesto.id ?: 0)
                if (res > 0) {
                    Toast.makeText(requireContext(), getString(R.string.msg_repuesto_eliminado), Toast.LENGTH_SHORT).show()
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
