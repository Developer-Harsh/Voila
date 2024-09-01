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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
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
import com.google.firebase.auth.FirebaseAuth
import `in`.snbapps.vidmeet.R
import `in`.snbapps.vidmeet.navigation.Pages
import `in`.snbapps.vidmeet.ui.theme.dark
import `in`.snbapps.vidmeet.ui.theme.focused
import `in`.snbapps.vidmeet.ui.theme.high
import `in`.snbapps.vidmeet.ui.theme.low
import `in`.snbapps.vidmeet.ui.theme.unfocused
import `in`.snbapps.vidmeet.ui.theme.yellow
import `in`.snbapps.vidmeet.utils.NetworkViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ForgotScreen(navHostController: NavHostController, viewModel: NetworkViewModel) {
    var email by remember {
        mutableStateOf(TextFieldValue(""))
    }

    val auth = FirebaseAuth.getInstance()

    val focusManager = LocalFocusManager.current

    val imeInsets = WindowInsets.ime
    val density = LocalDensity.current
    val scrollState = rememberScrollState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val isConnected by viewModel.isConnected.collectAsState()

    LaunchedEffect(isConnected) {
        if (!isConnected) {
            navHostController.navigate(Pages.Connection.route)
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

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.background(dark, RoundedCornerShape(14.dp)),
        )

        Text(
            text = "Forgot Password?",
            fontFamily = FontFamily(Font(R.font.readex_pro_bold)),
            fontSize = 30.sp,
            color = high,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 40.dp)
        )

        Text(
            text = "Please enter credentials in the fields below to request password reset link.",
            fontFamily = FontFamily(Font(R.font.readex_pro)),
            fontSize = 16.sp,
            color = low,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp, bottom = 50.dp)
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
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(
                    FocusDirection.Down
                )
            }),
            modifier = Modifier.fillMaxWidth(),
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
                if (email.text.isEmpty()) {
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
                } else {
                    auth.sendPasswordResetEmail(email.text).addOnSuccessListener {
                        scope.launch {
                            val snackbarJob = launch {
                                snackbarHostState.showSnackbar(
                                    message = "Password reset mail sent!",
                                    actionLabel = "Hide",
                                    duration = SnackbarDuration.Indefinite
                                )
                            }
                            delay(3000)
                            snackbarJob.cancel()
                        }
                    }.addOnFailureListener {
                        scope.launch {
                            val snackbarJob = launch {
                                snackbarHostState.showSnackbar(
                                    message = "Account doesn't exist",
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
                text = "Request Link",
                fontFamily = FontFamily(Font(R.font.readex_pro_medium)),
                fontSize = 16.sp,
                modifier = Modifier.padding(6.dp)
            )
        }

        Spacer(modifier = Modifier.height(70.dp))
    }
}