package `in`.snbapps.vidmeet.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import `in`.snbapps.vidmeet.R
import `in`.snbapps.vidmeet.ui.theme.high
import `in`.snbapps.vidmeet.ui.theme.white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressBottomSheet(onDismiss: () -> Unit) {
    val confirmStateChange: (SheetValue) -> Boolean = remember {
        { false }
    }
    val modalBottomSheetState =
        rememberModalBottomSheetState(confirmValueChange = confirmStateChange)
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.progress))
    val progress by animateLottieCompositionAsState(
        composition,
        isPlaying = true,
        iterations = LottieConstants.IterateForever
    )

    ModalBottomSheet(
        containerColor = white,
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Box(
            modifier = Modifier.padding(vertical = 30.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp),
                )

                Text(
                    text = "...",
                    fontFamily = FontFamily(Font(R.font.readex_pro_bold)),
                    fontSize = 28.sp,
                    color = high,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}