package `in`.snbapps.vidmeet.network

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import `in`.snbapps.vidmeet.R

class ServiceForNotification : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        showNotification(message.getNotification()?.title, message.getNotification()?.body);
    }

    private fun showNotification(title: String?, message: String?) {
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, "Voila Receivers")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.call)
                .setAutoCancel(false)
                .setContentText(message)

        val managerCompat = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        managerCompat.notify(2, builder.build())
    }
}