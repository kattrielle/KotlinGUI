package kotlinGUI

import javafx.beans.property.SimpleIntegerProperty
import registerCollection.DiscreteOut
import tornadofx.*

class ShowDiscreteOutFragment( val discreteOut : DiscreteOut ) : Fragment() {
    private val sampleRegister = SimpleIntegerProperty( discreteOut.registerValues )
    private val setpointRegister = SimpleIntegerProperty( discreteOut.registerSetpoint )
    private val setpointSampleRegister = SimpleIntegerProperty( discreteOut.registerSetpointSample )
    private val timeSetRegister = SimpleIntegerProperty( discreteOut.registerTimeSet )
    private val timeUnsetRegister = SimpleIntegerProperty( discreteOut.registerTimeUnset )
    private val weightRegister = SimpleIntegerProperty( discreteOut.registerWeigth )

    override val root = fieldset( discreteOut.descriptionValues ) {
        field("Выборка: ${discreteOut.descriptionValues}") {
            textfield(sampleRegister)
        }
        field("Уставка - адрес выборки: ${discreteOut.descriptionSetpointSample}") {
            textfield(setpointSampleRegister)
        }
        field("Уставка - величина срабатывания: ${discreteOut.descriptionSetpoint}") {
            textfield(setpointRegister)
        }
        field("Уставка - время срабатывания: ${discreteOut.descriptionTimeSet}") {
            textfield(timeSetRegister)
        }
        field("Уставка - время снятия: ${discreteOut.descriptionTimeUnset}") {
            textfield(timeUnsetRegister)
        }
        field("Уставка - весовой коэффициент: ${discreteOut.descriptionWeight}") {
            textfield(weightRegister)
        }
    }
}