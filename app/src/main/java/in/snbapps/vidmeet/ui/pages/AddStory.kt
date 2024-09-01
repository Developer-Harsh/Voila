package `in`.snbapps.vidmeet.ui.pages

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import `in`.snbapps.vidmeet.R
import `in`.snbapps.vidmeet.data.MediaContent
import `in`.snbapps.vidmeet.data.MediaType
import `in`.snbapps.vidmeet.data.Story
import `in`.snbapps.vidmeet.ui.theme.focused
import `in`.snbapps.vidmeet.ui.theme.low
import `in`.snbapps.vidmeet.ui.theme.unfocused
import `in`.snbapps.vidmeet.ui.theme.white
import `in`.snbapps.vidmeet.ui.theme.yellow
import `in`.snbapps.vidmeet.utils.ProgressBottomSheet
import `in`.snbapps.vidmeet.utils.SharedPrefs
import `in`.snbapps.vidmeet.utils.VoilaFields
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date

@Composable
fun AddStoryScreen(navHostController: NavHostController) {

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseDatabase.getInstance()
    val storage = FirebaseStorage.getInstance()
    val sharedPrefs = SharedPrefs(context)

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var text by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var fileExtension by remember { mutableStateOf<String?>(null) }

    var showSheet by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        fileExtension = uri?.let { getFileExtension(context, it) }
    }

    if (showSheet) {
        ProgressBottomSheet {
            showSheet = false
        }
    }

    LaunchedEffect(launcher) {
        launcher.launch("image/*")
    }

    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(horizontal = 15.dp, vertical = 30.dp),
        contentAlignment = Alignment.Center
    ) {
        selectedImageUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(24.dp)),
                contentScale = ContentScale.Crop
            )
            Row(
                modifier = Modifier
                    .padding(15.dp)
                    .align(Alignment.BottomCenter), verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = text, onValueChange = {
                        if (it.text.length <= 150) {
                            text = it
                        }
                    }, modifier = Modifier
                        .border(1.dp, unfocused, RoundedCornerShape(24.dp))
                        .weight(1f)
                        .background(
                            white, RoundedCornerShape(24.dp)
                        )
                        .padding(horizontal = 15.dp, vertical = 8.dp),
                    textStyle = TextStyle(
                        fontFamily = FontFamily(Font(R.font.readex_pro)),
                        fontSize = 16.sp,
                        color = focused
                    ),
                    maxLines = 5,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    }),
                    decorationBox = { innerTextField ->
                        if (text.text.isEmpty()) {
                            Text(
                                text = "Type context under 150 chars...",
                                fontFamily = FontFamily(Font(R.font.readex_pro)),
                                fontSize = 14.sp,
                                color = focused,
                            )
                        }
                        innerTextField()
                    }
                )
                Spacer(modifier = Modifier.width(15.dp))
                Button(
                    onClick = {
                        showSheet = true

                        fileExtension?.let {
                            val reference = storage.getReference(VoilaFields.STORIES)
                                .child(sharedPrefs.getUser()!!.uid.toString())
                                .child("${System.currentTimeMillis()}_voila.$it")

                            reference.putFile(uri).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    reference.downloadUrl.addOnCompleteListener { task1 ->
                                        if (task1.isSuccessful) {
                                            val download = task.result.toString()

                                            val ref = db.getReference(VoilaFields.STORIES)
                                                .child(sharedPrefs.getUser()!!.uid.toString())

                                            val key = ref.push().key

                                            val story = Story(
                                                key.toString(),
                                                sharedPrefs.getUser()!!.uid.toString(),
                                                MediaContent(download, MediaType.IMAGE),
                                                (text.text.ifEmpty { null }).toString(),
                                                Date().time
                                            )

                                            ref.child(key.toString()).setValue(story)
                                                .addOnSuccessListener {
                                                    showSheet = false
                                                    scope.launch {
                                                        val snackbarJob = launch {
                                                            snackbarHostState.showSnackbar(
                                                                message = "Story uploaded!",
                                                                actionLabel = "Hide",
                                                                duration = SnackbarDuration.Indefinite
                                                            )
                                                        }
                                                        delay(3000)
                                                        snackbarJob.cancel()
                                                    }
                                                    navHostController.popBackStack()
                                                }.addOnFailureListener {
                                                    showSheet = false
                                                    scope.launch {
                                                        val snackbarJob = launch {
                                                            snackbarHostState.showSnackbar(
                                                                message = "Failed to save story!",
                                                                actionLabel = "Hide",
                                                                duration = SnackbarDuration.Indefinite
                                                            )
                                                        }
                                                        delay(3000)
                                                        snackbarJob.cancel()
                                                    }
                                                }
                                        }
                                    }
                                }
                            }.addOnFailureListener {
                                showSheet = false
                                scope.launch {
                                    val snackbarJob = launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Failed to upload story!",
                                            actionLabel = "Hide",
                                            duration = SnackbarDuration.Indefinite
                                        )
                                    }
                                    delay(3000)
                                    snackbarJob.cancel()
                                }
                            }
                        }
                    },
                    colors = ButtonColors(yellow, Color.Black, low, Color.Black),
                    contentPadding = PaddingValues(0.dp),
                ) {
                    Text(
                        text = "Post",
                        fontFamily = FontFamily(Font(R.font.readex_pro_medium)),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
        }
    }
}

fun getFileExtension(context: Context, uri: Uri): String? {
    val contentResolver: ContentResolver = context.contentResolver
    val mimeTypeMap: MimeTypeMap = MimeTypeMap.getSingleton()
    return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
}