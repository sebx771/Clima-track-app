package com.example.mantenimiento.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mantenimiento.R
import com.example.mantenimiento.adapters.HistorialAdapter
import com.example.mantenimiento.models.Mantenimiento
import com.example.mantenimiento.repository.MantenimientoRepository
import com.example.mantenimiento.security.Role
import com.example.mantenimiento.security.SessionManager
import java.io.File

class HistorialFragment : Fragment() {

    private lateinit var repoMnt: MantenimientoRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var adapter: HistorialAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_historial, container, false)
        
        sessionManager = SessionManager(requireContext())
        repoMnt = MantenimientoRepository(requireContext())
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
        dialogView.findViewById<TextView>(R.id.tvDetalleFechaTipo).text = "Fecha: ${mnt.fecha}"
        dialogView.findViewById<TextView>(R.id.tvDetalleDesc).text = mnt.trabajoRealizado
        dialogView.findViewById<TextView>(R.id.tvDetalleObs).text = mnt.observaciones.ifEmpty { "Sin observaciones" }
        dialogView.findViewById<TextView>(R.id.tvDetalleEstado).text = "Finalizado"

        // Cargar Foto de Evidencia
        if (!mnt.fotoEvidencia.isNullOrEmpty()) {
            val file = File(mnt.fotoEvidencia!!)
            if (file.exists()) {
                val ivFoto = dialogView.findViewById<ImageView>(R.id.ivDetalleFoto)
                ivFoto.visibility = View.VISIBLE
                ivFoto.setImageBitmap(BitmapFactory.decodeFile(mnt.fotoEvidencia))
                dialogView.findViewById<TextView>(R.id.tvEvidenciasLabel).visibility = View.VISIBLE
            }
        }

        // Cargar Firma del Cliente
        if (!mnt.firmaCliente.isNullOrEmpty()) {
            val file = File(mnt.firmaCliente!!)
            if (file.exists()) {
                val ivFirma = dialogView.findViewById<ImageView>(R.id.ivDetalleFirma)
                ivFirma.visibility = View.VISIBLE
                ivFirma.setImageBitmap(BitmapFactory.decodeFile(mnt.firmaCliente))
                dialogView.findViewById<TextView>(R.id.tvFirmaLabel).visibility = View.VISIBLE
            }
        }

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Cerrar", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        cargarHistorial()
    }

    private fun cargarHistorial() {
        val role = sessionManager.getUserRole()
        val userId = sessionManager.getUserId()

        val lista = if (role == Role.CLIENTE) {
            repoMnt.getMantenimientosByCliente(userId)
        } else {
            repoMnt.getAllMantenimientos()
        }

        adapter.updateData(lista)
    }
}
