case class Position (x: Double, y: Double)
case class Health (current: Double, max: Double)
case class BufferCounter (start: Int, end: Int) {
	var value: Int = start
	def increment = {
		this.value += 1
		if (this.value > end) {
			this.value = start
		}
	}
}