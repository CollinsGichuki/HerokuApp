package com.snilloc.herokuapp.model

import com.google.gson.annotations.SerializedName

data class SignUpBody(val email: String, val name: String, val password: String)

data class FeedResults(val details: String)

data class SignInResponse(val token: String)

data class UserSignInBody(
    @SerializedName("username")
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
