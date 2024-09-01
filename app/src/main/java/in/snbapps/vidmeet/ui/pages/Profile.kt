package `in`.snbapps.vidmeet.ui.pages

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import `in`.snbapps.vidmeet.R
import `in`.snbapps.vidmeet.data.Story
import `in`.snbapps.vidmeet.data.User
import `in`.snbapps.vidmeet.representer.Stories
import `in`.snbapps.vidmeet.ui.theme.dark
import `in`.snbapps.vidmeet.ui.theme.high
import `in`.snbapps.vidmeet.ui.theme.low
import `in`.snbapps.vidmeet.ui.theme.medium
import `in`.snbapps.vidmeet.ui.theme.red
import `in`.snbapps.vidmeet.ui.theme.unfocused
import `in`.snbapps.vidmeet.ui.theme.white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileBottomSheet(
    user: User,
    onDismiss: () -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        containerColor = white,
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        val imeInsets = WindowInsets.ime
        val context = LocalContext.current
        val density = LocalDensity.current
        val scrollState = rememberScrollState()

        val stories = remember {
            mutableListOf<Story>()
        }

        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(start = 20.dp, end = 20.dp, bottom = with(density) {
                    imeInsets
                        .getBottom(density)
                        .toDp()
                }),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            IconButton(
                onClick = {
                    onDismiss()
                },
                modifier = Modifier
                    .border(BorderStroke(1.dp, unfocused), CircleShape)
                    .align(Alignment.End)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "back",
                    tint = dark
                )
            }
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(if (user.profile?.isEmpty() == true) R.drawable.placeholder else user.profile.toString())
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
                    .border(2.dp, unfocused, CircleShape),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )
            Text(
                text = user.fname.toString(),
                fontFamily = FontFamily(Font(R.font.readex_pro_bold)),
                fontSize = 18.sp,
                color = high,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 20.dp)
            )
            Text(
                text = user.uname.toString(),
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 15.dp)
            ) {
                Button(
                    onClick = {
                    },
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 3.dp),
                    colors = ButtonColors(high, Color.White, low, Color.Black)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.call),
                            contentDescription = "back",
                            tint = white,
                            modifier = Modifier
                                .width(15.dp)
                                .height(15.dp)
                        )
                        Text(
                            text = "Call",
                            fontFamily = FontFamily(Font(R.font.readex_pro)),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(15.dp))
                Button(
                    onClick = {
                        Intent(context, MessagingActivity::class.java).putExtra("user", user)
                    },
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 3.dp),
                    colors = ButtonColors(high, Color.White, low, Color.Black)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.message),
                            contentDescription = "back",
                            tint = white,
                            modifier = Modifier
                                .width(15.dp)
                                .height(15.dp)
                        )
                        Text(
                            text = "Message",
                            fontFamily = FontFamily(Font(R.font.readex_pro)),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
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

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Stories",
                    fontFamily = FontFamily(Font(R.font.readex_pro_bold)),
                    fontSize = 16.sp,
                    color = high,
                    modifier = Modifier.padding(start = 8.dp, top = 15.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = {

                }) {
                    Text(
                        text = "View All",
                        fontFamily = FontFamily(Font(R.font.readex_pro)),
                        fontSize = 16.sp,
                        color = medium,
                        modifier = Modifier.padding(start = 8.dp, top = 15.dp)
                    )
                }
            }

            StoryList(stories = stories)

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
                    painter = painterResource(id = R.drawable.block),
                    contentDescription = "edit",
                    tint = red,
                    modifier = Modifier
                        .width(25.dp)
                        .height(25.dp)
                )
                Text(
                    text = "Block User",
                    fontFamily = FontFamily(Font(R.font.readex_pro_medium)),
                    fontSize = 14.sp,
                    color = red,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 15.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.right),
                    contentDescription = "edit",
                    tint = red,
                    modifier = Modifier
                        .width(15.dp)
                        .height(15.dp)
                )
            }

            HorizontalDivider(
                color = unfocused, thickness = 1.dp, modifier = Modifier
                    .clip(
                        RoundedCornerShape(50.dp)
                    )
                    .padding(top = 15.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, start = 10.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.block),
                    contentDescription = "edit",
                    tint = red,
                    modifier = Modifier
                        .width(25.dp)
                        .height(25.dp)
                )
                Text(
                    text = "Report User",
                    fontFamily = FontFamily(Font(R.font.readex_pro_medium)),
                    fontSize = 14.sp,
                    color = red,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 15.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.right),
                    contentDescription = "edit",
                    tint = red,
                    modifier = Modifier
                        .width(15.dp)
                        .height(15.dp)
                )
            }

            HorizontalDivider(
                color = unfocused, thickness = 1.dp, modifier = Modifier
                    .clip(
                        RoundedCornerShape(50.dp)
                    )
                    .padding(top = 15.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, start = 10.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.trash),
                    contentDescription = "edit",
                    tint = red,
                    modifier = Modifier
                        .width(25.dp)
                        .height(25.dp)
                )
                Text(
                    text = "Delete Chat",
                    fontFamily = FontFamily(Font(R.font.readex_pro_medium)),
                    fontSize = 14.sp,
                    color = red,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 15.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.right),
                    contentDescription = "edit",
                    tint = red,
                    modifier = Modifier
                        .width(15.dp)
                        .height(15.dp)
                )
            }

            HorizontalDivider(
                color = unfocused, thickness = 1.dp, modifier = Modifier
                    .clip(
                        RoundedCornerShape(50.dp)
                    )
                    .padding(top = 15.dp)
            )
        }
    }
}

@Composable
fun StoryList(stories: List<Story>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(stories) { item: Story ->
            Stories(story = item)
        }
    }
}