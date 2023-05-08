package com.example.loveit.auth

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.loveit.MainActivity
import com.example.loveit.R
import com.example.loveit.databinding.ActivityLoginBinding
import com.example.loveit.databinding.ActivityRegistrationBinding
import com.example.loveit.model.UserModel
import com.example.loveit.utils.Config
import com.example.loveit.utils.Config.hideDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.net.URI

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    private var imageUri:Uri?=null

    private val selectImage=registerForActivityResult(ActivityResultContracts.GetContent())
    {
        imageUri=it
        binding.profilephoto.setImageURI(imageUri)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegistrationBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.profilephoto.setOnClickListener {
            selectImage.launch("image/*")

        }

        binding.savedata.setOnClickListener{
            validatedata()
        }

    }

    private fun validatedata() {
        if(binding.userName.text.toString().isEmpty()||binding.userEmail.text.toString().isEmpty()
            ||binding.userCity.text.toString().isEmpty()
            ||imageUri==null)
        {
            Toast.makeText(this, "Please Enter All Fields",Toast.LENGTH_SHORT).show()
        }
        else if(!binding.termscondition.isChecked)
        {
            Toast.makeText(this, "Please Accept Terms and Conditions",Toast.LENGTH_SHORT).show()
        }
        else
        {
            uploadImage()
        }
    }

    private fun uploadImage() {

        Config.showDialog(this)

        val storageRef=FirebaseStorage.getInstance().getReference("profile")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("profile.jpg")

        storageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener {
                    storeData(it)
                }.addOnFailureListener{
                    hideDialog()
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{
                hideDialog()
                Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
            }

    }

    private fun storeData(imageUrl: Uri?) {
        val data=UserModel(
            name = binding.userName.text.toString(),
            city = binding.userCity.text.toString(),
            number=FirebaseAuth.getInstance().currentUser?.phoneNumber.toString(),
            email = binding.userEmail.text.toString(),
            image = imageUrl.toString(),
        )

        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
            .setValue(data).addOnCompleteListener {
                hideDialog()
                if(it.isSuccessful)
                {
                    Toast.makeText(this,"User Registered Successfully",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()

                }
                else
                {
                    Toast.makeText(this,it.exception!!.message,Toast.LENGTH_SHORT).show()

                }
            }

    }
}