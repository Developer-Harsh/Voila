package `in`.snbapps.vidmeet.ui.pages

import android.util.Log
import android.util.Patterns
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import `in`.snbapps.vidmeet.R
import `in`.snbapps.vidmeet.data.User
import `in`.snbapps.vidmeet.navigation.Pages
import `in`.snbapps.vidmeet.ui.theme.dark
import `in`.snbapps.vidmeet.ui.theme.focused
import `in`.snbapps.vidmeet.ui.theme.high
import `in`.snbapps.vidmeet.ui.theme.low
import `in`.snbapps.vidmeet.ui.theme.unfocused
import `in`.snbapps.vidmeet.ui.theme.yellow
import `in`.snbapps.vidmeet.utils.NetworkViewModel
import `in`.snbapps.vidmeet.utils.ProgressBottomSheet
import `in`.snbapps.vidmeet.utils.SharedPrefs
import `in`.snbapps.vidmeet.utils.VoilaFields
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navHostController: NavHostController, viewModel: NetworkViewModel) {
    val context = LocalContext.current

    val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    var email by remember {
        mutableStateOf(TextFieldValue(""))
    }

    var showPassword by rememberSaveable { mutableStateOf(false) }
    var showConfirm by rememberSaveable { mutableStateOf(false) }

    var name by remember {
        mutableStateOf(TextFieldValue(""))
    }

    var confirm by remember {
        mutableStateOf(TextFieldValue(""))
    }

    var password by remember {
        mutableStateOf(TextFieldValue(""))
    }

    val focusManager = LocalFocusManager.current

    val imeInsets = WindowInsets.ime
    val density = LocalDensity.current
    val scrollState = rememberScrollState()


    val snackbarHostState = remember { SnackbarHostState() }
    var showSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    var sharedPrefs = SharedPrefs(context)

    val isConnected by viewModel.isConnected.collectAsState()

    LaunchedEffect(isConnected) {
        if (!isConnected) {
            navHostController.navigate(Pages.Connection.route)
        }
    }

    if (showSheet) {
        ProgressBottomSheet() {
            showSheet = false
        }
    }

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(start = 20.dp, end = 20.dp, top = 40.dp, bottom = with(density) {
                imeInsets
                    .getBottom(density)
                    .toDp()
            }),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = {
                navHostController.popBackStack()
            }, modifier = Modifier.border(BorderStroke(1.dp, unfocused), CircleShape)) {
                Icon(
                    painter = painterResource(id = R.drawable.left),
                    contentDescription = "back",
                    tint = dark
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                Log.e("back", "back")
            }, modifier = Modifier.border(BorderStroke(1.dp, unfocused), CircleShape)) {
                Icon(
                    painter = painterResource(id = R.drawable.menu),
                    contentDescription = "menu",
                    tint = dark
                )
            }
        }

        Text(
            text = "Create a new Account",
            fontFamily = FontFamily(Font(R.font.readex_pro_bold)),
            fontSize = 30.sp,
            color = high,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 40.dp)
        )

        Text(
            text = "Please enter credentials in the fields below to register to the Voila app.",
            fontFamily = FontFamily(Font(R.font.readex_pro)),
            fontSize = 16.sp,
            color = low,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp, bottom = 50.dp)
        )

//        OutlinedTextField(
//            value = username,
//            onValueChange = { username = it },
//            label = {
//                Text(
//                    text = "Username",
//                    fontFamily = FontFamily(Font(R.font.readex_pro)),
//                    fontSize = 14.sp
//                )
//            },
//            leadingIcon = {
//                Icon(
//                    painter = painterResource(id = R.drawable.username),
//                    contentDescription = "icon",
//                    tint = focused,
//                )
//            },
//            maxLines = 1,
//            textStyle = TextStyle(
//                fontFamily = FontFamily(Font(R.font.readex_pro)),
//                fontSize = 16.sp
//            ),
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Text,
//                imeAction = ImeAction.Next
//            ),
//            keyboardActions = KeyboardActions(onNext = {
//                focusManager.moveFocus(
//                    FocusDirection.Down
//                )
//            }),
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(12.dp),
//            colors = OutlinedTextFieldDefaults.colors(
//                unfocusedBorderColor = unfocused,
//                focusedBorderColor = focused,
//                unfocusedLabelColor = focused,
//                focusedLabelColor = focused,
//                focusedTextColor = focused,
//                unfocusedTextColor = focused
//            ),
//        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = {
                Text(
                    text = "Full Name",
                    fontFamily = FontFamily(Font(R.font.readex_pro)),
                    fontSize = 14.sp
                )
            },
            maxLines = 1,
            textStyle = TextStyle(
                fontFamily = FontFamily(Font(R.font.readex_pro)),
                fontSize = 16.sp
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.person),
                    contentDescription = "icon",
                    tint = focused,
                )
            },
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = unfocused,
                focusedBorderColor = focused,
                unfocusedLabelColor = focused,
                focusedLabelColor = focused,
                focusedTextColor = focused,
                unfocusedTextColor = focused
            ),
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = {
                Text(
                    text = "Email Address",
                    fontFamily = FontFamily(Font(R.font.readex_pro)),
                    fontSize = 14.sp
                )
            },
            maxLines = 1,
            textStyle = TextStyle(
                fontFamily = FontFamily(Font(R.font.readex_pro)),
                fontSize = 16.sp
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.email),
                    contentDescription = "icon",
                    tint = focused,
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(
                    FocusDirection.Down
                )
            }),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = unfocused,
                focusedBorderColor = focused,
                unfocusedLabelColor = focused,
                focusedLabelColor = focused,
                focusedTextColor = focused,
                unfocusedTextColor = focused
            ),
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text(
                    text = "Password",
                    fontFamily = FontFamily(Font(R.font.readex_pro)),
                    fontSize = 14.sp
                )
            },
            maxLines = 1,
            textStyle = TextStyle(
                fontFamily = FontFamily(Font(R.font.readex_pro)),
                fontSize = 16.sp
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.password),
                    contentDescription = "icon",
                    tint = focused,
                )
            },
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = if (showPassword) R.drawable.hide else R.drawable.show),
                    contentDescription = "icon",
                    tint = focused,
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = unfocused,
                focusedBorderColor = focused,
                unfocusedLabelColor = focused,
                focusedLabelColor = focused,
                focusedTextColor = focused,
                unfocusedTextColor = focused
            ),
        )

        OutlinedTextField(
            value = confirm,
            onValueChange = { confirm = it },
            label = {
                Text(
                    text = "Confirm Password",
                    fontFamily = FontFamily(Font(R.font.readex_pro)),
                    fontSize = 14.sp
                )
            },
            maxLines = 1,
            textStyle = TextStyle(
                fontFamily = FontFamily(Font(R.font.readex_pro)),
                fontSize = 16.sp
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.password),
                    contentDescription = "icon",
                    tint = focused,
                )
            },
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            visualTransformation = if (showConfirm) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = if (showConfirm) R.drawable.hide else R.drawable.show),
                    contentDescription = "icon",
                    tint = focused,
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = unfocused,
                focusedBorderColor = focused,
                unfocusedLabelColor = focused,
                focusedLabelColor = focused,
                focusedTextColor = focused,
                unfocusedTextColor = focused
            ),
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (name.text.isEmpty()) {
                    scope.launch {
                        val snackbarJob = launch {
                            snackbarHostState.showSnackbar(
                                message = "Full Name field is empty!",
                                actionLabel = "Hide",
                                duration = SnackbarDuration.Indefinite
                            )
                        }
                        delay(3000)
                        snackbarJob.cancel()
                    }
                } else if (name.text.length < 3) {
                    scope.launch {
                        val snackbarJob = launch {
                            snackbarHostState.showSnackbar(
                                message = "Full Name must be more than 3 chars!",
                                actionLabel = "Hide",
                                duration = SnackbarDuration.Indefinite
                            )
                        }
                        delay(3000)
                        snackbarJob.cancel()
                    }
                } else if (email.text.isEmpty()) {
                    scope.launch {
                        val snackbarJob = launch {
                            snackbarHostState.showSnackbar(
                                message = "Email address field is empty!",
                                actionLabel = "Hide",
                                duration = SnackbarDuration.Indefinite
                            )
                        }
                        delay(3000)
                        snackbarJob.cancel()
                    }
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email.text).matches()) {
                    scope.launch {
                        val snackbarJob = launch {
                            snackbarHostState.showSnackbar(
                                message = "Email pattern does not match!",
                                actionLabel = "Hide",
                                duration = SnackbarDuration.Indefinite
                            )
                        }
                        delay(3000)
                        snackbarJob.cancel()
                    }
                } else if (password.text.isEmpty()) {
                    scope.launch {
                        val snackbarJob = launch {
                            snackbarHostState.showSnackbar(
                                message = "Password field is empty!",
                                actionLabel = "Hide",
                                duration = SnackbarDuration.Indefinite
                            )
                        }
                        delay(3000)
                        snackbarJob.cancel()
                    }
                } else if (password.text.length < 6) {
                    scope.launch {
                        val snackbarJob = launch {
                            snackbarHostState.showSnackbar(
                                message = "Password must be 6 or more!",
                                actionLabel = "Hide",
                                duration = SnackbarDuration.Indefinite
                            )
                        }
                        delay(3000)
                        snackbarJob.cancel()
                    }
                } else if (confirm.text.isEmpty()) {
                    scope.launch {
                        val snackbarJob = launch {
                            snackbarHostState.showSnackbar(
                                message = "Confirm password field is empty!",
                                actionLabel = "Hide",
                                duration = SnackbarDuration.Indefinite
                            )
                        }
                        delay(3000)
                        snackbarJob.cancel()
                    }
                } else if (confirm.text != password.text) {
                    scope.launch {
                        val snackbarJob = launch {
                            snackbarHostState.showSnackbar(
                                message = "Password doesn't match!",
                                actionLabel = "Hide",
                                duration = SnackbarDuration.Indefinite
                            )
                        }
                        delay(3000)
                        snackbarJob.cancel()
                    }
                } else {
                    showSheet = true
                    auth.createUserWithEmailAndPassword(email.text, password.text)
                        .addOnSuccessListener {
                            auth.currentUser?.let { user ->
                                val userObject = User(
                                    email.text,
                                    password.text,
                                    "",
                                    "voila_user",
                                    name.text,
                                    "",
                                    user.uid,
                                    "Hey, there i am using Voila."
                                )

                                firestore.collection(VoilaFields.USERS).document(user.uid)
                                    .set(userObject).addOnSuccessListener {
                                    showSheet = false
                                    sharedPrefs.saveLoggedInState(true)
                                    sharedPrefs.saveUser(userObject)

                                    navHostController.navigate(Pages.Home.route)
                                }.addOnFailureListener {
                                    showSheet = false
                                    scope.launch {
                                        val snackbarJob = launch {
                                            snackbarHostState.showSnackbar(
                                                message = "Unable to upload data, try again later",
                                                actionLabel = "Hide",
                                                duration = SnackbarDuration.Indefinite
                                            )
                                        }
                                        delay(3000)
                                        snackbarJob.cancel()
                                    }
                                }
                            }
                        }.addOnFailureListener { exception ->
                        showSheet = false
                        scope.launch {
                            val snackbarJob = launch {
                                snackbarHostState.showSnackbar(
                                    message = "Account already exist",
                                    actionLabel = "Hide",
                                    duration = SnackbarDuration.Indefinite
                                )
                            }
                            delay(3000)
                            snackbarJob.cancel()
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonColors(yellow, Color.Black, low, Color.Black)
        ) {
            Text(
                text = "Continue",
                fontFamily = FontFamily(Font(R.font.readex_pro_medium)),
                fontSize = 16.sp,
                modifier = Modifier.padding(6.dp)
            )
        }

        Spacer(modifier = Modifier.height(70.dp))
    }
}