package com.example.geo3

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val locationService:LocationService = LocationService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnBoton = findViewById<Button>(R.id.btnBoton)
        val txtLocation: TextView = findViewById<TextView>(R.id.txtCoordenada)
        val btnSalir = findViewById<Button>(R.id.btnSalir)
        val btnBoton2 = findViewById<Button>(R.id.btnBoton2)

        btnBoton.setOnClickListener{

            lifecycleScope.launch {
                txtLocation.text = "voy a obtener el location"
                val result = locationService.getUserLocation(this@MainActivity)
                txtLocation.text = "pase...."
                if(result!=null){
                    txtLocation.text = result.latitude.toString()
                    txtLocation.text = "pase por donde me dieron la latitud"
                }
                else {
                    txtLocation.text = "parece que no hay informacion de la latitud"
                }
            }
        }
        btnBoton2.setOnClickListener {
            txtLocation.text = "cambio el texto"
        }
        btnSalir.setOnClickListener {
            finishAffinity()
        }

    }
}