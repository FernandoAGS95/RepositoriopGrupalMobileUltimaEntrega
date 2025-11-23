package com.example.app_grupo7.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app_grupo7.model.UsuarioUiState
import com.example.app_grupo7.model.UsuarioErrores
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UsuarioViewModel : ViewModel() {

    private val _ui = MutableStateFlow(UsuarioUiState())
    val ui: StateFlow<UsuarioUiState> = _ui

    private val _errores = MutableStateFlow(UsuarioErrores())
    val errores: StateFlow<UsuarioErrores> = _errores

    // --- setters de campos (la UI solo llama esto) ---
    fun onNombreChange(v: String)    { _ui.value = _ui.value.copy(nombre = v) }
    fun onCorreoChange(v: String)    { _ui.value = _ui.value.copy(correo = v) }
    fun onClaveChange(v: String)     { _ui.value = _ui.value.copy(clave = v) }
    fun onDireccionChange(v: String) { _ui.value = _ui.value.copy(direccion = v) }
    fun onAceptaChange(v: Boolean)   { _ui.value = _ui.value.copy(aceptaTerminos = v) }

    // --- validación centralizada ---
    fun validarFormulario(): Boolean {
        val u = _ui.value
        val errs = UsuarioErrores(
            nombre = if (u.nombre.isBlank()) "El nombre es obligatorio" else null,
            correo = when {
                u.correo.isBlank() -> "El correo es obligatorio"
                !u.correo.contains("@") -> "Formato de correo inválido"
                else -> null
            },
            clave = when {
                u.clave.isBlank() -> "La clave es obligatoria"
                u.clave.length < 6 -> "Mínimo 6 caracteres"
                else -> null
            },
            direccion = if (u.direccion.isBlank()) "La dirección es obligatoria" else null,
            aceptaTerminos = if (!u.aceptaTerminos) "Debes aceptar los términos" else null
        )
        _errores.value = errs
        // válido si todos los errores son null
        return listOf(
            errs.nombre, errs.correo, errs.clave, errs.direccion, errs.aceptaTerminos
        ).all { it == null }
    }
}
