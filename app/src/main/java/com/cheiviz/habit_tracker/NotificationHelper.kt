package com.cheiviz.habit_tracker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class NotificationHelper(private val context: Context) {

    private val CHANNEL_ID = "habitos_channel"
    private val CHANNEL_NAME = "Recordatorios de Hábitos"

    init {
        crearCanalNotificaciones()
    }

    private fun crearCanalNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canal para recordatorios de hábitos"
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            }

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun mostrarNotificacion(mensaje: String): Boolean {
        // Verificar explícitamente el permiso antes de proceder
        if (!tienePermisoNotificaciones()) {
            // No tenemos permiso, no podemos mostrar la notificación
            return false
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Recordatorio de hábito")
            .setContentText(mensaje)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        try {
            with(NotificationManagerCompat.from(context)) {
                notify(System.currentTimeMillis().toInt(), builder.build())
            }
            return true
        } catch (e: SecurityException) {
            // Manejar explícitamente la SecurityException
            e.printStackTrace()
            return false
        }
    }

    private fun tienePermisoNotificaciones(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Para Android 13+ necesitamos verificar el permiso POST_NOTIFICATIONS
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // Para versiones anteriores, el permiso se concede automáticamente
            true
        }
    }

    // Método para verificar si necesitamos solicitar el permiso
    fun necesitaSolicitarPermiso(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
    }
}