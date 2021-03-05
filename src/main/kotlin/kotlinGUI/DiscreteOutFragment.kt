package kotlinGUI

import kotlinGUI.viewModel.DiscreteOutViewProperties
import tornadofx.*

class DiscreteOutFragment : Fragment() {
    val discreteOut : DiscreteOutViewProperties by param()

    override val root = form {
        fieldset {
            field("Выборка") {
                textfield(discreteOut.selectValues)
            }
            field ("Уставка - адрес выборки") {
                textfield(discreteOut.selectSetpointSample)
            }
            field("Уставка - значение") {
                textfield(discreteOut.selectSetpoint)
            }
            field("Уставка - время установки") {
                textfield(discreteOut.selectTimeSet)
            }
            field("Уставка - время снятия") {
                textfield(discreteOut.selectTimeUnset)
            }
            field("Уставка - весовой коэффициент") {
                textfield(discreteOut.selectWeight)
            }

        }

    }

    init {
        titleProperty.bind( discreteOut.description )
    }

}