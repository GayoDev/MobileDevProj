Frogger Type Game - Projeto Android de Desenvolvimento de Jogos para Plataformas Móveis. 
João Gaui n25962
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

- **Controlos Intuitivos:** Expetiência fluída de gameplay construída especificamente para mobile.
- **Fast Paced Gameplay:** Progressão veloz que fumenta a adrenalina até ao inevitável "crash".
- **High Scores:** Implementação de high scores através de níveis com recurso ao firebase.

## Gameplay Overview

Inicialmente, o jogo era mais fiél à visão clássica do "Frogger" mas, devido aos seus controlos atráves de botões, movimento lento e metódico, tamanho ideal do ecrã e gameplay mais estratégica, mudou-se a visão para algo mais apelativo aos controlos, circunstâncias e game flow de mobile gaming. Neste âmbito, a perspetiva foi alterada para ser vertical, logo mais estreita, e a fantasia do jogo mudar de um sapo atravessar a rua para uma bola à solta na estrada. Com esta fantasia de gameplay foi possível conceptualizar uma gameplay mais frenética e replayable. A bola move-se do fundo do ecrã até ao topo, sendo que o jogador pode controlar se a bola se mexe para a esquerda ou direita tocando no lado correspondente do ecrã. Carros de direções opostas e velocidades diferentes deverão ser evitados de modo a atingir o topo. Sendo esta meta cumprida o nível aumenta, aumentando a velocidade tanto da bola como dos carros, fazendo com que cada nível seja mais rápido e díficil que o anterior.

## Código

You can add mote sprites to this game by creating a class tha follows this pattern:

- `x` and `y` properties define the position of the sprite
- `bitmap` the visual representation
- `detectCollision` the area of collision with other sprites
- the `update(playerSpeed :Int)` function should calculate the position and the `detectCollision` of the sprite in the next frame sequence

```kotlin
class Enemy {
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

        bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.enemy)
        x = maxX.toFloat()
        y = (generator.nextInt(maxY - bitmap.height) ).toFloat()
        speed = generator.nextInt(6) + 10

        detectCollision = Rect(x.toInt() ,y.toInt(), bitmap.width, bitmap.height)
    }

    fun update(playerSpeed :Int) {
        x -= playerSpeed
        x -= speed

        if (x < (0 - bitmap.width)){
            x = maxX.toFloat()
            y = (generator.nextInt(maxY - bitmap.height) ).toFloat()
            speed = generator.nextInt(6) + 10
        }

        detectCollision.left = x.toInt()
        detectCollision.top = y.toInt()
        detectCollision.right = x.toInt() + bitmap.width
        detectCollision.bottom = y.toInt() + bitmap.height
    }

}
```

- Than create an instance of Enemy on the class GameView and that should go through the update and draw function.

```kotlin
class GameView : SurfaceView, Runnable {
   //...
   val e: Enemy
   //...

   fun update() {
      //...
      e.update(player.speed)
      if (e.detectCollision.intersect(player.detectCollision)) {
         boom.x = e.x
         boom.y = e.y
      }

      //...
   }

   fun draw() {
      if (surfaceHolder.surface.isValid) {
         canvas = surfaceHolder.lockCanvas()
         canvas?.drawColor(Color.BLACK)
         //...
         canvas?.drawBitmap(e.bitmap, e.x, e.y, paint)
         //...
         surfaceHolder.unlockCanvasAndPost(canvas)
      }
   }

}
```

Conclusão
----------

Sure there are many ways to develop games in Android, but this is the simpler and lighter way to do it. Have fun. 

Créditos
-------

Creditos Sprites
Carro: <a href="https://www.flaticon.com/free-icons/car" title="car icons">Car icons created by Konkapp - Flaticon</a>
Bola: <a href="https://www.flaticon.com/free-icons/soccer" title="soccer icons">Soccer icons created by Freepik - Flaticon</a>
Play: <a href="https://www.flaticon.com/free-icons/play-button" title="play button icons">Play button icons created by Freepik - Flaticon</a>
High Score: <a href="https://www.flaticon.com/free-icons/trophy" title="trophy icons">Trophy icons created by Freepik - Flaticon</a>
Replay: <a href="https://www.flaticon.com/free-icons/replay" title="replay icons">Replay icons created by Freepik - Flaticon</a>
Game Over: <a href="https://www.flaticon.com/free-icons/game-over" title="game over icons">Game over icons created by Freepik - Flaticon</a>
Leave: <a href="https://www.flaticon.com/free-icons/leave" title="leave icons">Leave icons created by Freepik - Flaticon</a>




