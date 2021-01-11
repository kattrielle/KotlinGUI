package kotlinGUI

import javafx.beans.property.SimpleIntegerProperty
import tornadofx.*

class FormShowRegisters : View() {
    private val registerDefence = SimpleIntegerProperty(
            FormValues.setpoints.registerNumDefence)
    private val formContent = form {
        fieldset("Защита записи") {
            field(FormValues.setpoints.descriptionWriteDefence) {
                textfield(registerDefence)
            }
        }
    }

    override val root = scrollpane {
        add( formContent )
    }

    init {
        FormValues.setpoints.items.forEach {
            formContent.add( ShowDiscreteOutFragment(it) )
        }
    }
}