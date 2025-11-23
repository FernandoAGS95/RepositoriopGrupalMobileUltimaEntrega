# App Móviles — Catálogo de Perfumes 

**Integrantes:** Nicolás Carvajal - Fernando Gómez 
**Docente:** Nancy Bernal — **Sección:** DSY1105

## Descripción
App Android (Kotlin + Jetpack Compose) con login/registro y catálogo de perfumes.  
Incluye navegación, validaciones centralizadas en ViewModel, animaciones, persistencia local (DataStore) y diseño adaptable.

## Funcionalidades
- Login / Registro con validaciones desde ViewModel (errores por campo + mensajes).
- Catálogo con grilla adaptable (LazyVerticalGrid), imágenes, estados vacíos y animaciones.
- Persistencia local con DataStore
- 2 recursos nativos.

## Tecnologías
- Kotlin, Jetpack Compose, Material 3
- Navigation Compose, StateFlow
- DataStore + Gson

## Ejecutar
1. Abrir en **Android Studio**
2. Hacer **Gradle Sync**
3. Ejecutar el módulo `app` en un emulador o dispositivo Android API 33+

## Estructura (MVVM)
app/src/main/java/com/example/app_grupo7/
├─ data/ (AppState, DataStoreManager)
├─ model/ (Perfume, AuthUiState)
├─ repository/ (PerfumeRepository)
├─ viewmodel/ (AuthViewModel, PerfumeViewModel)
├─ ui/screens/ (Login, Registro, Catalogo, etc.)
└─ navigation/ (AppNav)

## Planificación
Trello: https://trello.com/b/WCtJqr1j