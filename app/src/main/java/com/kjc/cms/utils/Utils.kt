package com.kjc.cms.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Base64
import android.view.Window
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.navigation.NavigationView
import com.kjc.cms.R
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class Utils {
    companion object{
        fun fragMan(supportFragmentManager: FragmentManager, fragment: Fragment, navView: NavigationView? = null, menuId: Int? = null) {
            if (menuId != null && navView != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, fragment)
                    .commit()
                navView.setCheckedItem(menuId)
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentSecondaryView, fragment)
                    .commit()
            }
        }

//        private fun openDialogBox(context:Context, content: Int, confirmButton: Int, text:String? = null) {
//            val dialog = Dialog(context)
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            dialog.setCancelable(false)
//            dialog.setContentView(content)
//            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            val confBtn: TextView = dialog.findViewById(confirmButton)
//            if (text!=null){
//                val dialogText: TextView = dialog.findViewById(R.id.dialogText)
//                dialogText.text = text
//            }
//            confBtn.setOnClickListener{
//                dialog.dismiss()
//            }
//        }
//
//        fun convertImageToBase64(image: Bitmap): String {
//            val byteArrayOutputStream = ByteArrayOutputStream()
//            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
//            val byteArray = byteArrayOutputStream.toByteArray()
//            return Base64.encodeToString(byteArray, Base64.DEFAULT)
//        }
//
//        fun getBitmapFromURL(src: String): Bitmap {
//            val url = URL(src)
//            return BitmapFactory.decodeStream(url.openStream())
//        }

        fun getCartItems(){

        }
    }
}