package com.example.mantenimiento.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mantenimiento.databinding.ItemOrdenBinding
import com.example.mantenimiento.models.Orden

class OrdenesAdapter(
    private var listaOrdenes: List<Orden>,
    private val onItemClick: (Orden) -> Unit
) : RecyclerView.Adapter<OrdenesAdapter.OrdenViewHolder>() {

    inner class OrdenViewHolder(val binding: ItemOrdenBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdenViewHolder {
        val binding = ItemOrdenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrdenViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrdenViewHolder, position: Int) {
        val orden = listaOrdenes[position]

        with(holder.binding) {
            tvNumeroOrden.text = orden.numero
            tvEstado.text = orden.estado
            tvCliente.text = orden.cliente
            tvEquipo.text = "Equipo: ${orden.equipo}"
            tvTipoServicio.text = orden.tipoServicio
            tvFecha.text = orden.fecha

            // Evento al presionar la tarjeta (para navegar al detalle o registro)
            root.setOnClickListener {
                onItemClick(orden)
            }
        }
    }

    override fun getItemCount(): Int = listaOrdenes.size

    // Función para refrescar los datos cuando usemos los filtros
    fun actualizarLista(nuevaLista: List<Orden>) {
        listaOrdenes = nuevaLista
        notifyDataSetChanged()
    }
}