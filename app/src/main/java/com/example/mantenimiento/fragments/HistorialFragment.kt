package com.example.mantenimiento.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mantenimiento.R
import com.example.mantenimiento.adapters.HistorialAdapter
import com.example.mantenimiento.repository.MantenimientoRepository

class HistorialFragment : Fragment() {

    private lateinit var repo: MantenimientoRepository
    private lateinit var adapter: HistorialAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_historial, container, false)
        
        repo = MantenimientoRepository(requireContext())
        setupRecyclerView(view)

        return view
    }

    private fun setupRecyclerView(view: View) {
        val rv = view.findViewById<RecyclerView>(R.id.rvHistorial)
        rv.layoutManager = LinearLayoutManager(requireContext())
        adapter = HistorialAdapter(emptyList())
        rv.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        val lista = repo.getAllMantenimientos()
        adapter.updateData(lista)
    }
}
