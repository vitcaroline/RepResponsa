package com.example.represponsa.core.notification

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Date
import javax.inject.Inject

class RentNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationScheduler: NotificationScheduler
) {
    fun scheduleRentNotification(
        id: Int,
        triggerAt: Date,
        republicName: String,
        amount: Double = 0.0
    ) {
        val title = "Pagamento de aluguel"
        val message = if (amount > 0) {
            "Lembre-se de pagar R$$amount do aluguel da república $republicName."
        } else {
            "Lembre-se de pagar o aluguel da república $republicName."
        }

        notificationScheduler.scheduleNotification(
            id = id,
            triggerAt = triggerAt,
            title = title,
            message = message,
            channelId = "rent_channel"
        )
    }


    fun cancelRentNotification(id: Int) {
        notificationScheduler.cancelNotification(id)
    }
}