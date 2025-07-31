package com.example.notasapp

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notasapp.data.NotasManager
import com.example.notasapp.databinding.ActivityDetalleNotaBinding
import com.example.notasapp.model.Nota


class DetalleNotaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleNotaBinding
    private var nota: Nota? = null
    private var notaOriginVacia = false
    private var categoriaActual: String = "General" //Almacenar la categoría

    // Launcher para solicitar el permiso de notificaciones


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleNotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idNota = intent.getLongExtra("nota_id", -1)
        nota = NotasManager.obtenerNotasPorId(idNota)

        if (nota != null) {
            notaOriginVacia = nota!!.titulo.isEmpty() && nota!!.contenido.isEmpty()
            categoriaActual = nota!!.categoria //Cargar categoría

            binding.etTitulo.setText(nota!!.titulo)
            binding.etContenido.setText(nota!!.contenido)

        } else {
            finish()
            return
        }

        //Spinner de categorías en el detalle
        val categoriasDisponibles = NotasManager.obtenerCategoriasUnicas().filter { it != "Todas" } //no mostrar "todas" en el detalle
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoriasDisponibles)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDetalleCategoria.adapter = spinnerAdapter

        //Seleccionar categoria actual
        val posicionCategoria = categoriasDisponibles.indexOf(categoriaActual)
        if (posicionCategoria != -1) {
            binding.spinnerDetalleCategoria.setSelection(posicionCategoria)
        } else {
            if (categoriasDisponibles.isNotEmpty()) {
                categoriaActual = categoriasDisponibles[0]
                binding.spinnerDetalleCategoria.setSelection(0)
            }
        }

        binding.spinnerDetalleCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCategory = parent?.getItemAtPosition(position).toString()
                if (categoriaActual != selectedCategory) { // Solo registrar si la categoría realmente cambia
                    categoriaActual = selectedCategory
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        binding.btnGuardar.setOnClickListener {
            guardarNota()
            Toast.makeText(this, "Nota guardada", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btnEliminar.setOnClickListener {
            nota?.let {
                NotasManager.eliminarNota(it.id)
                Toast.makeText(this, "Nota eliminada", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }


    private fun guardarNota() {
        val titulo = binding.etTitulo.text.toString().trim()
        val contenido = binding.etContenido.text.toString().trim()

        nota?.let { notaActual ->
            if (titulo.isNotEmpty() || contenido.isNotEmpty()) {
                notaActual.titulo = titulo
                notaActual.categoria = categoriaActual // Guardar la categoría seleccionada
                notaActual.contenido = contenido

                NotasManager.actualizarNota(notaActual)

            }
        }
    }

    override fun onPause() {
        super.onPause()
        guardarNota()
    }

}