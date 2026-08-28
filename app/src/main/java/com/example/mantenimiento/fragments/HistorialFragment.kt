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
import com.example.mantenimiento.repository.OrdenRepository
import com.example.mantenimiento.security.Role
import com.example.mantenimiento.security.SessionManager
import java.io.File

class HistorialFragment : Fragment() {

    private lateinit var repoMnt: MantenimientoRepository
    private lateinit var repoOrdenes: OrdenRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var adapter: HistorialAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_historial, container, false)
        
        sessionManager = SessionManager(requireContext())
        repoMnt = MantenimientoRepository(requireContext())
        repoOrdenes = OrdenRepository(requireContext())
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

        // Cargar Foto
        if (!mnt.fotoEvidencia.isNullOrEmpty()) {
            val file = File(mnt.fotoEvidencia)
            if (file.exists()) {
                dialogView.findViewById<TextView>(R.id.tvEvidenciasLabel).visibility = View.VISIBLE
                val ivFoto = dialogView.findViewById<ImageView>(R.id.ivDetalleFoto)
                ivFoto.visibility = View.VISIBLE
                ivFoto.setImageBitmap(BitmapFactory.decodeFile(mnt.fotoEvidencia))
            }
        }

        // Cargar Firma
        if (!mnt.firmaCliente.isNullOrEmpty()) {
            val file = File(mnt.firmaCliente)
            if (file.exists()) {
                dialogView.findViewById<TextView>(R.id.tvFirmaLabel).visibility = View.VISIBLE
                val ivFirma = dialogView.findViewById<ImageView>(R.id.ivDetalleFirma)
                ivFirma.visibility = View.VISIBLE
                ivFirma.setImageBitmap(BitmapFactory.decodeFile(mnt.firmaCliente))
            }
        }

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton(R.string.btn_cerrar, null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        cargarHistorialCombinado()
    }

    private fun cargarHistorialCombinado() {
        val role = sessionManager.getUserRole()
        val empresa = sessionManager.getEmpresaCliente() ?: "ACME S.A.S"

        val listaMnt = if (role == Role.CLIENTE) {
            repoMnt.getMantenimientosByCliente(empresa)
        } else {
            repoMnt.getAllMantenimientos()
        }

        val listaOrdenesFinalizadas = if (role == Role.CLIENTE) {
            repoOrdenes.obtenerOrdenesFinalizadasPorCliente(empresa)
        } else {
            repoOrdenes.obtenerOrdenesFinalizadas()
        }

        // Convertir Órdenes a formato Mantenimiento para la vista
        val listaConvertida = listaOrdenesFinalizadas.map { orden ->
            Mantenimiento(
                id = orden.id,
                equipoId = 0,
                fecha = orden.fecha,
                tipo = orden.tipoServicio,
                descripcion = orden.descripcion,
                observaciones = "Orden Finalizada: ${orden.numero}",
                estadoFinal = orden.estado,
                equipoNombre = orden.equipo
            )
        }

        val total = (listaMnt + listaConvertida).sortedByDescending { it.fecha }
        adapter.updateData(total)
    }
}
