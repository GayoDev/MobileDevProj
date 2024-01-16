Frogger Type Game - Projeto Android de Desenvolvimento de Jogos para Plataformas Móveis. 
João Gaio n25962
===========================================


## Índice

- [Introdução](#description)
- [Características](#features)
- [Gameplay Overview](#installation)
- [Código](#usage)
- [Conclusão](#contributing)
- [Créditos](#coding)

## Introdução

Frogger Type Game é, como o nome indica, um jogo baseado no clássico jogo "Frogger". Originalmente na game engine LÖVE, partiu para o android studio após descobrir que não era possível a integração do firebase. Dito firebase possibilita o armazenamento de dados do jogo, de modo anónimo, numa base de dados online de modo a organizar um sistema de High Score.


## Características

- **Controlos Intuitivos:** Experiência fluída de gameplay construída especificamente para mobile.
- **Fast Paced Gameplay:** Progressão veloz que fumenta a adrenalina até ao inevitável "crash".
- **High Scores:** Implementação de high scores através de níveis com recurso ao firebase.

## Gameplay Overview

Inicialmente, o jogo era mais fiél à visão clássica do "Frogger" mas, devido aos seus controlos atráves de botões, movimento lento e metódico, tamanho ideal do ecrã e gameplay mais estratégica, mudou-se a visão para algo mais apelativo aos controlos, circunstâncias e game flow de mobile gaming. Neste âmbito, a perspetiva foi alterada para ser vertical, logo mais estreita, e a fantasia do jogo mudar de um sapo atravessar a rua para uma bola à solta na estrada. Com esta fantasia de gameplay foi possível conceptualizar uma gameplay mais frenética e replayable. A bola move-se do fundo do ecrã até ao topo, sendo que o jogador pode controlar se a bola se mexe para a esquerda ou direita tocando no lado correspondente do ecrã. Carros de direções opostas e velocidades diferentes deverão ser evitados de modo a atingir o topo. Sendo esta meta cumprida o nível aumenta, aumentando a velocidade tanto da bola como dos carros, fazendo com que cada nível seja mais rápido e díficil que o anterior.

## Código

_MainActivity_
-
Nesta activity verificamos se o jogador já se encontra autenticado no firebase, caso contrário este é autenticado de modo anónimo. Esta atividade também funciona como ecrã inicial de onde podemos partir para o jogo ou para os high scores clicando nos botões correspondentes.
```kotlin
package com.example.mobiledevproj

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth

        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInAnonymously:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInAnonymously:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }

        findViewById<ImageButton>(R.id.imagePlayButton).setOnClickListener(){
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.imageHighScoreButton).setOnClickListener(){
            val intent = Intent(this, HighscoreActivity::class.java)
            startActivity(intent)
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
    }

}
```

_GameActivity_
-
Nesta Activity inicializamos a "Game View" e sistemas adjacentes e exibimos dita view. Deste modo conseguimos ligar a Main Activity à janela do jogo.
```kotlin
import android.graphics.Point
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    lateinit var gameView : GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        gameView = GameView(this, size.x, size.y)
        setContentView(gameView)
    }

    override fun onResume() {
        super.onResume()
        gameView.resume()
    }

    override fun onPause() {
        super.onPause()
        gameView.pause()
    }
}
```

_HighscoreActivity_
-
Nesta Activity exibimos uma lista dos níveis mais altos armazenados no firestore (base de dados do firebase), de modo decrescente, recorrendo à classe Player para obter a lista de jogadores armazenados na base de dados. Infelizmente, apesar dos valores dos níveis serem corretamente armazenados no firestore, nada é exibido na lista de momento.
```kotlin
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
```

_Player_
-
Player é uma classe na qual extrai-se os jogadores registados na base de dados do firestore para uma lista.
```kotlin
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
```

_Ball_
-
A classe Ball é a classe responsável por construir a bola e de moldar todos os seus atributos de modo a proporcionar a melhor experiência de gameplay. Também é aqui que se estabelece a incrementação da velocidade da bola e o retorno ao fundo do ecrã após chegar ao topo.
```kotlin
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
```

_Car_
-
A classe Car é a classe responsável por construir os carros e de moldar todos os seus atributos de modo a proporcionar a melhor experiência de gameplay. Também é aqui que se estabelece a incrementação da velocidade dos carros e o "spawn" contínuo destes.
```kotlin
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
```
_GameView_
-
A Game View é a classe que trata do jogo propriamente dito. De como os elementos são renderizados a atualizações de game states. Começa or inicializar todas as variáveis necessárias para futuras inicialiazações ou atualizações de estados ou atributos. É também inicializado o construtor da Game View, que inclui inicializações de componentes estéticas, do jogador (ball) e dos carros, sendo que estes são renderizados verticalmente em zonas equidistantes do ecrã e podendo vir de ambos os lados do ecrã.
```kotlin
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

    var level : Int = 0

    var ball : Ball
    var cars : MutableList<Car> = arrayListOf()

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
```
De seguida são estabelcidas as funções complementares da função run: control, draw e update. Draw serve para controlar o que é desenhado e de que modo, update atualiza o game state e verifica colisões entre os carros e a bola, sendo que nesse caso trocamos para a GameOverActivity e armazena-se o nível a que o jogador ficou. O control simplesmente controla a thread count.
```kotlin
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
                intent.putExtra("level", level)
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

    override fun run() {
        while (isPlaying){
            update()
            draw()
            control()
        }
    }
```
As funções resume e pause verificam se o jogador está ou não a jogar e na função onTouchEvent verifica-se se o jogador clicou em que lado do ecrã, incrementando a velocidade da bola para o lado correspondente.
```kotlin
fun resume(){
        gameThread = Thread(this)
        gameThread?.start()
        isPlaying = true
    }

    fun pause() {
        isPlaying = false
        gameThread?.join()
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
                ball.speedX = 0
                return true
            }
        }
        return false
    }
}
```
_GameOverActivity_
-
A GameOverActivity é a atividade que o jogador encontra ao perder. O nível armazenado na GameView quando o jogador perde é recolhido aqui e chamado na função storePlayerLevel(). Desta atividade é possível voltar ao jogo ou verificar os highscores.
```kotlin
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
```

_FirestoreLevelManager_
-
O FirestoreLevelManager é um objeto no qual é inicializada a base de dados do firestore e a função storePlayerLevel(), que recebe um valor inteiro correspondente ao nível que o jogador alcançou e armazena-o na base de dados.
```kotlin
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
```

Conclusão
----------

Infelizmente, devido aos highscores não exiberem, esta implementação não se encontra completa, mesmo que o API da firebase esteja corretamente conectado. Mas o jogo em si acabou por ser divertido de desenvolver e jogar e cumpriu o objetivo de traduzir a experiência do "Frogger" para um modelo de gameplay mais atualizado, casual e free flowing que o mobile habilita e necessita.

Créditos
-------
Creditos Sprites:
-
Carro: <a href="https://www.flaticon.com/free-icons/car" title="car icons">Car icons created by Konkapp - Flaticon</a>
Bola: <a href="https://www.flaticon.com/free-icons/soccer" title="soccer icons">Soccer icons created by Freepik - Flaticon</a>
Play: <a href="https://www.flaticon.com/free-icons/play-button" title="play button icons">Play button icons created by Freepik - Flaticon</a>
High Score: <a href="https://www.flaticon.com/free-icons/trophy" title="trophy icons">Trophy icons created by Freepik - Flaticon</a>
Replay: <a href="https://www.flaticon.com/free-icons/replay" title="replay icons">Replay icons created by Freepik - Flaticon</a>
Game Over: <a href="https://www.flaticon.com/free-icons/game-over" title="game over icons">Game over icons created by Freepik - Flaticon</a>
Leave: <a href="https://www.flaticon.com/free-icons/leave" title="leave icons">Leave icons created by Freepik - Flaticon</a>




