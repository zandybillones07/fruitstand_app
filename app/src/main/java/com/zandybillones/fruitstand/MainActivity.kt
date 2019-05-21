package com.zandybillones.fruitstand

import android.Manifest
import android.animation.ArgbEvaluator
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import com.zandybillones.traceutil.Trace
import android.animation.ValueAnimator
import android.graphics.Color


private const val PERMISSION_REQUEST = 12

class MainActivity : AppCompatActivity() {


    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    private lateinit var mMessageReceiver:BroadcastReceiver;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        view_products.setOnClickListener {
            val intent = Intent(this, ProductListActivity::class.java)
            startActivity(intent)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(permissions)) {
                initService()
            } else {
                requestPermissions(permissions, PERMISSION_REQUEST)
            }
        } else {
            initService()
        }

        mMessageReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                Trace.show("im in message? " + intent.getStringExtra("MSG_FROM_SERVICE"))
                user_loc.text = intent.getStringExtra("MSG_FROM_SERVICE")
                animateText()

            }
        }

    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, IntentFilter("EVENT"));
    }

    private fun initService() {
        val intent = Intent(this, LocationService::class.java)
        startService(intent)
    }

    private fun checkPermission(permissionArray: Array<String>): Boolean {
        var allSuccess = true
        for (i in permissionArray.indices) {
            if (checkCallingOrSelfPermission(permissionArray[i]) == PackageManager.PERMISSION_DENIED)
                allSuccess = false
        }
        return allSuccess
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST) {
            var allSuccess = true
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    allSuccess = false
                    val requestAgain = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(permissions[i])
                    if (requestAgain) {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Go to settings and enable the permission", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if (allSuccess) {
                initService()
            }


        }
    }

    private fun animateText() {
        val anim = ValueAnimator()
        anim.setIntValues(Color.parseColor("#f44242"), Color.parseColor("#ffffff"))
        anim.setEvaluator(ArgbEvaluator())
        anim.addUpdateListener { valueAnimator -> loc_background.setBackgroundColor(valueAnimator.animatedValue as Int) }
        anim.duration = 1000
        anim.start()
    }
}
