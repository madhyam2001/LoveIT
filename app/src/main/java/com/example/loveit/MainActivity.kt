package com.example.loveit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.loveit.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener

class MainActivity : AppCompatActivity(), OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    var actionBarDrawerToggle: ActionBarDrawerToggle?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBarDrawerToggle= ActionBarDrawerToggle(this,binding.drawerLayout,R.string.open,R.string.close)

        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle!!)

        actionBarDrawerToggle!!.syncState()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.navigationView.setNavigationItemSelectedListener(this)

        val navController=findNavController(R.id.fragment)
        NavigationUI.setupWithNavController(binding.bottomNavigationView,navController)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favourite->{
                Toast.makeText(this,"Favourite",Toast.LENGTH_SHORT).show()

            }

            R.id.rateus->{
                Toast.makeText(this,"Rate Us",Toast.LENGTH_SHORT).show()

            }

            R.id.share->{
                Toast.makeText(this,"Shared with Friends",Toast.LENGTH_SHORT).show()

            }

            R.id.termsandcond->{
                Toast.makeText(this,"Terms and Conditions",Toast.LENGTH_SHORT).show()

            }

            R.id.privacyPolicy->{
                Toast.makeText(this,"Privacy and Policy",Toast.LENGTH_SHORT).show()

            }
            R.id.developer->{
                Toast.makeText(this,"Madhyam Patra",Toast.LENGTH_SHORT).show()

            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return if(actionBarDrawerToggle!!.onOptionsItemSelected(item)){
            true
        }
        else{
            super.onOptionsItemSelected(item)

        }


    }

    override fun onBackPressed() {

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            binding.drawerLayout.close()
        }
        else
        {
            super.onBackPressed()
        }
    }
}