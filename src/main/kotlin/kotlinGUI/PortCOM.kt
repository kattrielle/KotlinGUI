//package ExternalDevices
package kotlinGUI

import jssc.SerialPort

class PortCOM (settings : SettingsCOM ) : SerialPort(settings.name), IPort {
    override var isOpen : Boolean = false //fix???
    get() = false //есть ли свойство, определяющее, открыт ли данный порт?

    override fun OpenPort() : Boolean
    {
        println("Opening...")
        if ( isOpen )
        {
            return true
        }
        return openPort()
    }

    override fun ClosePort() : Boolean
    {
        println("Closing...")
        return closePort()
    }

    override fun SendMessage(message: String, delay: Int) : Boolean 
    {
        println("sending $message")
        return true
    }

    override fun SendMessage(message: Array<Byte>, delay: Int) : Boolean
    {
        println( "sending some bytes..." )
        return true
    }

    override fun SendQuery(message:String): Pair<Boolean, String>
    {
        println("sending $message")
        return Pair(true,"")
    }

    override fun SendQuery(message: Array<Byte>, delay:Int, 
        answerLen: Int, attempts: Int): Pair<Boolean,Array<Byte>>
    {
        println("sending some bytes...")
        val result = arrayOf<Byte>(0,1)
        return Pair(true,result)
    }
    
    private fun WaitPortAnswer() : Boolean
    {
        return false
    }
}