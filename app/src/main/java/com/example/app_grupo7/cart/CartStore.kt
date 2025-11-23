package com.example.app_grupo7.cart

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.app_grupo7.cart.CartItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.cartDataStore by preferencesDataStore(name = "cart_store")

class CartStore(
    private val context: Context,
    private val gson: Gson = Gson()
) {
    private val KEY_JSON = stringPreferencesKey("cart_json")

    val itemsFlow: Flow<List<CartItem>> = context.cartDataStore.data.map { prefs ->
        val json = prefs[KEY_JSON] ?: "[]"
        val type = object : TypeToken<List<CartItem>>() {}.type
        runCatching { gson.fromJson<List<CartItem>>(json, type) }.getOrElse { emptyList() }
    }

    suspend fun setItems(items: List<CartItem>) {
        val json = gson.toJson(items)
        context.cartDataStore.edit { it[KEY_JSON] = json }
    }

    suspend fun addOrIncrement(newItem: CartItem) {
        val current = itemsFlow.first()
        val updated = current.toMutableList()
        val idx = updated.indexOfFirst { it.perfumeId == newItem.perfumeId }
        if (idx == -1) updated += newItem
        else updated[idx] = updated[idx].copy(quantity = updated[idx].quantity + newItem.quantity)
        setItems(updated)
    }

    suspend fun setQuantity(perfumeId: String, qty: Int) {
        val current = itemsFlow.first()
        val updated = current.mapNotNull {
            if (it.perfumeId != perfumeId) it
            else if (qty <= 0) null
            else it.copy(quantity = qty)
        }
        setItems(updated)
    }

    suspend fun remove(perfumeId: String) {
        val current = itemsFlow.first()
        setItems(current.filterNot { it.perfumeId == perfumeId })
    }

    suspend fun clear() = setItems(emptyList())

    val totalFlow: Flow<Int> = itemsFlow.map { list -> list.sumOf { it.precio * it.quantity } }
}
