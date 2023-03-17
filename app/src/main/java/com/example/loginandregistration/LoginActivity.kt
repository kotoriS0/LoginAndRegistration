package com.example.loginandregistration

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.example.loginandregistration.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    companion object {
        // values to send in intents are called Extras and have EXTRA_BLAH format for naming key
        val EXTRA_USERNAME = "username"
        val EXTRA_PASSWORD = "password"

        val TAG = "LoginActivity"
    }

    val startRegistrationForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            // Handle the Intent to do stuff with return info
            binding.editTextLoginUsername.setText(intent?.getStringExtra(LoginActivity.EXTRA_USERNAME))
            binding.editTextLoginPassword.setText(intent?.getStringExtra(LoginActivity.EXTRA_PASSWORD))
        }
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initialize backendless
        Backendless.initApp( this, Constants.APP_ID, Constants.API_KEY )

        // login to backendless
        binding.buttonLoginLogin.setOnClickListener {
            Backendless.UserService.login(
                binding.editTextLoginUsername.text.toString(),
                binding.editTextLoginPassword.text.toString(),
                object : AsyncCallback<BackendlessUser?> {
                    override fun handleResponse(user: BackendlessUser?) {
                        // user has been logged in
                        Log.d(TAG, "onResponse: ${user?.getProperty("username")}")
                    }

                    override fun handleFault(fault: BackendlessFault?) {
                        // login failed, error: fault.message
                        Log.d(TAG, "onFailure: ${fault?.message}")
                    }
                }
            )
        }

        // launch registration activity
        binding.textViewLoginSignUp.setOnClickListener {
            // 1. create an Intent object with current activitu and destination activity's class
            val registrationIntent = Intent(this, RegistrationActivity::class.java)

            // 2. optionally add information to send with the intent
            // key value pairs just like with bundles
            registrationIntent.putExtra(EXTRA_USERNAME, binding.editTextLoginUsername.text.toString())
            registrationIntent.putExtra(EXTRA_PASSWORD, binding.editTextLoginPassword.text.toString())

            //// 3a. launch the new activity using the intent
            //startActivity(registrationIntent)
            //3b. launch the activity for a result using the variable from the register for result contract above
            startRegistrationForResult.launch(registrationIntent)
        }
    }
    
    private fun retrieveAllData() {
        Backendless.Data.of(LoanData::class.java).find(object : AsyncCallback<List<LoanData?>?> {
            override fun handleResponse(foundContacts: List<LoanData?>?) {
                // all Contact instances have been found
            }

            override fun handleFault(fault: BackendlessFault) {
                // an error has occurred, the error code can be retrieved with fault.getCode()
            }
        })
    }
}