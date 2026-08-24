package com.example.mantenimiento.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mantenimiento.R
import com.example.mantenimiento.models.Equipo

class EquipoAdapter(
    private var equipos: List<Equipo>,
    private val onItemClick: (Equipo) -> Unit,
) : RecyclerView.Adapter<EquipoAdapter.EquipoViewHolder>() {

    class EquipoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombreEquipo)
        val tvMarcaModelo: TextView = view.findViewById(R.id.tvMarcaModelo)
        val tvUbicacion: TextView = view.findViewById(R.id.tvUbicacion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_equipo, parent, false)
        return EquipoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EquipoViewHolder, position: Int) {
        val equipo = equipos[position]
        holder.tvNombre.text = equipo.nombre
        holder.tvMarcaModelo.text = "${equipo.marca} - ${equipo.modelo}"
        holder.tvUbicacion.text = equipo.ubicacion
        
        holder.itemView.setOnClickListener { onItemClick(equipo) }
    }

    override fun getItemCount(): Int = equipos.size

    fun updateData(newEquipos: List<Equipo>) {
        this.equipos = newEquipos
        notifyDataSetChanged()
    }
}
