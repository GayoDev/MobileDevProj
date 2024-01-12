package com.example.mobiledevproj

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Rect
import java.util.Random

class Car {
    var bitmap : Bitmap
    val matrix = Matrix()
    lateinit var reversedBitmap: Bitmap

    var x = 0f
    var y = 0f
    var speed = 0
    var maxX = 0
    var maxY = 0
    var isReversed : Boolean

    val generator = Random()

    lateinit var detectCollision : Rect
    constructor(context: Context, width : Int, height:Int, status : Boolean) {
        maxX = width
        maxY = height
        isReversed = status
        bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.car)

        if (status == false){
            x = 0f - bitmap.width
            detectCollision = Rect(x.toInt() ,y.toInt(), bitmap.width, bitmap.height)
        }
        else{
            x = maxX + bitmap.width.toFloat()
            matrix.postScale(-1f, 1f, (bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat())
            reversedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            detectCollision = Rect(x.toInt() ,y.toInt(), reversedBitmap.width, reversedBitmap.height)
        }

        y = height.toFloat()
        speed = generator.nextInt(11)
    }

    fun update() {
        if (isReversed){
            x -= speed
            if (x < (0 - bitmap.width)){
                x = maxX.toFloat() + bitmap.width
            }
        }else {
            x += speed

            if (x > (maxX.toFloat() + bitmap.width)) {
                x = 0f - bitmap.width
            }
        }

        if (y >= maxY) y -= maxY.toFloat()/5

        detectCollision.left = x.toInt() + 40
        detectCollision.top = y.toInt() + 80
        detectCollision.right = x.toInt() + bitmap.width - 40
        detectCollision.bottom = y.toInt() + bitmap.height - 80
    }
}

