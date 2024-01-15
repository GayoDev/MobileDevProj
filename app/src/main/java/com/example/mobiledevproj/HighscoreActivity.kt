package com.example.mobiledevproj

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HighscoreActivity : AppCompatActivity() {

    var playerList = mutableListOf<Player>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_highscore)

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
            val textViewProduct = rootView.findViewById<TextView>(R.id.textViewPlayerScore)

            return rootView
        }

    }


}