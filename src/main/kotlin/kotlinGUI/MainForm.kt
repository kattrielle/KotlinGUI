package kotlinGUI

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import javafx.scene.paint.Color
import registerMapTikModscan.SerializableCellsContainer
import tornadofx.*
import java.io.File

class MainMenu: Fragment()
{
    override val root = menubar {
        menu("Файл")
        {
            item("Выход")
            {
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
}

class MainForm: View()
{
    override val root = borderpane {
        top<MainMenu>()
        center = vbox {
            button("big red btn")
            {
                textFill = Color.RED
                action {
                    //openInternalWindow<FormSettings>()
                    openInternalWindow<FormSetValuesToDevice>()
                }
            }
            button("Parse xml")
            {
                action {
                    val xmlMapper = XmlMapper()
                            //xmlMapper.setPropertyNamingStrategy(
                            //TikModscanNamingStrategy()
                            //PropertyNamingStrategy.LOWER_CAMEL_CASE )
                    //xmlMapper.propertyNamingStrategy = PropertyNamingStrategy.LOWER_CASE
                    val map = xmlMapper.readValue(
                            File("/home/kate/Загрузки/Telegram Desktop/пользовательская_карта_регистров_v6.xml"),
                            SerializableCellsContainer::class.java)
                    println( map.guiData?.formatShowing )
                }
            }
        }
        setMinSize(500.0, 300.0)
    }
}