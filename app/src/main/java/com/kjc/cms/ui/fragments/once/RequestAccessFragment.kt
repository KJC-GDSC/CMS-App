package com.kjc.cms.ui.fragments.once

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.firestore.FirebaseFirestore
import com.kjc.cms.R
import com.kjc.cms.databinding.FragmentRequestAccessBinding
import com.kjc.cms.model.CurrentUser
import com.kjc.cms.ui.ContainerActivity

class RequestAccessFragment(private val account: GoogleSignInAccount) : Fragment() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var binding : FragmentRequestAccessBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRequestAccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        firestore = FirebaseFirestore.getInstance()
        binding.backButton.setOnClickListener{
            startActivity(Intent(context, ContainerActivity::class.java))
        }
        binding.noticeDescriptionemail.text = account.email.toString()
        binding.requestAccess.setOnClickListener{
            openDialogBox()
        }
    }

    private fun openDialogBox() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.request_access_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val requestName: TextView = dialog.findViewById(R.id.requestName)
        val requestEmail: TextView = dialog.findViewById(R.id.requestEmail)
        val requestDepartment: EditText = dialog.findViewById(R.id.requestDepartment)
        val confirmBtn: Button = dialog.findViewById(R.id.confirmRequest)
        val cancelBtn: Button = dialog.findViewById(R.id.requestCancel)

        requestName.text = "Name: " + account.displayName.toString()
        requestEmail.text = "Email: " + account.email.toString()

        confirmBtn.setOnClickListener{
            firestore.collection("Users")
                .add(CurrentUser(account.displayName.toString(), requestDepartment.text.toString(), account.email.toString()))
                .addOnSuccessListener {
                    Toast.makeText(context, "Request sent for access", Toast.LENGTH_SHORT).show()
                }
        }

        cancelBtn.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
    }

}