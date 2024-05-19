package com.example.geetsunam

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.geetsunam.databinding.ActivityMainBinding
import com.example.geetsunam.databinding.DrawerHeaderBinding
import com.example.geetsunam.features.presentation.for_you.viewmodel.RecommendEvent
import com.example.geetsunam.features.presentation.for_you.viewmodel.RecommendViewModel
import com.example.geetsunam.features.presentation.home.search.SearchActivity
import com.example.geetsunam.features.presentation.liked_song.viewmodel.FavSongEvent
import com.example.geetsunam.features.presentation.liked_song.viewmodel.FavSongViewModel
import com.example.geetsunam.features.presentation.login.LoginActivity
import com.example.geetsunam.features.presentation.login.viewmodel.LoginEvent
import com.example.geetsunam.features.presentation.login.viewmodel.LoginState
import com.example.geetsunam.features.presentation.login.viewmodel.LoginViewModel
import com.example.geetsunam.features.presentation.splash.viewmodel.SplashViewModel
import com.example.geetsunam.utils.CustomDialog
import com.example.geetsunam.utils.CustomToast
import com.example.geetsunam.utils.DateUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerHeaderBinding: DrawerHeaderBinding

    @Inject
    lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var splashViewModel: SplashViewModel

    @Inject
    lateinit var favSongViewModel: FavSongViewModel

    @Inject
    lateinit var recommendViewModel: RecommendViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Getting user and binding to the drawer header
        drawerHeaderBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.drawer_header, binding.drawerNavView, false
        )
        drawerHeaderBinding.tvGreeting.text = DateUtil().getGreetingMessage()
        drawerHeaderBinding.user = splashViewModel.splashState.value?.userEntity
        drawerHeaderBinding.executePendingBindings()
        binding.drawerNavView.addHeaderView(drawerHeaderBinding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        val navigationView = findViewById<NavigationView>(R.id.drawerNavView)
        navigationView.setupWithNavController(navController)
        val toggle = ActionBarDrawerToggle(
            this, binding.dlAppDrawer, R.string.app_name, R.string.app_name
        )
        binding.dlAppDrawer.addDrawerListener(toggle)
        toggle.syncState()

        binding.ibMenu.setOnClickListener {
            if (binding.dlAppDrawer.isDrawerOpen(navigationView)) {
                binding.dlAppDrawer.closeDrawer(navigationView)
            } else {
                binding.dlAppDrawer.openDrawer(navigationView)
            }
        }

        //goto search screen
        binding.ibSearch.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_logout -> {
                    binding.dlAppDrawer.closeDrawer(navigationView)
                    CustomDialog().showSureLogoutDialog(this) {
                        loginViewModel.onEvent(LoginEvent.LogoutUser)
                    }
                }
            }
            val handled = NavigationUI.onNavDestinationSelected(item, navController)
            if (handled) {
                binding.dlAppDrawer.closeDrawer(navigationView)
            }
            handled
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
                favSongViewModel.onEvent(FavSongEvent.Reset)
                recommendViewModel.onEvent(RecommendEvent.Reset)
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
}