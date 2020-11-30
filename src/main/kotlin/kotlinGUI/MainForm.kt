package kotlinGUI

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import javafx.scene.control.ToggleGroup
import javafx.scene.paint.Color
import javafx.stage.FileChooser
import registerCollection.DiscreteOutCollection
import registerMapTikModscan.SerializableCellsContainer
import tornadofx.*

class MainForm: View()
{
    private var mainFragment = CountSetpointFragment()

    override val root = borderpane {
        top = menubar {
            menu("Файл")
            {
                item("Загрузить карту регистров") {
                    action {
                        if  (loadTikModscanMap()) {
                            val window = FormSelectRegisters()
                            window.openModal()
                        }
                    }
                }
                item("Открыть...","Shortcut+O") {
                    action {
                        loadDiscreteOutMap()
                        reloadForm()
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
                    action {
                        val window = FormSettings()
                        window.openModal()
                    }
                }
                item("Просмотр адресов регистров") {
                    action {
                        val window = FormShowRegisters()
                        window.openModal()
                    }
                }
            }
        }
        center<CountSetpointFragment>()
        bottom = vbox {
            vbox {

            }
            button("big red btn")
            {
                textFill = Color.RED
                action {
                    openInternalWindow<FormSetValuesToDevice>()
                }
            }
        }
        setMinSize(500.0, 300.0)
    }

    private fun loadTikModscanMap() : Boolean
    {
        val extension = arrayOf(FileChooser.ExtensionFilter(
                "Tik-Modscan Xml Map (*.xml)", "*.xml"))

        var file = chooseFile( "", extension )
        return if ( file.isNotEmpty() )
        {
            FormValues.file = file.first()
            println( FormValues.getCurrentTime() + "selected Tik-Modscan file:" + FormValues.file )
            val xmlMapper = XmlMapper()
            FormValues.tikModscanMap = xmlMapper.readValue(
                    FormValues.file,
                    SerializableCellsContainer::class.java)
            true
        } else {
            false
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

    fun reloadForm()
    {
        root.center.getChildList()?.clear()
        root.center.add( CountSetpointFragment() )
    }

}