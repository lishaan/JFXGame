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

		val scaleSlider_label = new Label("Window Width"){
			layoutX = 250
			layoutY = 165
		}

		val scaleSlider = new Slider(400, 800, Const.gameWidth){
			layoutX = 370
			layoutY = 165
		}

		// scaleSlider.setShowTickMarks(true)
 		scaleSlider.setShowTickLabels(true)
		scaleSlider.setMajorTickUnit(0.25f)
		// scaleSlider.setBlockIncrement(0.1f)

		val playButton = new Button("Play"){
			layoutX = 240
			layoutY = 320
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
				Const.gameWidth = scaleSlider.getValue()
				val game: Stage = new Game(playerName)
				game.showAndWait()
			}
		}

		val returnToMenu = new Button("Main Menu"){
			layoutX = 240
			layoutY = 400
			prefWidth = 300
			style = "-fx-font-weight: bold; -fx-background-color: #aabbcc; -fx-background-radius: 50; -fx-font-size: 30; -fx-text-fill: white;"
			onMouseEntered = (e: MouseEvent) => {
				style = "-fx-font-weight: bold; -fx-background-color: #00CCFF; -fx-background-radius: 50; -fx-font-size: 30; -fx-text-fill: white;"
			}

			onMouseExited = (e: MouseEvent) => {
				style = "-fx-font-weight: bold; -fx-background-color: #aabbcc; -fx-background-radius: 50; -fx-font-size: 30; -fx-text-fill: white;"
			}

			onAction = (e: ActionEvent) => closeGame
		}
		content = List(playerInfo, playerName_label, playerName_textField,scaleSlider_label, speedSlider, scaleSlider, gameSpeed_label, gameSetupInfo, playButton, returnToMenu)
	}
}