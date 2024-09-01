package `in`.snbapps.vidmeet.utils

import com.google.gson.Gson
import `in`.snbapps.vidmeet.data.OutgoingData
import `in`.snbapps.vidmeet.data.User
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.UUID


class VoilaFields {
    companion object {
        val USERS: String = "users"
        val USER: String = "user"
        val CHATS: String = "chats"
        val MESSAGES: String = "messages"
        val CONTACTS: String = "contacts"
        val STORIES: String = "stories"
        val VIEWS: String = "views"
        val KEYS: String = "keys"

        val Email: String = "email"
        val Password: String = "password"
        val Phone: String = "phone"
        val Username: String = "uname"
        val FullName: String = "fname"
        val Profile_Url: String = "profile"
        val UID: String = "uid"
        val About: String = "about"

        val REMOTE_MSG_AUTHORIZATION: String = "Authorization"
        val REMOTE_MSG_CONTENT_TYPE: String = "Content-Type"

        val REMOTE_MSG_TYPE: String = "type"
        val REMOTE_MSG_INVITATION: String = "invitation"
        val REMOTE_MSG_MEETING_TYPE: String = "meetingType"
        val REMOTE_MSG_INVITER_TOKEN: String = "inviterToken"
        val REMOTE_MSG_DATA: String = "data"
        val REMOTE_MSG_REGISTRATION_IDS: String = "registration_ids"

        val REMOTE_MSG_INVITATION_RESPONSE: String = "invitationResponse"

        val REMOTE_MSG_INVITATION_ACCEPTED: String = "accepted"
        val REMOTE_MSG_INVITATION_REJECTED: String = "rejected"
        val REMOTE_MSG_INVITATION_CANCELLED: String = "cancelled"

        val REMOTE_MSG_MEETING_ROOM: String = "meetingRoom"

        fun getRemoteMessageHeaders(): Map<String?, String?> {
            return mapOf(
                "Authorization" to "key=YOUR_SERVER_KEY",
                "Content-Type" to "application/json"
            )
        }

        fun generateKey(length: Int = 26): String {
            val uuid = UUID.randomUUID().toString().replace("-", "")
            return uuid.take(length)
        }

        fun extractUsernameFromUrl(url: String): String? {
            val regex = Regex("""\?(.+)$""")
            val matchResult = regex.find(url)
            return matchResult?.groups?.get(1)?.value
        }

        fun serializeUser(user: User): String {
            val gson = Gson()
            val json = gson.toJson(user)
            return URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
        }

        fun deserializeUser(userJson: String?): User? {
            return try {
                val decodedJson = URLDecoder.decode(userJson, StandardCharsets.UTF_8.toString())
                Gson().fromJson(decodedJson, User::class.java)
            } catch (e: Exception) {
                null
            }
        }

        fun serializeCallData(data: OutgoingData): String {
            val gson = Gson()
            val json = gson.toJson(data)
            return URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
        }

        fun deserializeCallData(dataJson: String?): OutgoingData? {
            return try {
                val decodedJson = URLDecoder.decode(dataJson, StandardCharsets.UTF_8.toString())
                Gson().fromJson(decodedJson, OutgoingData::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }
}