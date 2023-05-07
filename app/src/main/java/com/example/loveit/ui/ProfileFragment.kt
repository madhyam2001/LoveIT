package com.example.loveit.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.loveit.R
import com.example.loveit.activity.EditProfileActivity
import com.example.loveit.auth.LoginActivity
import com.example.loveit.databinding.FragmentProfileBinding
import com.example.loveit.model.UserModel
import com.example.loveit.utils.Config
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Config.showDialog(requireContext())
        binding=FragmentProfileBinding.inflate(layoutInflater)


        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!).get()
            .addOnSuccessListener {
                if (it.exists())
                {
                    val data=it.getValue(UserModel::class.java)

                    binding.name.setText(data!!.name.toString())
                    binding.number.setText(data!!.number.toString())
                    binding.email.setText(data!!.email.toString())
                    binding.city.setText(data!!.city.toString())

                    Glide.with(requireContext()).load(data.image).placeholder(R.drawable.man).into(binding.userImage)

                    Config.hideDialog()
                }
            }


        binding.logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(),LoginActivity::class.java))
            requireActivity().finish()
        }


        binding.editprofile.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }
        return binding.root
    }



}