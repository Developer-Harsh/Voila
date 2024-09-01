package `in`.snbapps.vidmeet.representer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.snbapps.vidmeet.R
import `in`.snbapps.vidmeet.data.Story
import `in`.snbapps.vidmeet.ui.theme.white

@Composable
fun Stories(story: Story) {
    Row(
        modifier = Modifier
            .width(160.dp)
            .height(220.dp)
    ) {
        Box {
            Image(
                painter = painterResource(id = R.drawable.photo),
                contentDescription = "data",
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(RoundedCornerShape(16.dp))
            )
            Text(
                text = "some kfd vkjd vjs djv kdk jsfkj",
                fontFamily = FontFamily(Font(R.font.readex_pro)),
                fontSize = 10.sp,
                color = white,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .align(alignment = Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(15.dp)
            )
        }
    }
}