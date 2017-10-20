import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.{Node, Scene}
import scalafx.event.ActionEvent
import scalafx.scene.text.Text
import scalafx.scene.shape.Circle
import scalafx.scene.input.{KeyEvent, KeyCode}
import scalafx.event.ActionEvent
import scalafx.animation.AnimationTimer
import scalafx.scene.paint.Color
import scala.collection.mutable.{ArrayBuffer, Map}

class Game (playerName: String) extends JFXApp {
	def this() = this("Player")

	stage = new PrimaryStage {
		title = "Game"
		scene = new Scene(Const.sceneWidth, Const.sceneHeight) {
			var timerText = new Text(10, 20, "0.0")

			var enemies: ArrayBuffer[Enemy] = ArrayBuffer()
			var bullets: ArrayBuffer[Bullet] = ArrayBuffer()

			val player = new Player(playerName)

			content = List(player.shape, timerText)

			// Memory Allocations
			val bulletsBufferCounter = BufferCounter(Const.memory("Bullets").head, Const.memory("Bullets").tail)
			for (i <- Const.memory("Bullets").head to Const.memory("Bullets").tail) { content += new Circle() }
			
			val enemiesBufferCounter = BufferCounter(Const.memory("Enemies").head, Const.memory("Enemies").tail)
			for (i <- Const.memory("Enemies").head to Const.memory("Enemies").tail) { content += new Circle() }

			var keys = Map(
				"Right" -> false,
				"Left"  -> false
			)

			onKeyPressed = (e: KeyEvent) => {
				if (e.code == KeyCode.Right) keys("Right") = true
				if (e.code == KeyCode.Left) keys("Left") = true
			}

			onKeyReleased = (e: KeyEvent) => {
				if (e.code == KeyCode.Right) keys("Right") = false
				if (e.code == KeyCode.Left) keys("Left") = false
				if (e.code == KeyCode.Space) {
					bullets +:= new Bullet(Position(player.x, player.y))
					content(bulletsBufferCounter.value) = bullets.head
					bulletsBufferCounter.increment
				}
			}

			var lastTime = -3L
			val bulletSpeed = 300
			var spawnDelay = 1.0
			var seconds = 0.0

			println(player.name)

			val timer: AnimationTimer = AnimationTimer(t => {
				if(lastTime > 0) {
					val delta = (t-lastTime)/1e9

					var indexes: ArrayBuffer[Int] = ArrayBuffer()

					// Enemies
					if (!enemies.isEmpty) {
						for (i <- 0 until enemies.length) {
							val dx = player.x - enemies(i).x
							val dy = player.y - enemies(i).y

							val dist = math.sqrt(dx*dx + dy*dy)

							if (dist <= enemies(i).size + player.r) {
								content += new Text(195, 300, "You Lose!") 
								timer.stop
							}

							enemies(i).x = enemies(i).x + dx / dist * enemies(i).speed * delta
							enemies(i).y = enemies(i).y + dy / dist * enemies(i).speed * delta

							for (bullet <- bullets) {
								val dx = bullet.x - enemies(i).x
								val dy = bullet.y - enemies(i).y

								val dist = math.sqrt(dx*dx + dy*dy)

								// If Enemy Dies
								if (dist <= enemies(i).size + bullet.r) {
									enemies(i).remove
									if (!indexes.contains(i)) {
										indexes += i
									}
								}
							}
						}

						// Enemies Buffer
						indexes = indexes.distinct
						for (i <- 0 until indexes.length) {
							enemies.remove(indexes(i))
							player.incrementKills
						}
					}

					// Bullet Buffer
					if (!bullets.isEmpty) {
						indexes = ArrayBuffer()
						for (i <- 0 until bullets.length) {
							bullets(i).y = bullets(i).y - 4
							if (bullets(i).y < (-bullets(i).r)) {
								indexes += i
							}
						}
						for (i <- 0 until indexes.length) {
							bullets.remove(indexes(i))
						}
					}

					// Player
					if (keys("Right")) player.move("Right", delta)
					if (keys("Left")) player.move("Left", delta)

					// Enemies Spawn
					spawnDelay -= delta
					seconds += delta
					if (spawnDelay < 0) {
						val e = new Seeker()
						content(enemiesBufferCounter.value) = e.shape
						enemiesBufferCounter.increment

						enemies +:= e
						spawnDelay = 1.0
					}

					// DEBUG
					// println(content.length)
					// var count = 0
					// for (i <- content) {
					// 	println(s"$count ${i.toString}")
					// 	count += 1
					// }
					// println()
					timerText.text = "%.1f".format(seconds)
				}
				lastTime = t
			})
			timer.start
		}
	}
}