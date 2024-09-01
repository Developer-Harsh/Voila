package `in`.snbapps.vidmeet.utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.snbapps.vidmeet.R
import `in`.snbapps.vidmeet.ui.theme.dark
import `in`.snbapps.vidmeet.ui.theme.focused
import `in`.snbapps.vidmeet.ui.theme.high
import `in`.snbapps.vidmeet.ui.theme.low
import `in`.snbapps.vidmeet.ui.theme.medium
import `in`.snbapps.vidmeet.ui.theme.unfocused
import `in`.snbapps.vidmeet.ui.theme.white

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VoilaBottomNavigationBar(
    pagerState: PagerState,
    onIcon1Click: () -> Unit,
    onIcon2Click: () -> Unit,
    onTextButtonClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalDivider(
            color = unfocused,
            thickness = (0.9).dp,
        )
        BottomAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            containerColor = white,
            contentPadding = PaddingValues(0.dp),
            tonalElevation = 20.dp
        ) {
            IconButton(
                onClick = { onIcon1Click() },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    painter = painterResource(
                        id = if (pagerState.currentPage == 0) {
                            R.drawable.home_fill
                        } else {
                            R.drawable.home_out
                        }
                    ),
                    contentDescription = "Home",
                    tint = if (pagerState.currentPage == 0) high else medium
                )
            }

            Button(
                onClick = { onTextButtonClick() },
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                colors = ButtonColors(
                    focused,
                    white,
                    low,
                    dark
                ),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.plus),
                        contentDescription = "Plus",
                        tint = white,
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp)
                    )
                    Text(
                        text = "New Chat",
                        fontFamily = FontFamily(Font(R.font.readex_pro)),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 5.dp),
                        color = white,
                    )
                }
            }

            IconButton(
                onClick = { onIcon2Click() },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    painter = painterResource(
                        id = if (pagerState.currentPage == 1) {
                            R.drawable.profile_fill
                        } else {
                            R.drawable.profile_out
                        }
                    ),
                    contentDescription = "Profile",
                    tint = if (pagerState.currentPage == 1) high else medium
                )
            }
        }
    }
}