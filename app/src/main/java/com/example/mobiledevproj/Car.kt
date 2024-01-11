package com.example.mobiledevproj

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import java.util.Random

class Car {
    var bitmap : Bitmap

    var x = 0f
    var y = 0f
    var speed = 0
    var maxX = 0
    var maxY = 0

    val generator = Random()

    lateinit var detectCollision : Rect
    constructor(context: Context, width : Int, height:Int ) {
        maxY = height
        maxX = width

        bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.car)
        x = 0f - bitmap.width
        y = (generator.nextInt(maxY - bitmap.height) ).toFloat()
        speed = generator.nextInt(11)

        detectCollision = Rect(x.toInt() ,y.toInt(), bitmap.width, bitmap.height)
    }

    fun update() {
        x += speed

        if (x > (maxX.toFloat() + bitmap.width)){
            x = 0f - bitmap.width
            y = (generator.nextInt(maxY - bitmap.height) ).toFloat() + 2
        }

        detectCollision.left = x.toInt() - 10
        detectCollision.top = y.toInt() + 70
        detectCollision.right = x.toInt() + bitmap.width + 10
        detectCollision.bottom = y.toInt() + bitmap.height - 70
    }
}

