package `in`.snbapps.vidmeet.ui.pages

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import `in`.snbapps.vidmeet.R
import `in`.snbapps.vidmeet.ui.theme.btn
import `in`.snbapps.vidmeet.ui.theme.dark
import `in`.snbapps.vidmeet.ui.theme.white

@Composable
fun ViewPhotoScreen(navHostController: NavHostController, img: String) {

    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .background(dark)
            .fillMaxSize()
            .padding(horizontal = 15.dp, vertical = 30.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(Uri.decode(img).ifEmpty { R.drawable.placeholder })
                    .placeholder(R.drawable.placeholder)
                    .size(coil.size.Size.ORIGINAL)
                    .build(),
            ),
            contentDescription = "photo",
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale *= zoom
                        offsetX += pan.x
                        offsetY += pan.y
                    }
                }
                .graphicsLayer(
                    scaleX = maxOf(1f, minOf(3f, scale)),
                    scaleY = maxOf(1f, minOf(3f, scale)),
                    translationX = offsetX,
                    translationY = offsetY
                ),
            contentScale = ContentScale.Fit
        )
        IconButton(onClick = {
            navHostController.popBackStack()
        }, modifier = Modifier
            .background(btn, CircleShape)
            .align(Alignment.TopStart)) {
            Icon(
                painter = painterResource(id = R.drawable.left),
                contentDescription = "back",
                tint = white
            )
        }
    }
}