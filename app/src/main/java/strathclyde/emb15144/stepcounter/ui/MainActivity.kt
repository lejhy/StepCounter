package strathclyde.emb15144.stepcounter.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import strathclyde.emb15144.stepcounter.R
import strathclyde.emb15144.stepcounter.databinding.ActivityMainBinding
import strathclyde.emb15144.stepcounter.service.NotificationService
import strathclyde.emb15144.stepcounter.service.StepsSensorService
import strathclyde.emb15144.stepcounter.viewmodel.ObservablePreferences

class MainActivity : AppCompatActivity() {

    private lateinit var preferences: ObservablePreferences
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_steps,
            R.id.navigation_goals,
            R.id.navigation_history
        ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        PreferenceManager.setDefaultValues(this,
            R.xml.root_preferences, false)

        preferences = ObservablePreferences(application)
        preferences.automaticStepCounting.observeForever(automaticStepCountingObserver)
        preferences.automaticStepCounting.observeForever(notificationObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        preferences.automaticStepCounting.removeObserver(automaticStepCountingObserver)
        preferences.automaticStepCounting.removeObserver(notificationObserver)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.navigation_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(
            getString(R.string.channel_id),
            getString(R.string.channel_name),
            importance
        )
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private val automaticStepCountingObserver = Observer { isEnabled: Boolean ->
        when(isEnabled) {
            true -> application.startService(Intent(application, StepsSensorService::class.java))
            false -> application.stopService(Intent(application, StepsSensorService::class.java))
        }
    }

    private val notificationObserver = Observer { isEnabled: Boolean ->
        when(isEnabled) {
            true -> application.startService(Intent(application, NotificationService::class.java))
            false -> application.stopService(Intent(application, NotificationService::class.java))
        }
    }
}
