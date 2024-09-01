package `in`.snbapps.vidmeet.network

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import `in`.snbapps.vidmeet.R
import `in`.snbapps.vidmeet.data.User
import `in`.snbapps.vidmeet.ui.pages.IncomingCallActivity
import `in`.snbapps.vidmeet.utils.VoilaFields

class MessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val type = remoteMessage.data[VoilaFields.REMOTE_MSG_TYPE]
        if (type != null) {
            when (type) {
                VoilaFields.REMOTE_MSG_INVITATION -> {
                    showNotification(remoteMessage)

                    val intent =
                        Intent(applicationContext, IncomingCallActivity::class.java).apply {
                            putExtra(
                                VoilaFields.REMOTE_MSG_MEETING_TYPE,
                                remoteMessage.data[VoilaFields.REMOTE_MSG_MEETING_TYPE]
                            )
                            putExtra(
                                "user", User(
                                    email = remoteMessage.data[VoilaFields.Email],
                                    phone = remoteMessage.data[VoilaFields.Phone],
                                    uname = remoteMessage.data[VoilaFields.Username],
                                    fname = remoteMessage.data[VoilaFields.FullName],
                                    profile = remoteMessage.data[VoilaFields.Profile_Url],
                                    uid = remoteMessage.data[VoilaFields.UID],
                                    about = remoteMessage.data[VoilaFields.About],
                                )
                            )
                            putExtra(
                                VoilaFields.REMOTE_MSG_INVITER_TOKEN,
                                remoteMessage.data[VoilaFields.REMOTE_MSG_INVITER_TOKEN]
                            )
                            putExtra(
                                VoilaFields.REMOTE_MSG_MEETING_ROOM,
                                remoteMessage.data[VoilaFields.REMOTE_MSG_MEETING_ROOM]
                            )
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                    startActivity(intent)
                }

                VoilaFields.REMOTE_MSG_INVITATION_RESPONSE -> {
                    val intent = Intent(VoilaFields.REMOTE_MSG_INVITATION_RESPONSE).apply {
                        putExtra(
                            VoilaFields.REMOTE_MSG_INVITATION_RESPONSE,
                            remoteMessage.data[VoilaFields.REMOTE_MSG_INVITATION_RESPONSE]
                        )
                    }
                    sendBroadcast(intent)
                }
            }
        }
    }

    private fun showNotification(remoteMessage: RemoteMessage) {
        val txt =
            "Voila " + if (remoteMessage.data[VoilaFields.REMOTE_MSG_MEETING_TYPE] == "Video") "Video" else "Voice" + " Call"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "Voila",
                "Voila Calling",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = txt
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        val endCallIntent = Intent(this, IncomingCallActivity::class.java).apply {
            action = "END_CALL_ACTION"
        }
        val endCallPendingIntent = PendingIntent.getActivity(
            this,
            0,
            endCallIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, "Voila")
            .setSmallIcon(R.drawable.call)
            .setContentTitle("Voila Calling")
            .setContentText(txt)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .addAction(R.drawable.close, "End Call", endCallPendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(1, notificationBuilder.build())
        }
    }
}
