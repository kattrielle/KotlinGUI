package kotlinGUI.viewModel

import javafx.util.StringConverter

object ToIntConverter : StringConverter<Int>() {
    override fun toString(value: Int?): String = value.toString()

    override fun fromString(string: String?): Int {
        return if ( string == null ) {
            0
        } else return if ( string.toIntOrNull() == null ) {
            0
        } else ( string.toIntOrNull() )!!
    }
}