package com.hugobelman.basededatossqlite

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.Jsoup
import java.io.IOException
import kotlin.math.max

class MainActivity : AppCompatActivity() {

    var versionAplicacion = ""
    var versionPlayStore = ""
    private lateinit var database: AppDatabase
    var act = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //if(act == "1") {
            database = Room.databaseBuilder(
                application, AppDatabase::class.java, AppDatabase.DATABASE_NAME
            )
                .addMigrations(Migrations.MIGRATION_6_7)
                .allowMainThreadQueries()
                .build()
        //}

        var listaProductos = emptyList<Producto>()


        //verficaUpDate()
        Log.i("nava", "AquiMain")

        /** Verifico que el telefono tenga Internet*/

        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val actNetInfo = connectivityManager.activeNetworkInfo

        if (actNetInfo != null) {
            checkVersion()
        }

        database.productos().getAll().observe(this, Observer {
            listaProductos = it

            val adapter = ProductosAdapter(this, listaProductos)

            lista.adapter = adapter
        })

        lista.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, ProductoActivity::class.java)
            intent.putExtra("id", listaProductos[position].idProducto)
            startActivity(intent)
        }

        floatingActionButton.setOnClickListener {
            val intent = Intent(this, NuevoProductoActivity::class.java)
            startActivity(intent)
        }
    }


    internal class ServiceUpdateApplication : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String): String? {
            Log.i("nava", "Aquitoy")
            var packageName = params[0] //getApplicationContext().getPackageName()
            Log.i("nava", "PACKAGE NAME: " + packageName)
            var newVersion: String? = null
            //https://play.google.com/store/apps/developer?id=Comisi%C3%B3n+Nacional+de+Acuacultura+y+Pesca
            //https://play.google.com/store/apps/details?id=com.hugobelman.basededatossqlite
            try {
                val document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + packageName)
                    .timeout(3000)
                    .referrer("http://www.google.com")
                    .get()
                if (document != null) {
                    Log.i("nava", "Document: $document")
                    val element = document.getElementsContainingOwnText("Current Version")
                    for (ele in element) {
                        if (ele.siblingElements() != null) {
                            val sibElemets = ele.siblingElements()
                            for (sibElemet in sibElemets) {
                                newVersion = sibElemet.text()
                            }
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            Log.i("nava", "NUEVA VERSIÓN: " + newVersion.toString())
            return newVersion

        }
    }



    /**
     * Comprueba la versión actual con la del Play Store. Si la del Play Store es superior la abre
     * para que puedas actualizarla.
     */

    fun checkVersion() {
        var currentVersion = getCurrentVersion() //Obtiene la versión actual del dispositivo
        versionAplicacion = currentVersion
        val getContact = ServiceUpdateApplication()
        val onlineVersion : String = getContact.execute(applicationContext.packageName).get() //Obtiene la versión de la aplicación en el Play Store
        versionPlayStore = onlineVersion

        //Utils.printLog(TAG, "Current version: $currentVersion PlayStore version: $onlineVersion")
        if (!onlineVersion.isEmpty()) {
            if (isUpdateRequired(currentVersion, onlineVersion)) {
                //Utils.printLog(TAG, "Update is required!!! Current version: $currentVersion PlayStore version: $onlineVersion")
                Log.i(
                    "nava",
                    "Requiere Actualizacion, Version de Movil: $currentVersion Version de PlayStore: $onlineVersion"
                )

                var sp: SharedPreferences = getSharedPreferences("actualiza", Context.MODE_PRIVATE)
                var editorActualiza = sp.edit()
                editorActualiza.putString("varApp", currentVersion)
                editorActualiza.putString("varPlay", onlineVersion)
                editorActualiza.apply()

                startActivity(Intent(this, actualizacionActivity::class.java))



                /*var alertaActualizar = AlertDialog.Builder(this)

                alertaActualizar.setTitle("Hay una nueva versión de la app")
                alertaActualizar.setMessage("Version de tu app: $currentVersion \nVersion en PlayStore: $onlineVersion")
                alertaActualizar.setPositiveButton("ACTUALIZAR") { dialogInterface: DialogInterface, i: Int ->
                    openPlayStore() //Abre la ficha de la aplicación en el Play Store
                }

                alertaActualizar.show()*/
            } else {
                //Utils.printLog(TAG, "Update is NOT required!")
                    Toast.makeText(this, "Version de app: $currentVersion \nVersion en PlayStore:  $onlineVersion \nPor el momento no hay actualización", Toast.LENGTH_LONG).show()
                Log.i("nava", "No es necesario actualizar")
                Log.i(
                    "nava",
                    "Version de Movil: $currentVersion Version de PlayStore: $onlineVersion"
                )
            }
        }
    }

    /**
     * Obtiene la versión de la aplicación instalada en el dispositivo
     */
    fun getCurrentVersion(): String {
        try {
            return applicationContext.getPackageManager().getPackageInfo(
                applicationContext.packageName,
                0
            ).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return ""
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

    /**
     * Comprueba la versión actual con la del Play Store
     * @param versionActual Versión actual instalada en el dispositivo
     * @param versionOnline Versión de la aplicación en el Play Store
     * @return Devuelve true si hay una nueva versión para actualizar
     */
    fun isUpdateRequired(versionActual: String, versionOnline: String): Boolean {
        var listVersionActual = versionActual.split(".")
        var listVersionOnline = versionOnline.split(".")
        val max = max(listVersionActual.size, listVersionOnline.size)
        var i = 0
        while (i < max){
            try {
                if(listVersionActual[i].toInt() < listVersionOnline[i].toInt())
                    return true
            } catch (ex: IndexOutOfBoundsException){
                ex.printStackTrace()
                if(listVersionActual.size < listVersionOnline.size) return true
            } finally {
                i++
            }
        }
        return false
    }
}