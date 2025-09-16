package com.cheiviz.habit_tracker

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.cheiviz.habit_tracker.databinding.ActivityMain3Binding
import kotlinx.coroutines.launch

class MainActivity3 : AppCompatActivity() {
    private lateinit var binding: ActivityMain3Binding
    private lateinit var prefs: UserPreferences
    private lateinit var switchNotificaciones: Switch
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

    // 🔹 Lanzador para pedir permiso
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            iniciarNotificaciones()
        } else {
            binding.switchNotificaciones.isChecked = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = UserPreferences(this)

        lifecycleScope.launch {
            prefs.notificaciones.collect { activado ->
                // Solo refleja el estado visual del switch
                if (binding.switchNotificaciones.isChecked != activado) {
                    binding.switchNotificaciones.isChecked = activado
                }
            }
        }

        binding.switchNotificaciones.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                prefs.setNotificaciones(isChecked)
                if (isChecked) {
                    verificarPermisoYIniciar()
                } else {
                    detenerNotificaciones()
                }
            }
        }

    }

    private fun verificarPermisoYIniciar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    iniciarNotificaciones()
                }

                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    // 🔹 Aquí podrías mostrar un diálogo explicando por qué tu app necesita notificaciones
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }

                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            iniciarNotificaciones()
        }
    }

    private fun iniciarNotificaciones() {
        runnable = object : Runnable {
            override fun run() {
                NotificationHelper(this@MainActivity3).mostrarNotificacion("¡Es hora de revisar tus hábitos!")
                handler.postDelayed(this, 2 * 60 * 60 * 1000) // cada 2 horas
            }
        }
        handler.post(runnable!!)
    }

    private fun detenerNotificaciones() {
        runnable?.let { handler.removeCallbacks(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        detenerNotificaciones()
    }
}
