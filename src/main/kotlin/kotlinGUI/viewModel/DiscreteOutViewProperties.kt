package kotlinGUI.viewModel

import javafx.beans.property.SimpleStringProperty
import kotlinGUI.FormValues
import registerCollection.DiscreteOut
import tornadofx.onChange

class DiscreteOutViewProperties(discreteOut : DiscreteOut, num : Int ) {
    val selectValues = SimpleStringProperty(discreteOut.descriptionValues)
    val selectSetpointSample = SimpleStringProperty( discreteOut.descriptionSetpointSample )
    val selectSetpoint = SimpleStringProperty(discreteOut.descriptionSetpoint)
    val selectTimeSet = SimpleStringProperty(discreteOut.descriptionTimeSet)
    val selectTimeUnset = SimpleStringProperty(discreteOut.descriptionTimeUnset)
    val selectWeight = SimpleStringProperty( discreteOut.descriptionWeight )
    val description = SimpleStringProperty( num.toString() )

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