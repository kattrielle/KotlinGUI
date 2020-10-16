package kotlinGUI

import javafx.scene.paint.Color
import tornadofx.*

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
        bottom = button("big red btn")
        {
            textFill = Color.RED
            action {
                openInternalWindow<FormSettings>()
            }
        }
    }
}