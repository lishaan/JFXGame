import scala.collection.mutable.ArrayBuffer
import scalafx.Includes._
import scalafx.scene.paint.Color
import scalafx.scene.canvas.GraphicsContext

/** An abstract generalized superclass for all the enemies in the game, that inherits all the needed traits. */
abstract class Enemy extends Drawable with Moveable with Damageable

/** Factory for [[Enemy]] instances. */
object Enemy {
	
	/** Spawns an enemy by the given type of the enemy.
	 *
	 *  @param enemyType the type of the enemy
	 *  @return a new Enemy instance with the type determined by the enemyType.
	 */
	def spawn(enemyType: String): Enemy = {
		if (enemyType.equals("Seeker")) {
			return new Seeker
		} else if (enemyType.equals("Bouncer")) {
			return new Bouncer
		} else if (enemyType.equals("Shooter")) {
			return new Shooter
		} else {
			println(s"Warning: Enemy $enemyType cannot be not found, Seeker spawned instead")
			return new Seeker
		}
	}
}

/** An enemy that moves towards the player. */
class Seeker extends Enemy {
	val _position: Position = new Position(math.random*Global.gameWidth, 0)
	var _speed: Double = Global.speed("Seeker")
	var _size: Double = Global.size("Seeker")
	val _color: Color = Global.color("Seeker")
	val _health: Health = Health(Global.health("Seeker"))

	def move = {
		speed = Global.speed("Seeker")
		size = Global.size("Seeker")

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

/** An enemy that bounces off the edges of the game scene. */
class Bouncer extends Enemy {
	val _position: Position = new Position(Global.gameWidth/2, Global.playAreaHeight/2)
	var _speed: Double = Global.speed("Bouncer")
	var _size: Double = Global.size("Bouncer")
	val _color: Color = Global.color("Bouncer")
	val _health: Health = Health(Global.health("Bouncer"))

	private val _velocity: Velocity = new Velocity(speed)

	def velocity: Velocity = _velocity

	def move = {
		speed = Global.speed("Bouncer")
		size = Global.size("Bouncer")
		position.x = position.x + velocity.x * Global.delta
		position.y = position.y + velocity.y * Global.delta

		if (position.x < 0+size || position.x > Global.gameWidth-size) {
			velocity.x = -velocity.x
		}

		if (position.y < 0+size || position.y > Global.gameHeight-size) {
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

/** An enemy that shoots a bullet at the player. */
class Shooter extends Enemy with Shootable {
	/** The rotation iteration of the Shooter. */
	private var _rotation: Double = 0

	/** The rotation speed of the Shooter. */
	private var _rotationSpeed: Double = Global.speed("Shooter")*0.60

	/** The rotation radius of the Shooter. */
	private var _rotationRadius: Double = size*4

	/** The rotation position of the Shooter. */
	private val _rotationPos: Position = new Position(math.random*Global.gameWidth, Global.playAreaHeight/2)
	val _position: Position = new Position(
		size*math.cos(_rotationRadius)+_rotationPos.x, 
		size*math.sin(_rotationRadius)+_rotationPos.y
	)

	var _speed: Double = Global.speed("Shooter")
	var _size: Double = Global.size("Shooter")
	val _color: Color = Global.color("Shooter")
	val _health: Health = Health(Global.health("Shooter"))

	/** The direction of the Shooter. */
	private var dir: Int = 0

	def shootBullet: Unit = {
		if (Global.seconds.toInt % 2 == 0 && bullets.length < 1) {
			val currentPlayerPos = Global.playerPos
			_bullets +:= new ShooterBullet(this.position)
		}
	}

	def move = {
		speed = Global.speed("Shooter")
		size = Global.size("Shooter")

		position.x = size*math.cos(_rotationRadius)+_rotationPos.x + speed * Global.delta
		position.y = size*math.sin(_rotationRadius)+_rotationPos.y + speed * Global.delta

		_rotationRadius += 0.03
		if (_rotationRadius > (2*math.Pi)) {
			_rotationRadius = 0
		}

		dir match {
			case 0 => _rotationPos.x = _rotationPos.x + speed * Global.delta
			case 1 => _rotationPos.x = _rotationPos.x - speed * Global.delta
			case _ =>
		}

		// Change direction
		if (_rotationPos.x > Global.gameWidth-size) {
			this.dir = 1
		} else if (_rotationPos.x < size) {
			this.dir = 0
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

		// Bullets
		bullets.foreach(b => b.draw(drawer))
	}
}