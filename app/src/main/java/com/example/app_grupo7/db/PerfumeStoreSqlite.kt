package com.example.app_grupo7.perfume.store

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.app_grupo7.perfume.db.PerfumeDbHelper

data class PerfumeEntity(
    val id: Long? = null,
    val nombre: String?,
    val precio: Int?,
    val imageUri: String?
)

class PerfumeStoreSqlite(context: Context) {

    private val dbHelper = PerfumeDbHelper(context.applicationContext)

    fun getAll(): List<PerfumeEntity> {
        val db = dbHelper.readableDatabase
        val out = mutableListOf<PerfumeEntity>()
        db.rawQuery(
            """
            SELECT id, nombre, precio, image_uri
            FROM perfumes
            ORDER BY id DESC
            """.trimIndent(),
            null
        ).use { c ->
            while (c.moveToNext()) {
                out += PerfumeEntity(
                    id = c.getLong(0),
                    nombre = c.getString(1),
                    precio = c.getInt(2),
                    imageUri = c.getString(3)
                )
            }
        }
        return out
    }

    fun create(nombre: String, precio: Int, imageUri: String?): Long {
        val db = dbHelper.writableDatabase
        val cv = ContentValues().apply {
            put("nombre", nombre)
            put("precio", precio)
            if (imageUri != null) put("image_uri", imageUri) else putNull("image_uri")
        }
        return db.insert("perfumes", null, cv)
    }

    fun update(id: Long, nombre: String, precio: Int, imageUri: String?) {
        val db = dbHelper.writableDatabase
        val cv = ContentValues().apply {
            put("nombre", nombre)
            put("precio", precio)
            if (imageUri != null) put("image_uri", imageUri) else putNull("image_uri")
        }
        db.update("perfumes", cv, "id = ?", arrayOf(id.toString()))
    }

    fun delete(id: Long) {
        val db = dbHelper.writableDatabase
        db.delete("perfumes", "id = ?", arrayOf(id.toString()))
    }
}
