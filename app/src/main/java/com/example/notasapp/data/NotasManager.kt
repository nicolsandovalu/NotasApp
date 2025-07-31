package com.example.notasapp.data

import com.example.notasapp.model.Nota

object NotasManager {
     //Lista de notas en memoria
    private val notas = mutableListOf(
         Nota(System.currentTimeMillis(), "Nota", "Contenido de prueba",  categoria = "Personal"),
         Nota(System.currentTimeMillis() +1, "Idea para Proyecto", "Pensar en el diseño de la nueva App",  categoria = "Trabajo"),
         Nota(System.currentTimeMillis() +2, "Lista de compras", "Leche, Tofu, pan, huevo",  categoria = "Compras"),
         Nota(System.currentTimeMillis() +3, "Recordatorio", "Llamar al médico",  categoria = "General")

        )


    //Notas ordenadas por fecha / obtener notas o filtrar por categoría
    fun obtenerNotas(categoria: String? = null): List<Nota> {
        return if (categoria.isNullOrEmpty() || categoria == "Todas") {
            notas.sortedByDescending { it.fechaCreacion }
        } else {
            notas.filter { it.categoria.equals(categoria, ignoreCase = true) }
                .sortedByDescending { it.fechaCreacion }
        }
    }

    fun agregarNota(nota: Nota) {
        notas.add(nota)
    }

    fun eliminarNota(id: Long) {
        notas.removeIf { it.id == id }
    }

    fun buscarNotas(query: String, categoria: String? = null): List<Nota> {
        val filteredByQuery = notas.filter {
            it.titulo.contains(query, ignoreCase = true) ||
            it.contenido.contains(query, ignoreCase = true)
        }
        
        return if (categoria.isNullOrEmpty() || categoria == "Todas") {
            filteredByQuery.sortedByDescending { it.fechaCreacion }
        } else {
            filteredByQuery.filter { it.categoria.equals(categoria, ignoreCase = true)}
                .sortedByDescending { it.fechaCreacion }
        }
    }

    fun obtenerNotasPorId(id: Long): Nota? {
        return notas.find { it.id == id}
    }

    fun actualizarNota(nuevaNota: Nota) {
        val index = notas.indexOfFirst { it.id == nuevaNota.id }
        if (index != -1) {
            notas[index] = nuevaNota
        }
    }
    
    //obtener las categorías únicas, incluyendo todas
    fun obtenerCategoriasUnicas(): List<String> {
        val categorias = mutableSetOf("Todas") //añadir opción por defecto
        notas.forEach { categorias.add(it.categoria) }
        return categorias.toList().sorted() // ordenar alfabéticamente
    }
}