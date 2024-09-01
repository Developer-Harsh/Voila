package `in`.snbapps.vidmeet.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NetworkViewModel(context: Context) : ViewModel() {
    private val _isConnected = MutableStateFlow(isOnline(context))
    val isConnected: StateFlow<Boolean> = _isConnected
}
