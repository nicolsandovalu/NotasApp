package com.example.notasapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notasapp.R
import com.example.notasapp.model.Nota
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotasAdapter (

    private val onNotaClick: (Nota) -> Unit
) : RecyclerView.Adapter<NotasAdapter.NotaViewHolder>() {

    private val notas = mutableListOf<Nota>()

    fun actualizarNotas(nuevasNotas: List<Nota>) {
        notas.clear()
        notas.addAll(nuevasNotas)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_nota, parent, false)
        return NotaViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        holder.bind(notas[position])
    }

    override fun getItemCount(): Int {
        return notas.size
    }

    inner class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(nota: Nota) {
            val tvTitulo = itemView.findViewById<TextView>(R.id.tvTitulo)
            val tvCategoria = itemView.findViewById<TextView>(R.id.tvCategoria)
            val tvFecha = itemView.findViewById<TextView>(R.id.tvFecha)

            tvTitulo.text = if (nota.titulo.isNotEmpty()) nota.titulo else "Sin título"
            tvCategoria.text = nota.categoria

            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            tvFecha.text = sdf.format(Date(nota.fechaCreacion))

            itemView.setOnClickListener {
                onNotaClick(nota)
            }
        }
    }
}