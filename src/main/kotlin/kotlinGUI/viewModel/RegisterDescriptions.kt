package kotlinGUI.viewModel

import javafx.collections.FXCollections
import registerMapTikModscan.ElementFormat
import registerMapTikModscan.ElementType

class RegisterDescriptions {
    companion object {
        val types = FXCollections.observableArrayList(
                "Coil",
                "Discrete Input",
                "Holding",
                "Input" )

        val formats = FXCollections.observableArrayList(
                "Int",
                "Float",
                "swFloat",
                "Double",
                "swDouble",
                "String",
                "Int32",
                "swInt32")

        fun selectTypeFromTextLine( typeText : String ) : ElementType?
        {
            return when (typeText )
            {
                "Coil" -> ElementType.Coil
                "Discrete Input"-> ElementType.DiscreteInput
                "Holding" -> ElementType.HoldingRegister
                "Input" -> ElementType.InputRegister
                else -> null
            }
        }

        fun selectFormatFromTextLine( formatText : String ) : ElementFormat?
        {
            return when ( formatText )
            {
                "Int" -> ElementFormat.Int
                "Float" -> ElementFormat.Float
                "swFloat" -> ElementFormat.swFloat
                "Double" -> ElementFormat.Double
                "swDouble" -> ElementFormat.swDouble
                "String" -> ElementFormat.String
                "Int32" -> ElementFormat.Int32
                "swInt32" -> ElementFormat.swInt32
                else -> null
            }
        }

        fun convertFromTypeToText(type : ElementType? ) : String
        {
            return when ( type )
            {
                ElementType.Coil -> "Coil"
                ElementType.DiscreteInput -> "Discrete Input"
                ElementType.HoldingRegister -> "Holding"
                ElementType.InputRegister -> "Input"
                else -> ""
            }
        }

        fun convertFromFormatToText( format : ElementFormat? ) : String
        {
            return when ( format ) {
                ElementFormat.Int -> "Int"
                ElementFormat.Float -> "Float"
                ElementFormat.swFloat -> "swFloat"
                ElementFormat.Double -> "Double"
                ElementFormat.swDouble -> "swDouble"
                ElementFormat.String -> "String"
                ElementFormat.Int32 -> "Int32"
                ElementFormat.swInt32 -> "swInt32"
                else -> ""
            }
        }
    }
}