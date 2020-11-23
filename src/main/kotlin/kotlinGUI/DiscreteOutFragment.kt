package kotlinGUI

import javafx.beans.property.SimpleStringProperty
import registerCollection.DiscreteOut
import tornadofx.*

class DiscreteOutFragment ( val discreteOut : DiscreteOut) : Fragment() {
    private val registers = FormValues.getRegisterMapDescriptions()
    private val selectValues = SimpleStringProperty(discreteOut.descriptionValues)
    private val selectSetpoint = SimpleStringProperty(discreteOut.descriptionSetpoint)
    private val selectTimeSet = SimpleStringProperty(discreteOut.descriptionTimeSet)
    private val selectTimeUnset = SimpleStringProperty(discreteOut.descriptionTimeUnset)

    override val root = hbox {
        vbox {
            text( "Набор значений" )
            combobox(selectValues, registers)
        }
        vbox {
            text("Уставка")
            combobox(selectSetpoint, registers)
        }
        vbox {
            text("Время установки")
            combobox(selectTimeSet, registers)
        }
        vbox {
            text("Время снятия")
            combobox(selectTimeUnset, registers)
        }
        button("Найти") {
            action {
                fillDiscreteOutParams()
            }
        }
    }

    fun fillDiscreteOutParams()
    {
        discreteOut.values = FormValues.findRegister( selectValues.value )
        discreteOut.setpoint = FormValues.findRegister( selectSetpoint.value )
        discreteOut.timeSet = FormValues.findRegister( selectTimeSet.value )
        discreteOut.timeUnset = FormValues.findRegister( selectTimeUnset.value )
    }
}