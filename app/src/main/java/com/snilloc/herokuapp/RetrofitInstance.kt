package com.snilloc.herokuapp

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface ApiInterface {
    @Headers("format:json")
    @GET("profile/")
    fun getProfilesList(): Call<List<SignUpResponse>> //This works

    @Headers("Content-Type:application/json")
    @POST("login")
    fun signIn(@Body info: UserSignIn): Call<ResponseBody>

    @Headers("format:json")
    @POST("profile/")
    fun signUp(@Body info: UserBody) : Call<SignUpResponse> //This works

    @GET("feed/")
    fun getFeed(): Call<FeedResults>
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
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}

data class UserBody(val email: String, val name: String, val password: String)

data class UserSignIn(
    @SerializedName("email")
    val userEmail: String,
    @SerializedName("password")
    val userPassword: String)

data class SignUpResponse(
    @SerializedName("id")
    val responseId: Int,
    @SerializedName("email")
    val responseEmail: String,
    @SerializedName("name")
    val responseName: String)

data class FeedResults(val details: String)
