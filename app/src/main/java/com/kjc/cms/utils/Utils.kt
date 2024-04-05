package com.kjc.cms.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.navigation.NavigationView
import com.kjc.cms.R

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


    }
}