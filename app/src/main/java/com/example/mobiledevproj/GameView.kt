package com.example.mobiledevproj

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.MediaPlayer
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView : SurfaceView, Runnable {

    var isPlaying = false
    var gameThread : Thread? = null

    var surfaceHolder : SurfaceHolder
    var paint : Paint
    var paintTxt : Paint
    var canvas : Canvas? = null

    var ball : Ball
    var cars = arrayListOf<Car>()

    var score = 0

    constructor(context: Context, width : Int, height: Int) : super(context) {

        surfaceHolder = holder
        paint = Paint()

        paintTxt = Paint()
        paintTxt.textSize = 156f
        paintTxt.isFakeBoldText = true
        paintTxt.color = Color.BLACK

        ball = Ball(context, width, height)

        for ( i in 1..3){
            cars.add(Car(context,width, height))
        }

    }

    fun control(){
        Thread.sleep(17)
    }

    fun update(){
        ball.update()
        for (car in cars) {
            car.update(ball.speed)
            if (car.detectCollision.intersect(ball.detectCollision)){
                car.x = -300.toFloat()

                score += 10
            }
        }
    }

    fun draw() {
        if (surfaceHolder.surface.isValid){
            canvas = surfaceHolder.lockCanvas()

            canvas?.drawColor(Color.WHITE)

            canvas?.drawBitmap(ball.bitmap, ball.x , ball.y , paint)

            for (car in cars){
                canvas?.drawBitmap(car.bitmap, car.x , car.y , paint)
            }

            canvas?.drawText("Score:$score",10f, 200f, paintTxt)
            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }

    override fun run() {
        while (isPlaying){
            update()
            draw()
            control()
        }
    }

    fun resume(){
        gameThread = Thread(this)
        gameThread?.start()
        isPlaying = true
    }

    fun pause() {
        isPlaying = false
        gameThread?.join()
    }
}