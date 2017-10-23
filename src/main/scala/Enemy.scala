import scalafx.Includes._
import scalafx.scene.text.{Text, Font}
import scalafx.scene.shape.{Shape, Circle}
import scalafx.geometry.Bounds

class HealthText (health: Health, spawnPos: Position) extends Text {
	x = spawnPos.x - 10
	y = spawnPos.y + 30
	text = (((health.current / health.max) * 100.00).toInt).toString
	fill = Const.color("HealthText")
	// style = "-fx-font: normal 7pt sans-serif"

	def remove: Unit = { 
		this.x = -800
		this.visible = false 
	}
	def update(health: Health, pos: Position) {
		this.x = pos.x - 10
		this.y = pos.y + 30
		this.text = (((health.current / health.max) * 100.00).toInt).toString
	}
}

abstract class Enemy extends Moveable with Damageable

object Enemy {
	def spawn(enemyType: String): Enemy = {
		if (enemyType.equals("Seeker")) {
			return (new Seeker())
		} else {
			println(s"Error: Enemy $enemyType cannot be not found, Seeker:Enemy spawned instead")
			return (new Seeker())
		}
	}

	def drawHealth(health: Health, pos: Position): HealthText = {
		new HealthText(health, pos)
	}
}

class Seeker extends Enemy {
	val _shape: Circle = new Circle() {
		centerX = math.random * 400
		centerY = 0
		radius = 10
		fill = Const.color("Seeker")
	}

	val _speed: Double = 80

	val _health = Health(10)
	
	def size = _shape.radius.value
	def x = _shape.centerX.value
	def y = _shape.centerY.value
	def x_=(x: Double) = { _shape.centerX = x }
	def y_=(y: Double) = { _shape.centerY = y }

	def move = move(Global.playerPos, Global.delta)
	def move(playerPos: Position, delta: Double) = {
		val dx = playerPos.x - this.x
		val dy = playerPos.y - this.y

		val dist = math.sqrt(dx*dx + dy*dy)

		this.x = this.x + dx / dist * this._speed * delta
		this.y = this.y + dy / dist * this._speed * delta
	}
}

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
	protected val _health: Health

	def dead: Boolean = (_health.current <= 0)
	def alive: Boolean = !dead
	def health: Double = _health.current
	def healthObject: Health = _health
	def inflictDamage(damage: Double): Unit = { _health.current = _health.current - damage }
}