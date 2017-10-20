import scalafx.scene.shape.Circle
import scalafx.scene.paint.Color

class Bullet (pos: Position) extends Circle {
	radius = 4
	centerY = pos.y
	centerX = pos.x
	fill = Color.Red

	def r = radius.value
	def x = centerX.value
	def y = centerY.value
	def x_=(x: Double) = { centerX = x }
	def y_=(y: Double) = { centerY = y }
}