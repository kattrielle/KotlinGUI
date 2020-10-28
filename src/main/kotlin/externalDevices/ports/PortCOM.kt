package externalDevices.ports

import externalDevices.settings.SettingsCOM
import jssc.SerialPort

class PortCOM (name: String) : SerialPort(name), IPort {

    constructor(settings : SettingsCOM) : this( settings.name )
    {
        SetPortParameters( settings )
    }
    override var isOpen : Boolean = false //fix???
    get() = isOpened

    override var delayAnswerRead: Int
        get() = TODO("Not yet implemented")
        set(value) {}

    override var delayAnswerWrite: Int
        get() = TODO("Not yet implemented")
        set(value) {}

    fun SetPortParameters( settings: SettingsCOM) : Boolean
    {
        //как установить параметры, если они только к открытому порту применяются?
        if (!OpenPort()) {
            return false
        }
        setParams(settings.baudRate, settings.dataBits,
                settings.stopBits, settings.parity)
        ClosePort()
        return true
    }

    override fun OpenPort() : Boolean
    {
        //нужен ли мне тут трай-кэч, как в старой версии софта?
        println("Opening $portName...")
        return try {
            if (isOpen) {
                return true
            }
            openPort()
        } catch ( e : Exception )
        {
            false
        }
    }

    override fun ClosePort() : Boolean
    {
        println("Closing...")
        return closePort()
    }

    override fun SendMessage(message: String) : Boolean
    {
        println("sending $message")
        writeString( message )
        //Sleep(delayAnswerWrite) --- что делать с этим куском кода?
        return true
    }

    override fun SendMessage(message: Array<Byte>) : Boolean
    {
        println( "sending some bytes..." )
        //нужны ли тут дискардбуфферс?
        writeBytes( message.toByteArray() )
        //Sleep(delayAnswerWrite)
        return true
    }

    override fun SendQuery(message:String): Pair<Boolean, String>
    {
        println("sending $message")
        return try {
            SendMessage(message)
            val answer = readString()
            Pair( answer != null, answer )
        } catch ( e : Exception ) {
            Pair(false, "")
        }
    }

    override fun SendQuery(message: Array<Byte>, answerLen: Int,
                           attempts: Int): Pair<Boolean,Array<Byte>>
    {
        println("sending some bytes...")
        try {
            for (i in 0 until attempts )
            {
                SendMessage( message )
                val answer = readBytes()
                if ( answer != null )
                {
                    return Pair ( true, answer.toTypedArray() )
                }
            }
        } catch ( e : Exception )
        {

        }
        val result = arrayOf<Byte>(0,1)
        return Pair(false,result)
    }
    
    private fun WaitPortAnswer() : Boolean
    {
        return false
    }
}