package com.example.mobiledevproj

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<ImageButton>(R.id.imagePlayButton).setOnClickListener(){
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
    }
}