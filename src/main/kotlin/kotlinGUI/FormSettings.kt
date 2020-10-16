package kotlinGUI

import javafx.collections.FXCollections
import jssc.SerialPortList
import tornadofx.*

class FormSettings: View("Параметры") {
    private val comPorts = FXCollections.observableArrayList(SerialPortList.getPortNames())

    override val root = form {
        fieldset("Параметры связи") {
            field("Адрес устройства:") {
                combobox(values = comPorts)
                //listview(comPorts)
            }
        }
        hbox {
            button("OK")
            {
                action {
                    println("Button pressed!")
                }
            }
            button("Close")
            {
                action {
                    close()
                }
            }
        }
    }
}