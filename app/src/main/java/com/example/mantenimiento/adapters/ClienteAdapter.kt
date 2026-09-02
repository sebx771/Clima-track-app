package com.example.mantenimiento.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mantenimiento.R
import com.example.mantenimiento.models.Cliente

class ClienteAdapter(
    private var clientes: List<Cliente>,
    private val onEditClick: (Cliente) -> Unit,
    private val onDeleteClick: (Cliente) -> Unit
) : RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

    class ClienteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombreCliente)
        val tvTel: TextView = view.findViewById(R.id.tvTelefonoCliente)
        val tvDir: TextView = view.findViewById(R.id.tvDireccionCliente)
        val btnEdit: ImageView = view.findViewById(R.id.btnEditCliente)
        val btnDelete: ImageView = view.findViewById(R.id.btnDeleteCliente)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cliente, parent, false)
        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = clientes[position]
        holder.tvNombre.text = cliente.nombre
        holder.tvTel.text = "Tel: ${cliente.telefono}"
        holder.tvDir.text = cliente.direccion

        holder.btnEdit.setOnClickListener { onEditClick(cliente) }
        holder.btnDelete.setOnClickListener { onDeleteClick(cliente) }
    }

    override fun getItemCount(): Int = clientes.size

    fun updateData(newList: List<Cliente>) {
        this.clientes = newList
        notifyDataSetChanged()
    }
}
