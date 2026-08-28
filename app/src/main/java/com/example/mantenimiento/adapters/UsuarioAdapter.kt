package com.example.mantenimiento.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mantenimiento.R
import com.example.mantenimiento.models.Usuario

class UsuarioAdapter(
    private var usuarios: List<Usuario>,
    private val onDeleteClick: (Usuario) -> Unit
) : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    class UsuarioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombreUsuario)
        val tvRol: TextView = view.findViewById(R.id.tvRolUsuario)
        val tvEmail: TextView = view.findViewById(R.id.tvEmailUsuario)
        val btnDelete: ImageView = view.findViewById(R.id.btnDeleteUsuario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_usuario, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val user = usuarios[position]
        holder.tvNombre.text = user.nombre
        holder.tvRol.text = user.rol
        holder.tvEmail.text = user.email
        holder.btnDelete.setOnClickListener { onDeleteClick(user) }
    }

    override fun getItemCount() = usuarios.size

    fun updateData(newList: List<Usuario>) {
        usuarios = newList
        notifyDataSetChanged()
    }
}
