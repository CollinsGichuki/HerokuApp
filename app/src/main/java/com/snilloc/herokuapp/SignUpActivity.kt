package com.snilloc.herokuapp

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.snilloc.herokuapp.databinding.ActivitySignUpBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "SignUpActivity"

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnSignUp.setOnClickListener {
            signUp()
        }
    }

    private fun signUp() {
        binding.progressBar.visibility = View.VISIBLE
        //Check if the details entered are okay
        if (!validateDetails()) {
            return
        }

        val name = binding.nameEt.text.toString()
        val email = binding.emailAddressEt.text.toString()
        val password = binding.passwordEd.text.toString()

        val signUpDetails = UserBody(email, name, password)

        val retrofitInstance = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)

        //val call = retrofitInstance.getProfilesList()
        retrofitInstance.signUp(signUpDetails).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                if (response.isSuccessful) {
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this@SignUpActivity, "Profiles successful", Toast.LENGTH_LONG).show()
                    Log.d(TAG, "Profile Name: ${response.body()?.responseName}")
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                binding.progressBar.visibility = View.INVISIBLE
                Toast.makeText(this@SignUpActivity, "Profiles unsuccessful", Toast.LENGTH_LONG).show()
                Log.d(TAG, "error : $t")
            }

        })




//        call.enqueue(object : Callback<List<SignUpResponse>> {
//            override fun onResponse(
//                call: Call<List<SignUpResponse>>,
//                response: Response<List<SignUpResponse>>
//            ) {
//                if (response.isSuccessful) {
//                    binding.progressBar.visibility = View.INVISIBLE
//                    Toast.makeText(this@SignUpActivity, "Profiles successful", Toast.LENGTH_LONG).show()
//                    Log.d(TAG, "Profiles: ${response.body()?.get(1)?.responseName}")
//                }
//            }
//
//            override fun onFailure(call: Call<List<SignUpResponse>>, t: Throwable) {
//                binding.progressBar.visibility = View.INVISIBLE
//                Toast.makeText(this@SignUpActivity, "Profile unsuccessful", Toast.LENGTH_LONG).show()
//            }
//
//        })

//        retrofitInstance.getFeed().enqueue(object : Callback<FeedResults> {
//            override fun onResponse(call: Call<FeedResults>, response: Response<FeedResults>) {
//                if (response.isSuccessful) {
//                    Toast.makeText(this@SignUpActivity, "Sign up successful", Toast.LENGTH_LONG).show()
//                    Log.d(TAG, "Sign up successful. ID: ${response.body()}")
//                }
//            }
//
//            override fun onFailure(call: Call<FeedResults>, t: Throwable) {
//                Toast.makeText(this@SignUpActivity, "Sign up unsuccessful", Toast.LENGTH_LONG).show()
//                Log.d(TAG, "Sign up unsuccessful. Error: $t")
//            }
//        })

    }

    private fun validateDetails(): Boolean {
        //Validate the email and password
        var valid = true

        val name = binding.nameEt.text.toString()
        if (TextUtils.isEmpty(name)) {
            binding.nameEt.error = "Required"
            valid = false
        } else {
            binding.nameEt.error = null
        }

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