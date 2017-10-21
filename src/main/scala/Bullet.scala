import scalafx.scene.shape.Circle
import scalafx.scene.paint.Color

class Bullet (pos: Position) extends Circle {
	val damage: Double = 5
	val speed: Double = 300
	radius = 4
	centerY = pos.y
	centerX = pos.x
	fill = Color.Red

	def remove = { 
		this.centerX = -800
		this.visible = false 
	}

	def move = { centerY = (centerY.value - speed * Global.delta) }

	def r = radius.value
	def x = centerX.value
	def y = centerY.value
	def x_=(x: Double) = { centerX = x }
	def y_=(y: Double) = { centerY = y }
}