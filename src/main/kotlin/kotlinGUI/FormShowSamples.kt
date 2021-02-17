package kotlinGUI

import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.Parent
import javafx.scene.chart.NumberAxis
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import tornadofx.*

class FormShowSamples : View("Отображение выборок") {
    private val sampleCheckList = createCheckList()
    private val chartContainer = VBox()

    override val root = hbox {
        add( chartContainer )
        chartContainer.maxHeight = Double.MAX_VALUE
        chartContainer.vgrow = Priority.ALWAYS
        addChart( chartContainer )
        vbox {
            addCheckBoxes( this )

            maxHeight = Double.MAX_VALUE
            vgrow = Priority.ALWAYS
        }
    }

    init {
        sampleCheckList.forEach { it.onChange {
            redrawChart( chartContainer )
        } }
    }

    private fun addCheckBoxes( layout : Parent )
    {
        for ( i in FormValues.setpoints.items.indices ) {
            layout.add( checkbox( FormValues.setpoints.items[i].descriptionValues,
                    sampleCheckList[i] ) )
        }
    }

    private fun addChart( layout: Parent ) {
        layout.add(
                linechart("", NumberAxis(), NumberAxis()) {
                    for ( index in FormValues.setpoints.items.indices ) {
                        if ( sampleCheckList[index].value ) {
                            val discreteOut = FormValues.setpoints.items[index]
                            series( discreteOut.descriptionValues ) {
                                for (i in discreteOut.sample.indices) {
                                    data(i, discreteOut.sample[i])
                                }
                            }
                        }
                    }
                })
    }

    private fun redrawChart( layout : Parent )
    {
        layout.getChildList()?.clear()
        addChart( layout )
    }

    private fun createCheckList() : List<SimpleBooleanProperty>
    {
        val result = mutableListOf<SimpleBooleanProperty>()
        for ( i in FormValues.setpoints.items.indices ) {
            result.add(SimpleBooleanProperty(true))
        }
        return result
    }

}