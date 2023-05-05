package com.example.loveit.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.loveit.MainActivity
import com.example.loveit.R
import com.example.loveit.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.core.view.View
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    val auth=FirebaseAuth.getInstance();
    private var verificationId:String?=null

    private lateinit var dialog:AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dialog= AlertDialog.Builder(this).setView(R.layout.loading_layout)
            .setCancelable(false)
            .create()

        binding.sendotp.setOnClickListener{
            if(binding.number.text!!.isEmpty()){
                binding.number.error="Please Enter your number"
            }
            else
            {
                sendotp(binding.number.text.toString())
            }
        }

        binding.verifyotp.setOnClickListener{
            if(binding.otp.text!!.isEmpty()){
                binding.otp.error="Please Enter your number"
            }
            else
            {
                verifyotp(binding.otp.text.toString())
            }

        }
    }

    private fun verifyotp(otp: String) {
        dialog.show()

//        binding.sendotp.showLoadingButton()
        val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)
        signInWithPhoneAuthCredential(credential)

    }

    private fun sendotp(number: String) {
//        binding.sendotp.showLoadingButton();
        dialog.show();
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//                binding.sendotp.showNormalButton()
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                this@LoginActivity.verificationId=verificationId
                dialog.dismiss()

//                binding.sendotp.showNormalButton()

                binding.enternumber.visibility=GONE
                binding.number.visibility=GONE
                binding.sendotp.visibility=GONE

                binding.enterotp.visibility= VISIBLE
                binding.otp.visibility= VISIBLE
                binding.verifyotp.visibility= VISIBLE

            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91$number") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
//                binding.sendotp.showNormalButton()
                if (task.isSuccessful) {

                    checkUserExist(binding.number.text.toString())
//                    startActivity(Intent(this,MainActivity::class.java))
//                    finish()

                } else {
                    dialog.dismiss()
                    Toast.makeText(this,task.exception!!.message,Toast.LENGTH_LONG).show()

                }
            }
    }

    private fun checkUserExist(number: String) {
        FirebaseDatabase.getInstance().getReference("users").child(number)

            .addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    dialog.dismiss()
                    Toast.makeText(this@LoginActivity,p0.message,Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists())
                    {
                        dialog.dismiss()
                        startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                        finish()
                    }
                    else
                    {
                        startActivity(Intent(this@LoginActivity,RegistrationActivity::class.java))
                        finish()
                    }
                }
            })

    }
}