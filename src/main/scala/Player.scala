import scalafx.Includes._
import scalafx.scene.shape.{Circle, Ellipse}
import scalafx.geometry.Bounds
import scalafx.scene.paint.Color

class Player (private val _name: String, color: Color) extends Moveable {

	def this() = this("Player", Const.color("Player"))
    def this(name: String) = this(name, Const.color("Player"))

	private var _kills: Int = 0
	val _speed: Double = Const.playerSpeed

	// val _shape = new Polygon() {
	// 	x = (Const.gameWidth / 2).toInt
	// 	y = Const.gameHeight - 50
	// 	radius = Const.playerSize
	// 	fill = color
	// }

	val _shape = new Ellipse() {
		centerX = (Const.gameWidth / 2).toInt
		centerY = Const.gameHeight - 50
		radiusX = Const.playerSize / 2
		radiusY = Const.playerSize
		fill = color
	}

	def size = _shape.radiusY.value
	def x = _shape.centerX.value
	def y = _shape.centerY.value
	def x_=(x: Double) = { _shape.centerX = x }
	def y_=(y: Double) = { _shape.centerY = y }

	def move = println("Error: Parameters (direction: String, delta: Double) required")
	def move(direction: String, delta: Double) = {
		if (direction.equals("Right") && (_shape.centerX.value + _shape.radiusX.value < Const.gameWidth)) {
			_shape.centerX = _shape.centerX.value + speed * delta
		} else if (direction.equals("Left") && (_shape.centerX.value - _shape.radiusX.value > 0)) {
			_shape.centerX = _shape.centerX.value - speed * delta
		}
	}

	def kills = _kills
	def incrementKills = { _kills = _kills + 1 }
}