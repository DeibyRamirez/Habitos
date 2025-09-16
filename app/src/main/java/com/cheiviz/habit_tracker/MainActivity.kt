package com.cheiviz.habit_tracker

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cheiviz.habit_tracker.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: HabitoAdapter
    private lateinit var prefs: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = UserPreferences(this)

        // Configurar botones
        binding.btnAgregarHabito.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        binding.btnConfig.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }

        // INICIALIZAR EL RECYCLERVIEW CORRECTAMENTE
        // 1. Configurar el LayoutManager
        binding.recyclerHabitos.layoutManager = LinearLayoutManager(this)

        // 2. INICIALIZAR EL ADAPTER
        adapter = HabitoAdapter(emptyList()) // Inicializar con lista vacía

        // 3. Asignar el adapter al RecyclerView
        binding.recyclerHabitos.adapter = adapter

        // Observar hábitos en DataStore
        lifecycleScope.launch {
            prefs.habitos.collect { lista ->
                adapter.actualizarLista(lista)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}