package com.hugobelman.basededatossqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_nuevo_producto.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NuevoProductoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_producto)

        var idProducto: Int? = null

        if (intent.hasExtra("producto")) {
            val producto = intent.extras?.getSerializable("producto") as Producto

            nombre_et.setText(producto.nombre)
            precio_et.setText(producto.precio.toString())
            descripcion_et.setText(producto.descripcion)
            idProducto = producto.idProducto
        }

        val database = AppDatabase.getDatabase(this)

        save_btn.setOnClickListener {
            val nombre = nombre_et.text.toString()
            val precio = precio_et.text.toString().toDouble()
            val descripcion = descripcion_et.text.toString()
            val observaciones = "Producto de prueba"
            val ubicacion = "A0105"
            val costo = 10.00
            val tipo = "no lo se"
            val tipoespecie = "ESPEICE 1"
            val tipoprecio = 1
            val tipoubiacion = "ANAQUEL 1"
            val tipopesca = "Panga"
            val pesqueria = "WALAMO"

            val producto = Producto(nombre, precio, descripcion, observaciones, ubicacion, costo, tipo, tipoespecie, tipoprecio, tipoubiacion, pesqueria, tipopesca, R.drawable.ic_launcher_background)

            if (idProducto != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    producto.idProducto = idProducto

                    database.productos().update(producto)

                    this@NuevoProductoActivity.finish()
                }
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    database.productos().insertAll(producto)

                    this@NuevoProductoActivity.finish()
                }
            }
        }
    }
}