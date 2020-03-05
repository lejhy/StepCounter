package strathclyde.emb15144.stepcounter.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager

class ObservablePreferences(context: Context) {
    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val _editableGoals = MutableLiveData<Boolean>(preferences.getBoolean("editableGoals", false))
    private val _automaticStepCounting = MutableLiveData<Boolean>(preferences.getBoolean("automaticStepCounting", false))
    private val _notifications = MutableLiveData<Boolean>(preferences.getBoolean("notifications", false))

    val editableGoals: LiveData<Boolean> = _editableGoals
    val automaticStepCounting: LiveData<Boolean> = _automaticStepCounting
    val notifications: LiveData<Boolean> = _notifications

    private val prefListener: SharedPreferences.OnSharedPreferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { sp, key ->
        when(key) {
            "editableGoals" -> {
                _editableGoals.value = sp.getBoolean(key, false)
            }
            "automaticStepCounting" -> {
                _automaticStepCounting.value = sp.getBoolean(key, false)
            }
            "notifications" -> {
                _notifications.value = sp.getBoolean(key, false)
            }
        }
    }

    init {
        preferences.registerOnSharedPreferenceChangeListener(prefListener)
    }

    fun destroy() {
        preferences.unregisterOnSharedPreferenceChangeListener(prefListener)
    }
}
