import scala.collection.mutable.ArrayBuffer
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.stage.Stage
import scalafx.scene.Scene
import scalafx.scene.control.{Label, MenuBar, Menu, MenuItem, SeparatorMenuItem, CheckMenuItem, RadioMenuItem, ToggleGroup, Button}
import scalafx.event.ActionEvent
import scalafx.scene.input.{KeyCode, KeyCodeCombination, KeyCombination}


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
					val game: Stage = new Game("Jia Yung")
					// val game: Stage = new Game("Hans")
					stage.hide()
					game.showAndWait()
					stage.show()
				}
			}

			// openItem.onAction 

			val saveItem = new MenuItem("HighScore"){
			}

			val exitItem = new MenuItem("Exit"){
				accelerator = new KeyCodeCombination(KeyCode.Escape)
				onAction = (e: ActionEvent) => sys.exit(0)
			}
			
			fileMenu.items = List(openItem, saveItem, new SeparatorMenuItem, exitItem)

			val helpMenu = new Menu("Help")
			
			val helpAbout = new MenuItem("About"){
				onAction = (e: ActionEvent) => {
					// val openAbout: Stage = new AboutUs("Hans")
					// stage.hide()
					// openAbout.showAndWait()
					// stage.show()
				}
				accelerator = new KeyCodeCombination(KeyCode.A, KeyCombination.ControlDown)
			}

			helpMenu.items = List(helpAbout)

			val playButton = new Button("Play") {
				layoutX = 100
				layoutY = 50
				prefWidth = 250
				style = "-fx-font-size: 50; -fx-background-color: None; -fx-border-width: 2px; -fx-border-style: solid; -fx-border-color: red; -fx-border-radius: 45px"

				onAction = (e: ActionEvent) => { 
					val game: Stage = new Game("Hans")
					stage.hide()
					game.showAndWait()
					stage.show()
				}
			}

			val highScoreButton = new Button("Highscores"){
				layoutX = 125
				layoutY = 160
				prefWidth = 200
				style = "-fx-font-size: 25; -fx-background-color: None; -fx-border-width: 2px; -fx-border-style: solid; -fx-border-color: red; -fx-border-radius: 25px"
			}

			val exitButton = new Button("Exit Game"){
				layoutX = 125
				layoutY = 220
				prefWidth = 200
				style = "-fx-font-size: 25; -fx-background-color: None; -fx-border-width: 2px; -fx-border-style: solid; -fx-border-color: red; -fx-border-radius: 25px"

				onAction = (e: ActionEvent) => sys.exit(0)

			}

			menuBar.menus = List(fileMenu, helpMenu)
			content = List(playButton, highScoreButton, exitButton, menuBar)
		}
	}
}