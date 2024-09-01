package `in`.snbapps.vidmeet.data

import java.io.Serializable

data class User(
    var email: String? = "",
    val password: String? = "",
    var phone: String? = "",
    var uname: String? = "",
    var fname: String? = "",
    val profile: String? = "",
    val uid: String? = "",
    var about: String? = "",
    var token: String? = ""
) : Serializable