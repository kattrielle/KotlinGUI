package externalDevices.settings

import jssc.SerialPort

open class SettingsCOM( var name : String ) : PortSettings()
{
	var baudRate : Int = SerialPort.BAUDRATE_9600
	var dataBits : Int = SerialPort.DATABITS_8
    var parity : Int = SerialPort.PARITY_NONE
    var stopBits : Int = SerialPort.STOPBITS_1
    var dtrEnabled : Boolean = false
    var readTimeout : Int = 750
    var writeTimeout : Int = 750

	override val description : String
    get() = name

	override fun CheckName() : Boolean
    {
        return name.isNotEmpty()
    }
}