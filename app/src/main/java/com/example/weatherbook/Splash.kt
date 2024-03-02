package com.example.weatherbook

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherbook.ui.home.HomeScreen
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Executors.newSingleThreadScheduledExecutor().schedule({
            startActivity(Intent(this, HomeScreen::class.java))
        }, 2, TimeUnit.SECONDS)
    }
}