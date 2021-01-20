package com.snilloc.herokuapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.snilloc.herokuapp.ui.SignInActivity

private const val TAG = "SignUpActivity"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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