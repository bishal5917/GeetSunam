package com.example.geetsunam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.geetsunam.features.presentation.home.HomeFragment
import com.example.geetsunam.utils.CustomToast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)

        val drawerBtn = findViewById<ImageButton>(R.id.ibMenu)
        drawerBtn.setOnClickListener {
            //open drawer
        }

//        val drawerLayout = findViewById<DrawerLayout>(R.id.dlAppDrawer)
//        val navigationView = findViewById<NavigationView>(R.id.drawerNavView)
//        navigationView.setNavigationItemSelectedListener(this)
//        val toggle = ActionBarDrawerToggle(
//            this,
//            drawerLayout,
//            R.string.app_name,
//            R.string.app_name
//        )
//        drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.navHostFragment, HomeFragment()).commit()
//            navigationView.setCheckedItem(R.id.homeFragment)
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}