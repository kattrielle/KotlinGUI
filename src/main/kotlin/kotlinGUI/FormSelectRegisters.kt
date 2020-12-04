package kotlinGUI

import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.SelectionMode
import javafx.scene.control.TabPane
import registerCollection.DiscreteOut
import registerCollection.DiscreteOutViewProperties
import registerMapTikModscan.CellData
import tornadofx.*

class FormSelectRegisters : View( "Задание параметров цифровых выходов" )
{
    private val selectDefence = SimpleStringProperty()
    private val tabPane = TabPane()
    private var columnName = ""
    private var selectedTab = -1
        get() = tabPane.selectionModel.selectedIndex

    init {
        FormValues.setpoints.items.clear()

        selectDefence.onChange {
            FormValues.setpoints.registerWriteDefence =
                    FormValues.findRegister( selectDefence.value )
        }
    }

    override val root = gridpane {
        val registers = FormValues.tikModscanMap?.сellsArray.asList().asObservable()
        tableview ( registers ) {
            readonlyColumn("Регистр", CellData::address) //@todo надо сдвинуть адреса на 1, как?
            readonlyColumn("Название", CellData::name)

            selectionModel.selectionMode = SelectionMode.SINGLE

            onSelectionChange {
                println( "at table row selected:" + selectedCell?.row)
                columnName = selectedItem?.name ?: ""
            }
        }

        vbox {
            button("Защита →") {
                action {
                    println( FormValues.getCurrentTime() + "setting defence register to $columnName")
                    selectDefence.set( columnName )
                }
            }
            button("Выборка →") {
                action {
                    if ( selectedTab > -1 && FormValues.discreteOutProperties.size > selectedTab )
                    {
                        println( FormValues.getCurrentTime() + "setting register of sample to $columnName")
                        FormValues.discreteOutProperties[selectedTab].selectValues.set( columnName )
                    }
                }
            }
            button("Адрес →") {
                action {
                    if ( selectedTab > -1 && FormValues.discreteOutProperties.size > selectedTab )
                    {
                        println( FormValues.getCurrentTime() +
                                "setting register of sample address at setpoint to $columnName")
                        FormValues.discreteOutProperties[selectedTab].selectSetpointSample.set( columnName )
                    }
                }
            }
            button("Уставка →") {
                action {
                    if ( selectedTab > -1 && FormValues.discreteOutProperties.size > selectedTab )
                    {
                        println( FormValues.getCurrentTime() +
                                "setting register of setpoint value to $columnName")
                        FormValues.discreteOutProperties[selectedTab].selectSetpoint.set( columnName )
                    }
                }
            }
            button("Время установки →") {
                action {
                    if ( selectedTab > -1 && FormValues.discreteOutProperties.size > selectedTab )
                    {
                        println( FormValues.getCurrentTime() +
                                "setting register of setpoint time set to $columnName")
                        FormValues.discreteOutProperties[selectedTab].selectTimeSet.set( columnName )
                    }
                }
            }
            button("Время снятия →") {
                action {
                    if ( selectedTab > -1 && FormValues.discreteOutProperties.size > selectedTab )
                    {
                        println( FormValues.getCurrentTime() +
                                "setting register of setpoint time unset to $columnName")
                        FormValues.discreteOutProperties[selectedTab].selectTimeUnset.set( columnName )
                    }
                }
            }
            button("Вес →") {
                action {
                    if ( selectedTab > -1 && FormValues.discreteOutProperties.size > selectedTab )
                    {
                        println( FormValues.getCurrentTime() +
                                "setting register of setpoint weight to $columnName")
                        FormValues.discreteOutProperties[selectedTab].selectWeight.set( columnName )
                    }
                }
            }
            gridpaneConstraints {
                columnRowIndex(1,0)
            }
        }

        vbox {
            button("Закрыть") {
                action {
                    find(MainForm::class).reloadForm()
                    close()
                }
            }
            gridpaneConstraints {
                columnRowIndex(0,1)
            }
        }

        vbox {
            hbox {
                text("Регистр защиты от записи")
                textfield( selectDefence )
            }

            button("Добавить уставку") {
                action {
                    FormValues.setpoints.items.add( DiscreteOut())
                    FormValues.discreteOutProperties.add(
                            DiscreteOutViewProperties(FormValues.setpoints.items.last()) )
                    val param = "discreteOut" to FormValues.discreteOutProperties.last()
                    tabPane.add( find<DiscreteOutFragment>( param ))
                }
            }
            add( tabPane )

            gridpaneConstraints {
                columnRowIndex(2,0)
            }
        }

    }
}