package com.kjc.cms.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
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

class ContainerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityContainerBinding
    private lateinit var navigationView: NavigationView
    private lateinit var firestore: FirebaseFirestore
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var acct: GoogleSignInAccount

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
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        acct = GoogleSignIn.getLastSignedInAccount(this)!!
        firestore = FirebaseFirestore.getInstance()
        navigationView = findViewById<NavigationView>(R.id.nav_view);
        val headerView: View = navigationView.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.userName).text = acct.displayName.toString()
        headerView.findViewById<TextView>(R.id.userEmail).text = acct.email.toString()
        Glide.with(this).load(acct.photoUrl.toString()).into(headerView.findViewById<ImageView>(R.id.userImage))

        binding.navView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, binding.drawer, binding.toolbar, R.string.open_nav, R.string.close_nav)

        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()
        if (savedInstanceState == null){
            fragMan(binding.navView, HomeFragment(), R.id.nav_home)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_home ->
                fragMan(binding.navView, HomeFragment(), R.id.nav_home)
            R.id.nav_cart ->
                fragMan(binding.navView, CartFragment(), R.id.nav_cart)
            R.id.nav_about ->
                fragMan(binding.navView, AboutFragment(), R.id.nav_about)
            R.id.nav_history->
                fragMan(binding.navView, HistoryFragment(), R.id.nav_history)
            R.id.nav_logout -> {
                mGoogleSignInClient.signOut().addOnCompleteListener {
                    val intent= Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
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
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun fragMan( navView: NavigationView,fragment: Fragment, menuId: Int) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()
        navView.setCheckedItem(menuId)
    }
}