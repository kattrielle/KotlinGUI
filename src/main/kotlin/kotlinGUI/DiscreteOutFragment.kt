package kotlinGUI

import javafx.beans.property.SimpleStringProperty
import registerCollection.DiscreteOut
import tornadofx.*

class DiscreteOutFragment ( val discreteOut : DiscreteOut) : Fragment() {
    private val registers = FormValues.getRegisterMapDescriptions()
    private val selectValues = SimpleStringProperty(discreteOut.descriptionValues)
    private val selectSetpointSample = SimpleStringProperty( discreteOut.descriptionSetpointSample )
    private val selectSetpoint = SimpleStringProperty(discreteOut.descriptionSetpoint)
    private val selectTimeSet = SimpleStringProperty(discreteOut.descriptionTimeSet)
    private val selectTimeUnset = SimpleStringProperty(discreteOut.descriptionTimeUnset)
    private val selectWeight = SimpleStringProperty( discreteOut.descriptionWeight )

    override val root = hbox {
        vbox {
            text( "Набор значений" )
            combobox(selectValues, registers)
        }
        vbox {
            vbox {
                text("Уставка - адрес выборки")
                combobox(selectSetpointSample, registers)
            }
            vbox {
                text("Уставка - значение")
                combobox(selectSetpoint, registers)
            }
            vbox {
                text("Уставка - время установки")
                combobox(selectTimeSet, registers)
            }
            vbox {
                text("Уставка - время снятия")
                combobox(selectTimeUnset, registers)
            }
            vbox {
                text("Уставка - весовой коэффициент")
                combobox(selectWeight, registers)
            }
        }

    }

    init {
        selectValues.onChange {
            println( "finding ${selectValues.value}" )
            discreteOut.values = FormValues.findRegister( selectValues.value )
        }

        selectSetpoint.onChange {
            println( "finding ${selectSetpoint.value}" )
            discreteOut.setpoint = FormValues.findRegister( selectSetpoint.value )
        }

        selectSetpointSample.onChange {
            println( "finding ${selectSetpointSample.value}")
            discreteOut.setpointSample = FormValues.findRegister( selectSetpointSample.value )
        }

        selectTimeSet.onChange {
            println( "finding ${selectTimeSet.value}")
            discreteOut.timeSet = FormValues.findRegister( selectTimeSet.value )
        }

        selectTimeUnset.onChange {
            println( "finding ${selectTimeUnset.value}" )
            discreteOut.timeUnset = FormValues.findRegister( selectTimeUnset.value )
        }

        selectWeight.onChange {
            println( "finding ${selectWeight.value}" )
            discreteOut.weight = FormValues.findRegister( selectWeight.value )
        }
    }

}