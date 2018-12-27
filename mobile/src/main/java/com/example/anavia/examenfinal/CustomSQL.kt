package com.example.anavia.examenfinal

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import java.sql.SQLException

class CustomSQL(val context: Context,
                val name: String,
                val factory: SQLiteDatabase.CursorFactory?,
                var version: Int):SQLiteOpenHelper(context,name,factory,version) {


    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE Place(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " name String," +
                " description String," +
                " longitude Double," +
                " latitude Double )"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun insertar(registerPlace: RegisterPlace){
        try {
            val db = this.writableDatabase
            //otra forma mas de crear elemento clave valor
            var cv = ContentValues()
            //debe llamarse igual que la columna de la tabla creada
            cv.put("name", registerPlace.name)
            cv.put("description", registerPlace.description)
            cv.put("latitude", registerPlace.latitude)
            cv.put("longitude", registerPlace.longitude)

            val resultado = db.insert("Place",null,cv)
            db.close()

            val count = listar().size

            if (resultado ==-1L){
                Toast.makeText(context,"No se pudo guardar el registro", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "El lugar ha sido registrado", Toast.LENGTH_SHORT).show()
            }

        }catch (e:SQLException){
            Toast.makeText(context,"Error al guardar el registro ${e.message}", Toast.LENGTH_SHORT).show()
            Log.e("sqlListar", e.message)
        }
    }



    fun listar(): ArrayList<RegisterPlace> {
        var lista = ArrayList<RegisterPlace>()
        try {
            val db = this.writableDatabase
            var cursor: Cursor?
            cursor = db.rawQuery("select * from Place", null)
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(0)
                    val name = cursor.getString(1)
                    val description = cursor.getString(2)
                    val latitude = cursor.getDouble(3)
                    val longitude = cursor.getDouble(4)
                    val position = RegisterPlace(id,name,description, longitude,latitude)
                    lista.add(position)
                } while (cursor.moveToNext())

            }
            db.close()
            return lista
        } catch (e: SQLException) {
            Toast.makeText(context, "Error al listar ${e.message}", Toast.LENGTH_SHORT).show()
        }
        return lista

    }

    fun eliminar(id:Int){
        try {
            val db = this.writableDatabase
            val args = arrayOf(id.toString())
            val resultado = db.delete("Place", "id=?",args)
            db.close()
            if (resultado==0){
                Toast.makeText(context, "no se pudo eliminar", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "eliminado", Toast.LENGTH_SHORT).show()
            }

        }catch (e:SQLException){
            Toast.makeText(context,"Error al eliminar ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    }