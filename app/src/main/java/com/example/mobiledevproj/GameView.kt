package com.example.mobiledevproj

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.MediaPlayer
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView : SurfaceView, Runnable {

    var isPlaying = false
    var gameThread : Thread? = null

    var surfaceHolder : SurfaceHolder
    var paint : Paint
    var paintTxt : Paint
    var canvas : Canvas? = null

    var level = 0

    var ball : Ball
    var cars : MutableList<Car> = arrayListOf()

    var score = 0

    constructor(context: Context, width : Int, height: Int) : super(context) {

        surfaceHolder = holder
        paint = Paint()

        paintTxt = Paint()
        paintTxt.textSize = 156f
        paintTxt.isFakeBoldText = true
        paintTxt.color = Color.BLACK

        ball = Ball(context, width, height)

        for ( i in 1..5){
            if (i % 2 == 0) cars.add(Car(context,width, (height * i)/5, false))
            else cars.add(Car(context,width, (height * i)/5, true))
        }

    }

    fun control(){
        Thread.sleep(17)
    }

    fun update(){
        ball.update()
        updateLevel()
        for (car in cars) {
            car.update()
            if (car.detectCollision.intersect(ball.detectCollision)){
                val intent = Intent(context, GameOverActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    fun draw() {
        if (surfaceHolder.surface.isValid){
            canvas = surfaceHolder.lockCanvas()

            canvas?.drawColor(Color.WHITE)

            canvas?.drawBitmap(ball.bitmap, ball.x , ball.y , paint)

            for (car in cars){
                if (car.isReversed) canvas?.drawBitmap(car.reversedBitmap, car.x , car.y , paint)
                else canvas?.drawBitmap(car.bitmap, car.x , car.y , paint)
            }

            canvas?.drawText("Level:$level",10f, 200f, paintTxt)
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

    fun updateLevel(){
        if (ball.y - ball.bitmap.height < 0f){
            ball.y = height.toFloat()
            ball.speedY -= 3
            for (car in cars){
                car.speed += 3
            }
            level += 1
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                if(event.x < width / 2){
                    ball.speedX = -10
                }else{
                    ball.speedX = 10
                }
                return true
            }
            MotionEvent.ACTION_UP -> {
                return true
            }
        }
        return false
    }
}