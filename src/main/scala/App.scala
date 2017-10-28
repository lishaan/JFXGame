import scala.collection.mutable.ArrayBuffer
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
					val game: Stage = new Game("Jia Yung")
					stage.hide()
					game.showAndWait()
					stage.show()
				}
			}

			content = List(playButton)
		}
	}

	def getHighscores(file: String): ArrayBuffer[Score] = {
		var scores: ArrayBuffer[Score] = ArrayBuffer()
		try {
			val fScanner = new java.util.Scanner(new java.io.File(file))
			val firstLine = fScanner.nextLine
			while (fScanner.hasNextLine) {
				var name: String = ""
				var score: Double = 0.0

				while (!fScanner.hasNextDouble) {
					name += fScanner.next
					if (!fScanner.hasNextDouble) name += " "
				}
				score = fScanner.nextDouble

				scores += new Score(name, score)
			}
			fScanner.close
		} catch {
			case _: Throwable => println("Error: Highscore file not found (READ)")
		}

		return scores
	}

	def appendScore(file: String, score: Score): Unit = {
		try {
			val printWriter = new java.io.PrintWriter(new java.io.FileOutputStream(new java.io.File(file), true))
			printWriter.write(s"\n${score.name} ${"%.2f".format(score.score)}")
			printWriter.close
		} catch {
			case _: Throwable => println("Error: Highscore file not found (WRITE)")
		}
	}
}