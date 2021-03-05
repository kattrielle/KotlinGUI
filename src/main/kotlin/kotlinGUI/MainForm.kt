package kotlinGUI

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import javafx.stage.FileChooser
import registerCollection.DiscreteOutCollection
import registerCollection.DiscreteOutCollectionMapper
import registerMapTikModscan.SerializableCellsContainer
import saveableProperties.PropertiesSaver
import tornadofx.*
import java.io.File

class MainForm: View("Настройка уставок")
{
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
                            FormValues.clearDiscreteOutCollection()

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
                    action {
                        workCompletion()
                        close()
                    }
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
    }

    override fun onDock() {
        currentWindow?.setOnCloseRequest {
            workCompletion()
        }
        loadDiscreteOutMap( FormValues.savedProperties.selectedSetpointMapFile )
        reloadForm()
    }

    private fun loadTikModscanMap() : Boolean
    {
        val extension = arrayOf(FileChooser.ExtensionFilter(
                "Tik-Modscan Xml Map (*.xml)", "*.xml"))

        val file = chooseFile( "", extension,  )
        try {
            return if (file.isNotEmpty()) {
                val fileName = file.first()
                println(FormValues.getCurrentTime() + "selected Tik-Modscan file: " + fileName )
                println("directory " + fileName.absolutePath)
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
            val file = chooseFile("", extension)
            if (file.isNotEmpty()) {
                loadDiscreteOutMap( file.first().path )
                FormValues.savedProperties.selectedSetpointMapFile = file.first().path
            }
        } catch ( e : Exception )
        {
            println( FormValues.getCurrentTime() + "Error at loading SetpointMap : ${e.message}" )
        }
    }

    private fun loadDiscreteOutMap( fileName : String )
    {
        try {
            val file = File(fileName)
            println(FormValues.getCurrentTime() + "selected SetpointsMap file: " + fileName)
            val xmlMapper = XmlMapper()
            FormValues.setpoints = DiscreteOutCollection(
                    xmlMapper.readValue(file,
                            DiscreteOutCollectionMapper::class.java))
            FormValues.discreteOutTableViewProperties.clear()
            //FormValues.discreteOutModel.clear()
            FormValues.updateDiscreteOutTableViewPropertiesList()
        } catch (e: Exception) {
            println(FormValues.getCurrentTime() + "Error at loading SetpointMap : ${e.message}")
        }
    }

    private fun saveDiscreteOutMap()
    {
        val extension = arrayOf(FileChooser.ExtensionFilter(
                "Xml Setpoint Map (*.xml)", "*.xml"))
        val file = chooseFile( "", extension, mode = FileChooserMode.Save )

        if ( file.isNotEmpty() )
        {
            val fileName = file.first()
            println( FormValues.getCurrentTime() + "selected SetpointsMap file: " + fileName )
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

    private fun workCompletion()
    {
        PropertiesSaver.SaveProperties()
    }

}