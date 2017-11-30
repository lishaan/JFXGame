import scala.collection.immutable.Map
import scala.collection.mutable.ArrayBuffer
import scalafx.Includes._
import scalafx.scene.{Node, Scene}
import scalafx.scene.control.{Label, TextField, Button, Slider}
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color
import scalafx.event.ActionEvent

/** A static Scenes object that stores all the consistent values for the scenes in the Main Menu. */
object Scenes {
	/** The colors of the components in the Main Menu */
	val color: Map[String, Color] = Map(
		"Background" -> Color.web("000D0D")
	)

	/** The fx css stylings of the buttons in the Main Menu */
	val buttonStyle: Map[String, String] = Map(
		"Normal" -> "-fx-font-weight: bold; -fx-background-color: #004E52; -fx-background-radius: 50; -fx-font-size: 24; -fx-text-fill: #44f9ff;",
		"onEntered" -> "-fx-font-weight: bold; -fx-background-color: #003133; -fx-background-radius: 50; -fx-font-size: 24; -fx-text-fill: #44f9ff;",
		"onExited" -> "-fx-font-weight: bold; -fx-background-color: #004E52; -fx-background-radius: 50; -fx-font-size: 24; -fx-text-fill: #44f9ff;",
		"onAction" -> "-fx-font-weight: bold; -fx-background-color: #003133; -fx-background-radius: 50; -fx-font-size: 24; -fx-text-fill: #44f9ff; -fx-rotate: 5"
	)
}

/** The main menu scene of the game.
 *
 *  @constructor create a new instance of a MainMenu scene
 *  @param width the width of the scene
 *  @param height the height of the scene
 */
class MainMenu (_width: Double, _height: Double) extends Scene (_width, _height) {

	def this() = this(Global.gameWidth, Global.gameHeight)

	fill = Scenes.color("Background")

	val centerLayoutY = Global.gameHeight/2
	val layoutYSpacing = 60
	
	val headerText = new Label(s"${Game.name}") {
		prefWidth = 350
		style = "-fx-font: 41 Regular"
		layoutX = Global.gameWidth/2 - (350/2) + 4
		layoutY = 100
	}
	headerText.setTextFill(Color.web("#44f9ff"))

	val playButton = new Button("New Game") {
		prefWidth = 200
		prefHeight = 40
		layoutX = Global.gameWidth/2 - (200/2)
		layoutY = centerLayoutY - layoutYSpacing
		style = Scenes.buttonStyle("Normal")

		onMouseEntered = (e: MouseEvent) => style = Scenes.buttonStyle("onEntered")
		onMouseExited = (e: MouseEvent) => style = Scenes.buttonStyle("onExited")
		onAction = (e: ActionEvent) => {
			style = Scenes.buttonStyle("onAction")
			App.stage.title = s"${Game.name} - Game Setup"
			App.stage.scene = new GameSetup
		}
	}

	val highScoreButton = new Button("Highscores") {
		prefWidth = 200
		prefHeight = 40
		layoutX = Global.gameWidth/2 - ((200)/2)
		layoutY = centerLayoutY
		style = Scenes.buttonStyle("Normal")

		onMouseEntered = (e: MouseEvent) => style = Scenes.buttonStyle("onEntered")
		onMouseExited = (e: MouseEvent) => style = Scenes.buttonStyle("onExited")
		onAction = (e: ActionEvent) => {
			style = Scenes.buttonStyle("onAction")
			App.stage.title = s"${Game.name} - Highscores"
			App.stage.scene = new Highscores
		}
	}

	val exitButton = new Button("Exit") {
		prefWidth = 200
		prefHeight = 40
		layoutX = Global.gameWidth/2 - (200/2)
		layoutY = centerLayoutY + layoutYSpacing
		style = Scenes.buttonStyle("Normal")

		onMouseEntered = (e: MouseEvent) => style = Scenes.buttonStyle("onEntered")
		onMouseExited = (e: MouseEvent) => style = Scenes.buttonStyle("onExited")
		onAction = (e: ActionEvent) => sys.exit(0)
	}

	val aboutButton = new Button("?") {
		prefWidth = 40
		prefHeight = 40
		layoutX = Global.gameWidth - 60
		layoutY = Global.gameHeight - 60
		style = Scenes.buttonStyle("Normal")

		onMouseEntered = (e: MouseEvent) => style = Scenes.buttonStyle("onEntered")
		onMouseExited = (e: MouseEvent) => style = Scenes.buttonStyle("onExited")
		onAction = (e: ActionEvent) => {
			style = Scenes.buttonStyle("onAction")
			App.stage.title = s"${Game.name} - About"
			App.stage.scene = new About
		}
	}
	content = List(headerText, playButton, highScoreButton, exitButton, aboutButton)
}

/** The game setup scene of the game.
 *
 *  @constructor create a new instance of a GameSetup scene
 *  @param width the width of the scene
 *  @param height the height of the scene
 */
class GameSetup (_width: Double, _height: Double) extends Scene (_width, _height) {

	def this() = this(Global.gameWidth, Global.gameHeight)

	fill = Scenes.color("Background")

	var playerName = "Player"

	val back = new Button("«") {
		prefWidth = 40
		layoutX = 20
		layoutY = 20
		style = Scenes.buttonStyle("Normal")
		onMouseEntered = (e: MouseEvent) => style = Scenes.buttonStyle("onEntered")
		onMouseExited = (e: MouseEvent) => style = Scenes.buttonStyle("onExited")

		onAction = (e: ActionEvent) => {
			App.stage.title = s"${Game.name} - Main Menu"
			App.stage.scene = new MainMenu
		}
	}

	val pushY = 100

	val headerText = new Label("Game Setup") {
		prefWidth = 250
		style = "-fx-font: 35 Regular"
		layoutX = Global.gameWidth/2 - (250/2) + 15 + 4
		layoutY = 120
	}
	headerText.setTextFill(Color.web("#44f9ff"))

	val playerName_label = new Label("Name") {
		prefWidth = 200
		style = "-fx-font: 24 Regular"
		layoutX = Global.gameWidth/2 - (200/2) - 15 + 20
		layoutY = 140+pushY+0 - 10
	}
	playerName_label.setTextFill(Color.web("#00BCC5"))

	val playerName_textField = new TextField {
		prefWidth = 100
		layoutX = Global.gameWidth/2 - (100/2) + 15 + 20
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
		layoutX = Global.gameWidth/2 - (300/2)+32
		layoutY = 140+pushY+32.5
	}
	playerNameWarningText.setTextFill(Color.web("#315060"))

	val gameSpeedSlider_label = new Label("Game Speed") {
		prefWidth = 160
		style = "-fx-font: 21 Regular;"
		layoutX = Global.gameWidth/2 - (200/2) - 90 + 30
		layoutY = 200+pushY
	}
	gameSpeedSlider_label.setTextFill(Color.web("#00BCC5"))

	val gameScaleSlider_label = new Label("Game Scale") {
		prefWidth = 160
		style = "-fx-font: 21 Regular;"
		layoutX = Global.gameWidth/2 - (200/2) + 90 + 30 + 20
		layoutY = 200+pushY
	}
	gameScaleSlider_label.setTextFill(Color.web("#00BCC5"))

	val gameSpeed_slider = new Slider(1, 2, 1) {
		prefWidth = 180
		layoutX = Global.gameWidth/2 - (180/2) - 100
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
		layoutX = Global.gameWidth/2 - (180/2) + 100
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
		layoutX = Global.gameWidth/2 - 150+8
		layoutY = 280+pushY
	}
	warningText.setTextFill(Color.web("#315060"))

	val playButton = new Button("Play") {
		prefWidth = 150
		layoutX = Global.gameWidth/2 - (150/2)
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
				Global.appendToHighscoresFile = false
			} else {
				Global.appendToHighscoresFile = true
			}

			Global.gameSpeed = gameSpeed_slider.getValue()
			Global.gameScale = gameScale_slider.getValue() + 0.2
			Global.updateStats

			App.stage.hide
			App.stage.scene = new MainMenu
			App.stage.title = s"${Game.name} - Main Menu"

			do {
				val game: Game = new Game(playerName)
				game.showAndWait
			} while (Game.retry)

			App.stage.show
		}
	}

	content = List(headerText, playerName_label, playerName_textField, gameScaleSlider_label, gameSpeed_slider, gameScale_slider, gameSpeedSlider_label, playButton, back, warningText, playerNameWarningText)
}

/** A static Highscores object that stores all the methods that is needed to manipulate the highscores of the game. */
object Highscores {

	/** Creates the "highscores.txt" resource file at the System's temporary location. */
	def createFile: Unit = {
		val source = java.nio.channels.Channels.newChannel(Game.getClass.getClassLoader.getResourceAsStream("highscores.txt"))
		val fileOut = new java.io.File(Game.highscoresDir, "highscores.txt")
		val dest = new java.io.FileOutputStream(fileOut)
		
		dest.getChannel.transferFrom(source, 0, Long.MaxValue)
		source.close()
		dest.close()
	}

	/** Clears the highscores file. */
	def clear: Unit = {
		try {
			val printWriter = new java.io.PrintWriter(new java.io.File(Game.highscoresDir, "highscores.txt"))
			printWriter.print("Name Kills Score\n")
			printWriter.close
		} catch {
			case _: Throwable => println("Error: Highscore file not found (WRITE)")
		}
	}

	/** Returns the top ten sorted highscores stored in a List.
	 *
	 * @return the highscores as a List of type Score
	 */
	def toList: List[Score] = {
		var scores: ArrayBuffer[Score] = ArrayBuffer()

		try {
			val fScanner = new java.util.Scanner(new java.io.File(Game.highscoresDir, "highscores.txt"))
			val firstLine = fScanner.nextLine
			while (fScanner.hasNextLine) {
				var name: String = ""
				var kills: Int = 0
				var score: Double = 0.0

				while (!fScanner.hasNextDouble) {
					name += fScanner.next
					if (!fScanner.hasNextDouble) name += " "
				}
				kills = fScanner.nextInt
				score = fScanner.nextDouble

				scores += new Score(name, kills, score)
			}
			fScanner.close
		} catch {
			case _: Throwable => println("Error: Highscore file not found (READ)")
		}

		val unsorted = scores.toList
		val sorted = unsorted.sortBy(-_.score).take(10)

		return sorted
	}

	/** Appends a score to the highscore object.
	 *
	 * @param score the score as an instance of Score
	 * @return a boolean value that determines whether the score has been appended or not
	 */
	def append(score: Score): Boolean = {
		var shouldNotAppend: Boolean = false
		try {
			var lowest: Score = null
			val sorted: List[Score] = Highscores.toList

			if (sorted.isEmpty) {
				lowest = new Score("", 0, 0)
			} else {
				lowest = sorted(sorted.length-1)
			}

			// Should not append if the score is lower than the 10th lowest score in the highscores file
			shouldNotAppend = (sorted.length >= 10 && lowest.score > score.score)

			// Append the score
			if (!shouldNotAppend) {	
				val printWriter = new java.io.PrintWriter(new java.io.FileOutputStream(new java.io.File(Game.highscoresDir, "highscores.txt")), true)

				printWriter.print("Name Kills Score")
				
				sorted.foreach(s => printWriter.write(s"\n${s.name} ${s.kills} ${"%.1f".format(s.score)}"))

				printWriter.write(s"\n${score.name} ${score.kills} ${"%.1f".format(score.score)}")
				printWriter.close
			}

		} catch {
			case e: Throwable => println(s"Error: Highscore file not found (WRITE)\nException ${e.getMessage}")
		}

		return !shouldNotAppend
	}
}

/** The highcores scene of the game.
 *
 *  @constructor create a new instance of a Highscores scene
 *  @param width the width of the scene
 *  @param height the height of the scene
 */
class Highscores (_width: Double, _height: Double) extends Scene (_width, _height) {

	def this() = this(Global.gameWidth, Global.gameHeight)

	fill = Scenes.color("Background")

	val back = new Button("«") {
		prefWidth = 40
		layoutX = 20
		layoutY = 20
		style = Scenes.buttonStyle("Normal")
		onMouseEntered = (e: MouseEvent) => style = Scenes.buttonStyle("onEntered")
		onMouseExited = (e: MouseEvent) => style = Scenes.buttonStyle("onExited")

		onAction = (e: ActionEvent) => {
			App.stage.title = s"${Game.name} - Main Menu"
			App.stage.scene = new MainMenu
		}
	}

	val pushY = 100

	val headerText = new Label("Highscores") {
		prefWidth = 250
		style = "-fx-font: 35 Regular"
		layoutX = Global.gameWidth/2 - (250/2) + 29
		layoutY = 120
	}
	headerText.setTextFill(Color.web("#44f9ff"))

	val highscores: List[Score] = Highscores.toList

	var scoreNodes: ArrayBuffer[Node] = ArrayBuffer()

	val nameCol_label = new Label("Name") {
		prefWidth = 250
		style = "-fx-font: 17 Regular"
		layoutX = Global.gameWidth/2 - (250/2) - 26 - 20
		layoutY = 180
	}
	nameCol_label.setTextFill(Color.web("#00BCC5"))

	val killsCol_label = new Label("Kills") {
		prefWidth = 250
		style = "-fx-font: 17 Regular"
		layoutX = Global.gameWidth/2 - 20
		layoutY = 180
	}
	killsCol_label.setTextFill(Color.web("#00BCC5"))

	val scoreCol_label = new Label("Score") {
		prefWidth = 250
		style = "-fx-font: 17 Regular"
		layoutX = Global.gameWidth/2 + (250/2) - 36 + 20
		layoutY = 180
	}
	scoreCol_label.setTextFill(Color.web("#00BCC5"))

	var gap: Int = 0
	var count: Int = 1

	for (score <- highscores) {
		val name_label = new Label(s"$count. ${score.name.toString}") {
			prefWidth = 250
			style = "-fx-font: 17 Regular"
			layoutX = Global.gameWidth/2 - (250/2) - 26 - 20
			layoutY = 210+gap
		}
		name_label.setTextFill(Color.web("#00BCC5"))

		val kills_label = new Label(s"${score.kills.toString}") {
			prefWidth = 250
			style = "-fx-font: 17 Regular"
			layoutX = Global.gameWidth/2 - 20
			layoutY = 210+gap
		}
		kills_label.setTextFill(Color.web("#00BCC5"))

		val score_label = new Label(score.score.toString) {
			prefWidth = 250
			style = "-fx-font: 17 Regular"
			layoutX = Global.gameWidth/2 + (250/2) - 36 + 20
			layoutY = 210+gap
		}
		score_label.setTextFill(Color.web("#00BCC5"))

		scoreNodes += name_label
		scoreNodes += kills_label
		scoreNodes += score_label
		gap += 30
		count += 1
	}

	val resetButton = new Button("Reset") {
		prefWidth = 150
		layoutX = Global.gameWidth/2 - (150/2)
		layoutY = 210+gap+18
		style = Scenes.buttonStyle("Normal")
		onMouseEntered = (e: MouseEvent) => style = Scenes.buttonStyle("onEntered")
		onMouseExited = (e: MouseEvent) => style = Scenes.buttonStyle("onExited")

		onAction = (e: ActionEvent) => { 
			Highscores.clear
			App.stage.title = s"${Game.name} - Main Menu"
			App.stage.scene = new MainMenu
		}
	}

	content = List(back, headerText, resetButton) ++ scoreNodes.toList ++ List(nameCol_label, killsCol_label, scoreCol_label)
}

/** The about scene of the game.
 *
 *  @constructor create a new instance of a About scene
 *  @param width the width of the scene
 *  @param height the height of the scene
 */
class About (_width: Double, _height: Double) extends Scene (_width, _height) {

	def this() = this(Global.gameWidth, Global.gameHeight)

	fill = Scenes.color("Background")

	val back = new Button("«") {
		prefWidth = 40
		layoutX = 20
		layoutY = 20
		style = Scenes.buttonStyle("Normal")
		onMouseEntered = (e: MouseEvent) => style = Scenes.buttonStyle("onEntered")
		onMouseExited = (e: MouseEvent) => style = Scenes.buttonStyle("onExited")

		onAction = (e: ActionEvent) => {
			App.stage.title = s"${Game.name} - Main Menu"
			App.stage.scene = new MainMenu
		}
	}

	val pushY = 100
	val pullY = -40

	val headerText = new Label("About") {
		prefWidth = 250
		style = "-fx-font: 35 Regular"
		layoutX = Global.gameWidth/2 - (250/2) + 75
		layoutY = 120+pullY
	}
	headerText.setTextFill(Color.web("#44f9ff"))

	val aboutGameStr = s"${Game.name} is an endless 2D shooter game in which the\nplayer has to survive by killing enemies until the\nplayer is killed by an enemy.\nThe score is then determined by the time surpassed in seconds."

	val aboutGame = new Label(aboutGameStr) {
		prefWidth = 250 + (250/2)
		style = "-fx-font: 12 Regular; -fx-text-alignment: center;"
		layoutX = Global.gameWidth/2 - (250/2) - 50 - 10
		layoutY = 180+pullY
	}
	aboutGame.setTextFill(Color.web("#00BCC5"))

	val controlsText = new Label("Controls") {
		prefWidth = 250
		style = "-fx-font: 28 Regular"
		layoutX = Global.gameWidth/2 - (250/2) + 70
		layoutY = 280+pullY
	}
	controlsText.setTextFill(Color.web("#44f9ff"))

	val controlsStr = "Arrow Keys: Move around the play area\nSpace bar or Z key: Shoot bullets\nEsc: Pause Game" 

	val controls = new Label(controlsStr) {
		prefWidth = 250 + (250/3)
		style = "-fx-font: 12 Regular; -fx-text-alignment: center;"
		layoutX = Global.gameWidth/2 - (250/2) + 13
		layoutY = 320+pullY
	}
	controls.setTextFill(Color.web("#00BCC5"))

	val developersText = new Label("Developers") {
		prefWidth = 250
		style = "-fx-font: 28 Regular"
		layoutX = Global.gameWidth/2 - (250/2) + 50
		layoutY = 420+pullY
	}
	developersText.setTextFill(Color.web("#44f9ff"))

	val developersStr = "Lishan Abbas\nYap Jia Yung\nHans Maulloo\nDaniel Jedidiah\nDavid Thingee" 

	val developers = new Label(developersStr) {
		prefWidth = 250
		style = "-fx-font: 12 Regular; -fx-text-alignment: center;"
		layoutX = Global.gameWidth/2 - (250/4) + 20
		layoutY = 460+pullY
	}
	developers.setTextFill(Color.web("#00BCC5"))

	content = List(back, headerText, aboutGame, controlsText, controls, developersText, developers)
}