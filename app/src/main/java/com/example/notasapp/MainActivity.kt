package com.example.notasapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notasapp.adapter.NotasAdapter
import com.example.notasapp.data.NotasManager
import com.example.notasapp.databinding.ActivityMainBinding
import com.example.notasapp.model.Nota

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NotasAdapter
    private var categoriaSeleccionada: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = NotasAdapter { nota ->
            val intent = Intent(this, DetalleNotaActivity::class.java)
            intent.putExtra("nota_id", nota.id)
            startActivity(intent)
        }

        binding.rvNotas.layoutManager = LinearLayoutManager(this)
        binding.rvNotas.adapter = adapter

        binding.fabAgregarNota.setOnClickListener {
            val nuevaNotaId = System.currentTimeMillis()
            val categoriaInicialNuevaNota = if (categoriaSeleccionada == "Todas" || categoriaSeleccionada.isNullOrEmpty()){
                "General"
            } else {
                categoriaSeleccionada!!
            }
            val nuevaNota = Nota(nuevaNotaId, "", "", categoria = categoriaInicialNuevaNota)

            //Guardar nota temporal
            NotasManager.agregarNota(nuevaNota)

            val intent = Intent(this, DetalleNotaActivity::class.java)
            intent.putExtra("nota_id", nuevaNota.id)
            startActivity(intent)
        }

        //Configurar spinner
        val categorias = NotasManager.obtenerCategoriasUnicas()
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategorias.adapter = spinnerAdapter

        val posicionTodas = categorias.indexOf("Todas")
        if (posicionTodas != -1) {
            binding.spinnerCategorias.setSelection(posicionTodas)
        }

        binding.spinnerCategorias.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                categoriaSeleccionada = parent?.getItemAtPosition(position).toString()
                aplicarFiltros() //Categoria seleccionada
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                categoriaSeleccionada = null //No hay categoria seleccionada
                aplicarFiltros()
            }
        }

        //Configurar búsqueda

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                aplicarFiltros(query.orEmpty()) //aplicar filtro al enviar la busqueda
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                aplicarFiltros(newText.orEmpty()) //aplicar filtro al cambiar texto de búsqueda
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        adapter.actualizarNotas(NotasManager.obtenerNotas())
    }

    private fun aplicarFiltros(query: String = binding.searchView.query.toString()) {
        val notasFiltradas = NotasManager.buscarNotas(query, categoriaSeleccionada)
        adapter.actualizarNotas(notasFiltradas)
    }
}