package kotlinGUI

import externalDevices.devices.ModbusRTU
import jssc.SerialPortList
import tornadofx.*

class FormSettings: View("Параметры") {
    private val dataComConnection : DataComConnection by inject()

    override val root = form {
        fieldset("Устройство") {
            field("Порт:") {
                combobox( dataComConnection.selectPort, SerialPortList.getPortNames().toList() )
            }
            field("Адрес устройства:") {
                textfield( dataComConnection.deviceAddress )
            }

        }
        fieldset("Параметры соединения") {
            field("Скорость:") {
                combobox( dataComConnection.selectBaudrate, dataComConnection.baudrate )
            }
            field( "Чётность:" ) {
                combobox( dataComConnection.selectParity, dataComConnection.parity )
            }
            field("Биты данных:") {
                combobox( dataComConnection.selectDataBits, dataComConnection.dataBits )
            }
            field("Стоп-бит:") {
                combobox( dataComConnection.selectStopBits, dataComConnection.stopBits )
            }
        }
        hbox {
            button("OK")
            {
                action {
                    println("Button pressed!")
                    FormValues.settings = dataComConnection.setModbusParameters()
                    FormValues.device = ModbusRTU( FormValues.settings )
                    close()
                }
            }
            button("Close")
            {
                action {
                    close()
                }
                shortcut("Esc")
            }
        }
    }
}