package com.practicum.playlistmaker.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        val currentView=findViewById<View>(R.id.settings)
        ViewCompat.setOnApplyWindowInsetsListener(currentView) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val navigationBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.updatePadding(bottom = navigationBar.bottom)
            view.updatePadding(top = statusBar.top)
            insets
        }

        val pushbackbutton=findViewById<Button>(R.id.backtoMain)
        val pushshare = findViewById<Button>(R.id.sharingButton)
        val pushsupport = findViewById<Button>(R.id.callSupportButton)
        val pushagreement = findViewById<Button>(R.id.userAgreementButton)

        pushbackbutton.setOnClickListener {
            finish()
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.switchDayNight)

        themeSwitcher.setChecked((applicationContext as App).checktheme())

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        pushshare.setOnClickListener{
            val shareIntent= Intent(Intent.ACTION_SEND)
            shareIntent.type="text/plain"
            val shareBody=getString(R.string.link_to_yandex_course)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.this_app_made_with_course))
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(shareIntent,getString(R.string.share_with_help) ))
        }
        pushsupport.setOnClickListener{
            val sendIntent= Intent(Intent.ACTION_SENDTO)
            sendIntent.data= Uri.parse("mailto:")
            sendIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_mail)))
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_theme_to_devs))
            sendIntent.putExtra(Intent.EXTRA_TEXT,getString(R.string.thanks_to_devs))
            startActivity(sendIntent)
        }

        pushagreement.setOnClickListener{
            val url = Uri.parse(getString(R.string.link_to_offer))
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }


    }
}