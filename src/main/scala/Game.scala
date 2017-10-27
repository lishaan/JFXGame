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

class Game (val playerName: String) extends Stage {

	def this() = this("Player")
	def closeGame() = this.close

	def intersected(moverA: Moveable, moverB: Moveable): Boolean = {
		val dx = moverB.x - moverA.x
		val dy = moverB.y - moverA.y
		val dist = math.sqrt(dx*dx + dy*dy)

		return dist < math.abs(moverA.size + moverB.size)
	}

	def getHighscores(file: String): ArrayBuffer[Score] = {
		var scores: ArrayBuffer[Score] = ArrayBuffer()
		try {
			val fScanner = new java.util.Scanner(new java.io.File(file))
			while (fScanner.hasNextLine) {
				var name: String = ""
				while (!fScanner.hasNextDouble) {
					name += fScanner.next
				}
				var score: Double = fScanner.nextDouble

				scores += new Score(name, score)
			}
			fScanner.close
		} catch {
			case e: Exception => 
			case _: Throwable =>
		}

		return scores
	}

	title = "JFXGame - Play"
	resizable = false

	scene = new Scene(Const.gameWidth, Const.gameHeight) {
		var enemies: ArrayBuffer[Enemy] = ArrayBuffer()
		var bullets: ArrayBuffer[Bullet] = ArrayBuffer()
		
		val player = new Player(playerName)
		var timerText = new Text(10, 20, "0.0") {
			fill = Const.color("TimerText")
		}

		var keys = Map (
			"Right" -> false,
			"Left"  -> false
		)

		var lastTime: Long = -3
		var seconds: Double = 0.0

		// Canvas
		val canvas: Canvas = new Canvas(Const.gameWidth, Const.gameHeight);
		val drawer: GraphicsContext = canvas.graphicsContext2D

		val timer: AnimationTimer = AnimationTimer(t => {
			if(lastTime > 0) {
				val delta = (t-lastTime)/1e9
				
				Global.playerPos = player.position
				Global.delta = delta

				var indexes: ArrayBuffer[Int] = ArrayBuffer()

				// Enemies
				if (!enemies.isEmpty) {
					indexes = ArrayBuffer()
					for (i <- 0 until enemies.length) {
						// Player death
						if (intersected(enemies(i), player)) {
							timer.stop
							
							// TODO: Write ${player.kills} and $seconds to highscore
							// player.kills
							// seconds
							// val scores = getHighscores(Const.highscoresFile)
							// scores.foreach(score => println(s"Name: ${score.name} Score: ${score.score}"))

							root = new Button("Go Back To Main Menu") {
								onAction = (e: ActionEvent) => closeGame()
							}
						}

						// Enemies & Bullets
						bullets.foreach(bullet => {
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
				}

				// Enemies Buffer
				indexes = indexes.distinct
				indexes.foreach(index => enemies.remove(index))

				// Bullets
				if (!bullets.isEmpty) {
					indexes = ArrayBuffer()

					// Bullets move
					for (i <- 0 until bullets.length) {
						bullets(i).move
						if (bullets(i).y < (-bullets(i).size)) {
							indexes += i
						}
					}

					// Bullets Buffer
					indexes = indexes.distinct
					indexes.foreach(index => bullets.remove(index))
				}

				// Player move
				if (keys("Right")) player.move("Right")
				if (keys("Left")) player.move("Left")

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
				bullets.foreach(b => b.draw(drawer))
				enemies.foreach(e => e.draw(drawer))
				player.draw(drawer)

				seconds += delta
				timerText.text = "%.1f".format(seconds)
			}
			lastTime = t
		})

		onKeyPressed = (e: KeyEvent) => {
			e.code match {
				case KeyCode.Right => keys("Right") = true
				case KeyCode.Left => keys("Left") = true
				case _ => 
			}
		}

		onKeyReleased = (e: KeyEvent) => {
			e.code match {
				case KeyCode.Right => keys("Right") = false
				case KeyCode.Left => keys("Left") = false
				case KeyCode.Space => bullets +:= new Bullet(player.position)
				case KeyCode.Z => bullets +:= new Bullet(player.position)
				case _ =>
			}
		}

		content = List(canvas, timerText)

		timer.start
	}
}