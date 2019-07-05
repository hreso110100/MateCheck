package sk.spacecode.matecheck.enums

import kotlin.random.Random

enum class Colors(val value: String) {
    RED("#c94b4b"),
    PURPLE("#65499c"),
    BLUE("#42a5f5"),
    GREEN("#11998e"),
    PINK("#ec407a"),
    LIGHT_GREEN("#93F9B9"),
    CYAN("#3fada8"),
    YELLOW("#cac531"),
    ORANGE("#F2994A"),
    BLUE_GREY("#78909c"),
    GREY("#808080"),
    TEAL("#009688"),
    BROWN("#a1887f");

    companion object {
        fun pickRandom(): String {
            val listOfColors = values()
            val randomIndex = Random.nextInt(listOfColors.size)

            return listOfColors[randomIndex].value
        }
    }
}