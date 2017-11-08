import scala.io.Source 
import scala.collection.mutable.{ArrayBuffer, Map}
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.stage.Stage
import scalafx.scene.control.Button
import scalafx.scene.Scene
import scalafx.event.ActionEvent
import scalafx.scene.text.Text
import scalafx.scene.input.{KeyEvent, KeyCode}
import scalafx.event.ActionEvent
import scalafx.animation.AnimationTimer
import scalafx.scene.paint.Color
import scalafx.scene.canvas.{Canvas, GraphicsContext}

object Game {
	var paused = false
	var ended = false

	def togglePause = { Game.paused = !Game.paused }
}

class Game (val playerName: String) extends Stage {

	def this() = this("Player")

	title = "JFXGame - Play"
	resizable = false
	Game.paused = false
	Game.ended = false

	scene = new Scene(Const.gameWidth, Const.gameHeight) {
		var enemies: ArrayBuffer[Enemy] = ArrayBuffer()
		var bullets: ArrayBuffer[Bullet] = ArrayBuffer()
		
		val player = new Player(playerName)
		var timerText = new Text(10, 20, "0.0") {
			fill = Const.color("TimerText")
		}

		var keys = Map (
			"Up"     -> false,
			"Right"  -> false,
			"Down"   -> false,
			"Left"   -> false,
			"Escape" -> false
		)

		var lastTime: Long = -3
		var seconds: Double = 0.0

		// Canvas
		val canvas: Canvas = new Canvas(Const.gameWidth, Const.gameHeight);
		val drawer: GraphicsContext = canvas.graphicsContext2D

		var didAppend: Boolean = false
		var playerIsDead: Boolean = false

		val timer: AnimationTimer = AnimationTimer(timeNow => {
			if(lastTime > 0 && !Game.paused) {
				val delta = (timeNow-lastTime)/1e9
				
				Global.playerPos = player.position
				Global.delta = delta

				var indexes: ArrayBuffer[Int] = ArrayBuffer()

				// Enemies
				if (!enemies.isEmpty) {
					indexes = ArrayBuffer()
					for (i <- 0 until enemies.length) {
						// Player death
						playerIsDead = intersected(enemies(i), player)

						if (enemies(i).isInstanceOf[Shooter]) {
							enemies(i).asInstanceOf[Shooter].bullets.foreach(bullet => {
								playerIsDead = intersected(player, bullet)
							})
						}

						if (playerIsDead) { //playerIsDead
							// Highscores
							val playerScore = new Score(player.getName, seconds)
							val scores = Util.getHighscores(Const.highscoresFile)

							didAppend = Util.appendScore(Const.highscoresFile, playerScore)
							
							// if (didAppend) println(s"Score ${playerScore} appended to highscore")
							// else println("Cannot append to highscore")

							// Print the current highscore file
							// scores.foreach(score => println(s"Name: ${score.name} Score: ${score.score}"))
							Game.ended = true
							timer.stop
						}

						// Enemies & Bullets
						bullets.foreach(bullet => {
							if (intersected(bullet, enemies(i))) {
								enemies(i).inflictDamage(bullet.damage)
								bullet.remove
								
								if (enemies(i).dead) {
									// enemies(i).remove
									player.incrementKills
									if (!indexes.contains(i)) indexes += i
								}
							}
						})

						// Enemies move
						enemies(i).move
					}
				}

				// Enemies Buffer
				indexes.foreach(index => enemies.remove(index))

				// Bullets
				if (!bullets.isEmpty) {
					indexes = ArrayBuffer()

					// Bullets move
					for (i <- 0 until bullets.length) {
						bullets(i).move
						if (bullets(i).y < (-bullets(i).size)) {
							if (!indexes.contains(i)) indexes += i
						}
					}

					// Bullets Buffer
					indexes.foreach(index => bullets.remove(index))
				}

				// Player move
				if (keys("Up"   )) player.move("Up"   )
				if (keys("Right")) player.move("Right")
				if (keys("Down" )) player.move("Down" )
				if (keys("Left" )) player.move("Left" )

				// Game speed configuration
				if (seconds >= 20 ) Const.gameSpeed = 1.1
				if (seconds >= 40 ) Const.gameSpeed = 1.2
				if (seconds >= 80 ) Const.gameSpeed = 1.3
				if (seconds >= 100) Const.gameSpeed = 1.4
				if (seconds >= 120) Const.gameSpeed = 1.5
				if (seconds >= 140) Const.gameSpeed = 1.6
				Const.updateSpeeds

				// Enemies Spawn
				Global.spawnDelays.foreach(delay => {
					delay.update
					if (delay.stopped) {
						enemies +:= Enemy.spawn(delay.enemyName)
						delay.reset
					}
				})

				// Drawings
				drawer.fill = Const.color("Background")
				drawer.fillRect(0, 0, Const.gameWidth, Const.gameHeight)
				drawer.fill = Const.color("PlayArea")
				drawer.fillRect(0, Const.playAreaHeight, Const.gameWidth, Const.playAreaHeight)

				bullets.foreach(b => b.draw(drawer))
				enemies.foreach(e => e.draw(drawer))
				player.draw(drawer)

				seconds += delta
				Global.seconds = seconds
				timerText.text = "%.1f".format(seconds)
			}

			if (Game.paused) drawPausedScreen(drawer)
			if (Game.ended) drawEndGameScreen(drawer, didAppend)
			lastTime = timeNow
		})

		onKeyPressed = (e: KeyEvent) => {
			e.code match {
				// Movement Controls
				case KeyCode.Up => keys("Up") = true
				case KeyCode.Right => keys("Right") = true
				case KeyCode.Down => keys("Down") = true
				case KeyCode.Left => keys("Left") = true
					
				case _ => 
			}
		}

		onKeyReleased = (e: KeyEvent) => {
			e.code match {
				// Movement Controls
				case KeyCode.Up => keys("Up") = false
				case KeyCode.Right => keys("Right") = false
				case KeyCode.Down => keys("Down") = false
				case KeyCode.Left => keys("Left") = false

				// Actions
				case KeyCode.Space => bullets +:= new Bullet(player.position)
				case KeyCode.Z => bullets +:= new Bullet(player.position)

				// Pausing
				case KeyCode.Escape => {
					if (!Game.ended) Game.togglePause
					// TODO: View the highscore file in the highscore menu 
				}
				case KeyCode.Q => { 
					timer.stop
					if (Game.paused || Game.ended) closeGame
				}

				case _ =>
			}
		}

		content = List(canvas, timerText)

		timer.start
	}

	def closeGame = this.close

	def drawEndGameScreen(drawer: GraphicsContext, scoreAppended: Boolean): Unit = {
		val fontSize = 20*Const.gameScale
		drawer.fill = Const.color("PausedText")
		drawer.textAlign = scalafx.scene.text.TextAlignment.Center

		drawer.font = new scalafx.scene.text.Font(fontSize)
		drawer.fillText("You Lose", Const.gameWidth/2, Const.gameHeight/2)

		drawer.font = new scalafx.scene.text.Font(fontSize*0.50)
		drawer.fillText("Press Q to go back to Main Menu", Const.gameWidth/2, Const.gameHeight/2 + (fontSize*2))

		drawer.font = new scalafx.scene.text.Font(fontSize*0.50)
		drawer.fillText("Press Esc to open the highscore menu", Const.gameWidth/2, Const.gameHeight/2 + fontSize)
	}

	def drawPausedScreen(drawer: GraphicsContext): Unit = {
		val fontSize = 20*Const.gameScale
		drawer.fill = Const.color("PausedText")
		drawer.textAlign = scalafx.scene.text.TextAlignment.Center

		drawer.font = new scalafx.scene.text.Font(fontSize)
		drawer.fillText("Game Paused", Const.gameWidth/2, Const.gameHeight/2)

		drawer.font = new scalafx.scene.text.Font(fontSize*0.50)
		drawer.fillText("Press Q to go back to Main Menu", Const.gameWidth/2, Const.gameHeight/2 + (fontSize*2))

		drawer.font = new scalafx.scene.text.Font(fontSize*0.50)
		drawer.fillText("Press Esc to resume", Const.gameWidth/2, Const.gameHeight/2 + fontSize)
	}

	def intersected(moverA: Moveable, moverB: Moveable): Boolean = {
		val dx = moverB.x - moverA.x
		val dy = moverB.y - moverA.y
		val dist = math.sqrt(dx*dx + dy*dy)

		return dist < math.abs(moverA.size + moverB.size)
	}
}