package `in`.snbapps.vidmeet.data

import java.io.Serializable

data class OutgoingData(
    val user: User? = null,
    val selectedUsersJson: String? = "",
    val type: CallType? = null,
    val isMultiple: Boolean = false
) : Serializable

enum class CallType {
    VIDEO,
    VOICE
}