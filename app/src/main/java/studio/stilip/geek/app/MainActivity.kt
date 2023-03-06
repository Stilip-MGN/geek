package studio.stilip.geek.app

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import studio.stilip.geek.R
import studio.stilip.geek.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val hostViewModel by viewModels<HostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_games, R.id.navigation_home, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        lifecycleScope.launch {
            hostViewModel.toolbarTitle.flowWithLifecycle(lifecycle).collect { title ->
                supportActionBar?.title = title
            }
        }

        lifecycleScope.launch {
            hostViewModel.toolbarBackBtn.collect { isVisible ->
                supportActionBar?.setDisplayHomeAsUpEnabled(isVisible)
            }
        }

        lifecycleScope.launch {
            hostViewModel.bottomBarVisible.flowWithLifecycle(lifecycle).collect { isVisible ->
                binding.navView.visibility = if (isVisible) View.VISIBLE else View.GONE
            }
        }

    }
}