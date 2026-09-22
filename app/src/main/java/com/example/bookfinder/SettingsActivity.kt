package com.example.bookfinder

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var prefs: SharedPreferences

    private lateinit var accountEmailText: TextView
    private lateinit var darkModeSwitch: Switch
    private lateinit var logoutButton: Button
    private lateinit var btnLanguageEnglish: Button      // ← NEW
    private lateinit var btnLanguageAfrikaans: Button    // ← NEW
    private lateinit var btnLanguageZulu: Button         // ← NEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        auth = FirebaseAuth.getInstance()
        prefs = getSharedPreferences("app_settings", MODE_PRIVATE)

        accountEmailText = findViewById(R.id.accountEmailText)
        darkModeSwitch = findViewById(R.id.darkModeSwitch)
        logoutButton = findViewById(R.id.logoutButton)
        btnLanguageEnglish = findViewById(R.id.btnLanguageEnglish)      // ← NEW
        btnLanguageAfrikaans = findViewById(R.id.btnLanguageAfrikaans)  // ← NEW
        btnLanguageZulu = findViewById(R.id.btnLanguageZulu)            // ← NEW

        accountEmailText.text = getString(R.string.account_info) + ": ${auth.currentUser?.email ?: "Unknown"}"

        val isDarkMode = prefs.getBoolean("dark_mode", false)
        darkModeSwitch.isChecked = isDarkMode

        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("dark_mode", isChecked).apply()

            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        // ← NEW: language buttons
        btnLanguageEnglish.setOnClickListener { setLanguage("en") }
        btnLanguageAfrikaans.setOnClickListener { setLanguage("af") }
        btnLanguageZulu.setOnClickListener { setLanguage("zu") }

        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    // ← NEW: language-switching function
    private fun setLanguage(langCode: String) {
        val localeList = LocaleListCompat.forLanguageTags(langCode)
        AppCompatDelegate.setApplicationLocales(localeList)
    }
}
