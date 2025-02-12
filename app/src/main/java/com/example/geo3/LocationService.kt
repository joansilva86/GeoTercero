package com.example.geo3

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.location.Location


import android.os.Binder
import android.os.IBinder
import android.os.Looper
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority


class LocationService : Service() {
    @SuppressLint("MissingPermission")

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest:LocationRequest
    private lateinit var locationCallback: LocationCallback


    private val localBinder = LocalBinder()

    /*entiendo porque tengo que crear la clase pero no entiendo que hace ese get es como un linkeo*/
    inner class LocalBinder : Binder() {
        internal val service: LocationService
            get() = this@LocationService
    }

    private var currentLocation : Location? = null

    override fun onBind(intent: Intent?): IBinder {
        return localBinder
    }

    override fun onCreate() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        /*como me doy cuenta de que tipo es Builder.
        * se que no es un metodo o funcion porque empiza con mayuscula*/
        var builder = LocationRequest.Builder(0)
        builder.setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setIntervalMillis(0)
            .setMaxUpdateDelayMillis(0)
            .setMinUpdateIntervalMillis(0)
            .setMaxUpdateDelayMillis(0)
            .setMinUpdateDistanceMeters(5.0f)
        locationRequest= builder.build()
        class Joan : LocationCallback (){
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                currentLocation=p0.lastLocation
                var intent = Intent("com.example.android.geo3.action.LocationService")
                intent.putExtra("extraInfo",currentLocation)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            }
        }
        locationCallback=Joan()
    }
    fun suscribete(){
        startService(Intent(applicationContext,LocationService::class.java))
        try {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper())
        }
        catch (unlikely: SecurityException){

        }
    }
}