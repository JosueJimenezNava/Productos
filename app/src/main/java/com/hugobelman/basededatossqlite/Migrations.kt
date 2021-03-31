package com.hugobelman.basededatossqlite

import android.content.SharedPreferences
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migrations {
    val MIGRATION_6_7: Migration = object : Migration(6, 7) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // CREATE A TEMPORAL TABLE
            database.execSQL(
                "CREATE TABLE productos_temporal (" +
                        "idProducto INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "nombre TEXT NOT NULL, " +
                        "precio REAL NOT NULL, " +
                        "descripcion TEXT NOT NULL, " +
                        "observaciones TEXT NOT NULL, " +
                        "ubicacion TEXT NOT NULL, " +
                        "costo REAL NOT NULL, " +
                        "tipo TEXT NOT NULL, " +
                        "tipoespecie TEXT NOT NULL, " +
                        "tipoprecio INTEGER NOT NULL, " +
                        "tipoubiacion TEXT NOT NULL, " +
                        "pesqueria TEXT NOT NULL, " +
                        "tipopesca TEXT NOT NULL, " +
                        "imagen INTEGER NOT NULL" +
                        ")"
            )
            /**
             * INSETAMOS LA INFORMACION A LA TABLA TEMPORAL
             */
            database.execSQL(
                "INSERT INTO productos_temporal " +
                        "(idProducto, nombre, precio, descripcion, observaciones, ubicacion, costo, tipo, tipoespecie, tipoprecio, tipoubiacion, pesqueria, tipopesca, imagen) " +
                        "SELECT idProducto, nombre, precio, descripcion, observaciones, ubicacion, costo, tipo, tipoespecie, tipoprecio, '', '', '', imagen FROM productos"
            )
            // DELETE OLD DATABASE
            database.execSQL("DROP TABLE productos")
            // RENAME THE TEMPORAL DATABASE
            database.execSQL("ALTER TABLE productos_temporal RENAME TO productos")


        }

    }
}