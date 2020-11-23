package kotlinGUI

import countSetpoints.CountSetpoints
import countSetpoints.CountSetpointsDescriptions
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.RadioButton
import javafx.scene.control.ToggleGroup
import javafx.scene.paint.Color
import registerCollection.DiscreteOut
import tornadofx.*

class CountSetpointFragment : Fragment() {
    private val discreteOutGroup = ToggleGroup()
    private val sampleLen = SimpleIntegerProperty()
    private val selectCount = SimpleStringProperty(  )
    private val percent = SimpleDoubleProperty()
    private val countResult = SimpleDoubleProperty()
    private val timeSet = SimpleIntegerProperty()
    private val timeUnset = SimpleIntegerProperty()
    private var discreteOut : DiscreteOut? = null
    private var sample : List<Double> = listOf()

    override val root = hbox {
        vbox {
            for (discreteOut in FormValues.setpoints.items )
            {
                radiobutton( discreteOut.descriptionValues, discreteOutGroup )
            }
        }
        vbox {
            text("Размер выборки")
            textfield( sampleLen )
            button("Считать") {
                action {
                    val btn = discreteOutGroup.selectedToggle as RadioButton
                    discreteOut = FormValues.findSetpoint( btn.text )
                    style { backgroundColor += Color.GRAY }
                    sample = discreteOut?.getSample( sampleLen.value, FormValues.device )!!
                    style { backgroundColor += Color.WHITE }
                }
            }
        }
        vbox {
            combobox(selectCount, CountSetpointsDescriptions.getCountTypesList())
            //text(CountSetpointsDescriptions.getCountDescription( selectCount.value ))
            hbox {
                text("n=")
                textfield( percent )
            }
            button("Расчёт") {
                action {
                    countResult.value = CountSetpoints.count(
                            CountSetpointsDescriptions.selectCountType(selectCount.value),
                            sample, percent.value )
                }
            }
        }
        vbox{
            text("Величина уставки")
            textfield(countResult)
            text("Время установки")
            textfield( timeSet )
            text("Время снятия")
            textfield( timeUnset )
            button("Записать") {
                action {
                    style { backgroundColor += Color.GRAY }
                    discreteOut?.writeSetpointValue( countResult.value, FormValues.device )
                    discreteOut?.writeTimeSetValue( timeSet.value, FormValues.device )
                    discreteOut?.writeTimeUnsetValue( timeUnset.value, FormValues.device )
                    style { backgroundColor += Color.WHITE }
                }
            }
        }
    }

}