package `in`.snbapps.vidmeet.data

data class Story(
    val storyId: String? = "",
    val uid: String? = "",
    val mediaContent: MediaContent? = null,
    val caption: String? = null,
    val timeStamp: Long? = null,
)
