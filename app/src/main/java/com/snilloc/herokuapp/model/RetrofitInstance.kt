package com.snilloc.herokuapp.model

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface ApiInterface {
    @Headers("format:json")
    @GET("profile/")
    fun getProfilesList(): Call<List<SignUpResponse>>

    @Headers("format:json")
    @POST("login/")
    fun signIn(@Body info: SignInBody): Call<SignInResponse>

    @Headers("format:json")
    @POST("profile/")
    fun signUp(@Body info: SignUpBody): Call<SignUpResponse>

    @Headers("format:json")
    @GET("feed/")
    fun getFeed(): Call<FeedResults>

    @Headers("format:json")
    @POST("upload/")
    fun postPhoto(@Body photo: PhotoUploadBody): Call<ResponseBody>
}

class RetrofitInstance {
    companion object {
        private const val BASE_URL = "https://roctivapp.herokuapp.com/api/"
        //Http Logging
        private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        private val client: OkHttpClient = OkHttpClient.Builder().apply {
            //Server has little memory, add the time taken to communicate with the server
            connectTimeout(1, TimeUnit.MINUTES)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
            addInterceptor(interceptor)
        }.build()

        fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
    }
}
