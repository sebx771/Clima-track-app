package com.example.mantenimiento.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mantenimiento.R
import com.example.mantenimiento.models.Mantenimiento

class HistorialAdapter(
    private var mantenimientos: List<Mantenimiento>,
    private val onItemClick: (Mantenimiento) -> Unit,
) : RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder>() {

    class HistorialViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvEquipo: TextView = view.findViewById(R.id.tvHistorialEquipo)
        val tvFecha: TextView = view.findViewById(R.id.tvHistorialFecha)
        val tvTipo: TextView = view.findViewById(R.id.tvHistorialTipo)
        val tvDesc: TextView = view.findViewById(R.id.tvHistorialDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorialViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_historial, parent, false)
        return HistorialViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistorialViewHolder, position: Int) {
        val mnt = mantenimientos[position]
        holder.tvEquipo.text = mnt.equipoNombre ?: "Equipo Desconocido"
        holder.tvFecha.text = mnt.fecha
        holder.tvTipo.text = mnt.tipo
        holder.tvDesc.text = mnt.descripcion
        
        holder.itemView.setOnClickListener { onItemClick(mnt) }
    }

    override fun getItemCount(): Int = mantenimientos.size

    fun updateData(newMnt: List<Mantenimiento>) {
        this.mantenimientos = newMnt
        notifyDataSetChanged()
    }
}
