package com.kjc.cms.ui

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.kjc.cms.R
import com.kjc.cms.databinding.ActivityContainerBinding
import com.kjc.cms.ui.fragments.home.AboutFragment
import com.kjc.cms.ui.fragments.home.CartFragment
import com.kjc.cms.ui.fragments.home.HistoryFragment
import com.kjc.cms.ui.fragments.home.HomeFragment
import com.kjc.cms.utils.Utils

class ContainerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityContainerBinding
    private lateinit var navigationView: NavigationView
    private lateinit var firestore: FirebaseFirestore
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var acct: GoogleSignInAccount
    private lateinit var sp: SharedPreferences
    private lateinit var editor: Editor
    private lateinit var cartItems: MutableSet<String>

    //TODO("complete calling of data here and save data in local storage")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        acct = GoogleSignIn.getLastSignedInAccount(this)!!
        firestore = FirebaseFirestore.getInstance()
        sp = getSharedPreferences("Cart", MODE_PRIVATE)
        editor = sp.edit()
        cartItems = sp.getStringSet("items", mutableSetOf())!!
        // fetch user credential information

        navigationView = findViewById<NavigationView>(R.id.nav_view);
        val headerView: View = navigationView.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.userName).text = acct.displayName.toString()
        headerView.findViewById<TextView>(R.id.userEmail).text = acct.email.toString()
        Glide.with(this).load(acct.photoUrl.toString()).into(headerView.findViewById<ImageView>(R.id.userImage))
        binding.navView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(this, binding.drawer, binding.toolbar, R.string.open_nav, R.string.close_nav)
        // set up the side bar items

        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()
        if (savedInstanceState == null){
            //opens home page by default
            Utils.fragMan(supportFragmentManager, HomeFragment(cartItems), binding.navView, R.id.nav_home)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // handle item selection and change fragments based ont the item selected
        when(item.itemId){
            R.id.nav_home ->
                Utils.fragMan(supportFragmentManager, HomeFragment(cartItems), binding.navView, R.id.nav_home)
            R.id.nav_cart ->
                Utils.fragMan(supportFragmentManager, CartFragment(sp, editor), binding.navView, R.id.nav_cart)
            R.id.nav_about ->
                Utils.fragMan(supportFragmentManager, AboutFragment(), binding.navView, R.id.nav_about)
            R.id.nav_history->
                Utils.fragMan(supportFragmentManager, HistoryFragment(), binding.navView, R.id.nav_history)
            R.id.nav_logout -> {
                googleSignInClient.signOut().addOnCompleteListener {
                    val intent= Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, acct.displayName.toString()+" Logged Out", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
        binding.drawer.closeDrawer(GravityCompat.START)
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        if (binding.drawer.isDrawerOpen(GravityCompat.START)){
            binding.drawer.closeDrawer(GravityCompat.START)
            //if the sidebar is open should close the sidebar
        } else {
            onBackPressedDispatcher.onBackPressed()
            //else use the android default
        }
    }
}