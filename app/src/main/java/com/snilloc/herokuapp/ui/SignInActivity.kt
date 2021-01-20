package com.snilloc.herokuapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.snilloc.herokuapp.MainActivity
import com.snilloc.herokuapp.R
import com.snilloc.herokuapp.databinding.ActivitySignInBinding
import com.snilloc.herokuapp.model.ApiInterface
import com.snilloc.herokuapp.model.RetrofitInstance
import com.snilloc.herokuapp.model.SignInResponse
import com.snilloc.herokuapp.model.UserSignInBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "SIGN_IN_ACTIVITY"

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.apply {
            btnSignIn.setOnClickListener {
                signIn()
            }
            signUpTv.setOnClickListener {
                goToSignUpActivity()
            }
        }
    }

    private fun signIn() {
        binding.progressBar.visibility = View.VISIBLE

        //Check if the values entered are valid
        if (!validateDetails()) {
            return
        }

        val email = binding.emailAddressEt.text.toString()
        val password = binding.passwordEd.text.toString()

        val userSignInBody = UserSignInBody(email, password)

        val retrofitInstance =
            RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
        retrofitInstance.signIn(userSignInBody).enqueue(object : Callback<SignInResponse> {
            override fun onResponse(
                call: Call<SignInResponse>,
                response: Response<SignInResponse>
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                if (response.isSuccessful) {
                    //Update signedInValue
                    updateSignedInValue()
                    Log.d(TAG, "Sign in successful. Token: ${response.body()?.token}")
                    //Go to Home screen
                    goToMainActivity()

                } else {
                    Toast.makeText(this@SignInActivity, "Sign in unsuccessful", Toast.LENGTH_LONG).show()
                    Log.d(TAG, "Sign in unsuccessful. ${response.code()}")
                }
            }

            override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                binding.progressBar.visibility = View.INVISIBLE
                Toast.makeText(this@SignInActivity, "Sign in failed: $t", Toast.LENGTH_LONG).show()
                Log.d(TAG, "Sign in failed. Error: $t")
            }
        })
    }

    private fun updateSignedInValue() {
        val signedIn = true
        //Store value in SharedPref
        val sharedPreferences = this.getSharedPreferences(getString(R.string.signed_in_key), Context.MODE_PRIVATE) ?: return
        with(sharedPreferences.edit()) {
            putBoolean(getString(R.string.signed_in_key), signedIn)
            apply()
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this@SignInActivity, MainActivity::class.java)
        //Verify that the Intent will open up the Activity without any problems
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
            finish()
        }
    }

    private fun goToSignUpActivity() {
        val intent = Intent(this@SignInActivity, MainActivity::class.java)
        //Verify that the Intent will open up the Activity without any problems
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
            finish()
        }
    }

    private fun validateDetails(): Boolean {
        //Validate the email and password
        var valid = true

        val email = binding.emailAddressEt.text.toString()
        if (TextUtils.isEmpty(email)) {
            binding.emailAddressEt.error = "Required."
            valid = false
        } else {
            binding.emailAddressEt.error = null
        }

        val password = binding.passwordEd.text.toString()
        if (TextUtils.isEmpty(password)) {
            binding.passwordEd.error = "Required"
            valid = false
        } else if (password.length < 6) {
            binding.passwordEd.error = "Password should be more than 6 characters"
            valid = false
        } else {
            binding.passwordEd.error = null
        }

        return valid
    }
}