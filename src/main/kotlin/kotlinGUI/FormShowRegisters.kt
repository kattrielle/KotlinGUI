package kotlinGUI

import javafx.beans.property.SimpleIntegerProperty
import tornadofx.*

class FormShowRegisters : View() {
    private val registerDefence = SimpleIntegerProperty(
            FormValues.setpoints.registerNumDefence)

    override val root = form {
        fieldset("Защита записи") {
            field(FormValues.setpoints.descriptionWriteDefence) {
                textfield(registerDefence)
            }
        }
    }

    init {
        FormValues.setpoints.items.forEach {
            root.add( ShowDiscreteOutFragment(it) )
        }
    }
}