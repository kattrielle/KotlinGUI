package kotlinGUI

import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Insets
import javafx.scene.control.SelectionMode
import javafx.scene.control.TabPane
import javafx.scene.layout.Priority
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
        selectDefence.onChange {
            FormValues.setpoints.registerWriteDefence =
                    FormValues.findRegister( selectDefence.value )
        }

        if ( FormValues.setpoints.items.isNotEmpty() )
        {
            FormValues.discreteOutProperties.clear()
            for ( i in FormValues.setpoints.items.indices ) {
                addDiscreteOutTab( FormValues.setpoints.items[ i ] )
            }
        }
    }

    override val root = gridpane {
        val registers = FormValues.tikModscanMap?.сellsArray.asList().asObservable()
        tableview ( registers ) {
            readonlyColumn("Адрес", CellData::address) //@todo надо сдвинуть адреса на 1, как?
            readonlyColumn("Название", CellData::name)

            contextmenu {
                item("Перенести в уставку").action {

                }
                item("Автоматический выбор регистров адреса выборки для уставок").action {

                }
                item("Автоматический выбор регистров значений для уставок").action {

                }
                item("Автоматический выбор регистров времени установки для уставок").action {

                }
                item("Автоматический выбор регистров времени снятия для уставок").action {

                }
                item("Автоматический выбор регистров веса для уставок").action {

                }
            }

            //selectionModel.selectionMode = SelectionMode.SINGLE

            onSelectionChange {
                println( "at table row selected:" + selectedCell?.row)
                columnName = selectedItem?.name ?: ""
            }
        }

        vbox {
            button("Защита →") {
                maxWidth = Double.MAX_VALUE
                hgrow = Priority.ALWAYS
                action {
                    println( FormValues.getCurrentTime() + "setting defence register to $columnName")
                    selectDefence.set( columnName )
                }
            }
            button("Выборка →") {
                maxWidth = Double.MAX_VALUE
                hgrow = Priority.ALWAYS
                action {
                    if ( selectedTab > -1 && FormValues.discreteOutProperties.size > selectedTab )
                    {
                        println( FormValues.getCurrentTime() + "setting register of sample to $columnName")
                        FormValues.discreteOutProperties[selectedTab].selectValues.set( columnName )
                    }
                }
            }
            button("Адрес →") {
                maxWidth = Double.MAX_VALUE
                hgrow = Priority.ALWAYS
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
                maxWidth = Double.MAX_VALUE
                hgrow = Priority.ALWAYS
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
                maxWidth = Double.MAX_VALUE
                hgrow = Priority.ALWAYS
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
                maxWidth = Double.MAX_VALUE
                hgrow = Priority.ALWAYS
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
                maxWidth = Double.MAX_VALUE
                hgrow = Priority.ALWAYS
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
                margin = Insets( 5.0 )
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
                    addDiscreteOutTab( FormValues.setpoints.items.last() )
                }
            }
            add( tabPane )

            gridpaneConstraints {
                columnRowIndex(2,0)
            }
        }

    }

    fun addDiscreteOutTab( discreteOut : DiscreteOut )
    {
        FormValues.discreteOutProperties.add(
                DiscreteOutViewProperties( discreteOut ) )
        val param = "discreteOut" to FormValues.discreteOutProperties.last()
        tabPane.add( find<DiscreteOutFragment>( param ))
    }
}