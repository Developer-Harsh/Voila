package `in`.snbapps.vidmeet.ui.pages

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import `in`.snbapps.vidmeet.R
import `in`.snbapps.vidmeet.ui.theme.dark
import `in`.snbapps.vidmeet.ui.theme.high
import `in`.snbapps.vidmeet.ui.theme.low
import `in`.snbapps.vidmeet.ui.theme.unfocused

@Composable
fun WhatVoilaScreen(navHostController: NavHostController) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(top = 40.dp, start = 20.dp, end = 20.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(onClick = {
                Log.e("back", "back")
            }, modifier = Modifier.border(BorderStroke(1.dp, unfocused), CircleShape)) {
                Icon(
                    painter = painterResource(id = R.drawable.left),
                    contentDescription = "back",
                    tint = dark
                )
            }
            Text(
                text = "About Us",
                fontFamily = FontFamily(Font(R.font.readex_pro_medium)),
                fontSize = 16.sp,
                color = high,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
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
            text = "What is Voila?",
            fontFamily = FontFamily(Font(R.font.readex_pro_semibold)),
            fontSize = 16.sp,
            color = high,
            modifier = Modifier.padding(top = 20.dp)
        )
        Text(
            text = "Voila is an calling or chatting application brought to you by Sneva Technologies and Harsh Kumar Singh, the founder of Sneva Technologies. It's free to use and safe also we ensure our users safety.",
            fontFamily = FontFamily(Font(R.font.readex_pro)),
            fontSize = 14.sp,
            color = low,
            modifier = Modifier.padding(top = 10.dp)
        )
        Text(
            text = "Who is Harsh Kumar Singh?",
            fontFamily = FontFamily(Font(R.font.readex_pro_semibold)),
            fontSize = 16.sp,
            color = high,
            modifier = Modifier.padding(top = 20.dp)
        )
        Text(
            text = "Harsh Kumar Singh is an Programmer with expertise in most of the programming languages and in 2019 he thought about creating a Indian IT Company when he was 14 years old to provide high quality it solutions and applications to indians.",
            fontFamily = FontFamily(Font(R.font.readex_pro)),
            fontSize = 14.sp,
            color = low,
            modifier = Modifier.padding(top = 10.dp)
        )
        Text(
            text = "Version",
            fontFamily = FontFamily(Font(R.font.readex_pro_semibold)),
            fontSize = 16.sp,
            color = high,
            modifier = Modifier.padding(top = 20.dp)
        )
        Text(
            text = "The version of voila is 4.0",
            fontFamily = FontFamily(Font(R.font.readex_pro)),
            fontSize = 14.sp,
            color = low,
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}

@Preview
@Composable
fun PreviewMe() {
    WhatVoilaScreen(navHostController = NavHostController(LocalContext.current))
}