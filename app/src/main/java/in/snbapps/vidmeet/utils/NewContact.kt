package `in`.snbapps.vidmeet.utils

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.firebase.firestore.FirebaseFirestore
import `in`.snbapps.vidmeet.R
import `in`.snbapps.vidmeet.data.User
import `in`.snbapps.vidmeet.ui.theme.focused
import `in`.snbapps.vidmeet.ui.theme.high
import `in`.snbapps.vidmeet.ui.theme.unfocused
import `in`.snbapps.vidmeet.ui.theme.white
import network.chaintech.cmpcountrycodepicker.ui.CountryPicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewContactBottomSheet(
    onDismiss: () -> Unit,
    onAdd: (User) -> Unit
) {
    val context = LocalContext.current
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val pagerState = rememberPagerState(0, 0f) { 2 }
    var number by remember {
        mutableStateOf(TextFieldValue(""))
    }

    var email by remember {
        mutableStateOf(TextFieldValue(""))
    }

    val focusManager = LocalFocusManager.current

    var code by remember {
        mutableStateOf("")
    }

    var passString by remember {
        mutableStateOf("")
    }

    var isEmail by remember {
        mutableStateOf(false)
    }

    var user by remember { mutableStateOf<User?>(null) }

    user = User(
        "example@xyz",
        "12345678",
        "+0-0000000000",
        "username",
        "Full Name",
        "R.drawable.placeholder",
        "jabfaskjfba",
        "Hey, there i am using Voila",
    )

    val tabs = listOf(R.drawable.call, R.drawable.email)
    var isFinding = false
    var isFound = false

    ModalBottomSheet(
        modifier = Modifier.height(650.dp),
        containerColor = white,
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        TabRow(
            modifier = Modifier
                .padding(bottom = 20.dp)
                .width(80.dp)
                .align(Alignment.CenterHorizontally),
            selectedTabIndex = pagerState.currentPage,
            indicator = {},
            divider = {},
            containerColor = Color.Transparent
        ) {
            tabs.forEachIndexed { index, title ->
                Spacer(
                    modifier = Modifier
                        .height(2.dp)
                        .width(20.dp)
                        .background(
                            if (pagerState.currentPage == index) high else unfocused,
                            RoundedCornerShape(50.dp)
                        )
                )
            }
        }

        HorizontalPager(
            state = pagerState,
        ) { page ->
            when (page) {
                0 -> {
                    isEmail = false

                    Column(
                        modifier = Modifier.background(Color.White),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        if (isFound) {
                            Column(
                                modifier = Modifier
                                    .background(Color.White)
                                    .padding(start = 30.dp, end = 30.dp, top = 20.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .placeholder(R.drawable.placeholder)
                                            .data(if (user?.profile.toString() == "R.drawable.placeholder") R.drawable.placeholder else user?.profile.toString())
                                            .size(coil.size.Size.ORIGINAL)
                                            .build(),
                                        contentScale = ContentScale.Crop,
                                    ),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .height(140.dp)
                                        .width(140.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )

                                Text(
                                    text = user?.fname.toString(),
                                    fontFamily = FontFamily(Font(R.font.readex_pro_bold)),
                                    fontSize = 18.sp,
                                    color = high,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 30.dp)
                                )

                                Text(
                                    text = user?.uname.toString(),
                                    fontFamily = FontFamily(Font(R.font.readex_pro)),
                                    fontSize = 14.sp,
                                    color = high,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(bottom = 20.dp)
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 20.dp, start = 35.dp, end = 35.dp)
                                        .border(1.dp, unfocused, RoundedCornerShape(50.dp))
                                ) {
                                    CountryPicker(
                                        modifier = Modifier
                                            .border(1.dp, unfocused, RoundedCornerShape(50.dp))
                                            .padding(
                                                start = 5.dp,
                                                end = 3.dp,
                                                top = 4.dp,
                                                bottom = 4.dp
                                            ),
                                        defaultCountryCode = "in",
                                        showVerticalDivider = false,
                                        countryPhoneCodeTextStyle = TextStyle(
                                            fontFamily = FontFamily(Font(R.font.readex_pro)),
                                            fontSize = 14.sp,
                                            color = focused
                                        ),
                                        countryNameTextStyle = TextStyle(
                                            fontFamily = FontFamily(Font(R.font.readex_pro)),
                                            fontSize = 14.sp,
                                            color = focused
                                        ),
                                        countryCodeTextStyle = TextStyle(
                                            fontFamily = FontFamily(Font(R.font.readex_pro)),
                                            fontSize = 14.sp,
                                            color = focused
                                        ),
                                    ) { selectedCountry ->
                                        code = selectedCountry.countryPhoneNumberCode
                                    }

                                    Spacer(modifier = Modifier.width(15.dp))

                                    BasicTextField(
                                        value = number,
                                        onValueChange = {
                                            if (it.text.length <= 11) {
                                                number = it
                                                passString = "${code}-${number.text}"
                                            }
                                        },
                                        textStyle = TextStyle(
                                            fontFamily = FontFamily(Font(R.font.readex_pro)),
                                            fontSize = 24.sp,
                                            color = high,
                                            textAlign = TextAlign.Center
                                        ),
                                        readOnly = true
                                    )
                                }
                            }
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 20.dp, start = 35.dp, end = 35.dp)
                                    .border(1.dp, unfocused, RoundedCornerShape(50.dp))
                            ) {
                                CountryPicker(
                                    modifier = Modifier
                                        .border(1.dp, unfocused, RoundedCornerShape(50.dp))
                                        .padding(
                                            start = 5.dp,
                                            end = 3.dp,
                                            top = 4.dp,
                                            bottom = 4.dp
                                        ),
                                    defaultCountryCode = "in",
                                    showVerticalDivider = false,
                                    countryPhoneCodeTextStyle = TextStyle(
                                        fontFamily = FontFamily(Font(R.font.readex_pro)),
                                        fontSize = 14.sp,
                                        color = focused
                                    ),
                                    countryNameTextStyle = TextStyle(
                                        fontFamily = FontFamily(Font(R.font.readex_pro)),
                                        fontSize = 14.sp,
                                        color = focused
                                    ),
                                    countryCodeTextStyle = TextStyle(
                                        fontFamily = FontFamily(Font(R.font.readex_pro)),
                                        fontSize = 14.sp,
                                        color = focused
                                    ),
                                ) { selectedCountry ->
                                    code = selectedCountry.countryPhoneNumberCode
                                }

                                Spacer(modifier = Modifier.width(15.dp))

                                BasicTextField(
                                    value = number,
                                    onValueChange = {
                                        if (it.text.length <= 11) {
                                            number = it
                                            passString = "${code}-${number.text}"
                                        }
                                    },
                                    textStyle = TextStyle(
                                        fontFamily = FontFamily(Font(R.font.readex_pro)),
                                        fontSize = 24.sp,
                                        color = high,
                                        textAlign = TextAlign.Center
                                    ),
                                    readOnly = true
                                )
                            }

                            HorizontalDivider(
                                color = unfocused, thickness = 1.dp, modifier = Modifier
                                    .clip(
                                        RoundedCornerShape(50.dp)
                                    )
                            )

                            Column(
                                modifier = Modifier
                                    .padding(top = 30.dp, bottom = 20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .height(80.dp)
                                            .width(80.dp)
                                            .background(unfocused, CircleShape)
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {
                                                number = TextFieldValue("${number.text}1")
                                            }, contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "1",
                                            style = TextStyle(
                                                fontFamily = FontFamily(Font(R.font.readex_pro_light)),
                                                fontSize = 30.sp,
                                                color = high,
                                                textAlign = TextAlign.Center,
                                            ),
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(20.dp))
                                    Box(
                                        modifier = Modifier
                                            .height(80.dp)
                                            .width(80.dp)
                                            .background(unfocused, CircleShape)
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {
                                                number = TextFieldValue("${number.text}2")
                                            }, contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "2",
                                            style = TextStyle(
                                                fontFamily = FontFamily(Font(R.font.readex_pro_light)),
                                                fontSize = 30.sp,
                                                color = high,
                                                textAlign = TextAlign.Center,
                                            ),
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(20.dp))
                                    Box(
                                        modifier = Modifier
                                            .height(80.dp)
                                            .width(80.dp)
                                            .background(unfocused, CircleShape)
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {
                                                number = TextFieldValue("${number.text}3")
                                            }, contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "3",
                                            style = TextStyle(
                                                fontFamily = FontFamily(Font(R.font.readex_pro_light)),
                                                fontSize = 30.sp,
                                                color = high,
                                                textAlign = TextAlign.Center,
                                            ),
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .height(80.dp)
                                            .width(80.dp)
                                            .background(unfocused, CircleShape)
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {
                                                number = TextFieldValue("${number.text}4")
                                            }, contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "4",
                                            style = TextStyle(
                                                fontFamily = FontFamily(Font(R.font.readex_pro_light)),
                                                fontSize = 30.sp,
                                                color = high,
                                                textAlign = TextAlign.Center,
                                            ),
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(20.dp))
                                    Box(
                                        modifier = Modifier
                                            .height(80.dp)
                                            .width(80.dp)
                                            .background(unfocused, CircleShape)
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {
                                                number = TextFieldValue("${number.text}5")
                                            }, contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "5",
                                            style = TextStyle(
                                                fontFamily = FontFamily(Font(R.font.readex_pro_light)),
                                                fontSize = 30.sp,
                                                color = high,
                                                textAlign = TextAlign.Center,
                                            ),
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(20.dp))
                                    Box(
                                        modifier = Modifier
                                            .height(80.dp)
                                            .width(80.dp)
                                            .background(unfocused, CircleShape)
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {
                                                number = TextFieldValue("${number.text}6")
                                            }, contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "6",
                                            style = TextStyle(
                                                fontFamily = FontFamily(Font(R.font.readex_pro_light)),
                                                fontSize = 30.sp,
                                                color = high,
                                                textAlign = TextAlign.Center,
                                            ),
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .height(80.dp)
                                            .width(80.dp)
                                            .background(unfocused, CircleShape)
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {
                                                number = TextFieldValue("${number.text}7")
                                            }, contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "7",
                                            style = TextStyle(
                                                fontFamily = FontFamily(Font(R.font.readex_pro_light)),
                                                fontSize = 30.sp,
                                                color = high,
                                                textAlign = TextAlign.Center,
                                            ),
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(20.dp))
                                    Box(
                                        modifier = Modifier
                                            .height(80.dp)
                                            .width(80.dp)
                                            .background(unfocused, CircleShape)
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {
                                                number = TextFieldValue("${number.text}8")
                                            }, contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "8",
                                            style = TextStyle(
                                                fontFamily = FontFamily(Font(R.font.readex_pro_light)),
                                                fontSize = 30.sp,
                                                color = high,
                                                textAlign = TextAlign.Center,
                                            ),
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(20.dp))
                                    Box(
                                        modifier = Modifier
                                            .height(80.dp)
                                            .width(80.dp)
                                            .background(unfocused, CircleShape)
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {
                                                number = TextFieldValue("${number.text}9")
                                            }, contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "9",
                                            style = TextStyle(
                                                fontFamily = FontFamily(Font(R.font.readex_pro_light)),
                                                fontSize = 30.sp,
                                                color = high,
                                                textAlign = TextAlign.Center,
                                            ),
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .height(80.dp)
                                            .width(80.dp), contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "",
                                            style = TextStyle(
                                                fontFamily = FontFamily(Font(R.font.readex_pro_light)),
                                                fontSize = 30.sp,
                                                color = high,
                                                textAlign = TextAlign.Center,
                                            ),
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(20.dp))
                                    Box(
                                        modifier = Modifier
                                            .height(80.dp)
                                            .width(80.dp)
                                            .background(unfocused, CircleShape)
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {
                                                number = TextFieldValue("${number.text}0")
                                            }, contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "0",
                                            style = TextStyle(
                                                fontFamily = FontFamily(Font(R.font.readex_pro_light)),
                                                fontSize = 30.sp,
                                                color = high,
                                                textAlign = TextAlign.Center,
                                            ),
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(20.dp))
                                    Box(
                                        modifier = Modifier
                                            .height(80.dp)
                                            .width(80.dp)
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {
                                                if (number.text.length <= 9) {
                                                    if (number.text.isNotEmpty()) {
                                                        number =
                                                            TextFieldValue(number.text.dropLast(1))
                                                    }
                                                } else if (number.text.length >= 9) {
                                                    isFinding = true
                                                    FirebaseFirestore
                                                        .getInstance()
                                                        .collection(VoilaFields.USERS)
                                                        .whereEqualTo(
                                                            VoilaFields.Phone,
                                                            "${code}-${number.text}"
                                                        )
                                                        .get()
                                                        .addOnCompleteListener { task ->
                                                            if (task.isSuccessful && task.result != null && task.result.getDocuments().size > 0) {
                                                                val documentSnapshot =
                                                                    task.result!!.documents[0]
                                                                user =
                                                                    documentSnapshot.toObject(User::class.java)
                                                                isFinding = false
                                                                isFound = true
                                                            } else {
                                                                isFinding = false
                                                                Toast
                                                                    .makeText(
                                                                        context,
                                                                        "Account doesn't exist with this email!",
                                                                        Toast.LENGTH_SHORT
                                                                    )
                                                                    .show()
                                                            }
                                                        }
                                                        .addOnFailureListener {
                                                            isFinding = false
                                                            Toast
                                                                .makeText(
                                                                    context,
                                                                    "Network connectivity failed!",
                                                                    Toast.LENGTH_SHORT
                                                                )
                                                                .show()
                                                        }
                                                }
                                            }, contentAlignment = Alignment.Center
                                    ) {
                                        if (number.text.length <= 9) {
                                            Text(
                                                text = "⌫",
                                                style = TextStyle(
                                                    fontFamily = FontFamily(Font(R.font.readex_pro_light)),
                                                    fontSize = 25.sp,
                                                    color = high,
                                                    textAlign = TextAlign.Center,
                                                ),
                                            )
                                        } else if (isFinding) {
                                            CircularProgressIndicator(
                                                color = high,
                                                modifier = Modifier.size(25.dp),
                                                strokeWidth = 3.dp,
                                                strokeCap = StrokeCap.Round
                                            )
                                        } else if (number.text.length >= 9) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.search),
                                                contentDescription = "icon",
                                                tint = focused,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                1 -> {
                    isEmail = true

                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(start = 30.dp, end = 30.dp, top = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .placeholder(R.drawable.placeholder)
                                    .data(if (user?.profile.toString() == "R.drawable.placeholder") R.drawable.placeholder else user?.profile.toString())
                                    .size(coil.size.Size.ORIGINAL)
                                    .build(),
                                contentScale = ContentScale.Crop,
                            ),
                            contentDescription = "",
                            modifier = Modifier
                                .height(140.dp)
                                .width(140.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        Text(
                            text = user?.fname.toString(),
                            fontFamily = FontFamily(Font(R.font.readex_pro_bold)),
                            fontSize = 18.sp,
                            color = high,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 30.dp)
                        )

                        Text(
                            text = user?.uname.toString(),
                            fontFamily = FontFamily(Font(R.font.readex_pro)),
                            fontSize = 14.sp,
                            color = high,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 20.dp)
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = {
                                Text(
                                    text = "Email Address",
                                    fontFamily = FontFamily(Font(R.font.readex_pro)),
                                    fontSize = 14.sp
                                )
                            },
                            maxLines = 1,
                            textStyle = TextStyle(
                                fontFamily = FontFamily(Font(R.font.readex_pro)),
                                fontSize = 16.sp
                            ),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.email),
                                    contentDescription = "icon",
                                    tint = focused,
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = {
                                    if (email.text.isEmpty()) {
                                        Toast.makeText(
                                            context,
                                            "Email address field is empty!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else if (!Patterns.EMAIL_ADDRESS.matcher(email.text)
                                            .matches()
                                    ) {
                                        Toast.makeText(
                                            context,
                                            "Email pattern does not match!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        isFinding = true

                                        FirebaseFirestore.getInstance()
                                            .collection(VoilaFields.USERS)
                                            .whereEqualTo(VoilaFields.Email, email.text)
                                            .get()
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful && task.result != null && task.result.getDocuments().size > 0) {
                                                    val documentSnapshot =
                                                        task.result!!.documents[0]
                                                    user =
                                                        documentSnapshot.toObject(User::class.java)
                                                    isFinding = false
                                                } else {
                                                    isFinding = false
                                                    Toast.makeText(
                                                        context,
                                                        "Account doesn't exist with this email!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }.addOnFailureListener {
                                                isFinding = false
                                                Toast.makeText(
                                                    context,
                                                    "Network connectivity failed!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                    }
                                }) {
                                    if (isFinding) {
                                        CircularProgressIndicator(
                                            color = high,
                                            modifier = Modifier.size(25.dp),
                                            strokeWidth = 3.dp,
                                            strokeCap = StrokeCap.Round
                                        )
                                    } else {
                                        Icon(
                                            painter = painterResource(id = R.drawable.search),
                                            contentDescription = "icon",
                                            tint = focused,
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
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
                    }
                }
            }
        }

        OutlinedButton(
            onClick = {
                if (user?.uid == "jabfaskjfba") {
                    Toast.makeText(context, "Please find user first.", Toast.LENGTH_SHORT).show()
                } else {
                    onAdd(user!!)
                }
            },

            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 20.dp),
            border = BorderStroke(1.dp, unfocused),
            contentPadding = PaddingValues(vertical = 11.dp, horizontal = 47.dp)
        ) {
            Text(
                text = "Add Contact",
                fontFamily = FontFamily(Font(R.font.readex_pro)),
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 5.dp),
                color = high
            )
        }
    }
}