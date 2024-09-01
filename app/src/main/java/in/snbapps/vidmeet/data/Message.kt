package `in`.snbapps.vidmeet.data

data class Message(
    val messageId: String? = "",
    val senderId: String? = "",
    val text: String? = "",
    val timeStamp: Long? = null,
    val status: MessageStatus? = null,
    val reactions: MutableList<Reaction>? = null,
    val reply: String? = "",
    val media: MediaContent? = null,
)

enum class MessageStatus {
    SENT,
    DELIVERED,
    SEEN
}

data class Reaction(
    val userId: String? = "",
    val emoji: String? = ""
)