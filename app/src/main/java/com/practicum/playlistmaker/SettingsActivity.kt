package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val pushbackbutton=findViewById<Button>(R.id.backtoMain)
        val pushshare = findViewById<Button>(R.id.sharingButton)
        val pushsupport = findViewById<Button>(R.id.callSupportButton)
        val pushagreement = findViewById<Button>(R.id.userAgreementButton)

        pushbackbutton.setOnClickListener {
            finish()
        }

        pushshare.setOnClickListener{
            val shareIntent=Intent(Intent.ACTION_SEND)
            shareIntent.type="text/plain"
            val shareBody="https://practicum.yandex.ru/android-developer/"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Курс с помощью которого сделано это приложение")
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(shareIntent, "Поделиться с помощью"))
        }
        pushsupport.setOnClickListener{
            val sendIntent=Intent(Intent.ACTION_SENDTO)
            sendIntent.data=Uri.parse("mailto:")
            sendIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("enu.w@yandex.ru"))
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Сообщение разработчикам и разработчицам приложения Playlist Maker")
            sendIntent.putExtra(Intent.EXTRA_TEXT,"Спасибо разработчикам и разработчицам за крутое приложение!")
            startActivity(sendIntent)
        }

        pushagreement.setOnClickListener{
            val url = Uri.parse("https://yandex.ru/legal/practicum_offer/")
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }


    }
}