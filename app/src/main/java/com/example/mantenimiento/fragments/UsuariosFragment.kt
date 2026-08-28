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
import com.example.mantenimiento.activities.FormUsuarioActivity
import com.example.mantenimiento.adapters.UsuarioAdapter
import com.example.mantenimiento.models.Usuario
import com.example.mantenimiento.repository.UsuarioRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UsuariosFragment : Fragment() {

    private lateinit var repo: UsuarioRepository
    private lateinit var adapter: UsuarioAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_usuarios, container, false)
        
        repo = UsuarioRepository(requireContext())
        setupRecyclerView(view)

        view.findViewById<FloatingActionButton>(R.id.fabAddUsuario).setOnClickListener {
            val intent = Intent(requireContext(), FormUsuarioActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun setupRecyclerView(view: View) {
        val rv = view.findViewById<RecyclerView>(R.id.rvUsuarios)
        rv.layoutManager = LinearLayoutManager(requireContext())
        
        adapter = UsuarioAdapter(emptyList()) { user ->
            confirmDelete(user)
        }
        rv.adapter = adapter
    }

    private fun confirmDelete(user: Usuario) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar Usuario")
            .setMessage("¿Deseas eliminar a ${user.nombre}?")
            .setPositiveButton("Eliminar") { _, _ ->
                val result = repo.deleteUsuario(user.id)
                if (result > 0) {
                    Toast.makeText(requireContext(), "Usuario eliminado", Toast.LENGTH_SHORT).show()
                    loadUsuarios()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun loadUsuarios() {
        val lista = repo.getAllUsuarios()
        adapter.updateData(lista)
    }

    override fun onResume() {
        super.onResume()
        loadUsuarios()
    }
}
