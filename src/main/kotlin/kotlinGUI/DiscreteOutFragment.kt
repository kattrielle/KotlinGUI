package kotlinGUI

import javafx.beans.property.SimpleStringProperty
import registerCollection.DiscreteOut
import registerCollection.DiscreteOutViewProperties
import tornadofx.*

class DiscreteOutFragment : Fragment() {
    val discreteOut : DiscreteOutViewProperties by param()

    override val root = vbox {
        hbox {
            text( "Набор значений" )
            textfield(discreteOut.selectValues)
        }
        vbox {
            hbox {
                text("Уставка - адрес выборки")
                textfield(discreteOut.selectSetpointSample)
            }
            hbox {
                text("Уставка - значение")
                textfield(discreteOut.selectSetpoint)
            }
            hbox {
                text("Уставка - время установки")
                textfield(discreteOut.selectTimeSet)
            }
            hbox {
                text("Уставка - время снятия")
                textfield(discreteOut.selectTimeUnset)
            }
            hbox {
                text("Уставка - весовой коэффициент")
                textfield(discreteOut.selectWeight)
            }
        }

    }



}