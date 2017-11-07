import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.stage.Stage
import scalafx.scene.control.{Button, ListView}
import scalafx.scene.{Node, Scene}
import scalafx.scene.text.Text
import scalafx.scene.input.{KeyEvent, KeyCode}
import scalafx.event.ActionEvent
// import scala.collection.mutable.{ArrayBuffer, Map}

class AboutUs (val playerName: String) extends Stage {

	def this() = this("Player")
	def closeGame() = this.close()

	title = "JFXGame - Play"
	resizable = false

	scene = new Scene(Const.gameWidth, Const.gameHeight) {
		val instructions = new Text{
			text = "About Game: \nSimple game that permits the user to kill enemies. \nThe Spacebar is used to release bullets \nand the Right/Left key are used to navigate\n to right and left respectively. \n\nInstructions: \n\n-> Right/Left Key:\nTo move the player to right and left respectively \nwhile hitting the bullets to kill the enemies\n-> Space bar: Tap to release bullets by bearing\n in mind the time taken for the bullet to\n reach the enemies.\n"
			x = 20
			y = 180
		}

		val aboutDev = new Text{
			text = "Developpers\n1. Lishan Abbas\n2. Jia Yung\n3. David Thingee\n4. Daniel Antonio\n5. Hans Maulloo"
			x = 20
			y = 50
		}

		val returnToMenu = new Button("Return to Menu"){
			layoutX = 20
			layoutY = 550

			onAction = (e: ActionEvent) => closeGame
		}

		val aboutUs2 = new Text{}

		content = List(aboutDev, returnToMenu, instructions)
	}
}