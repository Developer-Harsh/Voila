package `in`.snbapps.vidmeet.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Path


interface ApiService {
    @POST("v1/projects/voila-sneva/messages:send")
    fun sendRemoteMessage(
        @Path("project_id") projectId: String?,
        @HeaderMap headers: Map<String?, String?>?,
        @Body remoteBody: String?
    ): Call<String?>?
}
