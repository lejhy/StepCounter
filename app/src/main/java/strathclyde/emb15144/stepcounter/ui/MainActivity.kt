package strathclyde.emb15144.stepcounter.ui

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import strathclyde.emb15144.stepcounter.R
import strathclyde.emb15144.stepcounter.databinding.ActivityMainBinding
import strathclyde.emb15144.stepcounter.viewmodel.MainActivityViewModel
import strathclyde.emb15144.stepcounter.viewmodel.MainActivityViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerTriggers()

        viewModel = ViewModelProvider(
            this,
            MainActivityViewModelFactory(this.application)
        ).get(MainActivityViewModel::class.java)

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

    private fun registerTriggers() {
        val intent = Intent("strathclyde.contextualtriggers.intent.action.NEW_TRIGGER")
            .putExtra("owner", "strathclyde.emb15144.stepcounter")
            .putExtra("title", "Current Step Count!")
            .putExtra("content", "Keep on going!")
            .putExtra("iconKey", "NOTIFICATION_IMPORTANT")
            .putExtra("active", true)
            .putExtra("useProgressBar", true)
            .putExtra("progressContentUri", "content://strathclyde.emb15144.stepcounter.provider/progress")
            .putExtra("actionKeys", "view,change")
            .putExtra("actionContentUri", "strathclyde.emb15144.stepcounter.receiver.ActionBroadcastReceiver")
            .putStringArrayListExtra("contextKeyList", arrayListOf("TIME"))
            .putIntegerArrayListExtra("greaterThanOrEqualToList", arrayListOf(0))
            .putIntegerArrayListExtra("lessThanOrEqualToList", arrayListOf(999))
            .setComponent(ComponentName("strathclyde.contextualtriggers", "strathclyde.contextualtriggers.receiver.TriggersBroadcastReceiver"))
        applicationContext.sendBroadcast(intent)
    }
}
