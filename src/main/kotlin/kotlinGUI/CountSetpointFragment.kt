package kotlinGUI

import countSetpoints.CountSetpoints
import countSetpoints.CountSetpointsDescriptions
import javafx.application.Platform
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.RadioButton
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import registerCollection.DiscreteOut
import tornadofx.*
import kotlin.concurrent.thread

class CountSetpointFragment : Fragment() {
    private val sampleLen = SimpleIntegerProperty()
    private val selectCount = SimpleStringProperty(  )
    private val percent = SimpleDoubleProperty()
    private val getPropertiesProgress = SimpleDoubleProperty()
    private val getSampleProgress = SimpleDoubleProperty()
    private val writePropertiesProgress = SimpleDoubleProperty()

    override val root = gridpane {
        vbox {
            hbox {
                button("Загрузить параметры") {
                    action {
                        thread {
                            isDisable = true
                            println(FormValues.getCurrentTime() + "getting parameters begin")
                            getPropertiesProgress.set(0.0)
                            FormValues.setpoints.getParameterValues(
                                    FormValues.device, getPropertiesProgress)
                            FormValues.device.CloseConnection()
                            println(FormValues.getCurrentTime() + "getting parameters done")
                            isDisable = false
                        }

                    }
                }
                progressindicator( getPropertiesProgress )
            }
            hbox {
                button("Записать") {
                    action {
                        thread {
                            isDisable = true
                            println(FormValues.getCurrentTime() + "writing parameters begin")
                            writePropertiesProgress.set(0.0)
                            FormValues.setpoints.writeParameterValues(
                                    FormValues.device, writePropertiesProgress)
                            FormValues.device.CloseConnection()
                            println(FormValues.getCurrentTime() + "writing parameters done")
                            isDisable = false
                        }
                    }
                }
                progressindicator( writePropertiesProgress )
            }
        }
        vbox {
            text("Размер выборки, запросов")
            textfield( sampleLen )
            hbox {
                button("Считать") {
                    action {
                        thread {
                            isDisable = true
                            println(FormValues.getCurrentTime() + "getting sample begin")
                            getSampleProgress.set (0.0)
                            FormValues.setpoints.getSamples(sampleLen.value,
                                    FormValues.device, getSampleProgress)
                            FormValues.device.CloseConnection()
                            println(FormValues.getCurrentTime() + "getting sample done")
                            isDisable = false
                        }
                    }
                }
                progressindicator( getSampleProgress )
            }
            gridpaneConstraints {
                columnRowIndex(1,0)
            }
        }
        vbox {
            combobox(selectCount, CountSetpointsDescriptions.getCountTypesList())
            //label(CountSetpointsDescriptions.getCountDescription( selectCount.value ))
            hbox {
                text("n=")
                textfield( percent )
            }
            button("Расчёт") {
                action {
                    FormValues.setpoints.countSetpointValues(
                            selectCount.value, percent.value)
                }
            }
            gridpaneConstraints {
                columnRowIndex(2,0)
            }
        }
        tableview( FormValues.setpoints.items ) {
            isEditable = true
            readonlyColumn("Выборка", DiscreteOut::descriptionValues)
            readonlyColumn("Регистр выборки", DiscreteOut::registerValues)
            column("Адрес выборки", DiscreteOut::valueSetpointSampleProperty )
            column("Значение уставки",DiscreteOut::valueSetpointProperty ).makeEditable()
            column("Время установки", DiscreteOut::valueTimeSetProperty).makeEditable()
            column("Время снятия", DiscreteOut::valueTimeUnsetProperty).makeEditable()
            column( "Вес", DiscreteOut::valueWeightProperty ).makeEditable()

            //contextmenu {}

            maxHeight( Double.MAX_VALUE )
            maxWidth( Double.MAX_VALUE )
            minWidth = 990.0
            vgrow = Priority.ALWAYS
            hgrow = Priority.ALWAYS

            gridpaneConstraints {
                columnRowIndex(0,1)
                columnSpan = 3
            }
        }
    }

}