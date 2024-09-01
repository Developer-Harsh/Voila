package `in`.snbapps.vidmeet.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.random.Random

fun getTimeAgo(timestamp: Long): String {
    val currentTime = System.currentTimeMillis()
    val diff = currentTime - timestamp

    return when {
        diff < TimeUnit.MINUTES.toMillis(1) -> "Just Now"
        diff < TimeUnit.HOURS.toMillis(1) -> {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
            "$minutes min ago"
        }

        diff < TimeUnit.DAYS.toMillis(1) -> {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            sdf.format(Date(timestamp))
        }

        diff < TimeUnit.DAYS.toMillis(2) -> "Yesterday"
        diff < TimeUnit.DAYS.toMillis(7) -> {
            val days = TimeUnit.MILLISECONDS.toDays(diff)
            "$days days ago"
        }

        diff < TimeUnit.DAYS.toMillis(30) -> {
            val weeks = diff / TimeUnit.DAYS.toMillis(7)
            if (weeks == 1L) "Last Week" else "$weeks weeks ago"
        }

        diff < TimeUnit.DAYS.toMillis(365) -> {
            val months = diff / TimeUnit.DAYS.toMillis(30)
            if (months == 1L) "Last Month" else "$months months ago"
        }

        else -> {
            val years = diff / TimeUnit.DAYS.toMillis(365)
            if (years == 1L) "Last Year" else "$years years ago"
        }
    }
}

fun getRelativeTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < TimeUnit.DAYS.toMillis(1) -> "Today"
        diff < TimeUnit.DAYS.toMillis(2) -> "Yesterday"
        diff < TimeUnit.DAYS.toMillis(30) -> "${TimeUnit.MILLISECONDS.toDays(diff)} days ago"
        diff < TimeUnit.DAYS.toMillis(365) -> "${TimeUnit.MILLISECONDS.toDays(diff) / 30} months ago"
        else -> "${TimeUnit.MILLISECONDS.toDays(diff) / 365} years ago"
    }
}

fun generateRandomTimestamp(count: Int): List<Long> {
    val timestamps = mutableListOf<Long>()
    val currentTime = System.currentTimeMillis()
    val baseTime = currentTime - 30 * 24 * 60 * 60 * 1000L // Start from 30 days ago

    for (i in 0 until count) {
        val randomOffset =
            Random.nextLong(0, 2 * 24 * 60 * 60 * 1000L) // Random offset up to 2 days
        val timestamp = baseTime + i * (2 * 24 * 60 * 60 * 1000L) + randomOffset
        timestamps.add(timestamp)
    }

    return timestamps.sorted()
}