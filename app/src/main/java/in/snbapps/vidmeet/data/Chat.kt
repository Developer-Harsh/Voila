package `in`.snbapps.vidmeet.data

import java.io.Serializable

data class Chat(
    val chatId: String? = "",
    val participants: Participants? = null,
    val lastMsg: LastMessage? = null,
) : Serializable

data class LastMessage(
    val messageId: String? = "",
    val senderId: String? = "",
    val text: String? = "",
    val content: MediaContent? = null,
    val timeStamp: Long? = null
) : Serializable

data class Participants(
    val unread: Int? = null,
    val presence: String? = ""
) : Serializable