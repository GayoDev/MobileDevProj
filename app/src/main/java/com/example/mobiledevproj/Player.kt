package com.example.mobiledevproj

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

data class Player(var levels: ArrayList<Int>? = null)

class FirestorePlayerManager {

    private val db = FirebaseFirestore.getInstance()

    fun fetchPlayers(callback: (List<Player>) -> Unit) {
        db.collection("players")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val playerList: MutableList<Player> = mutableListOf()

                for (document in querySnapshot.documents) {
                    val user = document.toObject(Player::class.java)
                    user?.let {
                        playerList.add(it)
                    }
                }
                Log.d("FirestorePlayerManager", "Fetched Players: $playerList")
                callback(playerList)
            }
            .addOnFailureListener { exception ->
                Log.e("FirestorePlayerManager", "Error fetching players", exception)
                callback(emptyList())
            }
    }
}