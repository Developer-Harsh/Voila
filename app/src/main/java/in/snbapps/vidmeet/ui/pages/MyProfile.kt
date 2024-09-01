package `in`.snbapps.vidmeet.ui.pages

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import `in`.snbapps.vidmeet.R
import `in`.snbapps.vidmeet.navigation.Pages
import `in`.snbapps.vidmeet.ui.theme.high
import `in`.snbapps.vidmeet.ui.theme.low
import `in`.snbapps.vidmeet.ui.theme.medium
import `in`.snbapps.vidmeet.ui.theme.unfocused
import `in`.snbapps.vidmeet.ui.theme.white
import `in`.snbapps.vidmeet.utils.SharedPrefs

@Composable
fun MyProfileScreen(navHostController: NavHostController) {
    val context = LocalContext.current

    val user = SharedPrefs(context).getUser()

    val imeInsets = WindowInsets.ime
    val density = LocalDensity.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(start = 20.dp, end = 20.dp, top = 70.dp, bottom = with(density) {
                imeInsets
                    .getBottom(density)
                    .toDp()
            }),
        horizontalAlignment = Alignment.CenterHorizontally,
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
            contentDescription = "profile",
            modifier = Modifier
                .height(130.dp)
                .width(130.dp)
                .clip(
                    CircleShape
                )
                .border(2.dp, unfocused, CircleShape)
                .clickable {
                    navHostController.navigate(Pages.ViewPhoto.data(Uri.encode(user?.profile.toString())))
                },
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )
        Text(
            text = user?.fname.toString(),
            fontFamily = FontFamily(Font(R.font.readex_pro_bold)),
            fontSize = 18.sp,
            color = high,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 20.dp)
        )
        Text(
            text = user?.uname.toString(),
            fontFamily = FontFamily(Font(R.font.readex_pro)),
            fontSize = 14.sp,
            color = high,
            textAlign = TextAlign.Center,
        )
        Column(
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.email),
                    contentDescription = "back",
                    tint = high,
                    modifier = Modifier
                        .width(18.dp)
                        .height(18.dp)
                )
                Text(
                    text = user?.email.toString(),
                    fontFamily = FontFamily(Font(R.font.readex_pro)),
                    fontSize = 14.sp,
                    color = high,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 7.dp)
                )
            }
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.pad),
                    contentDescription = "back",
                    tint = high,
                    modifier = Modifier
                        .width(18.dp)
                        .height(18.dp)
                )
                Text(
                    text = user?.phone.toString(),
                    fontFamily = FontFamily(Font(R.font.readex_pro)),
                    fontSize = 14.sp,
                    color = high,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 7.dp)
                )
            }
        }
        Spacer(modifier = Modifier.padding(top = 15.dp))
        Button(
            onClick = {
                navHostController.navigate(Pages.EditProfile.route)
            },
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 3.dp),
            colors = ButtonColors(high, Color.White, low, Color.Black)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription = "back",
                    tint = white,
                    modifier = Modifier
                        .width(15.dp)
                        .height(15.dp)
                )
                Text(
                    text = "Edit Profile",
                    fontFamily = FontFamily(Font(R.font.readex_pro)),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        HorizontalDivider(
            color = unfocused, thickness = 1.dp, modifier = Modifier
                .clip(
                    RoundedCornerShape(50.dp)
                )
                .padding(top = 20.dp)
        )

        Column(
            modifier = Modifier
                .padding(top = 20.dp, start = 10.dp, end = 10.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = "About Me",
                fontFamily = FontFamily(Font(R.font.readex_pro)),
                fontSize = 12.sp,
                color = medium,
                textAlign = TextAlign.Center,
            )
            Text(
                text = user?.about.toString(),
                fontFamily = FontFamily(Font(R.font.readex_pro_medium)),
                fontSize = 14.sp,
                color = high,
                textAlign = TextAlign.Center,
            )
        }

        HorizontalDivider(
            color = unfocused, thickness = 1.dp, modifier = Modifier
                .clip(
                    RoundedCornerShape(50.dp)
                )
                .padding(top = 20.dp)
        )

        Text(
            text = "More Options",
            fontFamily = FontFamily(Font(R.font.readex_pro_bold)),
            fontSize = 16.sp,
            color = high,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(start = 8.dp, top = 15.dp)
                .align(Alignment.Start)
                .fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp, start = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.settings),
                contentDescription = "edit",
                tint = high,
                modifier = Modifier
                    .width(25.dp)
                    .height(25.dp)
            )
            Text(
                text = "Settings",
                fontFamily = FontFamily(Font(R.font.readex_pro_medium)),
                fontSize = 14.sp,
                color = high,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 15.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.right),
                contentDescription = "edit",
                tint = high,
                modifier = Modifier
                    .width(15.dp)
                    .height(15.dp)
            )
        }

        HorizontalDivider(
            color = Color.Transparent, thickness = 1.dp, modifier = Modifier
                .clip(
                    RoundedCornerShape(50.dp)
                )
                .padding(top = 15.dp)
        )
    }
}