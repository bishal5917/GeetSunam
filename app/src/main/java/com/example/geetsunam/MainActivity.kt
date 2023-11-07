package com.example.geetsunam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.geetsunam.utils.CustomToast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                    CustomToast.showToast(this, "Logged out")
                    true
                }

                else -> false
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