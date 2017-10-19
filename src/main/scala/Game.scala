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

class Game extends JFXApp {
	stage = new PrimaryStage {
		title = "Game"
		scene = new Scene(400, 600) {
			var timerText = new Text(10, 20, "0.0")
			var enemies: ArrayBuffer[Circle] = ArrayBuffer()

			var bullets: ArrayBuffer[Circle] = ArrayBuffer()
			val player = new Circle() {
				centerY = 550
				centerX = 200
				radius = 20
				fill = Color.Blue
			}

			content = List(player, timerText)

			// Reserve space for bullets
			var bulletsCounter = 2
			for (i <- 0 until 20) {
				content += new Circle()
			}

			// Reserve space for enemies
			var enemiesCounter = 22
			for (i <- 0 to 10) {
				content += new Circle()
			}


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

					content(bulletsCounter) = bullets.head
					bulletsCounter += 1
					if (bulletsCounter > 21) {
						bulletsCounter = 2
					}
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
			var spawnDelay = 1.0
			var seconds = 0.0

			val timer: AnimationTimer = AnimationTimer(t => {
				if(lastTime > 0) {
					val delta = (t-lastTime)/1e9

					var indexes: ArrayBuffer[Int] = ArrayBuffer()

					// Enemies
					if (!enemies.isEmpty) {
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
									enemies(i).centerY = -20
									if (!indexes.contains(i)) {
										indexes += i
									}
								}
							}
						}
						indexes = indexes.distinct
						for (i <- 0 until indexes.length) {
							enemies.remove(indexes(i))
							enemiesKilled += 1
						}
					}

					// Bullets
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

					// Player
					if (keys("Right") && (player.centerX.value + player.radius.value < 400)) {
						player.centerX = player.centerX.value + playerSpeed*delta
					}

					if (keys("Left") && (player.centerX.value - player.radius.value > 0)) {
						player.centerX = player.centerX.value - playerSpeed*delta
					}

					// Enemies Spawn
					spawnDelay -= delta
					seconds += delta
					if (spawnDelay < 0) {
						val e = Circle(math.random * 400, 0, 10)
						content(enemiesCounter) = e
						enemiesCounter += 1

						if (enemiesCounter > 32) {
							enemiesCounter = 22
						}

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