package `in`.snbapps.vidmeet.representer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.firebase.firestore.FirebaseFirestore
import `in`.snbapps.vidmeet.R
import `in`.snbapps.vidmeet.data.Chat
import `in`.snbapps.vidmeet.data.MediaType
import `in`.snbapps.vidmeet.data.User
import `in`.snbapps.vidmeet.ui.pages.ProfileBottomSheet
import `in`.snbapps.vidmeet.ui.theme.high
import `in`.snbapps.vidmeet.ui.theme.low
import `in`.snbapps.vidmeet.ui.theme.unfocused
import `in`.snbapps.vidmeet.ui.theme.yellow
import `in`.snbapps.vidmeet.utils.SharedPrefs
import `in`.snbapps.vidmeet.utils.VoilaFields
import `in`.snbapps.vidmeet.utils.getTimeAgo

@Composable
fun Chats(chat: User, profile: () -> Unit) {
    var data by remember {
        mutableStateOf<Chat?>(null)
    }

    var showProfile by remember {
        mutableStateOf(false)
    }

    if (showProfile) {
        ProfileBottomSheet(user = chat) {
            showProfile = false
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable {
                profile()
            }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            FirebaseFirestore.getInstance().collection(VoilaFields.CHATS)
                .document("${SharedPrefs(LocalContext.current).getUser()?.uid}-${chat.uid}")
                .get().addOnSuccessListener { result ->
                    if (result.exists()) {
                        data = result.toObject(Chat::class.java)
                    }
                }

            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(if (chat.profile?.isEmpty() == true) R.drawable.placeholder else chat.profile.toString())
                        .placeholder(R.drawable.placeholder)
                        .size(coil.size.Size.ORIGINAL)
                        .build(),
                    contentScale = ContentScale.Crop,
                ),
                contentDescription = "data",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .width(60.dp)
                    .height(60.dp)
                    .border(1.dp, unfocused)
                    .clickable { showProfile = true }
            )
            Column(
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp)
                    .weight(1f)
            ) {
                Text(
                    text = chat.fname.toString(),
                    fontFamily = FontFamily(Font(R.font.readex_pro_medium)),
                    fontSize = 16.sp,
                    color = high,
                    textAlign = TextAlign.Start,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (data?.lastMsg?.content != null) {
                        Icon(
                            painter = painterResource(
                                id = if (data!!.lastMsg?.content?.mediaType == MediaType.AUDIO) R.drawable.audio_receive else if (data!!.lastMsg?.content?.mediaType == MediaType.IMAGE) R.drawable.image else if (data!!.lastMsg?.content?.mediaType == MediaType.VIDEO) R.drawable.video_received else if (data!!.lastMsg?.content?.mediaType == MediaType.DOCUMENT) R.drawable.document_received else R.drawable.location_received
                            ),
                            contentDescription = "icon",
                            tint = high,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = data?.lastMsg?.text.toString(),
                        fontFamily = FontFamily(Font(R.font.readex_pro)),
                        fontSize = 14.sp,
                        color = if ((data?.participants?.unread ?: 0) > 0) high else low,
                        maxLines = 1,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(start = if (data?.lastMsg?.content != null) 5.dp else 0.dp)
                    )
                }
            }
            Column {
                Text(
                    text = data?.lastMsg?.timeStamp?.let { getTimeAgo(it) } ?: "",
                    fontFamily = FontFamily(Font(R.font.readex_pro)),
                    fontSize = 14.sp,
                    color = low,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 5.dp)
                )

                if ((data?.participants?.unread ?: 0) > 0) {
                    Text(
                        text = data?.participants?.unread.toString(),
                        fontFamily = FontFamily(Font(R.font.readex_pro)),
                        fontSize = 10.sp,
                        color = high,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(yellow)
                            .size(23.dp)
                            .align(Alignment.End)
                    )
                }
            }
        }
    }
}