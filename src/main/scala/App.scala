import scala.collection.mutable.ArrayBuffer
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.stage._
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Label, Dialog, MenuBar, Alert, Menu, MenuItem, SeparatorMenuItem, CheckMenuItem, RadioMenuItem, ToggleGroup, Button, TextInputDialog}
import scalafx.event.{ActionEvent, EventHandler}
import scalafx.scene.input.{KeyCode, KeyCodeCombination, KeyCombination}
import scalafx.scene.input.MouseEvent
import scala.language.implicitConversions

object App extends JFXApp {
	var playerName: String = "Player"

	stage = new PrimaryStage {
		title = "JFXGame - Main Menu"
		scene = new Scene(500, 500) {

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
						initOwner(stage)
						title = "Player Name"
						headerText = "Name"
						contentText = "Please enter your name:- "
					}

					val result = nameDialogBox.showAndWait()

					result match {
						case Some(name) => println(s"Your name: $name")
							val game: Stage = new Game(name)
							stage.hide()
							game.showAndWait()
							stage.show()
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
					val openOptions : Stage = new Options(playerName)
					stage.hide()
					openOptions.showAndWait()
					stage.show()
				}
			}

			optionsMenu.items = List(controlsItem)

			val helpMenu = new Menu("Help")
			
			case class Result(username: String, password: String)
			val helpAbout = new MenuItem("About"){
				onAction = (e: ActionEvent) => {
					var alert = new Alert(AlertType.Information) {
						initOwner(stage)
						title = "Help"
						headerText = "About Us"
						contentText = "About Game: Simple game that permits the user to kill enemies. The Spacebar is used to release bullets and the Right/Left key are used to navigate to right and left respectively. Instructions: \n\n+ Right/Left Key: To move the player to right and left respectively while hitting the bullets to kill the enemies\n+ Space bar: Tap to release bullets by bearing in mind the time taken for the bullet to reach the enemies.\n"
					}
					alert.getDialogPane().setPrefSize(400, 320);
					val results = alert.showAndWait()
				}
				accelerator = new KeyCodeCombination(KeyCode.A, KeyCombination.ControlDown)
			}

			helpMenu.items = List(helpAbout)

			val playButton = new Button("Play") {
				layoutX = 100
				layoutY = 50
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
					
					val game: Stage = new Options(playerName)
					stage.hide()
					game.showAndWait()
					stage.show()
				}
			}

			val highScoreButton = new Button("Highscores"){
				layoutX = 100
				layoutY = 160
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
				}
			}

			val exitButton = new Button("Exit Game"){
				layoutX = 100
				layoutY = 220
				prefWidth = 300
				style = "-fx-font-weight: bold; -fx-background-color: #aabbcc; -fx-background-radius: 50; -fx-font-size: 30; -fx-text-fill: white;"
				onMouseEntered = (e: MouseEvent) => {
					style = "-fx-font-weight: bold; -fx-background-color: #00CCFF; -fx-background-radius: 50; -fx-font-size: 30; -fx-text-fill: white;"
				}

				onMouseExited = (e: MouseEvent) => {
					style = "-fx-font-weight: bold; -fx-background-color: #aabbcc; -fx-background-radius: 50; -fx-font-size: 30; -fx-text-fill: white;"
				}
				onAction = (e: ActionEvent) => sys.exit(0)

			}
			menuBar.menus = List(fileMenu, optionsMenu, helpMenu)
			content = List(playButton, highScoreButton, exitButton, menuBar)
		}
	}
}