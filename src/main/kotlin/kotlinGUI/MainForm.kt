package kotlinGUI

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import javafx.scene.control.ToggleGroup
import javafx.scene.paint.Color
import javafx.stage.FileChooser
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
                        loadTikModscanMap()
                        //openInternalWindow<FormSelectRegisters>()
                        val window = FormSelectRegisters()
                        //window.openWindow()
                        window.openModal( )
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
                        openInternalWindow<FormSettings>()
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
        println("redrawing")
        root.center.getChildList()?.clear()
        root.center.add( CountSetpointFragment() )
    }

}