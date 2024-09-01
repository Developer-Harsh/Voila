package `in`.snbapps.vidmeet.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.snbapps.vidmeet.R
import `in`.snbapps.vidmeet.ui.theme.dark
import `in`.snbapps.vidmeet.ui.theme.high
import `in`.snbapps.vidmeet.ui.theme.low
import `in`.snbapps.vidmeet.ui.theme.unfocused
import `in`.snbapps.vidmeet.ui.theme.white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewChatBottomSheet(
    onDismiss: () -> Unit,
    onQRClicked: () -> Unit,
    onNewContactClicked: () -> Unit
) {

    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        containerColor = Color.Transparent,
        modifier = Modifier
            .offset(y = ((-9).dp))
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(0.dp),
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = {},
    ) {
        Box(
            modifier = Modifier.background(white, RoundedCornerShape(30.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                        .clickable {
                            onNewContactClicked()
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.message),
                        contentDescription = "newCHat",
                        tint = dark,
                        modifier = Modifier
                            .width(25.dp)
                            .height(25.dp)
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = "New Chat",
                            fontFamily = FontFamily(Font(R.font.readex_pro_medium)),
                            fontSize = 14.sp,
                            color = high,
                        )
                        Text(
                            text = "Add a new contact to chat by phone number or email id.",
                            fontFamily = FontFamily(Font(R.font.readex_pro)),
                            fontSize = 12.sp,
                            color = low,
                        )
                    }
                }
                HorizontalDivider(
                    color = unfocused,
                    thickness = (0.9).dp,
                    modifier = Modifier.padding(top = 20.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                        .clickable {
                            onQRClicked()
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.scanner),
                        contentDescription = "newCHat",
                        tint = dark,
                        modifier = Modifier
                            .width(25.dp)
                            .height(25.dp)
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = "Scan QR Code",
                            fontFamily = FontFamily(Font(R.font.readex_pro_medium)),
                            fontSize = 14.sp,
                            color = high,
                        )
                        Text(
                            text = "Add contact by QR code, a easier way to chat.",
                            fontFamily = FontFamily(Font(R.font.readex_pro)),
                            fontSize = 12.sp,
                            color = low,
                        )
                    }
                }
                HorizontalDivider(
                    color = Color.Transparent,
                    thickness = (0.9).dp,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }
        }
        Button(
            onClick = {
                onDismiss()
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 20.dp),
            colors = ButtonColors(
                white,
                high,
                low,
                dark
            ),
            contentPadding = PaddingValues(vertical = 11.dp, horizontal = 47.dp)
        ) {
            Text(
                text = "Cancel",
                fontFamily = FontFamily(Font(R.font.readex_pro)),
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 5.dp),
                color = high,
            )
        }
    }
}