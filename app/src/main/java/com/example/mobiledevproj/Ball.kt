package com.example.mobiledevproj

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect

class Ball {
    var bitmap : Bitmap

    var x = 0f
    var y = 0f

    var speedY = -10
    var speedX = 0

    var isBoosting = false
    final val MAX_SPEED = 20
    final val MIN_SPEED = 1

    var maxX = 0
    var maxY = 0

    lateinit var detectCollision : Rect

    constructor(context: Context, width : Int, height:Int ) {
        bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ballplayer)
        maxY = height - bitmap.height
        maxX = width - bitmap.width
        x = (maxX.toFloat() /2f)
        y = maxY.toFloat()

        detectCollision = Rect(x.toInt() ,y.toInt(), bitmap.width, bitmap.height)

    }

    fun update() {

        y += speedY

        x += speedX

        if (x > maxX) x = maxX.toFloat()
        else if (x < 0) x = 0f

        detectCollision.left = x.toInt()
        detectCollision.top = y.toInt()
        detectCollision.right = x.toInt() + bitmap.width
        detectCollision.bottom = y.toInt() + bitmap.height

    }
}