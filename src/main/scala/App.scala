import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage

object App extends JFXApp {
	stage = new PrimaryStage {
		title = s"${Game.name} - Main Menu"
		scene = new MainMenu(Const.gameWidth, Const.gameHeight)
		resizable = false
	}
}