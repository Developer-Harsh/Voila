package `in`.snbapps.vidmeet.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.snbapps.vidmeet.R
import `in`.snbapps.vidmeet.ui.theme.focused
import `in`.snbapps.vidmeet.ui.theme.low
import `in`.snbapps.vidmeet.ui.theme.unfocused
import `in`.snbapps.vidmeet.ui.theme.white
import `in`.snbapps.vidmeet.ui.theme.yellow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateFieldsBottomSheet(
    field: String, text: String, painter: Painter, hint: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
) {
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var email by remember {
        mutableStateOf(TextFieldValue(text))
    }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        containerColor = Color.Transparent,
        modifier = Modifier
            .offset(y = ((-9).dp))
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(0.dp),
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        val padd = 20.dp

        Box(
            modifier = Modifier
                .background(white, RoundedCornerShape(30.dp))
                .padding(top = padd, bottom = padd),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = {
                        Text(
                            text = hint,
                            fontFamily = FontFamily(Font(R.font.readex_pro)),
                            fontSize = 14.sp
                        )
                    },
                    maxLines = if (field == VoilaFields.About) 3 else 1,
                    textStyle = TextStyle(
                        fontFamily = FontFamily(Font(R.font.readex_pro)),
                        fontSize = 16.sp
                    ),
                    leadingIcon = {
                        Icon(
                            painter = painter,
                            contentDescription = "icon",
                            tint = focused,
                        )
                    },
                    singleLine = if (field == VoilaFields.About) false else true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = if (field == VoilaFields.Phone) KeyboardType.Phone else KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    }),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = unfocused,
                        focusedBorderColor = focused,
                        unfocusedLabelColor = focused,
                        focusedLabelColor = focused,
                        focusedTextColor = focused,
                        unfocusedTextColor = focused
                    ),
                )
                Spacer(modifier = Modifier.height(15.dp))
                Button(
                    onClick = {
                        if (email.text.isEmpty()) {
                            scope.launch {
                                val snackbarJob = launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Field is empty!",
                                        actionLabel = "Hide",
                                        duration = SnackbarDuration.Indefinite
                                    )
                                }
                                delay(3000)
                                snackbarJob.cancel()
                            }
                        } else {
                            onSave(email.text)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonColors(yellow, Color.Black, low, Color.Black)
                ) {
                    Text(
                        text = "Save",
                        fontFamily = FontFamily(Font(R.font.readex_pro_medium)),
                        fontSize = 16.sp,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
        }
    }
}