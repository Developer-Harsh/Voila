package `in`.snbapps.vidmeet.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import `in`.snbapps.vidmeet.R
import `in`.snbapps.vidmeet.navigation.Pages
import `in`.snbapps.vidmeet.ui.theme.dark
import `in`.snbapps.vidmeet.ui.theme.white
import `in`.snbapps.vidmeet.utils.SharedPrefs
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navHostController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = remember { auth.currentUser }
    var sharedPrefs = SharedPrefs(LocalContext.current)

    LaunchedEffect(currentUser) {
        delay(3000)
        if (currentUser != null) {
            navHostController.navigate(Pages.Home.route) {
                popUpTo(0)
            }
        } else if (sharedPrefs.isLoggedIn()) {
            navHostController.navigate(Pages.Home.route) {
                popUpTo(0)
            }
        } else {
            navHostController.navigate(Pages.Introduction.route)
        }
    }

    Scaffold(
        Modifier.fillMaxSize(),
        containerColor = dark,
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Transparent,
                modifier = Modifier.padding(bottom = 40.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.shield),
                            tint = white,
                            contentDescription = "e2e",
                            modifier = Modifier.size(size = 15.dp)
                        )
                        Text(
                            text = "End to End Encrypted",
                            fontFamily = FontFamily(Font(R.font.readex_pro_light)),
                            fontSize = 12.sp,
                            color = white,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                    Text(
                        text = "version 4.0",
                        fontFamily = FontFamily(Font(R.font.readex_pro_light)),
                        fontSize = 14.sp,
                        color = white,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }
            }
        }
    ) {
        it.calculateBottomPadding()

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "voila",
                fontFamily = FontFamily(Font(R.font.readex_pro_bold)),
                fontSize = 55.sp,
                color = white,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 120.dp)
            )
        }
    }
}