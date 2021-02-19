package kotlinGUI.viewModel

import javafx.util.StringConverter

object ToDoubleConverter: StringConverter<Double>() {
    override fun toString(value: Double?): String = value.toString()

    override fun fromString(string: String?): Double {
        return if ( string == null ) {
            0.0
        } else return if ( string.toDoubleOrNull() == null ) {
            0.0
        } else ( string.toDoubleOrNull() )!!
    }
}