package com.snilloc.herokuapp.model

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {
    @Headers("format:json")
    @GET("profile/")
    fun getProfilesList(): Call<List<SignUpResponse>> //This works

    @Headers("format:json")
    @POST("login/")
    fun signIn(@Body info: UserSignInBody): Call<SignInResponse>

    @Headers("format:json")
    @POST("profile/")
    fun signUp(@Body info: SignUpBody): Call<SignUpResponse> //This works

    @GET("feed/")
    fun getFeed(): Call<FeedResults>

    @POST("upload/")
    fun postPhoto(): Call<ResponseBody>
}

class RetrofitInstance {
    companion object {
        private const val BASE_URL = "https://roctivapp.herokuapp.com/api/"

        private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        private val client: OkHttpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
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
