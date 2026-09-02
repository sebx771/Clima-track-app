package com.example.mantenimiento

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mantenimiento.fragments.*
import com.example.mantenimiento.security.AccessControl
import com.example.mantenimiento.security.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sessionManager = SessionManager(this)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        
        setupBottomNavMenu(bottomNav)
        
        val fragmentToLoad = intent.getStringExtra("FRAGMENT_TO_LOAD")
        
        if (savedInstanceState == null) {
            when (fragmentToLoad) {
                "ordenes" -> {
                    if (AccessControl.canViewOrders(sessionManager.getUserRole())) {
                        loadFragment(OrdenesFragment())
                        bottomNav.selectedItemId = R.id.nav_ordenes
                    } else {
                        loadFragment(DashboardFragment())
                        bottomNav.selectedItemId = R.id.nav_inicio
                    }
                }
                "equipos" -> {
                    loadFragment(EquiposFragment())
                    bottomNav.selectedItemId = R.id.nav_equipos
                }
                "historial" -> {
                    loadFragment(HistorialFragment())
                    bottomNav.selectedItemId = R.id.nav_historial
                }
                "clientes" -> {
                    if (sessionManager.getUserRole() == com.example.mantenimiento.security.Role.ADMIN) {
                        loadFragment(ClientesFragment())
                        bottomNav.selectedItemId = R.id.nav_clientes
                    } else {
                        loadFragment(DashboardFragment())
                        bottomNav.selectedItemId = R.id.nav_inicio
                    }
                }
                "usuarios" -> {
                    if (sessionManager.getUserRole() == com.example.mantenimiento.security.Role.ADMIN) {
                        loadFragment(UsuariosFragment())
                        bottomNav.selectedItemId = R.id.nav_usuarios
                    } else {
                        loadFragment(DashboardFragment())
                        bottomNav.selectedItemId = R.id.nav_inicio
                    }
                }
                else -> {
                    loadFragment(DashboardFragment())
                    bottomNav.selectedItemId = R.id.nav_inicio
                }
            }
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> loadFragment(DashboardFragment())
                R.id.nav_ordenes -> loadFragment(OrdenesFragment())
                R.id.nav_equipos -> loadFragment(EquiposFragment())
                R.id.nav_historial -> loadFragment(HistorialFragment())
                R.id.nav_clientes -> loadFragment(ClientesFragment())
                R.id.nav_usuarios -> loadFragment(UsuariosFragment())
            }
            true
        }
    }

    private fun setupBottomNavMenu(bottomNav: BottomNavigationView) {
        val role = sessionManager.getUserRole()
        val menu = bottomNav.menu
        
        if (!AccessControl.canViewOrders(role)) {
            menu.findItem(R.id.nav_ordenes)?.isVisible = false
        }

        if (role != com.example.mantenimiento.security.Role.ADMIN) {
            menu.findItem(R.id.nav_usuarios)?.isVisible = false
            menu.findItem(R.id.nav_clientes)?.isVisible = false
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
