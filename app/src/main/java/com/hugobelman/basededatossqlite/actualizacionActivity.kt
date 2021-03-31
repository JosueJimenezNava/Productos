package com.hugobelman.basededatossqlite

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_actualizacion.*

class actualizacionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizacion)

        var sp: SharedPreferences = getSharedPreferences("actualiza", Context.MODE_PRIVATE)
        var varApp = sp.getString("varApp", "")
        var varPlay = sp.getString("varPlay", "")

        tvVersionApp.text = varApp
        tvVersionPlaySotre.text = varPlay

        btnActualizar.setOnClickListener {
            val appPackageName = applicationContext.packageName
            Log.i("nava", "Si entre")
            try {
                val sharingIntent = Intent(Intent.ACTION_VIEW)
                sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                sharingIntent.data = Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                startActivity(Intent.createChooser(sharingIntent, "Presione en el incono para actualizar"))


                Log.i("nava", "Paquete: " + appPackageName)
                //ContextCompat.startActivity(applicationContext, Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")),null)
            } catch (anfe: android.content.ActivityNotFoundException) {
                Log.i("nava", "play")
                ContextCompat.startActivity(
                    applicationContext, Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    ), null
                )
            }
        }

    }

    /**
     * Abre la ficha de la aplicación en el Play Store para poder actualizarla
     */
    fun openPlayStore() {
        val appPackageName = applicationContext.packageName
        Log.i("nava", "Si entre")
        try {
            val sharingIntent = Intent(Intent.ACTION_VIEW)
            sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            sharingIntent.data = Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
            startActivity(Intent.createChooser(sharingIntent, "Presione en el incono para actualizar"))


            Log.i("nava", "Paquete: " + appPackageName)
            //ContextCompat.startActivity(applicationContext, Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")),null)
        } catch (anfe: android.content.ActivityNotFoundException) {
            Log.i("nava", "play")
            ContextCompat.startActivity(
                applicationContext, Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                ), null
            )
        }
    }
}



