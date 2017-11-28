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
	val _position: Position = new Position(math.random*Const.gameWidth, 0)
	var _speed: Double = Const.speed("Seeker")
	var _size: Double = Const.size("Seeker")
	val _color: Color = Const.color("Seeker")
	val _health: Health = Health(Const.health("Seeker"))

	def move = {
		speed = Const.speed("Seeker")
		size = Const.size("Seeker")

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
	val _position: Position = new Position(Const.gameWidth/2, Const.playAreaHeight/2)
	var _speed: Double = Const.speed("Bouncer")
	var _size: Double = Const.size("Bouncer")
	val _color: Color = Const.color("Bouncer")
	val _health: Health = Health(Const.health("Bouncer"))

	private val _velocity: Velocity = new Velocity(speed)

	def velocity: Velocity = _velocity

	def move = {
		speed = Const.speed("Bouncer")
		size = Const.size("Bouncer")
		position.x = position.x + velocity.x * Global.delta
		position.y = position.y + velocity.y * Global.delta

		if (position.x < 0+size || position.x > Const.gameWidth-size) {
			velocity.x = -velocity.x
		}

		if (position.y < 0+size || position.y > Const.gameHeight-size) {
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
	private var _rotationSpeed: Double = Const.speed("Shooter")*0.60

	/** The rotation radius of the Shooter. */
	private var _rotationRadius: Double = size*4

	/** The rotation position of the Shooter. */
	private val _rotationPos: Position = new Position(math.random*Const.gameWidth, Const.playAreaHeight/2)
	val _position: Position = new Position(
		size*math.cos(_rotationRadius)+_rotationPos.x, 
		size*math.sin(_rotationRadius)+_rotationPos.y
	)

	var _speed: Double = Const.speed("Shooter")
	var _size: Double = Const.size("Shooter")
	val _color: Color = Const.color("Shooter")
	val _health: Health = Health(Const.health("Shooter"))

	/** The direction of the Shooter. */
	private var dir: Int = 0

	def shootBullet: Unit = {
		if (Global.seconds.toInt % 2 == 0 && bullets.length < 1) {
			val currentPlayerPos = Global.playerPos
			_bullets +:= new ShooterBullet(this.position)
		}
	}

	def move = {
		speed = Const.speed("Shooter")
		size = Const.size("Shooter")

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
		if (_rotationPos.x > Const.gameWidth-size) {
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

/** A trait that defines an entity that can shoot an [[Ammo]] object. */
trait Shootable {
	protected var _bullets: ArrayBuffer[Ammo] = ArrayBuffer()

	/** The ammo array that stores all the ammo currently in the game scene. */
	def bullets: ArrayBuffer[Ammo] = _bullets

	/** Manages the bullets array by removing bullets when they go out of the game scene. */
	def updateBullets: Unit = {

		// Checks if the bullets array is empty or not
		if (!bullets.isEmpty) {
			// Creates a buffer that stores all the indexes of the bullets in the bullets array that has to be removed
			var indexes: ArrayBuffer[Int] = ArrayBuffer()

			// Bullets move
			for (i <- 0 until bullets.length) {
				bullets(i).move

				// If the bullet is out of the game scene, add the index to the indexes buffer
				if (bullets(i).y > Const.gameHeight-bullets(i).size || bullets(i).y < bullets(i).size) {
					if (!indexes.contains(i)) indexes += i
				}
			}

			// Remove all bullets that is out of the game scene
			indexes.foreach(index => bullets.remove(index))
		}
	}

	/** Defines how and when to shoot an Ammo. */
	def shootBullet: Unit
}

/** A trait that defines an entity that can be drawn into the game scene. */
trait Drawable {
	protected val _color: Color

	/** The color of the [[Drawable]] entitiy. */
	def color: Color = _color

	/** Defines how the entity is drawn.
	 *
	 *  @param drawer the GraphicsContext of where the entity gets drawn
	 */
	def draw(drawer: GraphicsContext): Unit
}

/** A trait that defines an entity that can move around the game scene. */
trait Moveable {
	protected val _position: Position
	protected var _speed: Double
	protected var _size: Double

	/** The current position of the Moveable entity */
	def position: Position = _position

	/** The current speed of the Moveable entity */
	def speed: Double = _speed

	/** The current size of the Moveable entity */
	def size: Double = _size
	
	/** The current x coordinate of the Moveable entity */
	def x: Double = _position.x

	/** The current y coordinate of the Moveable entity */
	def y: Double = _position.y

	def speed_=(speed: Double) = { _speed = speed }
	def size_=(size: Double) = { _size = size }

	/** Defines how the entity moves around the game scene. */
	def move: Unit

	/** Removes the entity from the game scene. */
	def remove: Unit = { _position.x = -800 }
}

/** A trait that defines an entity that can be damaged. */
trait Damageable {
	protected val _health: Health

	/** Checks whether the damageable entity is dead. */
	def dead: Boolean = (_health.current <= 0)

	/** Checks whether the damageable entity is alive. */
	def alive: Boolean = !dead

	/** Returns the current health object of the Damageable entity */
	def health: Health = _health

	/** Deducts health by the given damage.
	 *
	 *  @param damage the damage given
	 */
	def inflictDamage(damage: Double): Unit = { _health.current = _health.current - damage }
}