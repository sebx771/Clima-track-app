package com.example.mantenimiento

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mantenimiento.fragments.DashboardFragment
import com.example.mantenimiento.fragments.EquiposFragment
import com.example.mantenimiento.fragments.HistorialFragment
import com.example.mantenimiento.fragments.OrdenesFragment
import com.example.mantenimiento.fragments.RepuestosFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        
        // Manejar navegación desde Intent (Dashboard)
        val fragmentToLoad = intent.getStringExtra("FRAGMENT_TO_LOAD")
        
        if (savedInstanceState == null) {
            when (fragmentToLoad) {
                "ordenes" -> {
                    loadFragment(OrdenesFragment())
                    bottomNav.selectedItemId = R.id.nav_ordenes
                }
                "equipos" -> {
                    loadFragment(EquiposFragment())
                    bottomNav.selectedItemId = R.id.nav_equipos
                }
                "historial" -> {
                    loadFragment(HistorialFragment())
                    bottomNav.selectedItemId = R.id.nav_historial
                }
                else -> {
                    loadFragment(DashboardFragment()) // Ahora el Dashboard es el fragmento inicial
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
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}