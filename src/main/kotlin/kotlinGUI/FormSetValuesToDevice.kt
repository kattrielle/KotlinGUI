package kotlinGUI

import externalDevices.ports.IPort
import externalDevices.ports.PortCOM
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.control.ComboBox
import jssc.SerialPortList
import tornadofx.*

class FormSetValuesToDevice : View( "проверка порта" ) {
    private val comPorts = FXCollections.observableArrayList(SerialPortList.getPortNames())
    private lateinit var port : IPort
    private val select = SimpleStringProperty()
    private val textVal = SimpleStringProperty()
    //private val box = combobox ( values = comPorts )
    private val box = combobox( select, SerialPortList.getPortNames().toList() )
    //private val box = combobox( select, listOf( "COM1", "COM2", "COM3" ) )
    private val text = textfield(textVal)

    override val root = gridpane {
        row( "1" ) {
            add( box )
        }
        row("2") {
            button ( "Connect" ) {
                action{
                    port = PortCOM( select.value )
                    val success = port.OpenPort()
                    textVal.set( "Connecting to ${select.value}... $success" )
                    println( success )
                }
            }
            add(text)
        }
    }
}