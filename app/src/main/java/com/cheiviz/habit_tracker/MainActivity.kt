package com.cheiviz.habit_tracker

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cheiviz.habit_tracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAgregarHabito.setOnClickListener {
            intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)

        }

        binding.btnConfig.setOnClickListener {
            intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }

    }
}