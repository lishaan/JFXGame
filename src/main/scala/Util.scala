import scala.collection.mutable.{Map => MMap, ArrayBuffer}
import scala.collection.immutable.Map
import scalafx.Includes._
import scalafx.scene.paint.Color

case class Score (name: String, kills: Int, score: Double)

case class Health (max: Double) { 
	var current: Double = max 
	def percentage: Double = current / max

	def halved: Boolean = (current / max) <= 1/2
}

case class Position (var x: Double, var y: Double) {
	def moveUp    (speed: Double): Unit = { y = y - speed * Global.delta }
	def moveRight (speed: Double): Unit = { x = x + speed * Global.delta }
	def moveDown  (speed: Double): Unit = { y = y + speed * Global.delta }
	def moveLeft  (speed: Double): Unit = { x = x - speed * Global.delta }
}

case class Velocity (var speed: Double) {
	var x: Double = if (math.random*2 >= 1) speed else -speed
	var y: Double = if (math.random*2 <= 1) speed else -speed
}

class Spawner (val enemyName: String, val delayHead: Double, val delayTail: Double) {
	def this(enemyName: String, delay: Double) = this(enemyName, delay, delay)

	private val _random: scala.util.Random = new scala.util.Random
	def random: Double = (delayHead+_random.nextInt((delayTail-delayHead).toInt+1))

	var counter: Double = random
	
	def stopped: Boolean = (counter <= 0)
	def reset: Unit = { counter = random }
	def update: Unit = { counter -= Global.delta }
}

object Util {
	def createHighscoresFile: Unit = {
		val source = java.nio.channels.Channels.newChannel(Game.getClass.getClassLoader.getResourceAsStream("highscores.txt"))
		val fileOut = new java.io.File(Game.highscoresDir, "highscores.txt")
		val dest = new java.io.FileOutputStream(fileOut)
		
		dest.getChannel.transferFrom(source, 0, Long.MaxValue)
		source.close()
		dest.close()
	}

	def clearHighscores: Unit = {
		try {
			val printWriter = new java.io.PrintWriter(new java.io.File(Game.highscoresDir, "highscores.txt"))
			printWriter.print("Name Kills Score\n")
			printWriter.close
		} catch {
			case _: Throwable => println("Error: Highscore file not found (WRITE)")
		}
	}

	def getHighscores: List[Score] = {
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

	def appendScore(score: Score): Unit = {
		try {
			var lowest: Score = null
			val sorted: List[Score] = getHighscores

			if (sorted.isEmpty) {
				lowest = new Score("", 0, 0)
			} else {
				lowest = sorted(sorted.length-1)
			}

			val shouldAppend: Boolean = (sorted.length >= 10 && lowest.score > score.score)

			if (!shouldAppend) {	
				val printWriter = new java.io.PrintWriter(new java.io.FileOutputStream(new java.io.File(Game.highscoresDir, "highscores.txt")), true)

				sorted.foreach(s => printWriter.write(s"\n${s.name} ${s.kills} ${"%.1f".format(s.score)}"))

				printWriter.write(s"\n${score.name} ${score.kills} ${"%.1f".format(score.score)}")
				printWriter.close
			}

		} catch {
			case e: Throwable => println(s"Error: Highscore file not found (WRITE)\nException ${e.getMessage}")
		}
	}
}

object Const {
	var gameScale: Double = 1.2
	var gameSpeed: Double = 1.0 
	val gameWidth: Double = 600
	val gameHeight: Double = 600
	val playAreaHeight: Double = Const.gameHeight/1.5

	var appendToHighscoresFile: Boolean = true

	val SPEED: MMap[String, Double] = MMap (
		"Player"         -> Const.gameSpeed * 170,
		"Bullet"         -> Const.gameSpeed * 400,
		"Seeker"         -> Const.gameSpeed * 80,
		"Bouncer"        -> Const.gameSpeed * 120,
		"Shooter"        -> Const.gameSpeed * 70,
		"ShooterBullet"  -> Const.gameSpeed * 300
	)


	val SIZE: MMap[String, Double] = MMap (
		"Player"         -> Const.gameScale * 20,
		"Bullet"         -> Const.gameScale * 4,
		"Seeker"         -> Const.gameScale * 10,
		"Bouncer"        -> Const.gameScale * 17,
		"Shooter"        -> Const.gameScale * 30,
		"ShooterBullet"  -> Const.gameScale * 6
	)

	val color: Map[String, Color] = Map (
		"Background"    -> Color.web("041A1A"),
		"PlayArea"      -> Color.web("173B3B"),
		"TimerText"     -> Color.web("FBFBFB"),
		"PausedText"    -> Color.web("FBFBFB"),
		"Player"        -> Color.web("FBFBFB"),
		"Bullet"        -> Color.web("44f9ff"),
		"ShooterBullet" -> Color.web("F7E8D0"),
		"Seeker"        -> Color.web("6F997A"),
		"Bouncer"       -> Color.web("79678A"),
		"Shooter"       -> Color.web("856D48")
		/* Link: https://coolors.co/0c0910-cdd1c4-5c80bc-6b2737-1d7874 */
	)

	val speed: MMap[String, Double] = MMap (
		"Player"         -> Const.gameSpeed*SPEED("Player"),
		"Bullet"         -> Const.gameSpeed*SPEED("Bullet"),
		"Seeker"         -> Const.gameSpeed*SPEED("Seeker"),
		"Bouncer"        -> Const.gameSpeed*SPEED("Bouncer"),
		"Shooter"        -> Const.gameSpeed*SPEED("Shooter"),
		"ShooterBullet"  -> Const.gameSpeed*SPEED("ShooterBullet")
	)

	val size: MMap[String, Double] = MMap (
		"Player"         -> Const.gameScale*SIZE("Player"),
		"Bullet"         -> Const.gameScale*SIZE("Bullet"),
		"Seeker"         -> Const.gameScale*SIZE("Seeker"),
		"Bouncer"        -> Const.gameScale*SIZE("Bouncer"),
		"Shooter"        -> Const.gameScale*SIZE("Shooter"),
		"ShooterBullet"  -> Const.gameScale*SIZE("ShooterBullet")
	)

	val health: Map[String, Double] = Map (
		"Seeker"  -> 15,
		"Bouncer" -> 60,
		"Shooter" -> 100
	)

	def updateConsts: Unit = {
		Const.speed("Player" ) = Const.gameSpeed*SPEED("Player")
		Const.speed("Bullet" ) = Const.gameSpeed*SPEED("Bullet")
		Const.speed("Seeker" ) = Const.gameSpeed*SPEED("Seeker")
		Const.speed("Bouncer") = Const.gameSpeed*SPEED("Bouncer")
		Const.speed("Shooter") = Const.gameSpeed*SPEED("Shooter")
		Const.speed("ShooterBullet" ) = Const.gameSpeed*SPEED("ShooterBullet")

		Const.size("Player" ) = Const.gameScale*SIZE("Player")
		Const.size("Bullet" ) = Const.gameScale*SIZE("Bullet")
		Const.size("Seeker" ) = Const.gameScale*SIZE("Seeker")
		Const.size("Bouncer") = Const.gameScale*SIZE("Bouncer")
		Const.size("Shooter") = Const.gameScale*SIZE("Shooter")
		Const.size("ShooterBullet" ) = Const.gameScale*SIZE("ShooterBullet")
	}
}

object Global {
	var playerPos: Position = new Position(Const.gameWidth/2, Const.gameHeight-50)
	var delta: Double = 0
	var seconds: Double = 0
}