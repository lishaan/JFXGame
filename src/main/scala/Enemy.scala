import scalafx.scene.shape.Circle

abstract class Enemy {
	protected var health: Health
	protected val _speed: Double

	def currentHealth = health.current
	def maxHealth = health.max
	def speed = _speed

	def remove: Unit
	def size: Double
	def x: Double
	def y: Double
	def x_=(x: Double): Unit 
	def y_=(y: Double): Unit 
}