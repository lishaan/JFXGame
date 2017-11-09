import scala.collection.immutable.Map
import scala.collection.mutable.ArrayBuffer
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Bounds
import scalafx.stage.Stage
import scalafx.scene.layout.BorderPane
import scalafx.scene.{Node, Scene}
import scalafx.scene.text.Text
import scalafx.scene.control.{Label, TextField, Dialog, MenuBar, Menu, MenuItem, SeparatorMenuItem, CheckMenuItem, RadioMenuItem, ToggleGroup, Button, TextInputDialog, Slider, ListView}
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color
import scalafx.event.{ActionEvent, EventHandler}
import scalafx.scene.input.{KeyEvent, KeyCode, KeyCodeCombination, KeyCombination}

object Scenes {
	val color: Map[String, Color] = Map(
		"Background" -> Color.web("000D0D")
	)

	val buttonStyle: Map[String, String] = Map(
		"Normal" -> "-fx-font-weight: bold; -fx-background-color: #004E52; -fx-background-radius: 50; -fx-font-size: 24; -fx-text-fill: #44f9ff;",
		"onEntered" -> "-fx-font-weight: bold; -fx-background-color: #003133; -fx-background-radius: 50; -fx-font-size: 24; -fx-text-fill: #44f9ff;",
		"onExited" -> "-fx-font-weight: bold; -fx-background-color: #004E52; -fx-background-radius: 50; -fx-font-size: 24; -fx-text-fill: #44f9ff;",
		"onAction" -> "-fx-font-weight: bold; -fx-background-color: #003133; -fx-background-radius: 50; -fx-font-size: 24; -fx-text-fill: #44f9ff; -fx-rotate: 5"
	)
}

class MainMenu (_width: Double, _height: Double) extends Scene (_width, _height) {

	def this() = this(Const.gameWidth, Const.gameHeight)

	fill = Scenes.color("Background")

	val centerLayoutY = Const.gameHeight/2
	val layoutYSpacing = 60

	val playButton = new Button("New Game") {
		prefWidth = 200
		prefHeight = 40
		layoutX = Const.gameWidth/2 - (200/2)
		layoutY = centerLayoutY - layoutYSpacing
		style = Scenes.buttonStyle("Normal")

		onMouseEntered = (e: MouseEvent) => style = Scenes.buttonStyle("onEntered")
		onMouseExited = (e: MouseEvent) => style = Scenes.buttonStyle("onExited")
		onAction = (e: ActionEvent) => {
			style = Scenes.buttonStyle("onAction")
			App.stage.title = "JFXGame - Game Setup"
			App.stage.scene = new GameSetup
		}
	}

	val highScoreButton = new Button("Highscores") {
		prefWidth = 200
		prefHeight = 40
		layoutX = Const.gameWidth/2 - ((200)/2)
		layoutY = centerLayoutY
		style = Scenes.buttonStyle("Normal")

		onMouseEntered = (e: MouseEvent) => style = Scenes.buttonStyle("onEntered")
		onMouseExited = (e: MouseEvent) => style = Scenes.buttonStyle("onExited")
		onAction = (e: ActionEvent) => {
			style = Scenes.buttonStyle("onAction")
			App.stage.title = "JFXGame - Highscores"
			App.stage.scene = new Highscores
		}
	}

	val exitButton = new Button("Exit") {
		prefWidth = 200
		prefHeight = 40
		layoutX = Const.gameWidth/2 - (200/2)
		layoutY = centerLayoutY + layoutYSpacing
		style = Scenes.buttonStyle("Normal")

		onMouseEntered = (e: MouseEvent) => style = Scenes.buttonStyle("onEntered")
		onMouseExited = (e: MouseEvent) => style = Scenes.buttonStyle("onExited")
		onAction = (e: ActionEvent) => sys.exit(0)
	}
	content = List(playButton, highScoreButton, exitButton)
}

class GameSetup (_width: Double, _height: Double) extends Scene (_width, _height) {

	def this() = this(Const.gameWidth, Const.gameHeight)

	fill = Scenes.color("Background")

	var playerName = "Player"

	val returnToMenu = new Button("«") {
		prefWidth = 40
		layoutX = 20
		layoutY = 20
		style = Scenes.buttonStyle("Normal")
		onMouseEntered = (e: MouseEvent) => style = Scenes.buttonStyle("onEntered")
		onMouseExited = (e: MouseEvent) => style = Scenes.buttonStyle("onExited")

		onAction = (e: ActionEvent) => {
			App.stage.title = "JFXGame - Main Menu"
			App.stage.scene = new MainMenu
		}
	}

	val pushY = 100

	val headerText = new Label("Game Setup") {
		prefWidth = 250
		style = "-fx-font: 35 Regular"
		layoutX = Const.gameWidth/2 - (250/2) + 15 + 4
		layoutY = 120
	}
	headerText.setTextFill(Color.web("#44f9ff"))

	val playerName_label = new Label("Name") {
		prefWidth = 200
		style = "-fx-font: 24 Regular"
		layoutX = Const.gameWidth/2 - (200/2) - 15 + 20
		layoutY = 140+pushY+0 - 10
	}
	playerName_label.setTextFill(Color.web("#00BCC5"))

	val playerName_textField = new TextField {
		prefWidth = 100
		layoutX = Const.gameWidth/2 - (100/2) + 15 + 20
		layoutY = 140+pushY+1 - 10
	}
	playerName_textField.textProperty.addListener((o, oldVal, newVal) => {
		if (playerName_textField.getText == null || playerName_textField.getText.trim.isEmpty) {
			playerNameWarningText.visible = true
		} else {
			playerNameWarningText.visible = false
		}
	})

	val playerNameWarningText = new Label("The default player name is \"Player\"") {
		prefWidth = 300
		style = "-fx-font: 14 Regular; -fx-text-alignment: center;"
		layoutX = Const.gameWidth/2 - (300/2)+32
		layoutY = 140+pushY+32.5
	}
	playerNameWarningText.setTextFill(Color.web("#315060"))

	val gameSpeedSlider_label = new Label("Game Speed") {
		prefWidth = 160
		style = "-fx-font: 21 Regular;"
		layoutX = Const.gameWidth/2 - (200/2) - 90 + 30
		layoutY = 200+pushY
	}
	gameSpeedSlider_label.setTextFill(Color.web("#00BCC5"))

	val gameScaleSlider_label = new Label("Game Scale") {
		prefWidth = 160
		style = "-fx-font: 21 Regular;"
		layoutX = Const.gameWidth/2 - (200/2) + 90 + 30 + 20
		layoutY = 200+pushY
	}
	gameScaleSlider_label.setTextFill(Color.web("#00BCC5"))

	val gameSpeed_slider = new Slider(1, 2, 1) {
		prefWidth = 180
		layoutX = Const.gameWidth/2 - (180/2) - 100
		layoutY = 240+pushY
		blockIncrement = 0.1
		showTickMarks = true
		showTickLabels = true
		majorTickUnit = 0.5
	}
	gameSpeed_slider.valueProperty.addListener( 
		(o, oldVal, newVal) => {
			if (newVal.doubleValue <= 1) {
				if (gameScale_slider.getValue() <= 1) {
					warningText.visible = false
				}
			} else {
				warningText.visible = true
			}
		}
	)

	val gameScale_slider = new Slider(1, 2, 1) {
		prefWidth = 180
		layoutX = Const.gameWidth/2 - (180/2) + 100
		layoutY = 240+pushY
		blockIncrement = 0.1
		showTickMarks = true
		showTickLabels = true
		majorTickUnit = 0.5
	}
	gameScale_slider.valueProperty.addListener(
		(o, oldVal, newVal) => {
			if (newVal.doubleValue <= 1) {
				if (gameSpeed_slider.getValue() <= 1) {
					warningText.visible = false
				}
			} else {
				warningText.visible = true
			}
		}
	)

	val warningText = new Label("If you change the game speed/scale\nthe score won't append to the highscores") {
		visible = false
		prefWidth = 300
		style = "-fx-font: 14 Regular; -fx-text-alignment: center;"
		layoutX = Const.gameWidth/2 - 150+8
		layoutY = 280+pushY
	}
	warningText.setTextFill(Color.web("#315060"))

	val playButton = new Button("Play") {
		prefWidth = 150
		layoutX = Const.gameWidth/2 - (150/2)
		layoutY = 320+pushY+6
		style = Scenes.buttonStyle("Normal")
		onMouseEntered = (e: MouseEvent) => style = Scenes.buttonStyle("onEntered")
		onMouseExited = (e: MouseEvent) => style = Scenes.buttonStyle("onExited")

		onAction = (e: ActionEvent) => { 
			style = Scenes.buttonStyle("onAction")
			playerName = playerName_textField.getText

			if (playerName == null || playerName.trim.isEmpty) {
				playerName = "Player"
			}

			if (warningText.visible.value) {
				Const.appendToHighscoresFile = false
			} else {
				Const.appendToHighscoresFile = true
			}

			Const.gameSpeed = gameSpeed_slider.getValue()
			Const.gameScale = gameScale_slider.getValue()

			App.stage.hide
			App.stage.scene = new MainMenu
			App.stage.title = "JFXGame - Main Menu"

			println(s"Game starting with playerName: $playerName")
			do {
				val game: Game = new Game(playerName)
				game.showAndWait()
			} while (Game.retry)

			App.stage.show
		}
	}

	content = List(headerText, playerName_label, playerName_textField, gameScaleSlider_label, gameSpeed_slider, gameScale_slider, gameSpeedSlider_label, playButton, returnToMenu, warningText, playerNameWarningText)
}

class Highscores (_width: Double, _height: Double) extends Scene (_width, _height) {

	def this() = this(Const.gameWidth, Const.gameHeight)

	fill = Scenes.color("Background")

	val returnToMenu = new Button("«") {
		prefWidth = 40
		layoutX = 20
		layoutY = 20
		style = Scenes.buttonStyle("Normal")
		onMouseEntered = (e: MouseEvent) => style = Scenes.buttonStyle("onEntered")
		onMouseExited = (e: MouseEvent) => style = Scenes.buttonStyle("onExited")

		onAction = (e: ActionEvent) => {
			App.stage.title = "JFXGame - Main Menu"
			App.stage.scene = new MainMenu
		}
	}

	val pushY = 100

	val headerText = new Label("Highscores") {
		prefWidth = 250
		style = "-fx-font: 35 Regular"
		layoutX = Const.gameWidth/2 - (250/2) + 29
		layoutY = 120
	}
	headerText.setTextFill(Color.web("#44f9ff"))

	val highscores: List[Score] = Util.getHighscores(Const.highscoresFile)

	var scoreNodes: ArrayBuffer[Node] = ArrayBuffer()

	val nameCol_label = new Label("Name") {
		prefWidth = 250
		style = "-fx-font: 17 Regular"
		layoutX = Const.gameWidth/2 - (250/2) - 26 + 20
		layoutY = 180
	}
	nameCol_label.setTextFill(Color.web("#00BCC5"))

	val scoreCol_label = new Label("Score") {
		prefWidth = 250
		style = "-fx-font: 17 Regular"
		layoutX = Const.gameWidth/2 + (250/2) - 36 - 20
		layoutY = 180
	}
	scoreCol_label.setTextFill(Color.web("#00BCC5"))

	var gap: Int = 0
	var count: Int = 1

	for (score <- highscores) {
		val name_label = new Label(s"$count. ${score.name.toString}") {
			prefWidth = 250
			style = "-fx-font: 17 Regular"
			layoutX = Const.gameWidth/2 - (250/2) - 26 + 20
			layoutY = 210+gap
		}
		name_label.setTextFill(Color.web("#00BCC5"))

		val score_label = new Label(score.score.toString) {
			prefWidth = 250
			style = "-fx-font: 17 Regular"
			layoutX = Const.gameWidth/2 + (250/2) - 36 - 20
			layoutY = 210+gap
		}
		score_label.setTextFill(Color.web("#00BCC5"))

		scoreNodes += name_label
		scoreNodes += score_label
		gap += 30
		count += 1
	}

	val resetButton = new Button("Reset") {
		prefWidth = 150
		layoutX = Const.gameWidth/2 - (150/2)
		layoutY = 210+gap+18//320+pushY+6
		style = Scenes.buttonStyle("Normal")
		onMouseEntered = (e: MouseEvent) => style = Scenes.buttonStyle("onEntered")
		onMouseExited = (e: MouseEvent) => style = Scenes.buttonStyle("onExited")

		onAction = (e: ActionEvent) => { 
			Util.clearHighscores(Const.highscoresFile)
			App.stage.title = "JFXGame - Main Menu"
			App.stage.scene = new MainMenu
		}
	}

	content = List(returnToMenu, headerText, resetButton) ++ scoreNodes.toList ++ List(nameCol_label, scoreCol_label)
}