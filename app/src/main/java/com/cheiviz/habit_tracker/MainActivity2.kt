package com.cheiviz.habit_tracker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.cheiviz.habit_tracker.databinding.ActivityMain2Binding
import kotlinx.coroutines.launch

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    private lateinit var prefs: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ INICIALIZAR prefs
        prefs = UserPreferences(this)

        binding.btnGuardarHabito.setOnClickListener {
            val habito = binding.editTextHabito.text.toString()
            if (habito.isNotEmpty()) {
                lifecycleScope.launch {
                    prefs.agregarHabito(habito)
                    binding.editTextHabito.text.clear()
                    Toast.makeText(this@MainActivity2, "Hábito guardado", Toast.LENGTH_SHORT).show()

                    // Regresar a MainActivity
                    startActivity(Intent(this@MainActivity2, MainActivity::class.java))
                    finish()
                }
            } else {
                Toast.makeText(this, "Ingrese un hábito", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}