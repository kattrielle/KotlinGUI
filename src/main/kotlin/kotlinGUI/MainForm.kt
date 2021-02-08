package kotlinGUI

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import javafx.stage.FileChooser
import registerCollection.DiscreteOutCollection
import registerCollection.DiscreteOutCollectionMapper
import registerMapTikModscan.SerializableCellsContainer
import tornadofx.*

class MainForm: View("Настройка уставок")
{
    private var mainFragment = CountSetpointFragment()

    override val root = borderpane {
        top = menubar {
            menu("Файл")
            {
                item("Открыть...","Shortcut+O") {
                    action {
                        loadDiscreteOutMap()
                        reloadForm()
                    }
                }

                item("Сохранить","Shortcut+S") {
                    action {
                        println("Saving registers")
                        saveDiscreteOutMap()
                    }
                }

                separator()

                item("Создать из карты регистров") {
                    action {
                        if  (loadTikModscanMap()) {
                            FormValues.setpoints.items.clear()
                            FormValues.discreteOutTableViewProperties.clear()

                            val window = FormSelectRegisters()
                            window.openModal()
                        }
                    }
                }

                item("Отредактировать на основе карты регистров") {
                    action {
                        if ( loadTikModscanMap() ) {
                            val window = FormSelectRegisters()
                            window.openModal()
                        }
                    }
                }

                separator()

                item("Выход") {
                    action { close() }
                }
            }

            menu("Параметры") {
                item("Просмотр адресов регистров") {
                    action {
                        val window = FormShowRegisters()
                        window.openModal()
                    }
                }
                item("Инициализация уставок") {
                    action {
                        val window = FormInitSetpoints()
                        window.openModal()
                    }
                }
                item("Просмотр выборок") {
                    action {
                        val window = FormShowSamples()
                        window.openModal()
                    }
                }

                separator()

                item("Настройки")
                {
                    action {
                        val window = FormSettings()
                        window.openModal()
                    }
                }
            }
        }
        center<CountSetpointFragment>()
        bottom = vbox {

        }
        setMinSize(600.0,500.0)
    }

    private fun loadTikModscanMap() : Boolean
    {
        val extension = arrayOf(FileChooser.ExtensionFilter(
                "Tik-Modscan Xml Map (*.xml)", "*.xml"))

        var file = chooseFile( "", extension )
        try {
            return if (file.isNotEmpty()) {
                val fileName = file.first()
                println(FormValues.getCurrentTime() + "selected Tik-Modscan file:" + fileName )
                val xmlMapper = XmlMapper()
                FormValues.tikModscanMap = xmlMapper.readValue(
                        fileName, SerializableCellsContainer::class.java)
                true
            } else {
                false
            }
        } catch ( e : Exception )
        {
            println( FormValues.getCurrentTime() + "Error at loading Tik-Modscan Map : ${e.message}" )
            return false
        }
    }

    private fun loadDiscreteOutMap()
    {
        val extension = arrayOf(FileChooser.ExtensionFilter(
                "Xml Setpoint Map (*.xml)", "*.xml"))

        try {
            var file = chooseFile("", extension)
            if (file.isNotEmpty()) {
                val fileName = file.first()
                println(FormValues.getCurrentTime() + "selected SetpointsMap file:" + fileName )
                val xmlMapper = XmlMapper()
                FormValues.setpoints = DiscreteOutCollection(
                        xmlMapper.readValue( fileName,
                                DiscreteOutCollectionMapper::class.java))
                FormValues.updateDiscreteOutProperties()
            }
        } catch ( e : Exception )
        {
            println( FormValues.getCurrentTime() + "Error at loading SetpointMap : ${e.message}" )
        }
    }

    private fun saveDiscreteOutMap()
    {
        val extension = arrayOf(FileChooser.ExtensionFilter(
                "Xml Setpoint Map (*.xml)", "*.xml"))
        var file = chooseFile( "", extension, mode = FileChooserMode.Save )

        if ( file.isNotEmpty() )
        {
            val fileName = file.first()
            println( FormValues.getCurrentTime() + "selected SetpointsMap file:" + fileName )
            val xmlMapper = XmlMapper()
            val map = DiscreteOutCollectionMapper ( FormValues.setpoints )
            xmlMapper.writeValue( fileName, map )
        }
    }

    fun reloadForm()
    {
        root.center.getChildList()?.clear()
        root.center.add( CountSetpointFragment() )
    }

}