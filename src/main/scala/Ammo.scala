import scalafx.Includes._
import scalafx.scene.paint.Color
import scalafx.scene.canvas.GraphicsContext

/** A generalized superclass for all the [[Shootable]] entities. */
abstract class Ammo extends Drawable with Moveable {
	protected val _damage: Double = 5

	/** The damage of the Ammo object */
	def damage: Double = _damage
}

/** A bullet that can be shot from the position it was created. This bullet will move upwards on the game scene.
 *
 *  @constructor create a new bullet with a position
 *  @param playerPos the initial position of the bullet
 */
class Bullet (playerPos: Position) extends Ammo {
	val _position: Position = new Position(playerPos.x, playerPos.y - (Const.size("Player")/2))
	var _speed: Double = Const.speed("Bullet")
	var _size: Double  = Const.size("Bullet")
	val _color: Color = Const.color("Bullet")

	def move = { 
		speed = Const.speed("Bullet")
		size = Const.size("Bullet")
		position.moveUp(speed) 
	}

	def draw(drawer: GraphicsContext): Unit = {
		// Draws at center
		drawer.fill = color
		drawer.fillOval(position.x-size, position.y-size, size*2, size*2)
	}
}

/** A bullet that can be shot from the position it was created. This bullet will move downwards on the game scene.
 *
 *  @constructor create a new bullet with a position
 *  @param startPos the initial position of the bullet
 */
class ShooterBullet (startPos: Position) extends Ammo {
	val _position: Position = new Position(startPos.x, startPos.y + (Const.size("Shooter")/2))
	var _speed: Double = Const.speed("ShooterBullet")
	var _size: Double  = Const.size("ShooterBullet")
	val _color: Color = Const.color("ShooterBullet")

	def move = { 
		speed = Const.speed("ShooterBullet")
		size = Const.size("ShooterBullet")
		position.moveDown(speed) 
	}

	def draw(drawer: GraphicsContext): Unit = {
		// Draws at center
		drawer.fill = color
		drawer.fillOval(position.x-size, position.y-size, size*2, size*2)
	}
}