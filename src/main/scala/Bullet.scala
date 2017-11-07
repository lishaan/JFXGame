import scalafx.Includes._
import scalafx.scene.paint.Color
import scalafx.scene.canvas.GraphicsContext

class Bullet (playerPos: Position) extends Drawable with Moveable {
	val _damage: Double = 5

	val _position: Position = new Position(playerPos.x, playerPos.y - (Const.size("Player")/2))
	var _speed: Double = Const.speed("Bullet")
	val _size: Double  = Const.size("Bullet")
	val _color: Color = Const.color("Bullet")

	def move = { 
		speed = Const.speed("Bullet")
		position.moveUp(speed) 
	}

	def draw(drawer: GraphicsContext): Unit = {
		// Draws at center
		drawer.fill = color
		drawer.fillOval(position.x-size, position.y-size, size*2, size*2)
	}

	def damage = _damage
}

class ShooterBullet (startPos: Position) extends Drawable with Moveable {
	val _position: Position = new Position(startPos.x, startPos.y)
	var _speed: Double = Const.speed("ShooterBullet")
	val _size: Double  = Const.size("ShooterBullet")
	val _color: Color = Const.color("ShooterBullet")

	def move = { 
		speed = Const.speed("ShooterBullet")
		position.moveDown(speed) 
	}

	def draw(drawer: GraphicsContext): Unit = {
		// Draws at center
		drawer.fill = color
		drawer.fillOval(position.x-size, position.y-size, size*2, size*2)
	}
}