package com.kjc.cms.ui.fragments.once

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.kjc.cms.R
import com.kjc.cms.databinding.FragmentLoginBinding
import com.kjc.cms.model.CurrentUser
import com.kjc.cms.ui.ContainerActivity
import com.kjc.cms.utils.Utils.Companion.fragMan

class LoginFragment(private val editor: SharedPreferences.Editor) : Fragment() {
    private lateinit var binding : FragmentLoginBinding
    // login variables
    private lateinit var auth: FirebaseAuth
    private lateinit var gsign: GoogleSignInClient
    private lateinit var firestore: FirebaseFirestore
    // check access allowed or not

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get firebase instance
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        // get google sign in instance
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()
        gsign = GoogleSignIn.getClient(requireActivity(), gso)

        // on click listener to the login button
        binding.googleLogin.setOnClickListener {
            signInToGoogle()
        }
    }

    private fun signInToGoogle(){
        //Launch sign in intent to begin sign in process
        val signIntent = gsign.signInIntent
        launcher.launch(signIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                //Take the result after account is selected
                handleResults(task)
            }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            // If the data is passed properly and get a successful result
            if(account!= null) {
                verifyUser(account, task)
                // Start the login process
            }
        } else {
            Toast.makeText(requireActivity(), task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun verifyUser(account: GoogleSignInAccount, task: Task<GoogleSignInAccount>) {
        firestore.collection("Users").document(account.email.toString()).get().addOnSuccessListener{ documentSnapshot ->
            if (task.isSuccessful){
                val user: CurrentUser = documentSnapshot.toObject(CurrentUser::class.java)!!
                val gson = Gson()
                editor.putString("currentUser", gson.toJson(user))
                editor.apply()
                updateUI(account, true)
            } else {
                updateUI(account, false)
            }
        }
    }

    private fun updateUI(account: GoogleSignInAccount, access: Boolean) {
        val cred = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(cred).addOnCompleteListener {
            if (it.isSuccessful){
                if (access){
                    //start the app with the credentials
                    val intent = Intent(activity, ContainerActivity::class.java)
                    startActivity(intent)
                } else {
                    //if access is not given
                    fragMan(requireActivity().supportFragmentManager, RequestAccessFragment(account), menuId = 1)
                }
            } else {
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}