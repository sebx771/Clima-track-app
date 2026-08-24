package com.example.mantenimiento.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mantenimiento.R
import com.example.mantenimiento.adapters.HistorialAdapter
import com.example.mantenimiento.models.Mantenimiento
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
        adapter = HistorialAdapter(emptyList()) { mnt ->
            showDetalleDialog(mnt)
        }
        rv.adapter = adapter
    }

    private fun showDetalleDialog(mnt: Mantenimiento) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_detalle_mantenimiento, null)
        
        dialogView.findViewById<TextView>(R.id.tvDetalleEquipo).text = mnt.equipoNombre
        dialogView.findViewById<TextView>(R.id.tvDetalleFechaTipo).text = getString(R.string.fecha_tipo_format, mnt.fecha, mnt.tipo)
        dialogView.findViewById<TextView>(R.id.tvDetalleDesc).text = mnt.descripcion
        dialogView.findViewById<TextView>(R.id.tvDetalleObs).text = mnt.observaciones.ifEmpty { getString(R.string.no_observaciones) }
        dialogView.findViewById<TextView>(R.id.tvDetalleEstado).text = mnt.estadoFinal

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton(R.string.btn_cerrar, null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        val lista = repo.getAllMantenimientos()
        adapter.updateData(lista)
    }
}
