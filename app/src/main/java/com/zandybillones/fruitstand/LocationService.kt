package com.zandybillones.fruitstand

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import com.zandybillones.traceutil.Trace
import java.util.*
import android.support.v4.content.LocalBroadcastManager
import android.text.TextUtils




const val EXTRA_RETURN_MESSAGE = "MSG_FROM_SERVICE"
private const val DELAY = 10000L

class LocationService : Service() {


    private var locationNetwork: Location? = null
    private var locationGps: Location? = null
    private var hasGps = false
    private var hasNetwork = false

    lateinit var locationManager: LocationManager

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        getUserLocation()

    }

    override fun onDestroy() {
        Trace.show("im in")
        super.onDestroy()
    }


    private fun sendBroadcast(message: String) {
        val it = Intent("EVENT")

        if (!TextUtils.isEmpty(message)) {
            it.putExtra(EXTRA_RETURN_MESSAGE, message)
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(it)
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation() {

        if (hasGps || hasNetwork) {

            if (hasNetwork) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, DELAY, 0F, object :
                    LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        if (location != null) {
                            locationNetwork = location

                            val message = "Net Lat: " + locationNetwork!!.latitude + "\n" +
                                    "Net Lng: " + locationNetwork!!.longitude

                            sendBroadcast(message)
                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                    }

                    override fun onProviderEnabled(provider: String?) {

                    }

                    override fun onProviderDisabled(provider: String?) {

                    }

                })

                val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (localNetworkLocation != null)
                    locationNetwork = localNetworkLocation
            }

            if (hasGps) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, DELAY, 0F, object :
                    LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        if (location != null) {
                            locationGps = location

                            val message = "GPS Lat: " + locationGps!!.latitude + "\n" +
                                    "GPS Lng: " + locationGps!!.longitude

                            sendBroadcast(message)
                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                    }

                    override fun onProviderEnabled(provider: String?) {

                    }

                    override fun onProviderDisabled(provider: String?) {

                    }

                })

                val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                if (localGpsLocation != null) {
                    locationGps = localGpsLocation
                }
            }

            if (locationGps!= null && locationNetwork!= null) {

                if (locationGps!!.accuracy > locationNetwork!!.accuracy) {

                    val message = "Net Lat: " + locationNetwork!!.latitude + "\n" +
                            "Net Lng: " + locationNetwork!!.longitude

                    sendBroadcast(message)
                } else {
                    val message = "GPS Lat: " + locationGps!!.latitude + "\n" +
                            "GPS Lng: " + locationGps!!.longitude

                    sendBroadcast(message)
                }
            }

        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

}
