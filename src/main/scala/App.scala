import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.stage.Stage
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.event.ActionEvent

object App extends JFXApp {
	var playerName: String = "Player"

	stage = new PrimaryStage {
		title = "JFXGame - Main Menu"
		scene = new Scene(500, 500) {
			val playButton = new Button("Play") {
				layoutX = 20
				layoutY = 50
			
				onAction = (e: ActionEvent) => { 
					val game: Stage = new Game("Hans")
					stage.hide()
					game.showAndWait()
					stage.show()
				}
			}

			content = List(playButton)
		}
	}
}