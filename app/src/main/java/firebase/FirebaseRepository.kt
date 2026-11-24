package com.android.sistemabodegaappandroid.firebase

import android.util.Log
import com.google.firebase.database.FirebaseDatabase

class FirebaseRepository {

    private val db = FirebaseDatabase
        .getInstance("https://bodega-firebase-default-rtdb.firebaseio.com")
        .reference

    // Método SET: escritura directa en users/{userId}
    fun escribirUsuario(userId: String, nombre: String, email: String, fotoUrl: String) {
        val usuario = mapOf(
            "username" to nombre,
            "email" to email,
            "profile_picture" to fotoUrl
        )
        db.child("users").child(userId).setValue(usuario)
            .addOnSuccessListener { Log.d("FirebaseRepo", "SET ok: users/$userId") }
            .addOnFailureListener { Log.e("FirebaseRepo", "SET falló", it) }
    }

    // Método UPDATE: escribe el mismo producto en dos nodos a la vez (atómico)
    fun escribirProducto(uid: String, sku: String, nombre: String, stock: Int, precio: Int) {
        val producto = mapOf(
            "sku" to sku,
            "nombre" to nombre,
            "stock" to stock,
            "precio" to precio,
            "uid" to uid
        )
        val key = db.child("productos").push().key ?: return
        val updates = hashMapOf<String, Any>(
            "/productos/$key" to producto,
            "/productos-por-usuario/$uid/$key" to producto
        )
        db.updateChildren(updates)
            .addOnSuccessListener { Log.d("FirebaseRepo", "UPDATE ok: $key") }
            .addOnFailureListener { Log.e("FirebaseRepo", "UPDATE falló", it) }
    }
}