package com.finals.foodrunner.ui.login

import androidx.fragment.app.Fragment
import com.finals.foodrunner.R
import com.finals.foodrunner.controller.DrawerController
import java.lang.ClassCastException

class LoginFragment :Fragment(R.layout.login_fragment){
    private lateinit var drawerController: DrawerController;
    override fun onStart() {
        super.onStart()
        try{
            this.drawerController=activity as DrawerController
        }
        catch (e:ClassCastException){
            throw e
        }

    }

    override fun onResume() {
        super.onResume()
        if(this::drawerController.isInitialized){
            drawerController.lockDrawer()
        }

    }

}