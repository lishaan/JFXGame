import scalafx.scene.shape.{Shape, Circle}
import scalafx.geometry.Bounds

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
}