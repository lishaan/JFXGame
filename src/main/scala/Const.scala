import scala.collection.immutable.Map

case class Pair(head: Int, tail: Int)

object Const {
	val sceneWidth: Int = 400
	val sceneHeight: Int = 600

	val playerSpeed: Double = 130
	val playerSize: Int = 20

	val memory = Map(
		"Player"     -> Pair(0, 0),
		"TimerText"  -> Pair(1, 1),
		"Bullets"    -> Pair(2, 22),
		"Enemies"    -> Pair(23, 34)
	)
}