import scalafx.Includes._
import scalafx.scene.paint.Color
import scalafx.scene.canvas.GraphicsContext

abstract class Enemy extends Drawable with Moveable with Damageable
object Enemy {
	def spawn(enemyType: String): Enemy = {
		if (enemyType.equals("Seeker")) {
			return new Seeker
		} else if (enemyType.equals("Bouncer")) {
			return new Bouncer
		} else {
			println(s"Warning: Enemy $enemyType cannot be not found, Seeker spawned instead")
			return new Seeker
		}
	}
}

class Seeker extends Enemy {
	val _position: Position = new Position(math.random*Const.gameWidth, 0)
	var _speed: Double = Const.speed("Seeker")
	val _size: Double = Const.size("Seeker")
	val _color: Color = Const.color("Seeker")
	val _health: Health = Health(Const.health("Seeker"))

	def move = {
		speed = Const.speed("Seeker")
		val playerPos: Position = Global.playerPos

		val dx: Double = playerPos.x-position.x
		val dy: Double = playerPos.y-position.y

		val dist: Double = math.sqrt(dx*dx + dy*dy)

		position.x = position.x + dx / dist * speed * Global.delta
		position.y = position.y + dy / dist * speed * Global.delta
	}

	def draw(drawer: GraphicsContext): Unit = {
		// Draws at center
		drawer.fill = color
		drawer.fillOval(position.x-size, position.y-size, size*2, size*2)

		// Health Bar
		val x = position.x-size
		val y = position.y+size+(size/2)
		val w = size*2
		val h = size/4

		drawer.fill = Color.Blue
		drawer.fillRect(x, y, w, h)

		drawer.fill = Color.Red
		drawer.fillRect(x, y, w*health.percentage, h)
	}
}

class Bouncer extends Enemy {
	val _position: Position = new Position(math.random*Const.gameWidth, 0)
	var _speed: Double = Const.speed("Bouncer")
	val _size: Double = Const.size("Bouncer")
	val _color: Color = Const.color("Bouncer")
	val _health: Health = Health(Const.health("Bouncer"))

	val _velocity: Velocity = new Velocity(speed)

	def velocity: Velocity = _velocity

	def move = {
		speed = Const.size("Bouncer")
		position.x = position.x + velocity.x * Global.delta
		position.y = position.y + velocity.y * Global.delta

		if (position.x < 0 || position.x > Const.gameWidth) {
			velocity.x = -velocity.x
		}

		if (position.y < 0 || position.y > Const.gameHeight) {
			velocity.y = -velocity.y
		}
	}

	def draw(drawer: GraphicsContext): Unit = {
		// Draws at center
		drawer.fill = color
		drawer.fillOval(position.x-size, position.y-size, size*2, size*2)

		// Health Bar
		val x = position.x-size
		val y = position.y+size+(size/2)
		val w = size*2
		val h = size/4

		drawer.fill = Color.Blue
		drawer.fillRect(x, y, w, h)

		drawer.fill = Color.Red
		drawer.fillRect(x, y, w*health.percentage, h)
	}
}

// class Multiplier extends Enemy {
// 	val _position: Position = new Position(math.random * 400, math.random * 200)
// 	var _speed: Double = Const.speed("Bouncer")
// 	val _size: Double = Const.size("Bouncer")
// 	val _color: Color = Const.color("Bouncer")
// 	val _health: Health = Health(Const.health("Bouncer"))

// 	val _velocity: Velocity = new Velocity(speed)

// 	def velocity: Velocity = _velocity

// 	def move = {
// 		speed = Const.size("Bouncer")
// 		position.x = position.x + velocity.x * Global.delta
// 		position.y = position.y + velocity.y * Global.delta

// 		if (position.x < 0 || position.x > Const.gameWidth) {
// 			velocity.x = -velocity.x
// 		}

// 		if (position.y < 0 || position.y > Const.gameHeight) {
// 			velocity.y = -velocity.y
// 		}
// 	}

// 	def draw(drawer: GraphicsContext): Unit = {
// 		// Draws at center
// 		drawer.fill = color
// 		drawer.fillOval(position.x-size, position.y-size, size*2, size*2)

// 		// Health Bar
// 		val x = position.x-size
// 		val y = position.y+size+(size/2)
// 		val w = size*2
// 		val h = size/4

// 		drawer.fill = Color.Blue
// 		drawer.fillRect(x, y, w, h)

// 		drawer.fill = Color.Red
// 		drawer.fillRect(x, y, w*health.percentage, h)
// 	}
// }

trait Drawable {
	val _color: Color
	def color: Color = _color

	def draw(drawer: GraphicsContext): Unit
}

trait Moveable {
	val _position: Position
	var _speed: Double
	val _size: Double
	
	def position: Position = _position
	def speed: Double = _speed
	def size: Double = _size
	
	def x: Double = _position.x
	def y: Double = _position.y

	def speed_=(speed: Double) = { _speed = speed }

	def move: Unit
	def remove: Unit = { _position.x = -800 }
}

trait Damageable {
	val _health: Health

	def dead: Boolean = (_health.current <= 0)
	def alive: Boolean = !dead
	def health: Health = _health
	def inflictDamage(damage: Double): Unit = { _health.current = _health.current - damage }
}