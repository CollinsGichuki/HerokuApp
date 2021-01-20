package com.snilloc.herokuapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.snilloc.herokuapp.databinding.ActivityMainBinding
import com.snilloc.herokuapp.model.ApiInterface
import com.snilloc.herokuapp.model.PhotoUploadBody
import com.snilloc.herokuapp.model.RetrofitInstance
import com.snilloc.herokuapp.ui.SignInActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "SignUpActivity"
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var uploadPhotoUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.apply {
            chooseBtn.setOnClickListener {
                choosePhotoFromGallery()
            }
            uploadBtn.setOnClickListener {
                uploadPhotoToServer()
            }
        }
    }

    private fun choosePhotoFromGallery() {
        val intent = Intent()
            .setType("image/*")
            .setAction(Intent.ACTION_GET_CONTENT)

        startActivityForResult(Intent.createChooser(intent, "Select photo"), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            uploadPhotoUri = data.data!!
            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uploadPhotoUri)
            //Set the image
            binding.image1.setImageBitmap(bitmap)
        }
    }

    private fun uploadPhotoToServer() {
        if (this::uploadPhotoUri.isInitialized) {
            binding.progressBar.visibility = View.VISIBLE

            //Create a PhotoUploadBody object
            val photoUploadBody = PhotoUploadBody(uploadPhotoUri.toString())

            //Create the retrofit instance
            val retrofitInstance = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
            retrofitInstance.postPhoto(photoUploadBody).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        binding.progressBar.visibility = View.INVISIBLE
                        Log.d(TAG, "Photo uploaded successfully")
                        Toast.makeText(baseContext, "Photo successfully uploaded", Toast.LENGTH_LONG).show()
                    } else {
                        binding.progressBar.visibility = View.INVISIBLE
                        Log.d(TAG, "Photo uploaded unsuccessfully")
                        Toast.makeText(baseContext, "Photo uploaded unsuccessful", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    binding.progressBar.visibility = View.INVISIBLE
                    Log.d(TAG, "Photo uploaded failed")
                    Toast.makeText(baseContext, "Photo uploaded failed: $t", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    override fun onStart() {
        super.onStart()
        //Check whether user is signed in
        val sharedPreferences = this.getSharedPreferences(getString(R.string.signed_in_key), Context.MODE_PRIVATE) ?: return
        val signedIn = sharedPreferences.getBoolean(getString(R.string.signed_in_key), false)
        Log.d(TAG, "SignedIn sharedPref value: $signedIn")
        if (!signedIn) {
            goToSigningScreen()
        }
    }

    private fun goToSigningScreen() {
        val intent = Intent(this, SignInActivity::class.java)
        //Verify that the Intent will open up the Activity without any problems
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.sign_out) {
            signOut()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun signOut() {
        //Set the signedIn SharedPref to false
        val sharedPreferences = this.getSharedPreferences(getString(R.string.signed_in_key), Context.MODE_PRIVATE) ?: return
        with(sharedPreferences.edit()) {
            putBoolean(getString(R.string.signed_in_key), false)
            apply()
        }
        goToSigningScreen()
    }
}