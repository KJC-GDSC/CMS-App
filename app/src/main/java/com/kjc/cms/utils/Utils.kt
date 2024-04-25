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
            } else if(menuId == 1) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerMainView, fragment)
                    .commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentSecondaryView, fragment)
                    .commit()
            }
        }
    }
}