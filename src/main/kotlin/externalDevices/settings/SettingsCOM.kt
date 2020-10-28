package externalDevices.settings

open class SettingsCOM( var name : String ) : PortSettings()
{
	var baudRate : Int = 9600
	var dataBits : Int = 8
    var parity : Int = 0 //none
    var stopBits : Int = 1 //stopbits.one
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