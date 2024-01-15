package com.example.mobiledevproj

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.security.auth.callback.Callback

object FirestoreLevelManager {
    private val db = FirebaseFirestore.getInstance()

    fun storePlayerLevel(newLevel: Int) {
        val playerLevelsCollection = db.collection("playerLevels")
        val currentPlayer = FirebaseAuth.getInstance().currentUser
        val playerDocument = db.collection("players").document(currentPlayer?.uid!!)


        playerDocument.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    playerDocument.update("currentLevel", newLevel)
                        .addOnSuccessListener {
                            println("Player level updated successfully")
                        }
                        .addOnFailureListener { exception ->
                        }
                } else {
                    val playerLevel = hashMapOf("levels" to arrayListOf(newLevel))

                    playerLevelsCollection.document(currentPlayer.uid)
                        .set(playerLevel)
                        .addOnSuccessListener {
                            println("New player level added successfully")
                        }
                        .addOnFailureListener { exception ->
                        }
                }
            }
    }
}