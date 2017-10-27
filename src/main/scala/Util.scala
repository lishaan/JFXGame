import scalafx.Includes._
import scala.collection.mutable.{Map => MMap, ArrayBuffer}
import scala.collection.immutable.Map
import scalafx.scene.paint.Color

case class Health (max: Double) { 
	var current: Double = max 
	def percentage: Double = current / max
}

case class Position (var x: Double, var y: Double) {
	def moveUp    (speed: Double): Unit = { y = y - speed * Global.delta }
	def moveRight (speed: Double): Unit = { x = x + speed * Global.delta }
	def moveDown  (speed: Double): Unit = { y = y + speed * Global.delta }
	def moveLeft  (speed: Double): Unit = { x = x - speed * Global.delta }
}

case class Velocity (var speed: Double) {
	var x: Double = speed
	var y: Double = speed
}

case class Spawner (val enemyName: String, val delay: Double) {
	var counter = delay
	
	def stopped: Boolean = (counter <= 0)
	def reset: Unit = { counter = delay }
	def update: Unit = { counter -= Global.delta }
}

case class Score (name: String, score: Double)

object Const {
	val gameScale: Double = 1.3
	val gameSpeed: Double = 1.0
	val gameWidth: Double = 400
	val gameHeight: Double = 600

	val highscoresFile: String = "highscores.txt"

	val speed: MMap[String, Double] = MMap (
		"Player"  -> Const.gameSpeed * 130,
		"Bullet"  -> Const.gameSpeed * 300,
		"Seeker"  -> Const.gameSpeed * 80,
		"Bouncer" -> Const.gameSpeed * 60
	)

	val size: Map[String, Double] = Map (
		"Player"  -> Const.gameScale * 20,
		"Bullet"  -> Const.gameScale * 2,
		"Seeker"  -> Const.gameScale * 10,
		"Bouncer" -> Const.gameScale * 17
	)

	val color: Map[String, Color] = Map (
		"Background" -> Color.web("0c0910"),
		"TimerText"  -> Color.web("cdd1c4"),
		"Player"     -> Color.web("6B2737"),
		"Bullet"     -> Color.web("FE5F55"),
		"Seeker"     -> Color.web("49306B"),
		"Bouncer"    -> Color.web("E28413")
		/* Link: https://coolors.co/0c0910-cdd1c4-5c80bc-6b2737-1d7874 */
	)

	val health: Map[String, Double] = Map (
		"Seeker"  -> 15,
		"Bouncer" -> 60
	)
}

object Global {
	var playerPos: Position = new Position(Const.gameWidth/2, Const.gameHeight-50)
	var delta: Double = 0

	var spawnDelays: ArrayBuffer[Spawner] = ArrayBuffer (
		Spawner("Seeker" , 2.0), 
		Spawner("Bouncer", 8.0)
	)
}