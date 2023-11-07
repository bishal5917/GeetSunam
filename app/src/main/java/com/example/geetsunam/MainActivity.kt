package com.example.geetsunam

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.geetsunam.databinding.ActivityMusicBinding
import com.example.geetsunam.databinding.DrawerHeaderBinding
import com.example.geetsunam.features.domain.entities.UserEntity
import com.example.geetsunam.features.presentation.login.LoginActivity
import com.example.geetsunam.features.presentation.login.viewmodel.LoginEvent
import com.example.geetsunam.features.presentation.login.viewmodel.LoginState
import com.example.geetsunam.features.presentation.login.viewmodel.LoginViewModel
import com.example.geetsunam.features.presentation.splash.viewmodel.SplashViewModel
import com.example.geetsunam.utils.CustomDialog
import com.example.geetsunam.utils.CustomToast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerHeaderBinding: DrawerHeaderBinding

    @Inject
    lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerHeaderBinding = DrawerHeaderBinding.inflate(layoutInflater)

        //collecting flow and binding to the drawer header
        val userData = splashViewModel.userFlow.value
        drawerHeaderBinding.user = userData as UserEntity

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)

        val drawerBtn = findViewById<ImageButton>(R.id.ibMenu)
        drawerLayout = findViewById<DrawerLayout>(R.id.dlAppDrawer)
        val navigationView = findViewById<NavigationView>(R.id.drawerNavView)
        navigationView.setupWithNavController(navController)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.app_name, R.string.app_name
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        drawerBtn.setOnClickListener {
            if (drawerLayout.isDrawerOpen(navigationView)) {
                drawerLayout.closeDrawer(navigationView)
            } else {
                drawerLayout.openDrawer(navigationView)
            }
        }

        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_logout -> {
                    drawerLayout.closeDrawer(navigationView)
                    loginViewModel.onEvent(LoginEvent.LogoutUser)
                    true
                }

                else -> true
            }
        }
        observeLogoutProcess()
    }

    private fun observeLogoutProcess() {
        val dialog = Dialog(this)
        loginViewModel.loginState.observe(this) { response ->
            if (response.status == LoginState.LoginStatus.LogoutLoading) {
                //show loading dialog
                CustomDialog().showLoadingDialog(dialog)
            }
            if (response.status == LoginState.LoginStatus.LogoutSuccess) {
                CustomDialog().hideLoadingDialog(dialog)
                CustomToast.showToast(
                    context = this, "${
                        response.message
                    }"
                )
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
                finish()
            }
            if (response.status == LoginState.LoginStatus.LogoutFailed) {
                CustomDialog().hideLoadingDialog(dialog)
                CustomToast.showToast(
                    context = this, "${
                        response.message
                    }"
                )
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
//    @Deprecated("Deprecated in Java")
//    override fun onBackPressed() {
//        super.onBackPressed()
//        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout.closeDrawer(GravityCompat.START)
//        } else {
//            onBackPressedDispatcher.onBackPressed()
//        }
//    }
}