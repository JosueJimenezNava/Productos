package com.hugobelman.basededatossqlite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_existencias.*

class ExistenciasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_existencias)

        var nombreExistencia = "CAMARÓN NUM 7"
        var cantidadExistencia = 10.00
        var costoExitencia = 12.50
        var importeExistencia = cantidadExistencia * costoExitencia
        var lugarPesca = "Laguna del Salado"
        var estadoPesca = "Sinaloa"
        var formaPesca = "Red"
        var transporte = "Camioneta Toyota Roja"

        btnSiguiente.setOnClickListener {
            startActivity(Intent(this, ObservacionesActivity::class.java))
        }

        tvNombreExistenciaValor.text = nombreExistencia
        tvCantidadExistenciaValor.text = cantidadExistencia.toString()
        tvCostoExistenciaValor.text = costoExitencia.toString()
        tvImporteExistenciaValor.text = importeExistencia.toString()
        tvLugarPescaValor.text = lugarPesca
        tvEstadoValor.text = estadoPesca
        tvFormaPescaValor.text = formaPesca
        tvTrasnporteValor.text = transporte
    }
}