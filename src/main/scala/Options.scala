import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Bounds
import scalafx.stage.Stage
import scalafx.scene.control.{Button, ListView}
import scalafx.scene.layout.BorderPane
import scalafx.scene.{Node, Scene}
import scalafx.event.ActionEvent
import scalafx.scene.text.Text
import scalafx.scene.control.{Label, TextField, MenuBar, Menu, MenuItem, SeparatorMenuItem, CheckMenuItem, RadioMenuItem, ToggleGroup, Button, TextInputDialog, Slider}
import scalafx.scene.shape.Circle
import scalafx.scene.input.{KeyEvent, KeyCode}
import scalafx.event.ActionEvent
import scalafx.animation.AnimationTimer
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color
import scalafx.collections.ObservableBuffer
import scala.collection.mutable.{ArrayBuffer, Map}

class Options(var playerName: String) extends Stage {

	// def this() = this("Player")
	def closeGame() = this.close()

	title = "JFXGame - Controllers Setup"
	resizable = false

	scene = new Scene(Const.gameWidth, Const.gameHeight) {

		val playerInfo= new Label("Player Information"){
			style = "-fx-font-size: 30"
			layoutX = 15
			layoutY = 20
		}

		val playerName_label = new Label("Name")
		playerName_label.layoutX = 20
		playerName_label.layoutY = 60

		val playerName_textField = new TextField
		playerName_textField.layoutX = 70
		playerName_textField.layoutY = 60
		
		val gameSetupInfo = new Label("Game Setup"){
			style = "-fx-font-size: 30"
			layoutX = 15
			layoutY = 125
		}

		val gameSpeed_label = new Label("Speed"){
			layoutX = 20
			layoutY = 165
		}

		val speedSlider = new Slider(1, 5, 3.2){
			layoutX = 70
			layoutY = 165
		}

		speedSlider.setShowTickMarks(true)
 		speedSlider.setShowTickLabels(true)
		speedSlider.setMajorTickUnit(0.25f)
		speedSlider.setBlockIncrement(0.1f)

		val returnToMenu = new Button("Return to Menu"){
			layoutX = 20
			layoutY = 550

			onAction = (e: ActionEvent) => closeGame
		}

		val playButton = new Button("Play") {
			layoutX = 400
			layoutY = 400
			prefWidth = 300
			style = "-fx-font-weight: bold; -fx-background-color: #aabbcc; -fx-background-radius: 50; -fx-font-size: 30; -fx-text-fill: white;"
			onMouseEntered = (e: MouseEvent) => {
				style = "-fx-font-weight: bold; -fx-background-color: #00CCFF; -fx-background-radius: 50; -fx-font-size: 30; -fx-text-fill: white;"
			}

			onMouseExited = (e: MouseEvent) => {
				style = "-fx-font-weight: bold; -fx-background-color: #aabbcc; -fx-background-radius: 50; -fx-font-size: 30; -fx-text-fill: white;"
			}

			onAction = (e: ActionEvent) => { 
				style = "-fx-font-weight: bold; -fx-background-color: #00CCFF; -fx-background-radius: 50; -fx-font-size: 30; -fx-text-fill: white; -fx-rotate: 10"
				playerName = playerName_textField.getText()
				println(playerName)
				Const.gameSpeed = speedSlider.getValue()
				val game: Stage = new Game(playerName)
				// stage.hide()
				game.showAndWait()
				// stage.show()
			}
		}
		content = List(playerInfo, playerName_label, playerName_textField, speedSlider, gameSpeed_label, gameSetupInfo, returnToMenu, playButton)
	}
}