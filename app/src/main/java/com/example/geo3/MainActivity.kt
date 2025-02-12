package com.example.geo3

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {

    private var locationService:LocationService? = null
    private lateinit var txtLocation:TextView
    private lateinit var btnBoton:Button
    private lateinit var btnBoton2:Button
    private lateinit var btnSalir:Button

    private var unServiceConexion = MiServiceConexion()

    private lateinit var unBroadcastReceiver:MiBroadcastReceiver

    private inner class MiServiceConexion: ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as LocationService.LocalBinder
            locationService = binder.service
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            TODO("Not yet implemented")
        }

    }
    private inner class MiBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val location = intent.getParcelableExtra<Location>("extraInfo")

            if (location != null) {
                logResultsToScreen(location.latitude, location.longitude)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            unBroadcastReceiver, IntentFilter("com.example.android.geo3.action.LocationService")
        )
    }

    override fun onStart() {
        super.onStart()
        val unIntent = Intent(this,LocationService::class.java)
        bindService(unIntent,unServiceConexion,Context.BIND_AUTO_CREATE)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        unBroadcastReceiver = MiBroadcastReceiver()
        btnBoton = findViewById<Button>(R.id.btnBoton)
        txtLocation = findViewById<TextView>(R.id.txtCoordenada)
        btnSalir = findViewById<Button>(R.id.btnSalir)
        btnBoton2 = findViewById<Button>(R.id.btnBoton2)

        btnBoton.setOnClickListener{
                txtLocation.text = "voy a obtener el location"
                locationService?.suscribete()
                txtLocation.text = "pase...."
                btnBoton.visibility= View.INVISIBLE

        }
        btnBoton2.setOnClickListener {
            txtLocation.text = "cambio el texto"
        }
        btnSalir.setOnClickListener {
            finishAffinity()
        }

    }



    private fun logResultsToScreen(latitud: Double, longitud:Double) {
        var latitudS = " Lat:  ${latitud.toString()}"
        var longitudS = " Lon: ${longitud.toString()}"
        var tiempoS = "T : ${LocalDateTime.now().minute.toString()} : ${LocalDateTime.now().second.toString()}"
        val outputWithPreviousLogs = "$latitudS $longitudS  $tiempoS\n${txtLocation.text}"
        txtLocation.text = outputWithPreviousLogs + "\n"
    }
}