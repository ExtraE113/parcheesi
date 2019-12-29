import org.w3c.dom.Element
import kotlin.browser.document
import kotlin.dom.addClass

fun main(args: Array<String>) {
	val message = "Hello JavaScript!"
	println(message)
	createGameBoardNew(19)
}


fun createGameBoardNew(gameBoardSize:Int){

	//reusable function to create starting area
	val startArea = fun(area:Pair<Int, Int>): Element {

		val startingAreaIdHashMap = hashMapOf<Pair<Int, Int>, String>(
				Pair(0, 0) to "p1",
				Pair(0, 11) to "p2",
				Pair(11, 0) to "p3",
				Pair(11, 11) to "p4"
		)


		val td = document.createElement("td")
		startingAreaIdHashMap[area]?.let { td.addClass(it) } //in case this is called on a point that is not in the startingAreaIdHashMap, just skip assigning a html class
		td.setAttribute("colspan", "8")
		td.setAttribute("rowspan", "8")
		return td
	}

	//function to create home area. similar to startArea but much simpler
	val homeArea = fun(area:Pair<Int, Int>):Element{ //Takes area as a pair, but sense there's actually only one home area we can just ignore it
		val td = document.createElement("td")
		td.addClass("home")
		td.textContent = "HOME"
		td.setAttribute("colspan", "3")
		td.setAttribute("rowspan", "3")
		return td
	}

	val unclaimedSafeSquare = fun(area: Pair<Int, Int>): Element{
		val td = document.createElement("td")
		td.addClass("safe")
		return td
	}

	val claimedSafeSquare = fun(area: Pair<Int, Int>): Element{

		val enterAreaIdHashMap = hashMapOf<Pair<Int, Int>, Array<String>>(
				Pair(8, 4) to arrayOf("\u2bb7", "p1"),
				Pair(14, 8) to arrayOf("\u2bb0","p3"),
				Pair(10, 14) to arrayOf("\u2bb4", "p4"),
				Pair(4, 10) to arrayOf("\u2bb3","p2")
		)

		val td = unclaimedSafeSquare(area)
		val claimColorMarker = document.createElement("div")

		enterAreaIdHashMap[area]?.get(1)?.let { claimColorMarker.addClass(it) }
		claimColorMarker.addClass("safe")
		enterAreaIdHashMap[area]?.get(0)?.let { claimColorMarker.textContent = it }
		td.appendChild(claimColorMarker)
		return td
	}

	val enterArea = fun(area:Pair<Int, Int>): Element{

		val td = document.createElement("td")
		val claimColorMarker = document.createElement("div")

		val arrowDirection: String
		val player: String

		if (area.first == 9 && area.second <= 7){
			arrowDirection = "\u2b9f"
			player = "p1"
		} else if (area.first == 9 && area.second >= 11){
			arrowDirection = "\u2b9d"
			player = "p4"
		} else if (area.second == 9 && area.first <= 7){
			arrowDirection = "\u2b9e"
			player = "p2"
		} else {
			arrowDirection = "\u2b9c"
			player = "p3"
		}
		claimColorMarker.addClass(player)
		claimColorMarker.textContent = arrowDirection
		td.appendChild(claimColorMarker)
		return td
	}

	//hashmap of areas to their create functions
	val gameBoardDataHashMap: HashMap<Pair<Int, Int>, (Pair<Int, Int>) -> Element?> = hashMapOf()

	//if its in one of the areas that should be ignored (because its being filled by something else, just do nothing
	run {
		for (x in 0..7) {
			for (y in 0..7) {
				gameBoardDataHashMap[Pair(x, y)] = fun(_: Pair<Int, Int>): Element? { return null } //if its in one of the areas that should be ignored (because its being filled by something else, just do nothing
			}
		}
		for (x in 11..18) {
			for (y in 0..7) {
				gameBoardDataHashMap[Pair(x, y)] = fun(_: Pair<Int, Int>): Element? { return null } //if its in one of the areas that should be ignored (because its being filled by something else, just do nothing
			}
		}

		for (x in 0..7) {
			for (y in 11..18) {
				gameBoardDataHashMap[Pair(x, y)] = fun(_: Pair<Int, Int>): Element? { return null } //if its in one of the areas that should be ignored (because its being filled by something else, just do nothing
			}
		}

		for (x in 11..18) {
			for (y in 11..18) {
				gameBoardDataHashMap[Pair(x, y)] = fun(_: Pair<Int, Int>): Element? { return null } //if its in one of the areas that should be ignored (because its being filled by something else, just do nothing
			}
		}

		for (x in 8..10) {
			for (y in 8..10) {
				gameBoardDataHashMap[Pair(x, y)] = fun(_: Pair<Int, Int>): Element? { return null } //if its in one of the areas that should be ignored (because its being filled by something else, just do nothing
			}
		}
	}


	//config block, hand-written mapping of specific special areas to functions (start areas, safety squares, etc)
	run {
		gameBoardDataHashMap[Pair(0, 0)] = startArea
		gameBoardDataHashMap[Pair(0, 11)] = startArea
		gameBoardDataHashMap[Pair(11, 0)] = startArea
		gameBoardDataHashMap[Pair(11, 11)] = startArea


		gameBoardDataHashMap[Pair(8, 8)] = homeArea


		gameBoardDataHashMap[Pair(9, 0)] = unclaimedSafeSquare
		gameBoardDataHashMap[Pair(0, 9)] = unclaimedSafeSquare
		gameBoardDataHashMap[Pair(9, 0)] = unclaimedSafeSquare
		gameBoardDataHashMap[Pair(18, 9)] = unclaimedSafeSquare
		gameBoardDataHashMap[Pair(9, 18)] = unclaimedSafeSquare
		gameBoardDataHashMap[Pair(10, 4)] = unclaimedSafeSquare
		gameBoardDataHashMap[Pair(14, 10)] = unclaimedSafeSquare
		gameBoardDataHashMap[Pair(8, 14)] = unclaimedSafeSquare
		gameBoardDataHashMap[Pair(4, 8)] = unclaimedSafeSquare

		gameBoardDataHashMap[Pair(8, 4)] = claimedSafeSquare
		gameBoardDataHashMap[Pair(14, 8)] = claimedSafeSquare
		gameBoardDataHashMap[Pair(10, 14)] = claimedSafeSquare
		gameBoardDataHashMap[Pair(4, 10)] = claimedSafeSquare


		for (y in 1..7){
			gameBoardDataHashMap[Pair(9, y)] = enterArea
		}

		for (y in 11..17){
			gameBoardDataHashMap[Pair(9, y)] = enterArea
		}

		for (x in 1..7){
			gameBoardDataHashMap[Pair(x, 9)] = enterArea
		}

		for (x in 11..17){
			gameBoardDataHashMap[Pair(x, 9)] = enterArea
		}

	}


	val table = document.createElement("table")
	table.addClass("main-table")


	for (y in 0 until gameBoardSize){ //until excludes the endpoint, so starting from zero we get gameBoardSize number of calls

		val tr = document.createElement("tr") //create an empty row

		for(x in 0  until gameBoardSize){
			if (gameBoardDataHashMap[Pair(x, y)] != null){
				println("$x, $y: ${gameBoardDataHashMap[Pair(x, y)]}")
				gameBoardDataHashMap[Pair(x, y)]?.
						invoke(Pair(x,y))?.let {
							tr.appendChild(it)
						}
			}
			else {
				val td = document.createElement("td")
				td.textContent = "$x, $y"
				tr.appendChild(td)
			}
		}

		table.appendChild(tr) //add the row, and all its children cells, to the table
	}

	document.querySelector(".game-board")!!.appendChild(table)

}