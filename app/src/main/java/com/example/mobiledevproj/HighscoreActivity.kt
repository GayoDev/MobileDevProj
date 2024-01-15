package com.example.mobiledevproj

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.auth.User

class HighscoreActivity : AppCompatActivity() {

    var playerList = mutableListOf<Player>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_highscore)

        val highscoreAdapter = HighscoreAdapter()
        val playerManager = FirestorePlayerManager()

        playerManager.fetchPlayers {
            playerList = it.toMutableList()

            val listView = findViewById<ListView>(R.id.listViewHighScore)
            listView.adapter = highscoreAdapter

            highscoreAdapter.notifyDataSetChanged()
        }

        findViewById<ImageButton>(R.id.imageButtonLeave).setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    inner class HighscoreAdapter : BaseAdapter() {

        override fun getCount(): Int {
            return playerList.size
        }

        override fun getItem(position: Int): Any {
            return playerList[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_playerscore,parent,false)
            val textPlayerScore = rootView.findViewById<TextView>(R.id.textViewPlayerScore)
            var score = playerList[position].levels.toString()

            textPlayerScore.text = score
            return rootView
        }

    }


}