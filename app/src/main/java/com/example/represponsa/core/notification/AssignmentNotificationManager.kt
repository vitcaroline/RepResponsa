package com.example.represponsa.core.notification

import java.util.Date
import javax.inject.Inject

class AssignmentNotificationManager @Inject constructor(
    private val notificationScheduler: NotificationScheduler
) {
    fun scheduleNotification(
        id: Int,
        triggerAt: Date,
        title: String,
        message: String,
        channelId: String
    ) {
        notificationScheduler.scheduleNotification(id, triggerAt, title, message, channelId)
    }
}