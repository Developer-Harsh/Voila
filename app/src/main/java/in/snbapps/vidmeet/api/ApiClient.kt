package `in`.snbapps.vidmeet.api

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class ApiClient {
    companion object {
        var retrofit: Retrofit? = null

        fun getClient(): Retrofit? {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl("https://fcm.googleapis.com/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build()
            }
            return retrofit
        }
    }
}
