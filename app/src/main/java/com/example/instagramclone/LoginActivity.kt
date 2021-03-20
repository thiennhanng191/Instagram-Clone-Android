package com.example.instagramclone

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.parse.LogInCallback
import com.parse.ParseException
import com.parse.ParseUser

class LoginActivity : AppCompatActivity() {
    internal lateinit var etUsername: EditText
    internal lateinit var etPassword: EditText
    internal lateinit var btnLogin: Button

    companion object {
        const val TAG ="LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ParseUser.getCurrentUser()?.let{
            goToMainActivity()
        }
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                var username = etUsername.getText().toString()
                var password = etPassword.getText().toString()
                loginUser(username, password)
            }
        })

        btnLogin.setEnabled(!etUsername.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty())

        // disable compose tweet button when the user hasn't input anything

        etUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                btnLogin.setEnabled(!etUsername.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty())
            }

            override fun afterTextChanged(s: Editable) {}
        })

        etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                btnLogin.setEnabled(!etUsername.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty())
            }

            override fun afterTextChanged(s: Editable) {}
        })

    }
    private fun loginUser(username: String, password: String) {
        Log.i(TAG, "Attempting to login user: $username")

        ParseUser.logInInBackground(username, password, object : LogInCallback {
            override fun done(user: ParseUser?, e: ParseException?) {
                // navigate to the main activity once user successful logs in
                //TODO: add better error handling
                e?.let{
                    Log.e(TAG, "Issue with Login", e);
                    Toast.makeText(getApplicationContext(), "Issue with login", Toast.LENGTH_SHORT).show()
                    return
                }
                goToMainActivity();
                Toast.makeText(getApplicationContext(), "Login success!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun goToMainActivity() {
        val i = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(i)
        finish()
    }

}