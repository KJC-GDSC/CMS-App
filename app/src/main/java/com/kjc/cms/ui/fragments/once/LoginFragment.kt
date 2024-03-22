package com.kjc.cms.ui.fragments.once

import android.app.Activity
import android.content.Intent
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
import com.kjc.cms.R
import com.kjc.cms.databinding.FragmentLoginBinding
import com.kjc.cms.ui.ContainerActivity

class LoginFragment : Fragment() {
    private lateinit var binding : FragmentLoginBinding
    // login variables
    private lateinit var auth: FirebaseAuth
    private lateinit var gsign: GoogleSignInClient
    // check access allowed or not
    private var accessable = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get firebase instance
        auth = FirebaseAuth.getInstance()
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
        val signIntent = gsign.signInIntent
        launcher.launch(signIntent)
    }
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if(account!= null) {
                updateUI(account, account.email.toString().endsWith("@kristujayanti.com"))
            }
        } else {
            Toast.makeText(requireActivity(), task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount, access: Boolean) {
        val cred = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(cred).addOnCompleteListener {
            if (it.isSuccessful){
                if (access){
                    val intent = Intent(activity, ContainerActivity::class.java)
                    startActivity(intent)
                } else {
                    val fragmentManager = activity?.supportFragmentManager
                    val fragmentTransaction = fragmentManager?.beginTransaction()
                    fragmentTransaction?.replace(R.id.fragmentContainerView, RequestAccessFragment())
                    fragmentTransaction?.commit()
                }
            } else {
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}