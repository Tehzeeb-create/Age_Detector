package com.example.agedetector
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        dbHelper = DatabaseHelper(this)

        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        registerButton = findViewById(R.id.registerButton)

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (isValidRegistration(username, password)) {
                // Register the user in the database
                registerUser(username, password)
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

                // Navigate to the login activity
                navigateToLoginActivity()
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidRegistration(username: String, password: String): Boolean {
        // Implement your logic to check if the username is available for registration
        return !dbHelper.isUsernameExists(username)
    }

    private fun registerUser(username: String, password: String) {
        // Implement your logic to register the user (e.g., insert into the database)
        dbHelper.insertUser(username, password)
    }

    private fun navigateToLoginActivity() {

         val intent = Intent(this, LoginActivity::class.java)
         startActivity(intent)
        finish() // Finish the current activity after registration
    }
}
