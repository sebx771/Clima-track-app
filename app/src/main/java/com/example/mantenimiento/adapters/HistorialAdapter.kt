package com.example.mantenimiento.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mantenimiento.R
import com.example.mantenimiento.models.Mantenimiento

class HistorialAdapter(
    private var mantenimientos: List<Mantenimiento>
) : RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder>() {

    class HistorialViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitulo: TextView = view.findViewById(android.R.id.text1)
        val tvSubtitulo: TextView = view.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorialViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return HistorialViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistorialViewHolder, position: Int) {
        val mnt = mantenimientos[position]
        holder.tvTitulo.text = "${mnt.fecha} - ${mnt.tipo}"
        holder.tvSubtitulo.text = mnt.descripcion
    }

    override fun getItemCount(): Int = mantenimientos.size

    fun updateData(newMnt: List<Mantenimiento>) {
        this.mantenimientos = newMnt
        notifyDataSetChanged()
    }
}
