package kotlinGUI

import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Insets
import javafx.scene.layout.Priority
import kotlinGUI.viewModel.DiscreteOutCellsProperties
import tornadofx.*

class FormShowRegisters : View("Номера регистров") {
    private val cellsProperties = mutableListOf<DiscreteOutCellsProperties>()

    private val registerDefence = SimpleIntegerProperty(
            FormValues.setpoints.registerNumDefence)
    private val formContent = form {
        fieldset("Защита записи") {
            field(FormValues.setpoints.descriptionWriteDefence) {
                textfield(registerDefence)
                vgrow = Priority.ALWAYS
                hgrow = Priority.ALWAYS
                maxHeight = Double.MAX_VALUE
                maxWidth = Double.MAX_VALUE
            }
            vgrow = Priority.ALWAYS
            hgrow = Priority.ALWAYS
            maxHeight = Double.MAX_VALUE
            maxWidth = Double.MAX_VALUE
        }

        vgrow = Priority.ALWAYS
        hgrow = Priority.ALWAYS
        maxHeight = Double.MAX_VALUE
        maxWidth = Double.MAX_VALUE

        padding = Insets(15.0, 15.0, 0.0, 15.0 )
    }

    override val root = scrollpane {
        vbox {
            hbox {
                button("Сохранить") {
                    action {
                        saveChanges()
                        find(MainForm::class).reloadForm()
                        close()
                    }
                }
                button("Закрыть") {
                    action {
                        close()
                    }
                }
                padding = Insets(15.0)

                vgrow = Priority.ALWAYS
                hgrow = Priority.ALWAYS
                maxHeight = Double.MAX_VALUE
                maxWidth = Double.MAX_VALUE
            }
            hbox {
                add(formContent)

                vgrow = Priority.ALWAYS
                hgrow = Priority.ALWAYS
                maxHeight = Double.MAX_VALUE
                maxWidth = Double.MAX_VALUE
            }

            vgrow = Priority.ALWAYS
            hgrow = Priority.ALWAYS
            maxHeight = Double.MAX_VALUE
            maxWidth = Double.MAX_VALUE
        }

        maxHeight = 500.0
    }

    init {
        FormValues.setpoints.items.forEach {
            cellsProperties.add( DiscreteOutCellsProperties( it ))
            val param = "cells" to cellsProperties.last()
            formContent.add( find<ShowDiscreteOutFragment>(param) )
        }
    }

    override fun onDock() {
        setWindowMinSize( 580.0, 400.0 )
        setWindowMaxSize( 580.0, 900.0 )
    }

    private fun saveChanges()
    {
        for ( cellProperty in cellsProperties )
        {
            cellProperty.saveParams()
        }
        FormValues.setpoints.registerNumDefence = registerDefence.value
    }
}