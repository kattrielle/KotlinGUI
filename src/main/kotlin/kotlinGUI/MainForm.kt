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
    }

    private fun loadTikModscanMap() : Boolean
    {
        val extension = arrayOf(FileChooser.ExtensionFilter(
                "Tik-Modscan Xml Map (*.xml)", "*.xml"))

        var file = chooseFile( "", extension )
        try {
            return if (file.isNotEmpty()) {
                FormValues.file = file.first()
                println(FormValues.getCurrentTime() + "selected Tik-Modscan file:" + FormValues.file)
                val xmlMapper = XmlMapper()
                FormValues.tikModscanMap = xmlMapper.readValue(
                        FormValues.file,
                        SerializableCellsContainer::class.java)
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
                FormValues.file = file.first()
                println(FormValues.getCurrentTime() + "selected SetpointsMap file:" + FormValues.file)
                val xmlMapper = XmlMapper()
                FormValues.setpoints = DiscreteOutCollection(
                        xmlMapper.readValue(FormValues.file,
                                DiscreteOutCollectionMapper::class.java))
            }
        } catch ( e : Exception )
        {
            println( FormValues.getCurrentTime() + "Error at loading SetpointMap : ${e.message}" )
        }
    }

    private fun saveDiscreteOutMap()
    {
        val extension = arrayOf(FileChooser.ExtensionFilter(
                "Tik-Modscan Xml Map (*.xml)", "*.xml"))
        var file = chooseFile( "", extension, mode = FileChooserMode.Save )

        if ( file.isNotEmpty() )
        {
            FormValues.file = file.first()
            println( FormValues.getCurrentTime() + "selected SetpointsMap file:" + FormValues.file )
            val xmlMapper = XmlMapper()
            val map = DiscreteOutCollectionMapper ( FormValues.setpoints )
            xmlMapper.writeValue( FormValues.file, map )
        }
    }

    fun reloadForm()
    {
        root.center.getChildList()?.clear()
        root.center.add( CountSetpointFragment() )
    }

}