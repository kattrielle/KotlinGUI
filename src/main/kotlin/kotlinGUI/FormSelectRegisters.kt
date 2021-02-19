package kotlinGUI

import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Insets
import javafx.scene.control.TabPane
import javafx.scene.layout.Priority
import kotlinGUI.viewModel.DiscreteOutModel
import kotlinGUI.viewModel.DiscreteOutProperties
import kotlinGUI.viewModel.SearchRegisterProperties
import registerCollection.DiscreteOut
import kotlinGUI.viewModel.DiscreteOutViewProperties
import registerMapTikModscan.CellData
import tornadofx.*

class FormSelectRegisters : View( "Задание параметров цифровых выходов" )
{
    private val selectDefence = SimpleStringProperty()
    private val tabPane = TabPane()
    private var columnName = ""
    private val selectedTab
        get() = tabPane.selectionModel.selectedIndex
    private val searchDescriptions = SearchRegisterProperties()

    init {
        selectDefence.onChange {
            FormValues.setpoints.registerWriteDefence =
                    FormValues.findRegister( selectDefence.value )
        }

        if ( FormValues.setpoints.items.isNotEmpty() )
        {
            redrawDiscreteOutTabPane()
        }
    }

    override val root = gridpane {
        val registers = FormValues.tikModscanMap.сellsArray.asList().asObservable()
        tableview ( registers ) {
            readonlyColumn("Адрес", CellData::address) //@todo надо сдвинуть адреса на 1, как?
            readonlyColumn("Название", CellData::name)

            contextmenu {
                item("Автоматический выбор регистров адреса выборки для уставок").action {
                    val registersList = findRegistersByDescription( searchDescriptions.setpointSampleDescription.value,
                            searchDescriptions.baseDescription.value )
                    checkSetpointsCount( registersList.size )
                    for ( i in registersList.indices )
                    {
                        println( FormValues.getCurrentTime() +
                                "setting register of sample address at setpoint to ${registersList[i].name}")
                        FormValues.discreteOutProperties[ i ].selectSetpointSample.set( registersList[i].name )
                    }
                }
                item("Автоматический выбор регистров значений для уставок").action {
                    val registersList = findRegistersByDescription( searchDescriptions.setpointDescription.value,
                            searchDescriptions.baseDescription.value )
                    checkSetpointsCount( registersList.size )
                    for ( i in registersList.indices )
                    {
                        println( FormValues.getCurrentTime() +
                                "setting register of setpoint value to ${registersList[i].name}")
                        FormValues.discreteOutProperties[ i ].selectSetpoint.set( registersList[i].name )
                    }
                }
                item("Автоматический выбор регистров времени установки для уставок").action {
                    val registersList = findRegistersByDescription( searchDescriptions.timeSetDescription.value,
                            searchDescriptions.baseDescription.value )
                    checkSetpointsCount( registersList.size )
                    for ( i in registersList.indices )
                    {
                        println( FormValues.getCurrentTime() +
                                "setting register of setpoint time set to ${registersList[i].name}")
                        FormValues.discreteOutProperties[ i ].selectTimeSet.set( registersList[i].name )
                    }
                }
                item("Автоматический выбор регистров времени снятия для уставок").action {
                    val registersList = findRegistersByDescription( searchDescriptions.timeUnsetDescription.value,
                            searchDescriptions.baseDescription.value )
                    checkSetpointsCount( registersList.size )
                    for ( i in registersList.indices )
                    {
                        println( FormValues.getCurrentTime() +
                                "setting register of setpoint time unset to ${registersList[i].name}")
                        FormValues.discreteOutProperties[ i ].selectTimeUnset.set( registersList[i].name )
                    }
                }
                item("Автоматический выбор регистров веса для уставок").action {
                    val registersList = findRegistersByDescription( searchDescriptions.weightDescription.value,
                            searchDescriptions.baseDescription.value )
                    checkSetpointsCount( registersList.size )
                    for ( i in registersList.indices )
                    {
                        println( FormValues.getCurrentTime() +
                                "setting register of setpoint weight to ${registersList[i].name}")
                        FormValues.discreteOutProperties[ i ].selectWeight.set( registersList[i].name )
                    }
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
                    addDiscreteOut()
                }
            }
            add( tabPane )

            gridpaneConstraints {
                columnRowIndex(2,0)
            }
        }

    }

    private fun addDiscreteOut()
    {
        FormValues.setpoints.items.add( DiscreteOut())
        FormValues.discreteOutTableViewProperties.add(
                DiscreteOutProperties(FormValues.setpoints.items.last()) )
        //FormValues.discreteOutModel.add( DiscreteOutModel( FormValues.discreteOutTableViewProperties.last()) )
        addDiscreteOutTab( FormValues.setpoints.items.last(),
                FormValues.setpoints.items.size )
    }

    private fun addDiscreteOutTab(discreteOut : DiscreteOut, num : Int )
    {
        FormValues.discreteOutProperties.add(
                DiscreteOutViewProperties( discreteOut, num ) )
        val property = FormValues.discreteOutProperties.last()
        val param = "discreteOut" to FormValues.discreteOutProperties.last()
        tabPane.add( find<DiscreteOutFragment>( param ))
        tabPane.tabs.last().setOnCloseRequest {
            deleteDiscreteOut( property.description.value.toInt() - 1 )
        }
    }

    private fun redrawDiscreteOutTabPane()
    {
        tabPane.tabs.clear()
        FormValues.discreteOutProperties.clear()
        for ( i in FormValues.setpoints.items.indices ) {
            addDiscreteOutTab( FormValues.setpoints.items[ i ], i + 1 )
        }
    }

    private fun deleteDiscreteOut( num : Int )
    {
        FormValues.setpoints.items.removeAt( num )
        FormValues.discreteOutTableViewProperties.removeAt( num )
        //FormValues.discreteOutModel.removeAt( num )
        tabPane.tabs.removeAt( num )
        FormValues.discreteOutProperties.removeAt( num )
        for ( i in FormValues.discreteOutProperties.indices )
        {
            FormValues.discreteOutProperties[i].description.set( ( i + 1 ).toString() )
        }

    }

    private fun findRegistersByDescription( description : String, baseText : String ) : List<CellData>
    {
        val result = mutableListOf<CellData>()
        for ( cell in FormValues.tikModscanMap.сellsArray )
        {
            if (cell.name?.contains(description) == true && cell.name?.contains( baseText) == true)
            {
                result.add( cell )
            }
        }
        return result
    }

    private fun checkSetpointsCount( length : Int )
    {
        while ( FormValues.setpoints.items.size < length )
        {
            addDiscreteOut()
        }
    }
}