package `in`.snbapps.vidmeet.ui.pages

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import `in`.snbapps.vidmeet.R
import `in`.snbapps.vidmeet.navigation.Pages
import `in`.snbapps.vidmeet.ui.theme.dark
import `in`.snbapps.vidmeet.ui.theme.high
import `in`.snbapps.vidmeet.ui.theme.low
import `in`.snbapps.vidmeet.ui.theme.unfocused
import `in`.snbapps.vidmeet.ui.theme.white
import `in`.snbapps.vidmeet.ui.theme.yellow

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun IntroScreen(navHostController: NavHostController) {

    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    val mediaPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_AUDIO,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.FOREGROUND_SERVICE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.POST_NOTIFICATIONS,
            )
        )
    } else {
        rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.FOREGROUND_SERVICE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.POST_NOTIFICATIONS,
            )
        )
    }

    if (mediaPermission.allPermissionsGranted) {
        showBottomSheet = false
    } else {
        showBottomSheet = true
    }

    if (showBottomSheet) {
        val modalBottomSheetState = rememberModalBottomSheetState()

        ModalBottomSheet(
            containerColor = white,
            onDismissRequest = { showBottomSheet = false },
            sheetState = modalBottomSheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp)
            ) {
                Text(
                    text = "Permissions Required",
                    fontFamily = FontFamily(Font(R.font.readex_pro_bold)),
                    fontSize = 18.sp,
                    color = high
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "This app needs some permissions to run, in this app are some functionality that works with these permissions.",
                    fontFamily = FontFamily(Font(R.font.readex_pro)),
                    fontSize = 14.sp,
                    color = low
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    showBottomSheet = false
                    mediaPermission.launchMultiplePermissionRequest()
                }, colors = ButtonColors(yellow, high, unfocused, high)) {
                    Text(
                        text = "Grant Permission",
                        fontFamily = FontFamily(Font(R.font.readex_pro_medium)),
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(start = 30.dp, end = 30.dp, top = 50.dp, bottom = 40.dp)
    ) {
        IconButton(
            onClick = {
                Log.e("menu", "menu")
            },
            modifier = Modifier
                .border(BorderStroke(1.dp, unfocused), CircleShape)
                .align(Alignment.End)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.menu),
                contentDescription = "help",
                tint = dark
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.greeting),
            contentDescription = "intro1",
            modifier = Modifier
                .height(240.dp)
                .width(240.dp)
                .align(Alignment.CenterHorizontally)
                .clip(shape = CircleShape)
        )
        Text(
            text = "Welcome to Voila",
            fontFamily = FontFamily(Font(R.font.readex_pro_bold)),
            fontSize = 30.sp,
            color = high,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 90.dp)
        )
        Text(
            text = "Share your thoughts with anyone in your contacts, from anywhere and to anywhere.",
            fontFamily = FontFamily(Font(R.font.readex_pro)),
            fontSize = 16.sp,
            color = low,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp, bottom = 50.dp)
        )
        Button(
            onClick = {
                navHostController.navigate(Pages.Login.route)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonColors(yellow, Color.Black, low, Color.Black)
        ) {
            Text(
                text = "Sign In",
                fontFamily = FontFamily(Font(R.font.readex_pro_semibold)),
                fontSize = 16.sp,
                modifier = Modifier.padding(6.dp)
            )
        }
        Spacer(modifier = Modifier.padding(top = 20.dp))
        OutlinedButton(
            onClick = {
                navHostController.navigate(Pages.Register.route)
            },
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, unfocused),
            colors = ButtonColors(white, Color.Black, low, Color.Black)
        ) {
            Text(
                text = "Create an Account?",
                fontFamily = FontFamily(Font(R.font.readex_pro_medium)),
                fontSize = 16.sp,
                modifier = Modifier.padding(6.dp)
            )
        }
        TextButton(onClick = {
            navHostController.navigate(Pages.Policy.route)
        }, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "By using voila you will agree to our Privacy Policy or Terms of Service's.",
                fontFamily = FontFamily(Font(R.font.readex_pro)),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 20.dp, bottom = 50.dp),
                color = low,
            )
        }
    }
}