import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.event.ActionEvent
import scalafx.scene.text.Text
import scalafx.scene.shape.Circle
import scalafx.scene.input.{KeyEvent, KeyCode}
import scalafx.event.ActionEvent
import scalafx.animation.AnimationTimer
import scalafx.scene.paint.Color
import scala.collection.mutable.{ArrayBuffer, Map}

class Game extends JFXApp {
	stage = new PrimaryStage {
		title = "Game"
		scene = new Scene(400, 600) {
			var timerText = new Text(10, 20, "0.0")
			var enemies = ArrayBuffer(Circle(10,10,10))

			var bullets: ArrayBuffer[Circle] = ArrayBuffer()
			val player = new Circle() {
				centerY = 550
				centerX = 200
				radius = 20
				fill = Color.Blue
			}

			content = List(enemies.head, player)
			content += timerText

			var keys = Map(
				"Right" -> false,
				"Left"  -> false
			)

			onKeyPressed = (e: KeyEvent) => {
				if (e.code == KeyCode.Right) keys("Right") = true
				if (e.code == KeyCode.Left) keys("Left") = true
				if (e.code == KeyCode.Space) {
					bullets +:= new Circle() {
						radius = 4
						centerY = player.centerY.value
						centerX = player.centerX.value
						fill = Color.Red
					}

					content += bullets.head
				}
			}

			onKeyReleased = (e: KeyEvent) => {
				if (e.code == KeyCode.Right) keys("Right") = false
				if (e.code == KeyCode.Left) keys("Left") = false
			}

			var lastTime = -3L
			var enemiesKilled = 0
			var enemySpeed = 80
			val bulletSpeed = 300
			val playerSpeed = 130
			var spawnDelay = 2.0
			var seconds = 0.0

			val timer: AnimationTimer = AnimationTimer(t => {
				if(lastTime > 0) {
					val delta = (t-lastTime)/1e9

					var indexes: ArrayBuffer[Int] = ArrayBuffer()

					for (i <- 0 until enemies.length) {
						val dx = player.centerX.value - enemies(i).centerX.value
						val dy = player.centerY.value - enemies(i).centerY.value

						val dist = math.sqrt(dx*dx + dy*dy)

						if (dist <= enemies(i).radius.value + player.radius.value) {
							content += new Text(195, 300, "You Lose!") 
							timer.stop
						}

						enemies(i).centerX = enemies(i).centerX.value + dx / dist * enemySpeed * delta
						enemies(i).centerY = enemies(i).centerY.value + dy / dist * enemySpeed * delta

						for (b <- bullets) {
							val dx = b.centerX.value - enemies(i).centerX.value
							val dy = b.centerY.value - enemies(i).centerY.value

							val dist = math.sqrt(dx*dx + dy*dy)

							if (dist <= enemies(i).radius.value + b.radius.value) {
								if (!indexes.contains(i)) {
									indexes += i
								}
							}
						}
					}
					for (i <- 0 until indexes.length) {
						enemies.remove(indexes(i))
						enemiesKilled += 1
					}


					if (!bullets.isEmpty) {
						indexes = ArrayBuffer()
						for (i <- 0 until bullets.length) {
							bullets(i).centerY.value = bullets(i).centerY.value - 4
							if (bullets(i).centerY.value < (-bullets(i).radius.value)) {
								indexes += i
							}
						}
						for (i <- 0 until indexes.length) {
							bullets.remove(indexes(i))
						}
					}

					if (keys("Right") && (player.centerX.value + player.radius.value < 400)) {
						player.centerX = player.centerX.value + playerSpeed*delta
					}

					if (keys("Left") && (player.centerX.value - player.radius.value > 0)) {
						player.centerX = player.centerX.value - playerSpeed*delta
					}

					spawnDelay -= delta
					seconds += delta
					if (spawnDelay < 0) {
						val e = Circle(math.random * 400, 0, 10)
						content += e
						enemies +:= e
						spawnDelay = 5.0
					}
					println(content)
					timerText.text = "%.1f".format(seconds)
				}
				lastTime = t
			})
			timer.start
		}
	}
}