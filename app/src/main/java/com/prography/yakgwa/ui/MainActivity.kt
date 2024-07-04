package com.prography.yakgwa.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.ActivityMainBinding
import com.prography.yakgwa.util.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupJetpackNavigation()
    }

    private fun setupJetpackNavigation() {
        val host = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return
        navController = host.navController
        binding.navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.createPromiseTitleFragment ||
                destination.id == R.id.createPromiseThemeFragment ||
                destination.id == R.id.createPromiseTimeFragment ||
                destination.id == R.id.createPromisePlaceFragment ||
                destination.id == R.id.invitationLeaderFragment ||
                destination.id == R.id.invitationMemberFragment ||
                destination.id == R.id.votePromiseTimeFragment ||
                destination.id == R.id.votePromisePlaceFragment ||
                destination.id == R.id.voteCompletionFragment
            ) {
                binding.navView.visibility = View.GONE
            } else {
                binding.navView.visibility = View.VISIBLE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}