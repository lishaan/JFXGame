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
	var x: Double = if (math.random*2 >= 1) speed else -speed
	var y: Double = if (math.random*2 <= 1) speed else -speed
}

case class Spawner (val enemyName: String, val delayHead: Double, val delayTail: Double) {
	val _random: scala.util.Random = new scala.util.Random
	def random: Double = (delayHead+_random.nextInt((delayTail-delayHead).toInt+1))

	var counter: Double = random
	
	def stopped: Boolean = (counter <= 0)
	def reset: Unit = { counter = random }
	def update: Unit = { counter -= Global.delta }
}

case class Score (name: String, score: Double)

object Const {
	val gameScale: Double = 1.2
	var gameSpeed: Double = 1.0 
	val gameWidth: Double = 600
	val gameHeight: Double = 600
	val playAreaHeight: Double = Const.gameHeight/1.5

	val highscoresFile: String = "resources/highscores.txt"

	val SPEED: MMap[String, Double] = MMap (
		"Player"  -> Const.gameSpeed * 170,
		"Bullet"  -> Const.gameSpeed * 400,
		"Seeker"  -> Const.gameSpeed * 80,
		"Bouncer" -> Const.gameSpeed * 120
	)


	val size: Map[String, Double] = Map (
		"Player"  -> Const.gameScale * 20,
		"Bullet"  -> Const.gameScale * 4,
		"Seeker"  -> Const.gameScale * 10,
		"Bouncer" -> Const.gameScale * 17
	)

	val color: Map[String, Color] = Map (
		"Background" -> Color.web("0c0910"),
		"PlayArea"   -> Color.web("302D35"),
		"TimerText"  -> Color.web("cdd1c4"),
		"Player"     -> Color.web("6B2737"),
		"Bullet"     -> Color.web("FE5F55"),
		"Seeker"     -> Color.web("49306B"),
		"Bouncer"    -> Color.web("E28413")
		/* Link: https://coolors.co/0c0910-cdd1c4-5c80bc-6b2737-1d7874 */
	)

	val speed: MMap[String, Double] = MMap (
		"Player"  -> Const.gameSpeed*SPEED("Player" ),
		"Bullet"  -> Const.gameSpeed*SPEED("Bullet" ),
		"Seeker"  -> Const.gameSpeed*SPEED("Seeker" ),
		"Bouncer" -> Const.gameSpeed*SPEED("Bouncer")
	)

	val health: Map[String, Double] = Map (
		"Seeker"  -> 15,
		"Bouncer" -> 60
	)

	def updateSpeeds: Unit = {
		Const.speed("Player" ) = Const.gameSpeed*SPEED("Player" );
		Const.speed("Bullet" ) = Const.gameSpeed*SPEED("Bullet" );
		Const.speed("Seeker" ) = Const.gameSpeed*SPEED("Seeker" );
		Const.speed("Bouncer") = Const.gameSpeed*SPEED("Bouncer");
	}
}

object Global {
	var playerPos: Position = new Position(Const.gameWidth/2, Const.gameHeight-50)
	var delta: Double = 0

	val spawnDelays: ArrayBuffer[Spawner] = ArrayBuffer (
		Spawner("Seeker" , 1.0, 4.0), 
		Spawner("Bouncer", 6.0, 9.0)
	)
}