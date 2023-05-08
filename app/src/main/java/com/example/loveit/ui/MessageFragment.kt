package com.example.loveit.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.loveit.R
import com.example.loveit.adapter.MessageUserAdapter
import com.example.loveit.databinding.FragmentMessageBinding
import com.example.loveit.ui.DatingFragment.Companion.list
import com.example.loveit.utils.Config
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessageFragment : Fragment() {

    private lateinit var binding : FragmentMessageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentMessageBinding.inflate(layoutInflater)

        getdata()

        return binding.root
    }

    private fun getdata() {
        Config.showDialog(requireContext())
        var list= arrayListOf<String>()
        var newList= arrayListOf<String>()
        val currentId= FirebaseAuth.getInstance()?.currentUser!!.uid
        FirebaseDatabase.getInstance().getReference("chats")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (data in snapshot.children)
                    {
                        if(data.key!!.contains(currentId))
                        {
                            list.add(data.key!!.replace(currentId!!,""))
                            newList.add(data.key!!)
                        }

                    }

                    binding.recyclerView.adapter=MessageUserAdapter(requireContext(), list,newList)


                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(),error.message,Toast.LENGTH_SHORT).show()

                Config.hideDialog()

                }

            })
    }


}