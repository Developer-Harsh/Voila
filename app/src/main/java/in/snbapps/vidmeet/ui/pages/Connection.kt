package `in`.snbapps.vidmeet.ui.pages

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import `in`.snbapps.vidmeet.R
import `in`.snbapps.vidmeet.ui.theme.dark
import `in`.snbapps.vidmeet.ui.theme.high
import `in`.snbapps.vidmeet.ui.theme.low
import `in`.snbapps.vidmeet.ui.theme.unfocused
import `in`.snbapps.vidmeet.ui.theme.white
import `in`.snbapps.vidmeet.ui.theme.yellow

@Composable
fun ConnectionScreen(navHostController: NavHostController) {
    val imeInsets = WindowInsets.ime
    val density = LocalDensity.current
    val scrollState = rememberScrollState()
    val context = LocalContext.current

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
        IconButton(onClick = {
            (context as Activity).finish()
        },
            modifier = Modifier
                .border(BorderStroke(1.dp, unfocused), CircleShape)
                .align(Alignment.Start)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.close),
                contentDescription = "back",
                tint = dark
            )
        }

        Text(
            text = "Internet not Connected",
            fontFamily = FontFamily(Font(R.font.readex_pro_bold)),
            fontSize = 30.sp,
            color = high,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 40.dp)
        )

        Text(
            text = "Sorry, to inform but internet is needed to operate voila application, please connect to the network to use voila app.",
            fontFamily = FontFamily(Font(R.font.readex_pro)),
            fontSize = 16.sp,
            color = low,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp, bottom = 50.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                navHostController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonColors(yellow, Color.Black, low, Color.Black)
        ) {
            Text(
                text = "Recheck Config",
                fontFamily = FontFamily(Font(R.font.readex_pro_medium)),
                fontSize = 16.sp,
                modifier = Modifier.padding(6.dp)
            )
        }
        Spacer(modifier = Modifier.padding(top = 20.dp))
        OutlinedButton(
            onClick = {
                val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, unfocused),
            colors = ButtonColors(white, Color.Black, low, Color.Black)
        ) {
            Text(
                text = "Connect Now",
                fontFamily = FontFamily(Font(R.font.readex_pro_medium)),
                fontSize = 16.sp,
                modifier = Modifier.padding(6.dp)
            )
        }

        Spacer(modifier = Modifier.height(70.dp))
    }
}