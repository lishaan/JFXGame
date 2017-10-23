import scalafx.Includes._
import scala.collection.immutable.Map
import scalafx.geometry.Bounds
import scalafx.scene.shape.Shape
import scalafx.scene.paint.Color

case class Pair (head: Int, tail: Int)
case class Health (max: Double) { var current: Double = max }
case class Position (x: Double, y: Double)
case class BufferCounter (start: Int, end: Int) {
	var value: Int = start
	def increment = {
		this.value += 1
		if (this.value > end) this.value = start
	}
}

object Const {
	val gameWidth: Int = 400
	val gameHeight: Int = 600

	val playerSpeed: Double = 130
	val playerSize: Int = 20

	// https://coolors.co/0c0910-cdd1c4-5c80bc-6b2737-1d7874
	val color: Map[String, Color] = Map(
		"Background" -> Color.web("0c0910"),
		"TimerText"  -> Color.web("cdd1c4"),
		"Player"     -> Color.web("E6EFE9"),
		"Bullet"     -> Color.web("FE5F55"),
		"Seeker"     -> Color.web("49306B"),
		"HealthText" -> Color.web("ffffff")
	)

	val memory: Map[String, Pair] = Map(
		"Player"     -> Pair(0, 0),
		"TimerText"  -> Pair(1, 1),
		"Bullets"    -> Pair(2, 22),
		"Enemies"    -> Pair(23, 34),
		"HealthText" -> Pair(35, 46)
	)
}

object Global {
	var playerPos: Position = new Position((Const.gameWidth / 2).toInt, Const.gameHeight - 50)
	var delta: Double = 0
}