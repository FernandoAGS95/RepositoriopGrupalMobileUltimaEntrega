package com.example.app_grupo7.cart.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CartDbHelper(context: Context) : SQLiteOpenHelper(
    context, "cart.db", null, 2
) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE cart_items(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                perfume_id TEXT NOT NULL UNIQUE,
                nombre TEXT NOT NULL,
                precio INTEGER NOT NULL,
                image_res INTEGER,
                image_uri TEXT,              
                quantity INTEGER NOT NULL
            );
            """.trimIndent()
        )
        db.execSQL("CREATE UNIQUE INDEX idx_cart_perfume ON cart_items(perfume_id);")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE cart_items ADD COLUMN image_uri TEXT")
        }
    }
}
