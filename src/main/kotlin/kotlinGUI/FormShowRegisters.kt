package kotlinGUI

import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Insets
import tornadofx.*

class FormShowRegisters : View("Номера регистров") {
    private val registerDefence = SimpleIntegerProperty(
            FormValues.setpoints.registerNumDefence)
    private val formContent = form {
        fieldset("Защита записи") {
            field(FormValues.setpoints.descriptionWriteDefence) {
                textfield(registerDefence)
            }
        }
        padding = Insets(15.0)
    }

    override val root = scrollpane {
        vbox {
            hbox {
                button("Сохранить") {
                    action {
                        saveChanges()
                        close()
                    }
                }
                button("Закрыть") {
                    action {
                        close()
                    }
                }
                padding = Insets(15.0)
            }
            hbox {
                add(formContent)
            }
        }

        maxHeight = 500.0
    }

    init {
        FormValues.setpoints.items.forEach {
            formContent.add( ShowDiscreteOutFragment(it) )
        }
    }

    private fun saveChanges()
    {
        for ( fragment in formContent.children.filtered { it is Fragment } )
        {
        }
    }
}