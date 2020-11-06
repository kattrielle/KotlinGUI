package kotlinGUI

import externalDevices.devices.ModbusRTU
import externalDevices.ports.IPort
import externalDevices.ports.PortCOM
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import jssc.SerialPortList
import tornadofx.*

class FormSetValuesToDevice : View( "проверка порта" ) {
    private val comPorts = FXCollections.observableArrayList(SerialPortList.getPortNames())
    private var device = ModbusRTU( "" )
    private val selectPort = SimpleStringProperty()
    private val textValConnecting = SimpleStringProperty()
    private val textRegister = SimpleIntegerProperty()
    private val textReadedVal = SimpleStringProperty()
    private val textValueToWrite = SimpleIntegerProperty()
    private val textSendSuccess = SimpleStringProperty()

    override val root = gridpane {
        row( "Выбор порта" ) {
            combobox( selectPort, SerialPortList.getPortNames().toList() )
        }
        row("Подключение") {
            button ( "Connect" ) {
                action{
                    device = ModbusRTU(selectPort.value)
                    val success = device.OpenConnection()
                    textValConnecting.set( "Connecting to ${selectPort.value}... $success" )
                    println( success )
                }
            }
            label(textValConnecting)
        }
        row("Регистр") {
            textfield( textRegister )
        }
        row( "" ) {
            button( "Считать holding" ) {
                action {
                    val ( success, answer ) = device.GetOneHoldingValue( textRegister.value )
                    textReadedVal.set( answer.toString() )
                }
            }
            label(textReadedVal)

        }
        row("") {
            button( "Записать holding")
            {
                action {
                    val success = device.SetHoldingValue( textRegister.value,
                            textValueToWrite.value )
                    textSendSuccess.set( "Sending... $success" )
                }
            }
            textfield( textValueToWrite )
            label( textSendSuccess )
        }
        row( "" ) {
            button( "Close" )
            {
                action {
                    device.CloseConnection()
                    close()
                }
            }
        }
    }

    override fun onDock() {
        currentWindow?.setOnCloseRequest {
            println("Closing")
        }
    }
}