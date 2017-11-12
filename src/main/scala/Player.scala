import scalafx.Includes._
import scalafx.scene.paint.Color
import scalafx.scene.canvas.GraphicsContext

class Player (playerName: String) extends Drawable with Moveable {

    def this() = this("Player")

    val _name: String = playerName
	var _kills: Int = 0

	val _position: Position = new Position(Const.gameWidth/2, Const.gameHeight-50)
	var _speed: Double = Const.speed("Player")
	var _size: Double = Const.size("Player")
	val _color: Color = Const.color("Player")

	def move = println("Error: Parameter (direction: String) required")
	def move(direction: String): Unit = {
		speed = Const.speed("Player")
		size = Const.size("Player")

		if (direction.equals("Up") && (position.y-size > Const.playAreaHeight)) {
			position.moveUp(speed)
		} else if (direction.equals("Right") && (position.x+size < Const.gameWidth)) {
			position.moveRight(speed)
		} else if (direction.equals("Down") && (position.y+size < Const.gameHeight)) {
			position.moveDown(speed)
		} else if (direction.equals("Left") && (position.x-size > 0)) {
			position.moveLeft(speed)
		}
	}

	def draw(drawer: GraphicsContext): Unit = {
		// Draws at center
		drawer.fill = color
		drawer.fillOval(position.x-size, position.y-size, size*2, size*2)
	}

	def name = _name
	def kills = _kills
	def incrementKills = { _kills = _kills + 1 }
}