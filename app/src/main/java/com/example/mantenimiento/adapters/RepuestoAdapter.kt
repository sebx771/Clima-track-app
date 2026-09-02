package com.example.mantenimiento.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mantenimiento.R
import com.example.mantenimiento.models.Repuesto

class RepuestoAdapter(
    private var repuestos: List<Repuesto>,
    private val onOptionsClick: (Repuesto, View) -> Unit,
) : RecyclerView.Adapter<RepuestoAdapter.RepuestoViewHolder>() {

    class RepuestoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombreRepuesto)
        val tvCodigo: TextView = view.findViewById(R.id.tvCodigoRepuesto)
        val tvStock: TextView = view.findViewById(R.id.tvStockRepuesto)
        val btnOpciones: ImageView = view.findViewById(R.id.btnOpcionesRepuesto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepuestoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_repuesto, parent, false)
        return RepuestoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepuestoViewHolder, position: Int) {
        val repuesto = repuestos[position]
        holder.tvNombre.text = repuesto.nombre
        holder.tvCodigo.text = holder.itemView.context.getString(R.string.codigo_label, repuesto.codigo)
        holder.tvStock.text = "${repuesto.cantidadDisponible} ${repuesto.unidad ?: ""}"
        
        holder.btnOpciones.setOnClickListener { onOptionsClick(repuesto, it) }
    }

    override fun getItemCount(): Int = repuestos.size

    fun updateData(newRepuestos: List<Repuesto>) {
        this.repuestos = newRepuestos
        notifyDataSetChanged()
    }
}
