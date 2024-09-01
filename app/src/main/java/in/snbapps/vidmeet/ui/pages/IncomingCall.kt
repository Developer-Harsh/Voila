package `in`.snbapps.vidmeet.ui.pages

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import `in`.snbapps.vidmeet.R
import `in`.snbapps.vidmeet.api.ApiClient
import `in`.snbapps.vidmeet.api.ApiService
import `in`.snbapps.vidmeet.data.User
import `in`.snbapps.vidmeet.ui.theme.VoilaTheme
import `in`.snbapps.vidmeet.ui.theme.answer
import `in`.snbapps.vidmeet.ui.theme.call_top
import `in`.snbapps.vidmeet.ui.theme.dark
import `in`.snbapps.vidmeet.ui.theme.focused
import `in`.snbapps.vidmeet.ui.theme.lowOpacWhite
import `in`.snbapps.vidmeet.ui.theme.reject
import `in`.snbapps.vidmeet.ui.theme.tinyOpacWhite
import `in`.snbapps.vidmeet.ui.theme.unfocused
import `in`.snbapps.vidmeet.ui.theme.white
import `in`.snbapps.vidmeet.utils.VoilaFields
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IncomingCallActivity : ComponentActivity() {

    var meetingType: String = ""
    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VoilaTheme {
                meetingType = intent.getStringExtra(VoilaFields.REMOTE_MSG_MEETING_TYPE).toString()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    user = intent.getSerializableExtra("user", User::class.java)
                }

                IncomingCallScreen(meetingType = meetingType, user = user, onCancelCall = {
                    sendInvitationResponse(
                        VoilaFields.REMOTE_MSG_INVITATION_REJECTED,
                        intent.getStringExtra(VoilaFields.REMOTE_MSG_INVITER_TOKEN)
                    )
                }) {
                    sendInvitationResponse(
                        VoilaFields.REMOTE_MSG_INVITATION_ACCEPTED,
                        intent.getStringExtra(VoilaFields.REMOTE_MSG_INVITER_TOKEN)
                    )
                }
            }
        }
    }

    fun sendInvitationResponse(type: String, receiverToken: String?) {
        try {
            val tokens = JSONArray().apply {
                put(receiverToken)
            }

            val data = JSONObject().apply {
                put(VoilaFields.REMOTE_MSG_TYPE, VoilaFields.REMOTE_MSG_INVITATION_RESPONSE)
                put(VoilaFields.REMOTE_MSG_INVITATION_RESPONSE, type)
            }

            val body = JSONObject().apply {
                put(VoilaFields.REMOTE_MSG_DATA, data)
                put(VoilaFields.REMOTE_MSG_REGISTRATION_IDS, tokens)
            }

            sendRemoteMessage(body.toString(), type)
        } catch (exception: Exception) {
            Toast.makeText(this@IncomingCallActivity, exception.message, Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun sendRemoteMessage(remoteMessageBody: String, type: String) {
        ApiClient.getClient()?.create(ApiService::class.java)
            ?.sendRemoteMessage(
                "voila-sneva",
                VoilaFields.getRemoteMessageHeaders(),
                remoteMessageBody
            )
            ?.enqueue(object : Callback<String?> {
                override fun onResponse(call: Call<String?>, response: Response<String?>) {
                    if (response.isSuccessful) {
                        if (type == VoilaFields.REMOTE_MSG_INVITATION_ACCEPTED) {
                            try {
//                                val serverURL = URL("https://meet.jit.si")
//                                val builder = JitsiMeetConferenceOptions.Builder()
//                                builder.setServerURL(serverURL)
//                                builder.setWelcomePageEnabled(false)
//                                builder.setRoom(intent.getStringExtra(VoilaFields.REMOTE_MSG_MEETING_ROOM))
//
//                                if (meetingType == "audio") {
//                                    builder.setVideoMuted(true)
//                                }
//
//                                JitsiMeetActivity.launch(this@IncomingInvitationActivity, builder.build())
//                                finish()
                            } catch (exception: Exception) {
                                Toast.makeText(
                                    this@IncomingCallActivity,
                                    exception.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            }
                        } else {
                            Toast.makeText(
                                this@IncomingCallActivity,
                                "Invitation Rejected",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                    } else {
                        Toast.makeText(
                            this@IncomingCallActivity,
                            response.message(),
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }

                override fun onFailure(call: Call<String?>, t: Throwable) {
                    Toast.makeText(this@IncomingCallActivity, t.message, Toast.LENGTH_SHORT).show()
                    finish()
                }
            })
    }

    private val invitationResponseReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val type = intent.getStringExtra(VoilaFields.REMOTE_MSG_INVITATION_RESPONSE)
            if (type != null) {
                if (type == VoilaFields.REMOTE_MSG_INVITATION_CANCELLED) {
                    Toast.makeText(context, "Invitation Accepted", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(VoilaFields.REMOTE_MSG_INVITATION_RESPONSE)
        registerReceiver(invitationResponseReceiver, filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(invitationResponseReceiver)
    }
}

@Composable
fun IncomingCallScreen(
    meetingType: String,
    user: User?,
    onCancelCall: () -> Unit,
    onAnswerCall: () -> Unit
) {
    Image(
        painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(if (user?.profile?.isEmpty() == true) R.drawable.placeholder else user?.profile.toString())
                .placeholder(R.drawable.placeholder)
                .size(coil.size.Size.ORIGINAL)
                .build(),
            contentScale = ContentScale.Crop,
        ),
        contentDescription = "image",
        modifier = Modifier.blur(25.dp),
        contentScale = ContentScale.Crop,
        colorFilter = ColorFilter.tint(call_top, blendMode = BlendMode.Darken)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 80.dp)
    ) {
        Box(modifier = Modifier.padding(start = 30.dp, end = 30.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(call_top, RoundedCornerShape(50.dp))
                    .padding(horizontal = 15.dp, vertical = 15.dp)
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(if (user?.profile?.isEmpty() == true) R.drawable.placeholder else user?.profile.toString())
                            .placeholder(R.drawable.placeholder)
                            .size(coil.size.Size.ORIGINAL)
                            .build(),
                        contentScale = ContentScale.Crop,
                    ),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp)
                        .clip(CircleShape)
                )
                Column(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = user?.fname.toString(),
                        fontFamily = FontFamily(Font(R.font.readex_pro_medium)),
                        fontSize = 14.sp,
                        color = white,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = user?.uname.toString(),
                        fontFamily = FontFamily(Font(R.font.readex_pro)),
                        fontSize = 14.sp,
                        color = lowOpacWhite,
                        textAlign = TextAlign.Center,
                    )
                }
                IconButton(
                    onClick = {
                        Log.e("back", "back")
                    },
                    modifier = Modifier
                        .background(tinyOpacWhite, CircleShape)
                        .border(1.dp, tinyOpacWhite, CircleShape)
                        .height(40.dp)
                        .width(40.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.down),
                        contentDescription = "back",
                        tint = dark
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .background(call_top, RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .padding(horizontal = 15.dp, vertical = 30.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = if (meetingType == "Video") R.drawable.video else R.drawable.call),
                    contentDescription = "video",
                    tint = lowOpacWhite,
                    modifier = Modifier
                        .width(18.dp)
                        .height(18.dp)
                )
                Text(
                    text = "Incoming " + if (meetingType == "Video") "Video" else "Voice" + " Call",
                    fontFamily = FontFamily(Font(R.font.readex_pro_medium)),
                    fontSize = 14.sp,
                    color = lowOpacWhite,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
            Text(
                text = user?.fname.toString(),
                fontFamily = FontFamily(Font(R.font.readex_pro_semibold)),
                fontSize = 20.sp,
                color = white,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 15.dp)
            )
            Text(
                text = "00:30 time",
                fontFamily = FontFamily(Font(R.font.readex_pro)),
                fontSize = 14.sp,
                color = lowOpacWhite,
                textAlign = TextAlign.Center,
            )
            Row(
                modifier = Modifier.padding(
                    top = 40.dp,
                    bottom = 70.dp,
                    start = 60.dp,
                    end = 60.dp
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledIconButton(
                    onClick = {
                        onCancelCall()
                    },
                    colors = IconButtonColors(
                        containerColor = answer,
                        contentColor = white,
                        unfocused,
                        focused
                    ),
                    modifier = Modifier
                        .height(70.dp)
                        .width(70.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.call),
                        contentDescription = "answer"
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                FilledIconButton(
                    onClick = {
                        onAnswerCall()
                    },
                    colors = IconButtonColors(
                        containerColor = reject,
                        contentColor = white,
                        unfocused,
                        focused
                    ),
                    modifier = Modifier
                        .height(70.dp)
                        .width(70.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.call),
                        contentDescription = "answer",
                        modifier = Modifier.rotate(135f)
                    )
                }
            }
        }
    }
}