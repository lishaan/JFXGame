import scala.collection.immutable.Map
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
		"Background" -> Color.web("051923")
	)

	val buttonStyle: Map[String, String] = Map(
		"Normal" -> "-fx-font-weight: bold; -fx-background-color: #1E3E4D; -fx-background-radius: 50; -fx-font-size: 24; -fx-text-fill: white;",
		"onEntered" -> "-fx-font-weight: bold; -fx-background-color: #4A6978; -fx-background-radius: 50; -fx-font-size: 24; -fx-text-fill: white;",
		"onExited" -> "-fx-font-weight: bold; -fx-background-color: #1E3E4D; -fx-background-radius: 50; -fx-font-size: 24; -fx-text-fill: white;",
		"onAction" -> "-fx-font-weight: bold; -fx-background-color: #4A6978; -fx-background-radius: 50; -fx-font-size: 24; -fx-text-fill: white; -fx-rotate: 5"
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
		prefWidth = 250
		prefHeight = 40
		layoutX = Const.gameWidth/2 - ((250)/2)
		layoutY = centerLayoutY
		style = Scenes.buttonStyle("Normal")

		onMouseEntered = (e: MouseEvent) => style = Scenes.buttonStyle("onEntered")
		onMouseExited = (e: MouseEvent) => style = Scenes.buttonStyle("onExited")
		onAction = (e: ActionEvent) => style = Scenes.buttonStyle("onAction")
	}

	val exitButton = new Button("Exit") {
		prefWidth = 150
		prefHeight = 40
		layoutX = Const.gameWidth/2 - (150/2)
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

	val returnToMenu = new Button("<-") {
		prefWidth = 60
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

	val playerInfo = new Label("Game Setup") {
		prefWidth = 250
		style = "-fx-font: 35 Regular"
		layoutX = Const.gameWidth/2 - (250/2) + 15
		layoutY = 120
	}
	playerInfo.setTextFill(Color.web("#315060"))

	val playerName_label = new Label("Name") {
		prefWidth = 200
		style = "-fx-font: 24 Regular"
		layoutX = Const.gameWidth/2 - (200/2) - 15
		layoutY = 140+pushY
	}
	playerName_label.setTextFill(Color.web("#315060"))

	val playerName_textField = new TextField {
		prefWidth = 100
		layoutX = Const.gameWidth/2 - (100/2) + 15
		layoutY = 140+pushY+1
	}

	val gameSpeedSlider_label = new Label("Game Speed") {
		prefWidth = 160
		style = "-fx-font: 21 Regular;"
		layoutX = Const.gameWidth/2 - (200/2) - 90 + 30
		layoutY = 200+pushY
	}
	gameSpeedSlider_label.setTextFill(Color.web("#315060"))

	val gameScaleSlider_label = new Label("Game Scale") {
		prefWidth = 160
		style = "-fx-font: 21 Regular;"
		layoutX = Const.gameWidth/2 - (200/2) + 90 + 30 + 20
		layoutY = 200+pushY
	}
	gameScaleSlider_label.setTextFill(Color.web("#315060"))

	val gameSpeed_slider = new Slider(1, 2, 1) {
		prefWidth = 180
		layoutX = Const.gameWidth/2 - (180/2) - 100
		layoutY = 240+pushY
		blockIncrement = 0.1
		showTickMarks = true
		showTickLabels = true
		majorTickUnit = 0.5
	}
	gameSpeed_slider.valueProperty.addListener { 
		(o: javafx.beans.value.ObservableValue[_ <: Number], oldVal: Number, newVal: Number) => {
			if (newVal.doubleValue <= 1) {
				if (gameScale_slider.getValue() <= 1) {
					warningText.visible = false
				}
			} else {
				warningText.visible = true
			}
		}
	}

	val gameScale_slider = new Slider(1, 2, 1) {
		prefWidth = 180
		layoutX = Const.gameWidth/2 - (180/2) + 100
		layoutY = 240+pushY
		blockIncrement = 0.1
		showTickMarks = true
		showTickLabels = true
		majorTickUnit = 0.5
	}
	gameScale_slider.valueProperty.addListener { 
		(o: javafx.beans.value.ObservableValue[_ <: Number], oldVal: Number, newVal: Number) => {
			if (newVal.doubleValue <= 1) {
				if (gameSpeed_slider.getValue() <= 1) {
					warningText.visible = false
				}
			} else {
				warningText.visible = true
			}
		}
	}

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
			playerName = playerName_textField.getText()

			if (playerName == null || playerName.trim().isEmpty()) {
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
			val game: Game = new Game(playerName)
			game.showAndWait()

			App.stage.show
		}
	}

	content = List(playerInfo, playerName_label, playerName_textField, gameScaleSlider_label, gameSpeed_slider, gameScale_slider, gameSpeedSlider_label, playButton, returnToMenu, warningText)
}

/*
Menu Bar

val menuBar = new MenuBar{
	useSystemMenuBar = true
	minHeight = 30
	menus.add(new Menu("Tssssd"))
}

menuBar.prefWidth = 500

val fileMenu = new Menu("File")
val openItem = new MenuItem("Open a New Game"){
	accelerator = new KeyCodeCombination(KeyCode.O, KeyCombination.ControlDown)
	onAction = (e: ActionEvent) => { 
		val nameDialogBox = new TextInputDialog(defaultValue = "Lishaan"){
			headerText = "Name"
			contentText = "Please enter your name: "
		}

		val result = nameDialogBox.showAndWait()

		result match {
			case Some(name) => println(s"Your name: $name")
				val game: Stage = new Game(name)
				App.stage.hide()
				game.showAndWait()
				App.stage.show()
			case None => println("Game will start with default name")
		}
	}
}

val saveItem = new MenuItem("HighScore"){
}

val exitItem = new MenuItem("Exit"){
	accelerator = new KeyCodeCombination(KeyCode.Escape)
	onAction = (e: ActionEvent) => sys.exit(0)
}

fileMenu.items = List(openItem, saveItem, new SeparatorMenuItem, exitItem)

val optionsMenu = new Menu("Options")
val controlsItem = new MenuItem("Controls"){
	onAction = (e: ActionEvent) => {
		// val openOptions : Stage = new Options(playerName)
		// parentStage.hide()
		// openOptions.showAndWait()
		// parentStage.show()
	}
}

optionsMenu.items = List(controlsItem)

val helpMenu = new Menu("Help")

val helpAbout = new MenuItem("About"){
	onAction = (e: ActionEvent) => {
		var alert = new Alert(AlertType.Information) {
			// initOwner(this.parentStage)
			title = "Help"
			headerText = "About Us"
			contentText = "About Game: Simple game that permits the user to kill enemies. The Spacebar is used to release bullets and the Right/Left key are used to navigate to right and left respectively. Instructions: \n\n+ Right/Left Key: To move the player to right and left respectively while hitting the bullets to kill the enemies\n+ Space bar: Tap to release bullets by bearing in mind the time taken for the bullet to reach the enemies.\n"
		}
		alert.getDialogPane().setPrefSize(480, 320);
		val results = alert.showAndWait()
	}
	accelerator = new KeyCodeCombination(KeyCode.A, KeyCombination.ControlDown)
}

helpMenu.items = List(helpAbout)
content = menuBar
*/