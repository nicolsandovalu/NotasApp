package com.example.notasapp.model

data class Nota(

    val id: Long,
    var titulo: String,
    var contenido: String,
    val fechaCreacion: Long = System.currentTimeMillis(),
    var categoria: String = "General"

)