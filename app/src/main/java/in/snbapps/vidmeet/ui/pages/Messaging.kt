package `in`.snbapps.vidmeet.ui.pages

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import `in`.snbapps.vidmeet.R
import `in`.snbapps.vidmeet.data.CallType
import `in`.snbapps.vidmeet.data.Chat
import `in`.snbapps.vidmeet.data.LastMessage
import `in`.snbapps.vidmeet.data.MediaContent
import `in`.snbapps.vidmeet.data.MediaType
import `in`.snbapps.vidmeet.data.Message
import `in`.snbapps.vidmeet.data.MessageStatus
import `in`.snbapps.vidmeet.data.OutgoingData
import `in`.snbapps.vidmeet.data.Participants
import `in`.snbapps.vidmeet.data.User
import `in`.snbapps.vidmeet.ui.theme.VoilaTheme
import `in`.snbapps.vidmeet.ui.theme.dark
import `in`.snbapps.vidmeet.ui.theme.focused
import `in`.snbapps.vidmeet.ui.theme.high
import `in`.snbapps.vidmeet.ui.theme.low
import `in`.snbapps.vidmeet.ui.theme.medium
import `in`.snbapps.vidmeet.ui.theme.unfocused
import `in`.snbapps.vidmeet.ui.theme.white
import `in`.snbapps.vidmeet.ui.theme.yellow
import `in`.snbapps.vidmeet.utils.SharedPrefs
import `in`.snbapps.vidmeet.utils.VoiceRecorder
import `in`.snbapps.vidmeet.utils.VoilaFields
import `in`.snbapps.vidmeet.utils.getRelativeTime
import kotlinx.coroutines.delay
import java.util.Date

class MessagingActivity : ComponentActivity() {

    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VoilaTheme {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    user = intent.getSerializableExtra("user", User::class.java)
                }

                MessagingScreen(user = user!!)
            }
        }
    }
}

@Composable
fun MessagingScreen(user: User) {

    val context = LocalContext.current

    var message by remember {
        mutableStateOf(TextFieldValue(""))
    }

    val isMultiple by remember {
        mutableStateOf(false)
    }

    var replyTo by remember {
        mutableStateOf("")
    }

    val messages = remember {
        mutableStateListOf<Message>()
    }

    var showReply by remember {
        mutableStateOf(false)
    }

    var isRecording by remember {
        mutableStateOf(false)
    }

    val buttonInteractionSource = remember { MutableInteractionSource() }
    val recorder = VoiceRecorder(context, VoilaFields.generateKey(26))

    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.size > 0) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val groupedMessages = remember(messages) {
        mutableStateOf(
            messages.groupBy { message ->
                getRelativeTime(message.timeStamp!!)
            }
        )
    }

    LaunchedEffect(Unit) {
        FirebaseFirestore.getInstance().collection(VoilaFields.CHATS)
            .document("${SharedPrefs(context).getUser()?.uid}-${user.uid}")
            .collection(VoilaFields.MESSAGES)
            .get()
            .addOnSuccessListener { value ->
                messages.clear()

                for (document in value) {
                    val dataMessage = document.toObject<Message>()

                    messages.add(dataMessage)
                }

                groupedMessages.value = messages.groupBy { message ->
                    getRelativeTime(message.timeStamp!!)
                }.mapValues { entry -> entry.value.sortedBy { it.timeStamp } }
            }
    }

    var showProfile by remember {
        mutableStateOf(false)
    }

    if (showProfile) {
        ProfileBottomSheet(user = user) {
            showProfile = false
        }
    }

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(top = 40.dp, bottom = 30.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .clickable {
                    showProfile = true
                }
        ) {
            IconButton(
                onClick = {
                    (context as ComponentActivity).finish()
                },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.left),
                    contentDescription = "back",
                    tint = dark
                )
            }
            Image(
                modifier = Modifier
                    .height(45.dp)
                    .width(45.dp)
                    .clip(CircleShape),
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context)
                        .data(if (user.profile?.isEmpty() == true) R.drawable.placeholder else user.profile.toString())
                        .placeholder(R.drawable.placeholder)
                        .size(coil.size.Size.ORIGINAL)
                        .build(),
                    contentScale = ContentScale.Crop,
                ),
                contentScale = ContentScale.Crop,
                contentDescription = "data",
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 15.dp)
            ) {
                Text(
                    text = user.fname.toString(),
                    fontFamily = FontFamily(Font(R.font.readex_pro_semibold)),
                    fontSize = 16.sp,
                    color = high,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = user.uname.toString(),
                    fontFamily = FontFamily(Font(R.font.readex_pro)),
                    fontSize = 12.sp,
                    color = medium,
                    textAlign = TextAlign.Center,
                )
            }
            IconButton(onClick = {
                val callData = OutgoingData(
                    user,
                    "",
                    CallType.VIDEO,
                    isMultiple,
                )

                val intent = Intent(context, OutgoingCallActivity::class.java)
                intent.putExtra("callData", callData)
                context.startActivity(intent)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.video),
                    contentDescription = "video",
                    tint = dark,
                    modifier = Modifier
                        .height(22.dp)
                        .width(22.dp)
                )
            }
            IconButton(onClick = {
                val callData = OutgoingData(
                    user,
                    "",
                    CallType.VOICE,
                    isMultiple,
                )

                val intent = Intent(context, OutgoingCallActivity::class.java)
                intent.putExtra("callData", callData)
                context.startActivity(intent)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.call),
                    contentDescription = "video",
                    tint = dark,
                    modifier = Modifier
                        .height(20.dp)
                        .width(20.dp)
                )
            }
        }

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(bottom = 5.dp, top = 5.dp),
            modifier = Modifier.weight(1f)
        ) {
            groupedMessages.value.forEach { (relativeTime, messages) ->
                item {
                    Text(
                        text = relativeTime,
                        fontFamily = FontFamily(Font(R.font.readex_pro)),
                        fontSize = 14.sp,
                        color = low,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 7.dp, bottom = 2.dp)
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth()
                    )
                }
                items(messages.size) { index ->
                    MessagesList(message = messages[index], messages) { replyId ->
                        replyTo = replyId
                        showReply = true
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .background(white)
                .fillMaxWidth()
                .padding(vertical = 5.dp, horizontal = 10.dp)
        ) {
            if (showReply) {
                Text(
                    text = "Reply to",
                    fontFamily = FontFamily(Font(R.font.readex_pro)),
                    fontSize = 10.sp,
                    maxLines = 2,
                    color = focused,
                    modifier = Modifier
                        .background(white)
                        .fillMaxWidth()
                        .padding(start = 15.dp, bottom = 2.dp)
                )
                Box(
                    modifier = Modifier
                        .border(1.dp, unfocused, RoundedCornerShape(24.dp))
                        .padding(horizontal = 15.dp, vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = fetchReplyMessage(
                                messages = messages,
                                messageId = replyTo
                            )?.text.toString(),
                            fontFamily = FontFamily(Font(R.font.readex_pro)),
                            fontSize = 14.sp,
                            maxLines = 2,
                            color = focused,
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .weight(1f)
                        )
                        IconButton(modifier = Modifier.size(18.dp), onClick = {
                            replyTo = ""
                            showReply = false
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.close),
                                contentDescription = "video",
                                tint = dark,
                            )
                        }
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    //
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.emoji),
                        contentDescription = "video",
                        tint = dark,
                        modifier = Modifier
                            .height(26.dp)
                            .width(26.dp)
                    )
                }
                if (!isRecording) {
                    Box(
                        modifier = Modifier
                            .background(unfocused, RoundedCornerShape(24.dp))
                            .weight(1f)
                    ) {
                        BasicTextField(
                            value = message,
                            onValueChange = { message = it },
                            modifier = Modifier
                                .padding(horizontal = 15.dp, vertical = 7.dp),
                            textStyle = TextStyle(
                                fontFamily = FontFamily(Font(R.font.readex_pro)),
                                fontSize = 14.sp,
                                color = focused
                            ),
                            maxLines = 5,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            decorationBox = { innerTextField ->
                                if (message.text.isEmpty()) {
                                    Text(
                                        text = "Type something here...",
                                        fontFamily = FontFamily(Font(R.font.readex_pro)),
                                        fontSize = 16.sp,
                                        color = focused,
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }
                } else {
                    Text(
                        text = "Slide to cancel.",
                        fontFamily = FontFamily(Font(R.font.readex_pro)),
                        fontSize = 16.sp,
                        color = focused,
                        modifier = Modifier
                            .padding(horizontal = 15.dp, vertical = 7.dp)
                            .weight(1f)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                if (message.text.isEmpty()) {
                    IconButton(
                        modifier = Modifier.scaleOnPress(
                            buttonInteractionSource,
                            onHold = {
                                recorder.startRecording()
                                isRecording = true
                            },
                            onRelease = {
                                recorder.stopRecording()
                                isRecording = false

                                FirebaseDatabase.getInstance()
                                    .getReference(VoilaFields.CONTACTS)
                                    .child(user.uid.toString())
                                    .child(SharedPrefs(context).getUser()?.uid.toString())
                                    .setValue(true)

                                val db =
                                    FirebaseFirestore.getInstance().collection(VoilaFields.CHATS)

                                val messId = VoilaFields.generateKey(30)

                                val reference =
                                    FirebaseStorage.getInstance().getReference(VoilaFields.CHATS)
                                        .child(SharedPrefs(context).getUser()?.uid.toString())
                                        .child("${System.currentTimeMillis()}_voila.3gp")

                                val recordedFile = recorder.getRecordedFile()

                                if (recordedFile != null) {
                                    reference.putFile(Uri.fromFile(recordedFile))
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                reference.downloadUrl.addOnCompleteListener { task1 ->
                                                    if (task1.isSuccessful) {
                                                        val download = task.result.toString()

                                                        val messData = Message(
                                                            messageId = messId,
                                                            senderId = SharedPrefs(context).getUser()?.uid,
                                                            timeStamp = Date().time,
                                                            status = MessageStatus.SENT,
                                                            media = MediaContent(
                                                                download,
                                                                MediaType.AUDIO
                                                            )
                                                        )

                                                        val otherMessData = Message(
                                                            messageId = messId,
                                                            senderId = SharedPrefs(context).getUser()?.uid,
                                                            timeStamp = Date().time,
                                                            status = MessageStatus.DELIVERED,
                                                            media = MediaContent(
                                                                download,
                                                                MediaType.AUDIO
                                                            )
                                                        )

                                                        val myChat = Chat(
                                                            "${SharedPrefs(context).getUser()?.uid}-${user.uid}",
                                                            Participants(0),
                                                            LastMessage(
                                                                messageId = messId,
                                                                senderId = "${SharedPrefs(context).getUser()?.uid}",
                                                                text = "You sent a voice note.",
                                                                content = MediaContent(mediaType = MediaType.AUDIO),
                                                                timeStamp = Date().time
                                                            ),
                                                        )

                                                        val otherChat = Chat(
                                                            "${user.uid}-${SharedPrefs(context).getUser()?.uid}",
                                                            Participants(1),
                                                            LastMessage(
                                                                messageId = messId,
                                                                senderId = "${SharedPrefs(context).getUser()?.uid}",
                                                                text = "Received a voice note.",
                                                                content = MediaContent(mediaType = MediaType.AUDIO),
                                                                timeStamp = Date().time
                                                            ),
                                                        )

                                                        db.document("${SharedPrefs(context).getUser()?.uid}-${user.uid}")
                                                            .set(myChat)
                                                        db.document(
                                                            "${user.uid}-${
                                                                SharedPrefs(
                                                                    context
                                                                ).getUser()?.uid
                                                            }"
                                                        )
                                                            .set(otherChat)

                                                        db.document("${SharedPrefs(context).getUser()?.uid}-${user.uid}")
                                                            .collection(VoilaFields.MESSAGES)
                                                            .document(messId)
                                                            .set(messData).addOnSuccessListener {
                                                                db.document(
                                                                    "${user.uid}-${
                                                                        SharedPrefs(
                                                                            context
                                                                        ).getUser()?.uid
                                                                    }"
                                                                )
                                                                    .collection(VoilaFields.MESSAGES)
                                                                    .document(messId)
                                                                    .set(otherMessData)
                                                                    .addOnSuccessListener {

                                                                    }
                                                            }
                                                    }
                                                }
                                            }
                                        }
                                }
                            }
                        ) {
                            isRecording = false
                            recorder.stopRecording()
                            recorder.cleanUp()
                        },
                        interactionSource = buttonInteractionSource,
                        onClick = {
                        }) {
                        Icon(
                            painter = painterResource(id = if (message.text.isEmpty()) R.drawable.microphone else R.drawable.send),
                            contentDescription = "sendChat",
                            tint = dark,
                            modifier = Modifier
                                .height(26.dp)
                                .width(26.dp)
                        )
                    }
                } else {
                    IconButton(
                        onClick = {
                            FirebaseDatabase.getInstance()
                                .getReference(VoilaFields.CONTACTS)
                                .child(user.uid.toString())
                                .child(SharedPrefs(context).getUser()?.uid.toString())
                                .setValue(true)

                            val db = FirebaseFirestore.getInstance().collection(VoilaFields.CHATS)

                            val messId = VoilaFields.generateKey(30)

                            val messData = Message(
                                messageId = messId,
                                senderId = SharedPrefs(context).getUser()?.uid,
                                text = message.text,
                                timeStamp = Date().time,
                                status = MessageStatus.SENT,
                                reply = if (replyTo.isNotEmpty()) replyTo else ""
                            )

                            val otherMessData = Message(
                                messageId = messId,
                                senderId = SharedPrefs(context).getUser()?.uid,
                                text = message.text,
                                timeStamp = Date().time,
                                status = MessageStatus.DELIVERED,
                                reply = if (replyTo.isNotEmpty()) replyTo else ""
                            )

                            val myChat = Chat(
                                "${SharedPrefs(context).getUser()?.uid}-${user.uid}",
                                Participants(0),
                                LastMessage(
                                    messageId = messId,
                                    senderId = "${SharedPrefs(context).getUser()?.uid}",
                                    text = message.text,
                                    timeStamp = Date().time
                                ),
                            )

                            val otherChat = Chat(
                                "${user.uid}-${SharedPrefs(context).getUser()?.uid}",
                                Participants(1),
                                LastMessage(
                                    messageId = messId,
                                    senderId = "${SharedPrefs(context).getUser()?.uid}",
                                    text = message.text,
                                    timeStamp = Date().time
                                ),
                            )

                            db.document("${SharedPrefs(context).getUser()?.uid}-${user.uid}")
                                .set(myChat)
                            db.document("${user.uid}-${SharedPrefs(context).getUser()?.uid}")
                                .set(otherChat)

                            db.document("${SharedPrefs(context).getUser()?.uid}-${user.uid}")
                                .collection(VoilaFields.MESSAGES)
                                .document(messId)
                                .set(messData).addOnSuccessListener {
                                    db.document("${user.uid}-${SharedPrefs(context).getUser()?.uid}")
                                        .collection(VoilaFields.MESSAGES)
                                        .document(messId)
                                        .set(otherMessData).addOnSuccessListener {
                                            message = TextFieldValue("")
                                            if (replyTo.isNotEmpty()) {
                                                replyTo = ""
                                                showReply = false
                                            }
                                        }
                                }
                        }) {
                        Icon(
                            painter = painterResource(id = if (message.text.isEmpty()) R.drawable.microphone else R.drawable.send),
                            contentDescription = "sendChat",
                            tint = dark,
                            modifier = Modifier
                                .height(26.dp)
                                .width(26.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
fun MessagesList(message: Message, messages: List<Message>, onReply: (String) -> Unit) {
    val context = LocalContext.current

    var user by remember { mutableStateOf<User?>(null) }

    var isReceived by remember {
        mutableStateOf(false)
    }

    var isReply by remember {
        mutableStateOf(false)
    }

    var sentVisibility by remember {
        mutableStateOf(false)
    }

    var receivedVisibility by remember {
        mutableStateOf(false)
    }

    var reactionVisibility by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        isReceived = message.senderId != SharedPrefs(context).getUser()?.uid
        isReply = message.reply.toString().isNotEmpty()

        FirebaseFirestore.getInstance().collection(VoilaFields.USERS)
            .document(message.senderId.toString())
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null && document.exists()) {
                        user = document.toObject(User::class.java)
                    }
                }
            }

        val unreadCount = Chat(participants = Participants(0))

        FirebaseFirestore.getInstance().collection(VoilaFields.CHATS)
            .document("${SharedPrefs(context).getUser()?.uid}-${message.senderId.toString()}")
            .collection("participants")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    document.reference.update("unread", unreadCount)
                }
            }

        FirebaseFirestore.getInstance().collection(VoilaFields.CHATS)
            .document("${SharedPrefs(context).getUser()?.uid}-${message.senderId.toString()}")
            .collection(VoilaFields.MESSAGES)
            .document(message.messageId.toString())
            .update("status", MessageStatus.SEEN)
    }

    if (sentVisibility) {
        LaunchedEffect(Unit) {
            delay(3000)
            sentVisibility = false
        }
    }

    if (receivedVisibility) {
        LaunchedEffect(Unit) {
            delay(3000)
            receivedVisibility = false
        }
    }

    val startPadding = if (isReceived) 110.dp else 0.dp
    val endPadding = if (!isReceived) 110.dp else 0.dp

    var offsetX by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, start = 6.dp, end = 6.dp)
            .graphicsLayer(translationX = offsetX)
            .pointerInput(Unit) {
                if (!isReceived) {
                    detectDragGestures(
                        onDragEnd = {
                            if (offsetX <= -8f) {
                                onReply(message.messageId.toString())
                            }
                            offsetX = 0f
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x.coerceIn(-8f, 0f)
                    }
                } else {
                    detectDragGestures(
                        onDragEnd = {
                            if (offsetX >= 8f) {
                                onReply(message.messageId.toString())
                            }
                            offsetX = 0f
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x.coerceIn(0f, 8f)
                    }
                }
            },
    ) {
        Column(
            modifier = Modifier
                .align(if (isReceived) Alignment.CenterStart else Alignment.CenterEnd)
                .padding(end = startPadding, start = endPadding)
        ) {
            Column(
                modifier = Modifier
                    .padding(5.dp)
                    .background(if (isReceived) unfocused else yellow, RoundedCornerShape(16.dp))
                    .clickable {
                        if (isReceived) sentVisibility = true else receivedVisibility = true
                    }
            ) {
                if (isReply) {
                    Row(
                        modifier = Modifier.padding(start = 10.dp, end = 12.dp, top = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        VerticalDivider(
                            modifier = Modifier
                                .width(2.dp)
                                .height(20.dp)
                                .background(high, RoundedCornerShape(50.dp)),
                            color = Color.Transparent
                        )

                        Column(modifier = Modifier.padding(start = 8.dp)) {
                            Text(
                                text = fetchReplyMessage(messages, message.reply)?.text.toString(),
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.readex_pro)),
                                color = medium,
                                maxLines = 1,
                            )
                        }
                    }
                }

                Text(
                    text = message.text.toString(),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.readex_pro)),
                    color = high,
                    modifier = Modifier.padding(
                        start = 10.dp,
                        end = 10.dp,
                        bottom = 5.dp,
                        top = if (!isReply) 5.dp else 0.dp
                    )
                )
            }

            val left = if (isReceived) 10.dp else 0.dp
            val right = if (isReceived) 0.dp else 10.dp

            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .align(if (isReceived) Alignment.Start else Alignment.End)
            ) {
                if (receivedVisibility) {
                    Text(
                        text = if (message.status == MessageStatus.DELIVERED) "Delivered" else if (message.status == MessageStatus.SEEN) "Seen" else "Sent",
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.readex_pro)),
                        color = medium,
                        modifier = Modifier
                            .offset(y = (-5).dp)
                            .padding(end = right, start = left)
                    )
                }

                if (message.reactions != null) {
                    reactionVisibility = true
                }

                if (reactionVisibility) {
                    Row(
                        modifier = Modifier
                            .padding(end = right, start = left)
                            .offset(y = (-8).dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            modifier = Modifier
                                .shadow(6.dp, shape = MaterialTheme.shapes.medium)
                                .background(white, RoundedCornerShape(50.dp))
                                .border(4.dp, white, RoundedCornerShape(50.dp)),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            val isUnequal =
                                message.reactions?.get(1)?.emoji != message.reactions?.get(0)?.emoji

                            Text(
                                text = message.reactions?.get(0)?.emoji.toString(),
                                fontSize = 15.sp,
                                fontFamily = FontFamily(Font(R.font.readex_pro)),
                                color = medium,
                                modifier = Modifier.padding(
                                    start = 4.dp,
                                    top = 2.dp,
                                    bottom = 2.dp,
                                    end = if (isUnequal) 0.dp else 4.dp
                                )
                            )
                            if (isUnequal) {
                                Text(
                                    text = message.reactions?.get(1)?.emoji.toString(),
                                    fontSize = 15.sp,
                                    fontFamily = FontFamily(Font(R.font.readex_pro)),
                                    color = medium,
                                    modifier = Modifier.padding(
                                        start = 3.dp,
                                        top = 2.dp,
                                        bottom = 2.dp,
                                        end = 4.dp
                                    )
                                )
                            }
                        }
                    }
                }

                if (sentVisibility) {
                    Text(
                        text = if (message.status == MessageStatus.DELIVERED) "Delivered" else if (message.status == MessageStatus.SEEN) "Seen" else "Sent",
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.readex_pro)),
                        color = medium,
                        modifier = Modifier
                            .offset(y = (-5).dp)
                            .padding(end = right, start = left)
                    )
                }
            }
        }
    }
}

@Composable
fun fetchReplyMessage(messages: List<Message>, messageId: String?): Message? {
    return remember(messageId, messages) {
        messages.find { it.messageId == messageId }
    }
}

fun Modifier.scaleOnPress(
    interactionSource: InteractionSource,
    onHold: () -> Unit,
    onRelease: () -> Unit,
    onDiscard: () -> Unit,
) = composed {
    val isPressed by interactionSource.collectIsPressedAsState()
    var offsetX by remember { mutableFloatStateOf(0f) }
    val scale by animateFloatAsState(
        if (isPressed) {
            1.5f
        } else {
            1f
        }
    )

    if (isPressed) {
        onHold()
    } else {
        onRelease()
    }

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
            translationX = offsetX
        }
        .pointerInput(Unit) {
            detectDragGestures(
                onDragEnd = {
                    if (offsetX <= -50f) {
                        onDiscard()
                    }
                    offsetX = 0f
                }
            ) { change, dragAmount ->
                change.consume()
                offsetX += dragAmount.x.coerceIn(-50f, 0f)
            }
        }
}