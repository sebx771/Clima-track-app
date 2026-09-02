package com.example.mantenimiento.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mantenimiento.R
import com.example.mantenimiento.activities.FormClienteActivity
import com.example.mantenimiento.adapters.ClienteAdapter
import com.example.mantenimiento.models.Cliente
import com.example.mantenimiento.repository.ClienteRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ClientesFragment : Fragment() {

    private lateinit var repo: ClienteRepository
    private lateinit var adapter: ClienteAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_clientes, container, false)
        
        repo = ClienteRepository(requireContext())
        setupRecyclerView(view)

        view.findViewById<FloatingActionButton>(R.id.fabAddCliente).setOnClickListener {
            val intent = Intent(requireContext(), FormClienteActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun setupRecyclerView(view: View) {
        val rv = view.findViewById<RecyclerView>(R.id.rvClientes)
        rv.layoutManager = LinearLayoutManager(requireContext())
        
        adapter = ClienteAdapter(emptyList(), 
            onEditClick = { cliente ->
                val intent = Intent(requireContext(), FormClienteActivity::class.java)
                intent.putExtra("CLIENTE_ID", cliente.id)
                startActivity(intent)
            },
            onDeleteClick = { cliente ->
                confirmDelete(cliente)
            }
        )
        rv.adapter = adapter
    }

    private fun confirmDelete(cliente: Cliente) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar Cliente")
            .setMessage("¿Deseas eliminar a ${cliente.nombre}?")
            .setPositiveButton("Eliminar") { _, _ ->
                val result = repo.deleteCliente(cliente.id!!)
                if (result > 0) {
                    Toast.makeText(requireContext(), "Cliente eliminado", Toast.LENGTH_SHORT).show()
                    loadClientes()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun loadClientes() {
        val lista = repo.getAllClientes()
        adapter.updateData(lista)
    }

    override fun onResume() {
        super.onResume()
        loadClientes()
    }
}
