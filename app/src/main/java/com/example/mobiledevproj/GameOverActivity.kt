package com.example.mobiledevproj

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class GameOverActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameover)

        findViewById<ImageButton>(R.id.imageButtonReplay).setOnClickListener(){
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
    }
}