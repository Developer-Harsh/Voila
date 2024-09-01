package `in`.snbapps.vidmeet.navigation

import android.net.Uri

sealed class Pages(val route: String) {
    object Splash : Pages("splash")
    object Home : Pages("home")
    object Login : Pages("login")
    object Register : Pages("register")
    object Introduction : Pages("intro")
    object Policy : Pages("policy")
    object Forgot : Pages("forgot")
    object WhatVoila : Pages("what_voila")
    object EditProfile : Pages("edit_profile")
    object Connection : Pages("connection")
    object AddStory : Pages("add_story")
    object Scanner : Pages("scanner")
    object MyProfile : Pages("my_profile")
    object ViewPhoto : Pages("view_photo/{img}") {
        fun data(img: String): String = "view_photo/${Uri.encode(img)}"
    }
}