package `in`.snbapps.vidmeet.ui.pages

import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import `in`.snbapps.vidmeet.R
import `in`.snbapps.vidmeet.data.User
import `in`.snbapps.vidmeet.ui.theme.btn
import `in`.snbapps.vidmeet.ui.theme.high
import `in`.snbapps.vidmeet.ui.theme.lowOpacWhite
import `in`.snbapps.vidmeet.ui.theme.medium
import `in`.snbapps.vidmeet.ui.theme.red
import `in`.snbapps.vidmeet.ui.theme.white
import `in`.snbapps.vidmeet.ui.theme.yellow
import `in`.snbapps.vidmeet.utils.SharedPrefs
import `in`.snbapps.vidmeet.utils.VoilaFields
import io.github.alexzhirkevich.qrose.options.QrBallShape
import io.github.alexzhirkevich.qrose.options.QrBrush
import io.github.alexzhirkevich.qrose.options.QrFrameShape
import io.github.alexzhirkevich.qrose.options.QrLogoPadding
import io.github.alexzhirkevich.qrose.options.QrLogoShape
import io.github.alexzhirkevich.qrose.options.QrPixelShape
import io.github.alexzhirkevich.qrose.options.brush
import io.github.alexzhirkevich.qrose.options.circle
import io.github.alexzhirkevich.qrose.options.roundCorners
import io.github.alexzhirkevich.qrose.options.solid
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import kotlinx.coroutines.launch

@ExperimentalGetImage
@Composable
fun ScannerScreen(navHostController: NavHostController) {
    val pagerState = rememberPagerState(0, 0f) { 2 }
    var flashlightOn by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val tabs = listOf(R.drawable.scanner, R.drawable.scanned)
    var camera: Camera? = null

    val myProf = SharedPrefs(LocalContext.current).getUser()
    val lifecycleOwner = LocalLifecycleOwner.current

    val data = "voila://appdata:findUser=user?${myProf?.uid.toString()}"

    val logoPainter: Painter = painterResource(R.drawable.logo)

    val qrcodePainter: Painter = rememberQrCodePainter(data) {
        logo {
            painter = logoPainter
            padding = QrLogoPadding.Natural(0.5f)
            shape = QrLogoShape.circle()
            size = 0.2f
        }

        shapes {
            ball = QrBallShape.circle()
            darkPixel = QrPixelShape.circle()
            frame = QrFrameShape.roundCorners(1f)
        }

        colors {
            dark = QrBrush.brush {
                Brush.linearGradient(
                    0f to yellow,
                    1f to red,
                    end = Offset(it, it)
                )
            }
            frame = QrBrush.solid(high)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (pagerState.currentPage == 0) Brush.linearGradient(
                    listOf(
                        high,
                        medium
                    )
                ) else Brush.linearGradient(
                    listOf(
                        Color(0xFFffe259),
                        Color(0xFFffa751)
                    )
                )
            )
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 130.dp)
                    ) {
                        IconButton(onClick = {
                            if (!flashlightOn) {
                                flashlightOn = true
                                camera?.cameraControl?.enableTorch(flashlightOn)
                            } else {
                                flashlightOn = false
                                camera?.cameraControl?.enableTorch(flashlightOn)
                            }
                        }, modifier = Modifier.padding(bottom = 60.dp)) {
                            Icon(
                                painter = painterResource(id = if (flashlightOn) R.drawable.flashed else R.drawable.flash),
                                contentDescription = "",
                                tint = if (flashlightOn or !flashlightOn) white else lowOpacWhite
                            )
                        }

                        PreviewViewComposable(lifecycleOwner) {
                            camera = it
                        }
                    }
                }

                1 -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Image(
                            painter = qrcodePainter,
                            contentDescription = null,
                            modifier = Modifier
                                .size(300.dp)
                                .background(white, RoundedCornerShape(20.dp))
                                .padding(20.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }

        TabRow(
            modifier = Modifier.padding(horizontal = 30.dp, vertical = 30.dp),
            selectedTabIndex = pagerState.currentPage,
            indicator = {},
            divider = {},
            containerColor = Color.Transparent
        ) {
            tabs.forEachIndexed { index, title ->
                val icon = if (pagerState.currentPage == index) white else lowOpacWhite
                val bg = if (pagerState.currentPage == index) btn else Color.Transparent

                Tab(
                    icon = {
                        Icon(
                            painter = painterResource(id = title),
                            contentDescription = "icon",
                            tint = icon
                        )
                    },
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                    selectedContentColor = icon,
                    unselectedContentColor = icon,
                    modifier = Modifier
                        .background(color = bg, shape = RoundedCornerShape(50.dp))
                        .height(40.dp),
                )
            }
        }
    }
}

@ExperimentalGetImage
@Composable
fun PreviewViewComposable(lifecycleOwner: LifecycleOwner, flashlight: (Camera) -> Unit) {
    var user by remember { mutableStateOf<User?>(null) }

    var showProfile by remember {
        mutableStateOf(true)
    }

    if (showProfile) {
        user?.let {
            ProfileBottomSheet(user = it) {
                showProfile = false
            }
        }
    }

    Box(contentAlignment = Alignment.Center) {
        AndroidView(
            factory = { context ->
                val previewView = PreviewView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }

                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()

                    cameraProvider.unbindAll()

                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val barcodeScanner = BarcodeScanning.getClient()
                    val imageAnalyzer = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also {
                            it.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                                val mediaImage = imageProxy.image
                                if (mediaImage != null) {
                                    val image = InputImage.fromMediaImage(
                                        mediaImage,
                                        imageProxy.imageInfo.rotationDegrees
                                    )
                                    barcodeScanner.process(image)
                                        .addOnSuccessListener { barcodes ->
                                            for (barcode in barcodes) {
                                                barcode.rawValue?.let { value ->

                                                    cameraProvider.unbindAll()

                                                    Toast.makeText(
                                                        context,
                                                        "Barcode found $value",
                                                        Toast.LENGTH_SHORT
                                                    ).show()

                                                    val text =
                                                        VoilaFields.extractUsernameFromUrl(value)

                                                    FirebaseFirestore.getInstance()
                                                        .collection(VoilaFields.USERS)
                                                        .whereEqualTo(
                                                            VoilaFields.UID,
                                                            text
                                                        )
                                                        .get()
                                                        .addOnCompleteListener { task ->
                                                            if (task.isSuccessful && task.result != null && task.result.getDocuments().size > 0) {
                                                                val documentSnapshot =
                                                                    task.result!!.documents[0]
                                                                user =
                                                                    documentSnapshot.toObject(
                                                                        User::class.java
                                                                    )

                                                                showProfile = true
                                                            } else {
                                                                Toast.makeText(
                                                                    context,
                                                                    "Account doesn't exist with this email!",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }
                                                        }.addOnFailureListener {
                                                            Toast.makeText(
                                                                context,
                                                                "Network connectivity issue!",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                }
                                            }
                                        }
                                        .addOnCompleteListener {
                                            imageProxy.close()
                                        }
                                }
                            }
                        }

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                    val camera = cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalyzer
                    )

                    flashlight(camera)
                }, ContextCompat.getMainExecutor(context))

                previewView

            },
            modifier = Modifier
                .height(300.dp)
                .width(300.dp)
                .clip(RoundedCornerShape(18.dp))
        )
    }
}