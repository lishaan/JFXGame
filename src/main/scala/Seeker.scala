import scalafx.geometry.Bounds
import scalafx.scene.shape.{Shape, Circle}

class Seeker extends Enemy {
	val _shape: Circle = Circle(math.random * 400, 0, 10)

	val _speed: Double = 80

	var _health = Health(10, 10)
	
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