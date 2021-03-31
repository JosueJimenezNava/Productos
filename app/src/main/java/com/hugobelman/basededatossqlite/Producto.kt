package com.hugobelman.basededatossqlite

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "productos")
class Producto(
    val nombre:String,
    val precio: Double,
    val descripcion: String,
    val observaciones: String,
    val ubicacion: String,
    val costo: Double,
    val tipo: String,
    val tipoespecie: String,
    val tipoprecio: Int,
    val tipoubiacion: String,
    val pesqueria: String,
    val tipopesca: String,
    val imagen: Int,
    @PrimaryKey(autoGenerate = true)
    var idProducto: Int = 0
) : Serializable