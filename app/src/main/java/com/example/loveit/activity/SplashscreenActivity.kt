package com.example.loveit.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.loveit.MainActivity
import com.example.loveit.R
import com.example.loveit.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.ktx.Firebase

class SplashscreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        val user= FirebaseAuth.getInstance().currentUser;

            Handler(Looper.getMainLooper()).postDelayed({
                if(user==null) {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
                else
                {
                    startActivity(Intent(this,MainActivity::class.java))

                }

            },3000)
    }
}