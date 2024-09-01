package `in`.snbapps.vidmeet.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NetworkViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NetworkViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NetworkViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}