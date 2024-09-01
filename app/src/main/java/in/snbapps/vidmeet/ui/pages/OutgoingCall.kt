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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import `in`.snbapps.vidmeet.R
import `in`.snbapps.vidmeet.api.ApiClient
import `in`.snbapps.vidmeet.api.ApiService
import `in`.snbapps.vidmeet.data.CallType
import `in`.snbapps.vidmeet.data.OutgoingData
import `in`.snbapps.vidmeet.data.User
import `in`.snbapps.vidmeet.ui.theme.VoilaTheme
import `in`.snbapps.vidmeet.ui.theme.call_top
import `in`.snbapps.vidmeet.ui.theme.dark
import `in`.snbapps.vidmeet.ui.theme.focused
import `in`.snbapps.vidmeet.ui.theme.lowOpacWhite
import `in`.snbapps.vidmeet.ui.theme.reject
import `in`.snbapps.vidmeet.ui.theme.tinyOpacWhite
import `in`.snbapps.vidmeet.ui.theme.unfocused
import `in`.snbapps.vidmeet.ui.theme.white
import `in`.snbapps.vidmeet.utils.SharedPrefs
import `in`.snbapps.vidmeet.utils.VoilaFields
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OutgoingCallActivity : ComponentActivity() {

    var callingData: OutgoingData? = null
    var inviterToken: String = ""
    var names: String = ""
    var meetingRoom: String = VoilaFields.generateKey(30)
    var totalReceivers: Int = 0
    var rejectionCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VoilaTheme {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    callingData = intent.getSerializableExtra("callData", OutgoingData::class.java)
                }

                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        inviterToken = task.result

                        if (callingData?.type != null) {
                            if (callingData?.isMultiple == true) {
                                val type = object : TypeToken<ArrayList<User>>() {}.type
                                val receivers = Gson().fromJson<ArrayList<User>>(
                                    callingData?.selectedUsersJson,
                                    type
                                )
                                totalReceivers = receivers?.size ?: 0
                                initiateMeeting(
                                    meetingType = if (callingData?.type == CallType.VIDEO) "video" else "voice",
                                    null,
                                    receivers
                                )
                            } else {
                                callingData?.user?.let {
                                    totalReceivers = 1
                                    initiateMeeting(
                                        meetingType = if (callingData?.type == CallType.VIDEO) "video" else "voice",
                                        receiverToken = callingData?.user?.token,
                                        receivers = null
                                    )
                                }
                            }
                        }
                    }
                }

                OutgoingCallScreen(
                    callingData = callingData!!,
                ) {
                    if (callingData?.isMultiple == true) {
                        val type = object : TypeToken<ArrayList<User>>() {}.type
                        val receivers =
                            Gson().fromJson<ArrayList<User>>(callingData?.selectedUsersJson, type)
                        cancelInvitation(null, receivers)
                    } else {
                        callingData?.user?.let {
                            cancelInvitation(it.token, null)
                        }
                    }
                }
            }
        }
    }

    fun initiateMeeting(
        meetingType: String?,
        receiverToken: String?,
        receivers: ArrayList<User>?
    ) {
        try {
            val tokens = JSONArray()

            receiverToken?.let {
                tokens.put(it)
            }

            receivers?.let {
                val userNames = StringBuilder()
                for (user in it) {
                    tokens.put(user.token)
                    userNames.append("${user.uname}")
                }
                names = userNames.toString()
            }

            val user = SharedPrefs(this@OutgoingCallActivity).getUser()

            val data = JSONObject().apply {
                put(VoilaFields.REMOTE_MSG_TYPE, VoilaFields.REMOTE_MSG_INVITATION)
                put(VoilaFields.REMOTE_MSG_MEETING_TYPE, meetingType)
                put(VoilaFields.FullName, user?.fname.toString())
                put(VoilaFields.Username, user?.uname.toString())
                put(VoilaFields.Email, user?.email.toString())
                put(VoilaFields.Phone, user?.phone.toString())
                put(VoilaFields.About, user?.about.toString())
                put(VoilaFields.Profile_Url, user?.profile.toString())
                put(VoilaFields.UID, user?.uid.toString())
                put(VoilaFields.REMOTE_MSG_INVITER_TOKEN, inviterToken)
                put(VoilaFields.REMOTE_MSG_MEETING_ROOM, meetingRoom)
            }

            val body = JSONObject().apply {
                put(VoilaFields.REMOTE_MSG_DATA, data)
                put(VoilaFields.REMOTE_MSG_REGISTRATION_IDS, tokens)
            }

            sendRemoteMessage(body.toString(), VoilaFields.REMOTE_MSG_INVITATION)
        } catch (exception: Exception) {
            Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    fun sendRemoteMessage(remoteMessageBody: String, type: String) {
        ApiClient.getClient()?.create(ApiService::class.java)
            ?.sendRemoteMessage(
                "voila-sneva",
                VoilaFields.getRemoteMessageHeaders(),
                remoteMessageBody
            )
            ?.enqueue(object : Callback<String?> {
                override fun onResponse(call: Call<String?>, response: Response<String?>) {
                    if (response.isSuccessful) {
                        when (type) {
                            VoilaFields.REMOTE_MSG_INVITATION -> {
                                Toast.makeText(
                                    this@OutgoingCallActivity,
                                    "Invitation sent successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            VoilaFields.REMOTE_MSG_INVITATION_RESPONSE -> {
                                Toast.makeText(
                                    this@OutgoingCallActivity,
                                    "Invitation cancelled",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            }
                        }
                    } else {
                        Toast.makeText(
                            this@OutgoingCallActivity,
                            response.message(),
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }

                override fun onFailure(call: Call<String?>, t: Throwable) {
                    Toast.makeText(this@OutgoingCallActivity, t.message, Toast.LENGTH_SHORT).show()
                    finish()
                }
            })
    }

    fun cancelInvitation(receiverToken: String?, receivers: ArrayList<User>?) {
        try {
            val tokens = JSONArray()

            receiverToken?.let {
                tokens.put(it)
            }

            receivers?.let {
                for (user in it) {
                    tokens.put(user.token)
                }
            }

            val data = JSONObject().apply {
                put(VoilaFields.REMOTE_MSG_TYPE, VoilaFields.REMOTE_MSG_INVITATION_RESPONSE)
                put(
                    VoilaFields.REMOTE_MSG_INVITATION_RESPONSE,
                    VoilaFields.REMOTE_MSG_INVITATION_CANCELLED
                )
            }

            val body = JSONObject().apply {
                put(VoilaFields.REMOTE_MSG_DATA, data)
                put(VoilaFields.REMOTE_MSG_REGISTRATION_IDS, tokens)
            }

            sendRemoteMessage(body.toString(), VoilaFields.REMOTE_MSG_INVITATION_RESPONSE)
        } catch (exception: Exception) {
            Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private val invitationResponseReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val type = intent.getStringExtra(VoilaFields.REMOTE_MSG_INVITATION_RESPONSE)
            if (type != null) {
                when (type) {
                    VoilaFields.REMOTE_MSG_INVITATION_ACCEPTED -> {
                        try {
                            Toast.makeText(context, "Call accepted", Toast.LENGTH_SHORT).show()

//                        val serverURL = URL("https://meet.jit.si")
//
//                        val builder = JitsiMeetConferenceOptions.Builder()
//                        builder.setServerURL(serverURL)
//                        builder.setWelcomePageEnabled(false)
//                        builder.setRoom(meetingRoom)
//                        if (meetingType == "audio") {
//                            builder.setVideoMuted(true)
//                        }
//
//                        JitsiMeetActivity.launch(this@OutgoingInvitationActivity, builder.build())
//                        finish()
                        } catch (exception: Exception) {
                            Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }

                    VoilaFields.REMOTE_MSG_INVITATION_REJECTED -> {
                        rejectionCount += 1
                        if (rejectionCount == totalReceivers) {
                            Toast.makeText(context, "Invitation Rejected", Toast.LENGTH_SHORT)
                                .show()
                            finish()
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(VoilaFields.REMOTE_MSG_INVITATION_RESPONSE)
        registerReceiver(invitationResponseReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(invitationResponseReceiver)
    }
}

@Composable
fun OutgoingCallScreen(
    callingData: OutgoingData,
    cancelCall: () -> Unit
) {
    var name by remember {
        mutableStateOf("")
    }

    var uname by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        name = callingData.user?.fname.toString()
        uname = callingData.user?.uname.toString()
    }

    Image(
        painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(if (callingData.user?.profile?.isEmpty() == true) R.drawable.placeholder else callingData.user?.profile.toString())
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
                            .data(if (callingData.user?.profile?.isEmpty() == true) R.drawable.placeholder else callingData.user?.profile.toString())
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
                        text = name,
                        fontFamily = FontFamily(Font(R.font.readex_pro_medium)),
                        fontSize = 14.sp,
                        color = white,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = uname,
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
                    painter = painterResource(id = if (callingData.type == CallType.VIDEO) R.drawable.video else R.drawable.call),
                    contentDescription = "video",
                    tint = lowOpacWhite,
                    modifier = Modifier
                        .width(18.dp)
                        .height(18.dp)
                )

                Text(
                    text = "Outgoing " + if (callingData.type == CallType.VIDEO) "Video" else "Voice" + " Call",
                    fontFamily = FontFamily(Font(R.font.readex_pro_medium)),
                    fontSize = 14.sp,
                    color = lowOpacWhite,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
            Text(
                text = name,
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
            Spacer(modifier = Modifier.height(40.dp))
            FilledIconButton(
                onClick = {
                    cancelCall()
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
            Spacer(modifier = Modifier.height(70.dp))

            //
        }
    }
}
