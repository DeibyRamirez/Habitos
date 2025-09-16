package com.cheiviz.habit_tracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class HabitoAdapter(private var habitos: List<String>) :
    RecyclerView.Adapter<HabitoAdapter.HabitoViewHolder>() {

    // Clase ViewHolder que representa cada item
    class HabitoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtHabitName: TextView = itemView.findViewById(R.id.txtHabitName)
        val txtHabitTime: TextView = itemView.findViewById(R.id.txtHabitTime)
        val cardView: MaterialCardView = itemView.findViewById(R.id.cardView) // Si necesitas acceder al CardView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitoViewHolder {
        // ✅ Inflar el layout correcto (item_habito.xml)
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.habito_item, parent, false)
        return HabitoViewHolder(view)
    }

    override fun onBindViewHolder(holder: HabitoViewHolder, position: Int) {
        val habito = habitos[position]

        // Configurar los datos en las vistas
        holder.txtHabitName.text = habito
        holder.txtHabitTime.text = "Recordatorio: 8:00 AM" // Puedes personalizar esto

        // Opcional: agregar click listener
        holder.itemView.setOnClickListener {
            // Acción al hacer click en el hábito
        }
    }

    override fun getItemCount(): Int = habitos.size

    // Método para actualizar la lista
    fun actualizarLista(nuevaLista: List<String>) {
        habitos = nuevaLista
        notifyDataSetChanged()
    }
}