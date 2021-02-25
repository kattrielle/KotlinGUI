package kotlinGUI.viewModel

import javafx.util.StringConverter

object ToDoubleConverter: StringConverter<Double>() {
    override fun toString(value: Double?): String = value.toString()

    override fun fromString(string: String?): Double {
        return when {
            string == null -> {
                0.0
            }
            string.replace(",", ".").toDoubleOrNull() == null -> {
                0.0
            }
            else -> ( string.replace(",", ".").toDoubleOrNull() )!!
        }
    }
}