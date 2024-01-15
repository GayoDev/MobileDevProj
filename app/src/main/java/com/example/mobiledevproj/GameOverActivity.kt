package com.example.mobiledevproj

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class GameOverActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameover)
        val level : Int = intent.getIntExtra("level", 0)

        FirestoreLevelManager.storePlayerLevel(level)

        findViewById<ImageButton>(R.id.imageButtonReplay).setOnClickListener(){
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.imageButtonHighScore).setOnClickListener(){
            val intent = Intent(this, HighscoreActivity::class.java)
            startActivity(intent)
        }
    }
}