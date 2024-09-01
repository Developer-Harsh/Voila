package `in`.snbapps.vidmeet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import `in`.snbapps.vidmeet.navigation.VoilaRoutes
import `in`.snbapps.vidmeet.ui.theme.VoilaTheme
import `in`.snbapps.vidmeet.utils.NetworkViewModel
import `in`.snbapps.vidmeet.utils.NetworkViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VoilaTheme {
                VoilaPreview()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VoilaPreview() {
    val navController = rememberNavController()
    val factory = NetworkViewModelFactory(LocalContext.current)
    val viewModel: NetworkViewModel = viewModel(factory = factory)
    VoilaRoutes(navController = navController, viewModel)
}