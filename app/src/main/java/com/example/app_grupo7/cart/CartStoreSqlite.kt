package com.example.app_grupo7.cart.store

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.app_grupo7.cart.db.CartDbHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

data class CartItem(
    val perfumeId: String,
    val nombre: String,
    val precio: Int,
    val imageRes: Int? = null,
    val imageUri: String? = null,     // 👈 ahora también soportamos URI
    val quantity: Int = 1
)

interface CartPort {
    val itemsFlow: Flow<List<CartItem>>
    val totalFlow: Flow<Int>
    suspend fun setItems(items: List<CartItem>)
    suspend fun addOrIncrement(newItem: CartItem)
    suspend fun setQuantity(perfumeId: String, qty: Int)
    suspend fun remove(perfumeId: String)
    suspend fun clear()
}

class CartStoreSqlite(context: Context) : CartPort {

    private val dbHelper = CartDbHelper(context.applicationContext)
    private val itemsState = MutableStateFlow<List<CartItem>>(emptyList())

    override val itemsFlow: Flow<List<CartItem>> = itemsState
    override val totalFlow: Flow<Int> = itemsState.map { it.sumOf { ci -> ci.precio * ci.quantity } }

    init { itemsState.value = readAll() }

    override suspend fun setItems(items: List<CartItem>) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            db.delete("cart_items", null, null)
            items.forEach { insertOrReplace(db, it) }
            db.setTransactionSuccessful()
        } finally { db.endTransaction() }
        emitRefresh()
    }

    override suspend fun addOrIncrement(newItem: CartItem) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            val existing = findByPerfumeId(db, newItem.perfumeId)
            if (existing == null) {
                insertOrReplace(db, newItem)
            } else {
                val cv = ContentValues().apply {
                    put("quantity", existing.quantity + newItem.quantity)
                    if (existing.imageUri == null && newItem.imageUri != null) put("image_uri", newItem.imageUri)
                    if (existing.imageRes == null && newItem.imageRes != null) put("image_res", newItem.imageRes)
                }
                db.update("cart_items", cv, "perfume_id = ?", arrayOf(newItem.perfumeId))
            }
            db.setTransactionSuccessful()
        } finally { db.endTransaction() }
        emitRefresh()
    }

    override suspend fun setQuantity(perfumeId: String, qty: Int) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            if (qty <= 0) db.delete("cart_items", "perfume_id = ?", arrayOf(perfumeId))
            else {
                val cv = ContentValues().apply { put("quantity", qty) }
                db.update("cart_items", cv, "perfume_id = ?", arrayOf(perfumeId))
            }
            db.setTransactionSuccessful()
        } finally { db.endTransaction() }
        emitRefresh()
    }

    override suspend fun remove(perfumeId: String) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            db.delete("cart_items", "perfume_id = ?", arrayOf(perfumeId))
            db.setTransactionSuccessful()
        } finally { db.endTransaction() }
        emitRefresh()
    }

    override suspend fun clear() {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            db.delete("cart_items", null, null)
            db.setTransactionSuccessful()
        } finally { db.endTransaction() }
        emitRefresh()
    }

    private fun insertOrReplace(db: SQLiteDatabase, it: CartItem) {
        val cv = ContentValues().apply {
            put("perfume_id", it.perfumeId)
            put("nombre", it.nombre)
            put("precio", it.precio)
            if (it.imageRes != null) put("image_res", it.imageRes) else putNull("image_res")
            if (it.imageUri != null) put("image_uri", it.imageUri) else putNull("image_uri")
            put("quantity", it.quantity)
        }
        db.insertWithOnConflict("cart_items", null, cv, SQLiteDatabase.CONFLICT_REPLACE)
    }

    private fun findByPerfumeId(db: SQLiteDatabase, perfumeId: String): CartItem? {
        db.rawQuery(
            """
            SELECT perfume_id, nombre, precio, image_res, image_uri, quantity
            FROM cart_items WHERE perfume_id = ?
            """.trimIndent(),
            arrayOf(perfumeId)
        ).use { c ->
            return if (c.moveToFirst()) {
                CartItem(
                    perfumeId = c.getString(0),
                    nombre = c.getString(1),
                    precio = c.getInt(2),
                    imageRes = if (!c.isNull(3)) c.getInt(3) else null,
                    imageUri = if (!c.isNull(4)) c.getString(4) else null,
                    quantity = c.getInt(5)
                )
            } else null
        }
    }

    private fun readAll(): List<CartItem> {
        val db = dbHelper.readableDatabase
        val out = mutableListOf<CartItem>()
        db.rawQuery(
            """
            SELECT perfume_id, nombre, precio, image_res, image_uri, quantity
            FROM cart_items ORDER BY id DESC
            """.trimIndent(),
            null
        ).use { c ->
            while (c.moveToNext()) {
                out += CartItem(
                    perfumeId = c.getString(0),
                    nombre = c.getString(1),
                    precio = c.getInt(2),
                    imageRes = if (!c.isNull(3)) c.getInt(3) else null,
                    imageUri = if (!c.isNull(4)) c.getString(4) else null,
                    quantity = c.getInt(5)
                )
            }
        }
        return out
    }

    private fun emitRefresh() {
        itemsState.value = readAll()
    }
}
