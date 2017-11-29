import scala.collection.mutable.{ArrayBuffer, Map}
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.stage.Stage
import scalafx.scene.Scene
import scalafx.scene.text.Text
import scalafx.scene.paint.Color
import scalafx.scene.input.{KeyEvent, KeyCode}
import scalafx.scene.canvas.{Canvas, GraphicsContext}
import scalafx.event.ActionEvent
import scalafx.animation.AnimationTimer

/** The static Game object that manages all the games played. */
object Game {
	/** Name of the game */
	val name: String = "Spherical Insanity"

	/** The path of the system's temporary directory to store the highscores.txt file */
	private val highscoresFilePath: String = System.getProperty("java.io.tmpdir") + "/highscores.txt"
	val highscoresDir: String = System.getProperty("java.io.tmpdir") + "/"

	// Copy resources/highscores.txt to temporary directory IF it doesn't already exist there
	if (!(java.nio.file.Files.exists(java.nio.file.Paths.get(highscoresFilePath)))) {
		Highscores.createFile
	}

	var paused: Boolean = false
	var ended: Boolean = false
	var retry: Boolean = false

	/** Toggles the pausing of the game. */
	def togglePause: Unit = { Game.paused = !Game.paused }
}

/** A stage where a game is played on the scene until the game ends.
 *
 *  @param playerName the name of the current game's player
 */
class Game (val playerName: String) extends Stage {

	def this() = this("Player")

	title = s"${Game.name} - Play"
	resizable = false

	scene = new Scene(Global.gameWidth, Global.gameHeight) {
		Game.paused = false
		Game.ended = false
		Game.retry = false

		val spawners: ArrayBuffer[Spawner] = ArrayBuffer (
			new Spawner("Bouncer", 12),
			new Spawner("Seeker" , 2.5, 5.0),
			new Spawner("Shooter", 30.0, 40.0)
		)

		var enemies: ArrayBuffer[Enemy] = ArrayBuffer()
		
		val player = new Player(playerName)
		var timerText = new Text(10, 20, "Score: 0.0") {
			fill = Global.color("TimerText")
		}

		var keys = Map (
			"Up"     -> false,
			"Right"  -> false,
			"Down"   -> false,
			"Left"   -> false
		)

		var lastTime: Long = -3
		var seconds: Double = 0.0
		Global.seconds = seconds

		// Canvas
		val canvas: Canvas = new Canvas(Global.gameWidth, Global.gameHeight);
		val drawer: GraphicsContext = canvas.graphicsContext2D

		var didAppend: Boolean = false
		var playerIsDead: Boolean = false

		val timer: AnimationTimer = AnimationTimer(timeNow => {
			if (lastTime > 0 && !Game.paused) {
				// Delta time
				val delta = (timeNow-lastTime)/1e9
				
				player.updateBullets
				Global.playerPos = player.position
				Global.delta = delta

				// Enemies Spawn
				spawners.foreach(delay => {
					delay.update
					if (delay.stopped) {
						enemies +:= Enemy.spawn(delay.enemyName)
						delay.reset
					}
				})

				// Drawings
				drawer.fill = Global.color("Background")
				drawer.fillRect(0, 0, Global.gameWidth, Global.gameHeight)
				drawer.fill = Global.color("PlayArea")
				drawer.fillRect(0, Global.playAreaHeight, Global.gameWidth, Global.playAreaHeight)

				player.draw(drawer)
				player.bullets.foreach(b => b.draw(drawer))
				enemies.foreach(e => e.draw(drawer))

				// Enemies
				if (!enemies.isEmpty) {
					var indexes: ArrayBuffer[Int] = ArrayBuffer()
					for (i <- 0 until enemies.length) {
						
						// Player death from intersecting with enemies
						playerIsDead = intersected(enemies(i), player)

						// Player death from Shooter's bullets
						if (enemies(i).isInstanceOf[Shooter]) {
							enemies(i).asInstanceOf[Shooter].shootBullet
							enemies(i).asInstanceOf[Shooter].updateBullets
							enemies(i).asInstanceOf[Shooter].bullets.foreach(bullet => {
								playerIsDead = intersected(player, bullet)
							})
						}

						// Player death
						if (playerIsDead) {

							// Highscores
							if (Global.appendToHighscoresFile)
								didAppend = Highscores.append(new Score(player.name, player.kills, seconds))

							Game.ended = true
							timer.stop
						}

						// Enemy death from Player's bullets
						player.bullets.foreach(bullet => {
							if (intersected(bullet, enemies(i))) {
								enemies(i).inflictDamage(bullet.damage)
								bullet.remove
								
								if (enemies(i).dead) {
									enemies(i).remove
									player.incrementKills
									if (!indexes.contains(i)) indexes += i
								}
							}
						})

						// Enemies move
						enemies(i).move
					}
					indexes.foreach(index => enemies.remove(index))
				}
				
				// Player move
				if (keys("Up"   )) player.move("Up"   )
				if (keys("Right")) player.move("Right")
				if (keys("Down" )) player.move("Down" )
				if (keys("Left" )) player.move("Left" )


				// Game speed configuration
				if (Global.appendToHighscoresFile || (Global.gameScale==1.2 && Global.gameSpeed==1.0)) {					
					if (seconds >= 10 ) Global.gameSpeed = 1.1
					if (seconds >= 40 ) Global.gameSpeed = 1.2
					if (seconds >= 70 ) Global.gameSpeed = 1.3
					if (seconds >= 110) Global.gameSpeed = 1.4
					if (seconds >= 160) Global.gameSpeed = 1.5
					if (seconds >= 220) Global.gameSpeed = 1.6
					if (seconds >= 290) Global.gameSpeed = 1.7
					if (seconds >= 360) Global.gameSpeed = 1.8
					if (seconds >= 450) Global.gameSpeed = 1.9
					if (seconds >= 550) Global.gameSpeed = 2.0
				}

				seconds += delta
				Global.seconds = seconds
				timerText.text = "Score: %.1f".format(seconds)
			}

			if (Game.paused) drawPausedScreen(drawer)
			if (Game.ended) drawEndGameScreen(drawer, didAppend)
			lastTime = timeNow
			Global.updateConsts

			// println("Fps: %.2f".format(1.0/Global.delta))
		})

		// Key pressed events
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

		// Key released events
		onKeyReleased = (e: KeyEvent) => {
			e.code match {
				// Movement Controls
				case KeyCode.Up => keys("Up") = false
				case KeyCode.Right => keys("Right") = false
				case KeyCode.Down => keys("Down") = false
				case KeyCode.Left => keys("Left") = false

				// Actions
				case KeyCode.Space => player.shootBullet
				case KeyCode.Z => player.shootBullet

				// Pausing
				case KeyCode.Escape => {
					if (!Game.ended) Game.togglePause
				}

				// Quitting
				case KeyCode.Q => { 
					if (Game.paused || Game.ended) {
						timer.stop
						closeGame
					}
				}

				// Retrying
				case KeyCode.R => {
					if (Game.ended) {
						Game.retry = true
						Global.gameSpeed = 1.0
						timer.stop
						closeGame
					}
				}

				case _ =>
			}
		}

		content = List(canvas, timerText)
		timer.start
	}

	/** Closes/ends the current game by closing the stage. */
	def closeGame = this.close

	/** Draws the menu that is displayed when a game ends.
	 *
	 *  @param drawer the graphicsContext of where the menu is drawn
	 *  @param scoreAppended a boolean value that determines whether the score appends to the highscore
	 */
	def drawEndGameScreen(drawer: GraphicsContext, scoreAppended: Boolean): Unit = {
		val fontSize = 20*Global.gameScale
		drawer.fill = Global.color("PausedText")
		drawer.textAlign = scalafx.scene.text.TextAlignment.Center

		drawer.font = new scalafx.scene.text.Font(fontSize)
		drawer.fillText("You Lose", Global.gameWidth/2, Global.playAreaHeight/2)

		drawer.font = new scalafx.scene.text.Font(fontSize*0.50)
		drawer.fillText("Press Q to go back to Main Menu", Global.gameWidth/2, Global.playAreaHeight/2 + (fontSize))

		drawer.font = new scalafx.scene.text.Font(fontSize*0.50)
		drawer.fillText("Press R to try again", Global.gameWidth/2, Global.playAreaHeight/2 + (fontSize*2))

		drawer.font = new scalafx.scene.text.Font(fontSize*0.50)
		drawer.fillText(if (scoreAppended) "Your score has been appended to the highscore" else "You score did not append to the highscore", Global.gameWidth/2, Global.playAreaHeight/2 + (fontSize*3))			
	}

	/** Draws the menu that is displayed when a game pauses.
	 *
	 *  @param drawer the graphicsContext of where the menu is drawn
	 */
	def drawPausedScreen(drawer: GraphicsContext): Unit = {
		val fontSize = 20*Global.gameScale
		drawer.fill = Global.color("PausedText")
		drawer.textAlign = scalafx.scene.text.TextAlignment.Center

		drawer.font = new scalafx.scene.text.Font(fontSize)
		drawer.fillText("Game Paused", Global.gameWidth/2, Global.playAreaHeight/2)

		drawer.font = new scalafx.scene.text.Font(fontSize*0.50)
		drawer.fillText("Press Q to go back to Main Menu", Global.gameWidth/2, Global.playAreaHeight/2 + (fontSize*2))

		drawer.font = new scalafx.scene.text.Font(fontSize*0.50)
		drawer.fillText("Press Esc to resume", Global.gameWidth/2, Global.playAreaHeight/2 + fontSize)
	}

	/** Checks whether two Moveable entities are intersected.
	 *
	 *  @param moverA The first mover entity
	 *  @param moverB The second mover entity
	 *  @return a boolean value that determines whether moverA and moverB has intersected
	 */
	def intersected(moverA: Moveable, moverB: Moveable): Boolean = {
		val dx = moverB.x - moverA.x
		val dy = moverB.y - moverA.y
		val dist = math.sqrt(dx*dx + dy*dy)

		return dist < math.abs(moverA.size + moverB.size)
	}
}