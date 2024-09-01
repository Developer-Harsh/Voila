package `in`.snbapps.vidmeet.ui.pages

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
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
import androidx.navigation.NavHostController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import `in`.snbapps.vidmeet.R
import `in`.snbapps.vidmeet.data.Chat
import `in`.snbapps.vidmeet.data.LastMessage
import `in`.snbapps.vidmeet.data.Participants
import `in`.snbapps.vidmeet.data.User
import `in`.snbapps.vidmeet.navigation.Pages
import `in`.snbapps.vidmeet.representer.Chats
import `in`.snbapps.vidmeet.ui.theme.dark
import `in`.snbapps.vidmeet.ui.theme.focused
import `in`.snbapps.vidmeet.ui.theme.high
import `in`.snbapps.vidmeet.ui.theme.unfocused
import `in`.snbapps.vidmeet.ui.theme.white
import `in`.snbapps.vidmeet.utils.NewChatBottomSheet
import `in`.snbapps.vidmeet.utils.NewContactBottomSheet
import `in`.snbapps.vidmeet.utils.ProgressBottomSheet
import `in`.snbapps.vidmeet.utils.SharedPrefs
import `in`.snbapps.vidmeet.utils.VoilaBottomNavigationBar
import `in`.snbapps.vidmeet.utils.VoilaFields
import kotlinx.coroutines.launch
import java.util.Date

@Composable
fun HomeScreen(navHostController: NavHostController) {

    var showSheet by remember { mutableStateOf(false) }
    var showProgress by remember { mutableStateOf(false) }
    var showNewContact by remember { mutableStateOf(false) }
    var showSearch by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(0, 0f) { 2 }
    val coroutineScope = rememberCoroutineScope()
    val db = FirebaseFirestore.getInstance().collection(VoilaFields.CHATS)
    var search by remember { mutableStateOf(TextFieldValue("")) }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    if (showSheet) {
        NewChatBottomSheet(
            onDismiss = { showSheet = false },
            onQRClicked = {
                showSheet = false
                navHostController.navigate(Pages.Scanner.route)
            }) {
            showSheet = false
            showNewContact = true
        }
    }

    val messId = VoilaFields.generateKey(35)
    val sharedPrefs = SharedPrefs(context)

    if (showNewContact) {
        NewContactBottomSheet(onDismiss = { showNewContact = false }) { user ->
            showNewContact = false
            showProgress = true

            val data = Chat(
                "${sharedPrefs.getUser()?.uid}-${user.uid}",
                Participants(1),
                LastMessage(
                    messageId = messId,
                    senderId = "${sharedPrefs.getUser()?.uid}",
                    text = "You added ${user.fname} to your chats, say Hi to get started.",
                    timeStamp = Date().time
                ),
            )

            db.document("${sharedPrefs.getUser()?.uid}-${user.uid}").get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document != null && document.exists()) {
                            showProgress = false

                            Toast.makeText(
                                context,
                                "User already exists in chats!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            db.document("${sharedPrefs.getUser()?.uid}-${user.uid}")
                                .set(data)
                                .addOnSuccessListener {
                                    FirebaseDatabase.getInstance()
                                        .getReference(VoilaFields.CONTACTS)
                                        .child(sharedPrefs.getUser()?.uid.toString())
                                        .child(user.uid.toString())
                                        .setValue(true)

                                    showProgress = false
                                }
                                .addOnFailureListener {
                                    showProgress = false
                                }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Error checking document: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    if (showProgress) {
        ProgressBottomSheet {
            showProgress = false
        }
    }

    val chatHistory = remember {
        mutableStateListOf<User>()
    }

    val idList = remember {
        mutableStateListOf<String>()
    }

    LaunchedEffect(idList.size) {
        FirebaseDatabase.getInstance().getReference(VoilaFields.CONTACTS)
            .child(sharedPrefs.getUser()?.uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    idList.clear()
                    for (dataSnapshot in snapshot.children) {
                        dataSnapshot.key?.let { idList.add(it) }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    Scaffold(
        containerColor = white,
        bottomBar = {
            VoilaBottomNavigationBar(
                pagerState = pagerState,
                onIcon1Click = { coroutineScope.launch { pagerState.animateScrollToPage(0) } },
                onIcon2Click = { coroutineScope.launch { pagerState.animateScrollToPage(1) } },
                onTextButtonClick = { showSheet = true }
            )
        }
    ) {
        it.calculateTopPadding()
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Crossfade(targetState = showSearch) { show ->
                            if (show) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(
                                        top = 40.dp,
                                        start = 20.dp,
                                        end = 20.dp
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .weight(1f)
                                            .border(1.dp, unfocused, RoundedCornerShape(14.dp))
                                            .padding(vertical = 11.dp, horizontal = 15.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.search),
                                            contentDescription = "icon",
                                            tint = focused,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        BasicTextField(
                                            value = search,
                                            onValueChange = { search = it },
                                            modifier = Modifier.padding(horizontal = 15.dp),
                                            textStyle = TextStyle(
                                                fontFamily = FontFamily(Font(R.font.readex_pro)),
                                                fontSize = 16.sp,
                                                color = focused
                                            ),
                                            singleLine = true,
                                            keyboardOptions = KeyboardOptions(
                                                keyboardType = KeyboardType.Text,
                                                imeAction = ImeAction.Done
                                            ),
                                            keyboardActions = KeyboardActions(onNext = {
                                                focusManager.moveFocus(
                                                    FocusDirection.Down
                                                )
                                            }),
                                            decorationBox = { innerTextField ->
                                                if (search.text.isEmpty()) {
                                                    Text(
                                                        text = "Search voila...",
                                                        fontFamily = FontFamily(Font(R.font.readex_pro)),
                                                        fontSize = 16.sp,
                                                        color = focused,
                                                    )
                                                }
                                                innerTextField()
                                            }
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(15.dp))
                                    IconButton(
                                        onClick = {
                                            showSearch = !showSearch
                                        },
                                        modifier = Modifier
                                            .border(BorderStroke(1.dp, unfocused), CircleShape)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.close),
                                            contentDescription = "search",
                                            tint = dark
                                        )
                                    }
                                }
                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(
                                        top = 40.dp,
                                        start = 20.dp,
                                        end = 20.dp
                                    )
                                ) {
                                    Text(
                                        text = "Voila",
                                        fontFamily = FontFamily(Font(R.font.readex_pro_bold)),
                                        fontSize = 22.sp,
                                        color = high,
                                        textAlign = TextAlign.Center,
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    IconButton(
                                        onClick = {
                                            showSearch = !showSearch
                                        },
                                        modifier = Modifier
                                            .border(BorderStroke(1.dp, unfocused), CircleShape)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.search),
                                            contentDescription = "search",
                                            tint = dark
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(15.dp))

                        /// story view here

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 10.dp, start = 20.dp, end = 20.dp)
                        ) {
                            Text(
                                text = "Chats",
                                fontFamily = FontFamily(Font(R.font.readex_pro_medium)),
                                fontSize = 18.sp,
                                color = high,
                                textAlign = TextAlign.Center,
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(
                                onClick = {
                                    Log.e("menu", "menu")
                                },
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.menu),
                                    contentDescription = "search",
                                    tint = dark
                                )
                            }
                        }

                        LaunchedEffect(Unit) {
                            FirebaseFirestore.getInstance().collection(VoilaFields.USERS)
                                .get().addOnSuccessListener { result ->
                                    chatHistory.clear()
                                    for (document in result.documents) {
                                        val user = document.toObject(User::class.java)
                                        for (id in idList) {
                                            try {
                                                if (user != null && sharedPrefs.getUser()?.uid != user.uid) {
                                                    if (id == user.uid) {
                                                        chatHistory.add(user)
                                                    }
                                                }
                                            } catch (_: Exception) {
                                            }
                                        }
                                    }
                                }
                        }

                        ChatList(chats = chatHistory, context)
                    }
                }

                1 -> {
                    MyProfileScreen(navHostController = navHostController)
                }
            }
        }
    }
}

@Composable
fun ChatList(chats: List<User>, context: Context) {
    LazyColumn {
        items(chats.size) { index ->
            Chats(chat = chats[index]) {
                context.startActivity(
                    Intent(context, MessagingActivity::class.java).putExtra("user", chats[index])
                )
            }
        }
    }
}