package kotlinGUI

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import tornadofx.*

class FormInitSetpoints : View() {
    private val valueSetpoint = SimpleDoubleProperty()
    private val valueTimeSet = SimpleIntegerProperty()
    private val valueTimeUnset = SimpleIntegerProperty()
    private val valueWeight = SimpleIntegerProperty()

    override val root = form {
        fieldset {
            field("Величина уставки") {
                textfield( valueSetpoint )
            }
            field("Время установки") {
                textfield( valueTimeSet )
            }
            field("Время снятия") {
                textfield( valueTimeUnset )
            }
            field("Весовой коэффициент") {
                textfield( valueWeight )
            }
        }
        button( "Записать" ) {
            action {
                println( FormValues.getCurrentTime() + "writing init values to all setpoints" )
                FormValues.setpoints.unlockWrite( FormValues.device )
                for ( discreteOut in FormValues.setpoints.items )
                {
                    discreteOut.writeSetpointValue( valueSetpoint.value, FormValues.device )
                    discreteOut.writeSetpointSampleValue( FormValues.device )
                    discreteOut.writeTimeSetValue( valueTimeSet.value, FormValues.device )
                    discreteOut.writeTimeUnsetValue( valueTimeUnset.value, FormValues.device )
                    discreteOut.writeWeightValue( valueWeight.value, FormValues.device )
                }
                FormValues.setpoints.lockWrite( FormValues.device )
                FormValues.device.CloseConnection()
            }
        }

    }
}