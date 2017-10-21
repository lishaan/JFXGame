import scala.collection.immutable.Map
import scalafx.geometry.Bounds
import scalafx.scene.shape.Shape

case class Position (x: Double, y: Double)
case class Health (var current: Double, max: Double)
case class BufferCounter (start: Int, end: Int) {
	var value: Int = start
	def increment = {
		this.value += 1
		if (this.value > end) this.value = start
	}
}

case class Pair(head: Int, tail: Int)

trait Moveable {
	protected val _shape: Shape
	protected val _speed: Double

	def shape: Shape = _shape
	def speed: Double = _speed
	def pos: Position = new Position(this.x, this.y)

	def bounds = _shape.boundsInLocal.value
	def intersects(bounds: Bounds): Boolean = _shape.intersects(bounds)
	def remove: Unit = { _shape.visible = false }

	def size: Double
	def x: Double
	def y: Double
	def x_=(x: Double)
	def y_=(y: Double)

	def move: Unit
}

trait Damageable {
	protected var _health: Health

	def dead: Boolean = (_health.current <= 0)
	def alive: Boolean = !dead
	def health: Double = _health.current
	def inflictDamage(damage: Double): Unit = { _health.current = _health.current - damage }
}

object Const {
	val gameWidth: Int = 400
	val gameHeight: Int = 600

	val playerSpeed: Double = 130
	val playerSize: Int = 20

	val memory = Map(
		"Player"     -> Pair(0, 0),
		"TimerText"  -> Pair(1, 1),
		"Bullets"    -> Pair(2, 22),
		"Enemies"    -> Pair(23, 34)
	)
}

object Global {
	var playerPos: Position = new Position((Const.gameWidth / 2).toInt, Const.gameHeight - 50)
	var delta: Double = 0
}