package com.example.loveit.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.loveit.databinding.ActivityMessageBinding
import com.example.loveit.databinding.FragmentMessageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class MessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sendIcon.setOnClickListener {

            if(binding.yourMessage.text!!.isEmpty())
            {
                Toast.makeText(this,"Please Enter your message",Toast.LENGTH_SHORT).show()
            }
            else
            {
                sendMessage(binding.yourMessage.text.toString())
            }
        }
        
    }

    private fun sendMessage(msg: String) {
        val receiverId=intent.getStringExtra("userId")
        val senderId=FirebaseAuth.getInstance().currentUser!!.phoneNumber

        val chatId=senderId+receiverId

        val currentDate: String=SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val currentTime: String=SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())

        val map=hashMapOf<String,String>()

        map["message"]=msg
        map["senderId"]=senderId!!
        map["currentTime"]=currentTime
        map["CurrentDate"]=currentDate
        val reference=FirebaseDatabase.getInstance().getReference("chats").child(chatId)


        reference.child(reference.push().key!!).setValue(map).addOnCompleteListener {
                if(it.isSuccessful)
                {
                    binding.yourMessage.text=null
                    Toast.makeText(this,"Message sent",Toast.LENGTH_SHORT).show()
                }
                else
                {
                    Toast.makeText(this,"Something Went wrong",Toast.LENGTH_SHORT).show()
                }
            }

    }
}