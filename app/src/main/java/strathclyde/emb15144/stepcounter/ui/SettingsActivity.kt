package strathclyde.emb15144.stepcounter.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import kotlinx.coroutines.*
import strathclyde.emb15144.stepcounter.R
import strathclyde.emb15144.stepcounter.model.MainDatabase
import strathclyde.emb15144.stepcounter.utils.launchIO

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.settings,
                SettingsFragment()
            )
            .commit()
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        private var viewModelJob = Job()
        private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            preference?.let {
                if (it.key == "deleteHistory") {
                    AlertDialog.Builder(requireActivity())
                        .setTitle(getString(R.string.AreYouSure))
                        .setMessage(getString(R.string.DeleteHistoryAlertText))
                        .setPositiveButton(getString(R.string.Delete)) { _, _ ->
                            uiScope.launch {
                                withContext(Dispatchers.IO) {
                                    MainDatabase.getInstance(requireActivity()).dayDao.deleteAllButLatest()
                                }
                                Toast.makeText(requireActivity(), getString(R.string.HistoryDeleted), Toast.LENGTH_SHORT).show()
                            }
                        }
                        .setNegativeButton(getString(R.string.Cancel)) { _, _ -> }
                        .create()
                        .show()
                    return true
                }
            }
            return false
        }
    }
}
