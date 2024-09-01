package `in`.snbapps.vidmeet.ui.pages

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.firebase.firestore.FirebaseFirestore
import `in`.snbapps.vidmeet.R
import `in`.snbapps.vidmeet.navigation.Pages
import `in`.snbapps.vidmeet.ui.theme.dark
import `in`.snbapps.vidmeet.ui.theme.high
import `in`.snbapps.vidmeet.ui.theme.medium
import `in`.snbapps.vidmeet.ui.theme.unfocused
import `in`.snbapps.vidmeet.ui.theme.white
import `in`.snbapps.vidmeet.utils.ProgressBottomSheet
import `in`.snbapps.vidmeet.utils.SharedPrefs
import `in`.snbapps.vidmeet.utils.UpdateFieldsBottomSheet
import `in`.snbapps.vidmeet.utils.UpdateProfileImage
import `in`.snbapps.vidmeet.utils.VoilaFields

@Composable
fun EditProfileScreen(navHostController: NavHostController) {
    val context = LocalContext.current

    val user = SharedPrefs(context).getUser()
    val firestore = FirebaseFirestore.getInstance()
    val imeInsets = WindowInsets.ime
    val density = LocalDensity.current
    val scrollState = rememberScrollState()
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var fileExtension by remember { mutableStateOf<String?>(null) }
    var showUpdateSheet by remember { mutableStateOf(false) }
    var updateField by remember { mutableStateOf(false) }
    var showSheet by remember { mutableStateOf(false) }
    var field by remember { mutableStateOf("") }
    var icon by remember { mutableStateOf(R.drawable.close) }
    val drawable = painterResource(id = icon)
    var hint by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
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

    if (showUpdateSheet) {
        UpdateProfileImage(
            onDismiss = {
                showUpdateSheet = false
            },
            onViewImage = {
                navHostController.navigate(Pages.ViewPhoto.data(Uri.encode(user?.profile.toString())))
            },
            onUpdateImage = {
                launcher.launch("image/*")
            }) {
            showUpdateSheet = false
            showSheet = true
            firestore.collection(VoilaFields.USERS).document(user?.uid.toString())
                .update("profile", "").addOnSuccessListener {
                    showSheet = false
                    Toast.makeText(context, "Profile image removed!", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    showSheet = false
                    Toast.makeText(context, "Profile image cannot be removed!", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

    if (updateField) {
        UpdateFieldsBottomSheet(
            field = field,
            text = text,
            painter = drawable,
            hint = hint,
            onDismiss = { updateField = false }) { string ->
            firestore.collection(VoilaFields.USERS)
                .document(user?.uid.toString())
                .update(field, string)
                .addOnSuccessListener {
                    updateField = false
                    SharedPrefs(context).updateUserField {
                        when (field) {
                            VoilaFields.Username -> it.uname = string
                            VoilaFields.FullName -> it.fname = string
                            VoilaFields.About -> it.about = string
                            VoilaFields.Phone -> it.phone = string
                            else -> it.email = string
                        }
                    }
                    Toast.makeText(
                        context,
                        "Field updated successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                }.addOnFailureListener {
                    updateField = false
                    Toast.makeText(
                        context,
                        "Field couldn't be updated!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(start = 20.dp, end = 20.dp, top = 40.dp, bottom = with(density) {
                imeInsets
                    .getBottom(density)
                    .toDp()
            }),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = {
                navHostController.popBackStack()
            }, modifier = Modifier.border(BorderStroke(1.dp, unfocused), CircleShape)) {
                Icon(
                    painter = painterResource(id = R.drawable.left),
                    contentDescription = "back",
                    tint = dark
                )
            }
            Text(
                text = "Edit Profile",
                fontFamily = FontFamily(Font(R.font.readex_pro_medium)),
                fontSize = 14.sp,
                color = high,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = {
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.menu),
                    contentDescription = "menu",
                    tint = Color.Transparent
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

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
                .clickable {
                    showUpdateSheet = true
                }
                .clip(
                    CircleShape
                )
                .border(2.dp, unfocused, CircleShape),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )

        IconButton(modifier = Modifier
            .height(35.dp)
            .width(35.dp)
            .offset(y = ((-20).dp)),
            onClick = {
                showUpdateSheet = true
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.camera),
                contentDescription = "edit",
                tint = white,
                modifier = Modifier
                    .width(35.dp)
                    .height(35.dp)
                    .background(dark, CircleShape)
                    .padding(10.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.username),
                contentDescription = "edit",
                tint = dark,
                modifier = Modifier
                    .width(22.dp)
                    .height(22.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Username",
                    fontFamily = FontFamily(Font(R.font.readex_pro)),
                    fontSize = 12.sp,
                    color = medium,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = user?.uname.toString(),
                    fontFamily = FontFamily(Font(R.font.readex_pro_medium)),
                    fontSize = 14.sp,
                    color = high,
                    textAlign = TextAlign.Center,
                )
            }
            IconButton(onClick = {
                field = VoilaFields.Username
                text = user?.uname.toString()
                icon = R.drawable.username
                hint = "Username"
                updateField = true
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.right),
                    contentDescription = "edit",
                    tint = dark,
                    modifier = Modifier
                        .width(14.dp)
                        .height(14.dp)
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.person),
                contentDescription = "edit",
                tint = dark,
                modifier = Modifier
                    .width(22.dp)
                    .height(22.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Full Name",
                    fontFamily = FontFamily(Font(R.font.readex_pro)),
                    fontSize = 12.sp,
                    color = medium,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = user?.fname.toString(),
                    fontFamily = FontFamily(Font(R.font.readex_pro_medium)),
                    fontSize = 14.sp,
                    color = high,
                    textAlign = TextAlign.Center,
                )
            }
            IconButton(onClick = {
                field = VoilaFields.FullName
                text = user?.fname.toString()
                icon = R.drawable.person
                hint = "Full Name"
                updateField = true
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.right),
                    contentDescription = "edit",
                    tint = dark,
                    modifier = Modifier
                        .width(14.dp)
                        .height(14.dp)
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.edit),
                contentDescription = "edit",
                tint = dark,
                modifier = Modifier
                    .width(22.dp)
                    .height(22.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp)
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
            IconButton(onClick = {
                field = VoilaFields.About
                text = user?.about.toString()
                icon = R.drawable.edit
                hint = "About Me"
                updateField = true
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.right),
                    contentDescription = "edit",
                    tint = dark,
                    modifier = Modifier
                        .width(14.dp)
                        .height(14.dp)
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.email),
                contentDescription = "edit",
                tint = dark,
                modifier = Modifier
                    .width(22.dp)
                    .height(22.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Email Address",
                    fontFamily = FontFamily(Font(R.font.readex_pro)),
                    fontSize = 12.sp,
                    color = medium,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = user?.email.toString(),
                    fontFamily = FontFamily(Font(R.font.readex_pro_medium)),
                    fontSize = 14.sp,
                    color = high,
                    textAlign = TextAlign.Center,
                )
            }
            IconButton(onClick = {

            }) {
                Icon(
                    painter = painterResource(id = R.drawable.right),
                    contentDescription = "edit",
                    tint = dark,
                    modifier = Modifier
                        .width(14.dp)
                        .height(14.dp)
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.call),
                contentDescription = "edit",
                tint = dark,
                modifier = Modifier
                    .width(22.dp)
                    .height(19.dp)
                    .padding(start = 3.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Phone Number",
                    fontFamily = FontFamily(Font(R.font.readex_pro)),
                    fontSize = 12.sp,
                    color = medium,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = user?.phone.toString(),
                    fontFamily = FontFamily(Font(R.font.readex_pro_medium)),
                    fontSize = 14.sp,
                    color = high,
                    textAlign = TextAlign.Center,
                )
            }
            IconButton(onClick = {
                field = VoilaFields.Phone
                text = user?.phone.toString()
                icon = R.drawable.call
                hint = "Phone Number"
                updateField = true
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.right),
                    contentDescription = "edit",
                    tint = dark,
                    modifier = Modifier
                        .width(14.dp)
                        .height(14.dp)
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
    }
}

@Preview
@Composable
fun LoadPreview() {
    EditProfileScreen(navHostController = NavHostController(LocalContext.current))
}