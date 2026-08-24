package com.example.mantenimiento

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mantenimiento.fragments.EquiposFragment
import com.example.mantenimiento.fragments.HistorialFragment
import com.example.mantenimiento.fragments.RepuestosFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        
        // Cargar fragment inicial (Equipos) solo la primera vez
        if (savedInstanceState == null) {
            loadFragment(EquiposFragment())
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_equipos -> loadFragment(EquiposFragment())
                R.id.nav_historial -> loadFragment(HistorialFragment())
                R.id.nav_repuestos -> loadFragment(RepuestosFragment())
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