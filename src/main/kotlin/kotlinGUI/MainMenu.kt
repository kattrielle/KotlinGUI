package kotlinGUI

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import javafx.stage.FileChooser
import registerMapTikModscan.SerializableCellsContainer
import tornadofx.*

class MainMenu: Fragment()
{
    override val root = menubar {
        menu("Файл")
        {
            item("Загрузить карту регистров") {
                action { loadTikModscanMap() }
            }
            item("Открыть...","Shortcut+O") {
                action {
                    loadDiscreteOutMap()
                    println("Opening file")
                }
            }
            item("Сохранить","Shortcut+S") {
                action {
                    println("Saving registers")
                    saveDiscreteOutMap()
                }

            }
            separator()
            item("Выход") {
                action { close() }
            }
        }
        menu("Параметры") {
            item("Настройки")
            {
                action { openInternalWindow<FormSettings>() }
            }
        }
    }

    private fun loadTikModscanMap()
    {
        val extension = arrayOf(FileChooser.ExtensionFilter(
                "Tik-Modscan Xml Map (*.xml)", "*.xml"))

        var file = chooseFile( "", extension )
        if ( file.isNotEmpty() )
        {
            FormValues.file = file.first()
            val xmlMapper = XmlMapper()
            FormValues.tikModscanMap = xmlMapper.readValue(
                    FormValues.file,
                    SerializableCellsContainer::class.java)
            openInternalWindow<FormSelectRegisters>()
        }
    }

    private fun loadDiscreteOutMap()
    {
        val extension = arrayOf(FileChooser.ExtensionFilter(
                "Tik-Modscan Xml Map (*.xml)", "*.xml"))

        var file = chooseFile( "", extension )
        if ( file.isNotEmpty() )
        {
            FormValues.file = file.first()
            val xmlMapper = XmlMapper()
            FormValues.setpoints = xmlMapper.readValue(
                    FormValues.file,
                    DiscreteOutCollection::class.java)
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
            val xmlMapper = XmlMapper()
            xmlMapper.writeValue(
                    FormValues.file, FormValues.setpoints)
        }
    }
}