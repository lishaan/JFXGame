import scalafx.scene.shape.Circle
import scalafx.scene.paint.Color

class Player (private val _name: String, color: Color) {

    def this(name: String) = this(name, Color.Blue)

	private var _kills: Int = 0
	private var speed: Double = Const.playerSpeed

	private var _shape = new Circle() {
		centerY = Const.sceneHeight - 50
		centerX = (Const.sceneWidth / 2).toInt
		radius = Const.playerSize
		fill = color
	}

	def move(direction: String, delta: Double): Unit = {
		if (direction.equals("Right") && (_shape.centerX.value + _shape.radius.value < Const.sceneWidth)) {
			_shape.centerX = _shape.centerX.value + speed * delta
		} else if (direction.equals("Left") && (_shape.centerX.value - _shape.radius.value > 0)) {
			_shape.centerX = _shape.centerX.value - speed * delta
		}
	}

	def x = _shape.centerX.value
	def y = _shape.centerY.value
	def r = _shape.radius.value
	def name = _name
	def shape = _shape
	def kills = _kills
	def incrementKills = { _kills += 1 }
}