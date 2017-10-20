import scalafx.scene.shape.{Shape, Circle}

class Seeker extends Enemy {
	val _shape: Circle = Circle(math.random * 400, 0, 10)

	var health: Health = Health(300, 300)
	val _speed: Double = 80

	def shape = _shape
	def remove = { _shape.centerY = -200 }
	def size = _shape.radius.value
	def x = _shape.centerX.value
	def y = _shape.centerY.value
	def x_=(x: Double) = { _shape.centerX = x }
	def y_=(y: Double) = { _shape.centerY = y }
}