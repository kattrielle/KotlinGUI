package kotlinGUI

import externalDevices.settings.SettingsModbusRTU
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import jssc.SerialPort
import tornadofx.Controller

class DataComConnection : Controller()
{
    val baudrate = FXCollections.observableArrayList(
            SerialPort.BAUDRATE_110,
            SerialPort.BAUDRATE_300,
            SerialPort.BAUDRATE_600,
            SerialPort.BAUDRATE_1200,
            SerialPort.BAUDRATE_2400,
            SerialPort.BAUDRATE_4800,
            SerialPort.BAUDRATE_9600,
            SerialPort.BAUDRATE_14400,
            SerialPort.BAUDRATE_19200,
            SerialPort.BAUDRATE_38400,
            SerialPort.BAUDRATE_57600,
            SerialPort.BAUDRATE_115200,
            SerialPort.BAUDRATE_128000,
            SerialPort.BAUDRATE_256000 )

    val parity = FXCollections.observableArrayList(
            "None", //SerialPort.PARITY_NONE
            "Even", //SerialPort.PARITY_EVEN
            "Mark", //SerialPort.PARITY_MARK
            "Odd", //SerialPort.PARITY_ODD
            "Space" ) //SerialPort.PARITY_SPACE

    val dataBits = FXCollections.observableArrayList(
            SerialPort.DATABITS_5,
            SerialPort.DATABITS_6,
            SerialPort.DATABITS_7,
            SerialPort.DATABITS_8 )

    val stopBits = FXCollections.observableArrayList( "1", //SerialPort.STOPBITS_1
            "1.5", // SerialPort.STOPBITS_1_5
            "2" ) // SerialPort.STOPBITS_2

    val selectPort = SimpleStringProperty( FormValues.settings.name )
    val deviceAddress = SimpleIntegerProperty( FormValues.settings.address.toInt() )
    val selectBaudrate = SimpleIntegerProperty( FormValues.settings.baudRate )
    val selectParity = SimpleStringProperty( "None" )
    val selectDataBits = SimpleIntegerProperty( FormValues.settings.dataBits )
    val selectStopBits = SimpleStringProperty( "1" )
    val selectDelayAnswerRead = SimpleIntegerProperty( FormValues.settings.delayAnswerRead )
    val selectDelayAnswerWrite = SimpleIntegerProperty( FormValues.settings.delayAnswerWrite )

    fun setModbusParameters() : SettingsModbusRTU
    {
        val settings = SettingsModbusRTU( selectPort.value )
        settings.address = deviceAddress.value.toByte()
        settings.baudRate = selectBaudrate.value
        settings.dataBits = selectDataBits.value
        settings.delayAnswerRead = selectDelayAnswerRead.value
        settings.delayAnswerWrite = selectDelayAnswerWrite.value

        when ( selectParity.value )
        {
            "None" -> settings.parity = SerialPort.PARITY_NONE
            "Even" -> settings.parity = SerialPort.PARITY_EVEN
            "Mark" -> settings.parity = SerialPort.PARITY_MARK
            "Odd" -> settings.parity = SerialPort.PARITY_ODD
            "Space" -> settings.parity = SerialPort.PARITY_SPACE
        }

        when ( selectStopBits.value )
        {
            "1" -> settings.stopBits = SerialPort.STOPBITS_1
            "1.5" -> settings.stopBits = SerialPort.STOPBITS_1_5
            "2" -> settings.stopBits = SerialPort.STOPBITS_2
        }

        return settings
    }
}