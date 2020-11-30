package externalDevices.ports

import externalDevices.settings.SettingsCOM
import jssc.SerialPort
import kotlinGUI.FormValues

class PortCOM (name: String) : SerialPort(name), IPort {

    constructor(settings : SettingsCOM) : this( settings.name )
    {
        SetPortParameters( settings )
    }
    override var isOpen : Boolean = false // @todo fix??? или тут всё уже хорошо?
    get() = isOpened

    override var delayAnswerRead: Int = 100

    override var delayAnswerWrite: Int = 100

    fun SetPortParameters( settings: SettingsCOM ) : Boolean
    {
        println("setting COM parameters")
        delayAnswerRead = settings.delayAnswerRead
        delayAnswerWrite = settings.delayAnswerWrite
        if ( portName.isEmpty() || !OpenPort()) {
            return false
        }
        setParams(settings.baudRate, settings.dataBits,
                settings.stopBits, settings.parity)
        ClosePort()
        return true
    }

    override fun OpenPort() : Boolean
    {
        // @todo нужен ли мне тут трай-кэч, как в старой версии софта?
        println( FormValues.getCurrentTime() + "Opening $portName...")
        return try {
            if (isOpen) {
                return true
            }
            openPort()
        } catch ( e : Exception )
        {
            println(FormValues.getCurrentTime() +
                    "При открытии порта произошла ошибка: ${e.message}")
            false
        }
    }

    override fun ClosePort() : Boolean
    {
        println("Closing...")
        if ( !isOpen )
        {
            return true
        }
        return closePort()
    }

    override fun SendMessage(message: String) : Boolean
    {
        if ( !isOpen ){
            OpenPort()
        }
        println( FormValues.getCurrentTime() + "sending $message")
        writeString( message )
        // @todo Sleep(delayAnswerWrite) --- что делать с этим куском кода?
        Thread.sleep( delayAnswerWrite.toLong() )
        return true
    }

    override fun SendMessage(message: Array<Byte>) : Boolean
    {
        if (!isOpen )
        {
            OpenPort()
        }
        println( FormValues.getCurrentTime() + "sending some bytes..." )
        // @todo нужны ли тут дискардбуфферс?
        writeBytes( message.toByteArray() )
        // @todo Sleep(delayAnswerWrite)
        Thread.sleep( delayAnswerWrite.toLong() )
        return true
    }

    override fun SendQuery(message:String): Pair<Boolean, String>
    {
        return try {
            SendMessage(message)
            val answer = readString()
            println( FormValues.getCurrentTime() + "getting $answer")
            Thread.sleep( delayAnswerRead.toLong() )
            Pair( answer != null, answer )
        } catch ( e : Exception ) {
            println( FormValues.getCurrentTime() +
                    "При отправке запроса через порт возникла ошибка: ${e.message}")
            Pair(false, "")
        }
    }

    override fun SendQuery(message: Array<Byte>, answerLen: Int,
                           attempts: Int): Pair<Boolean,Array<Byte>>
    {
        try {
            println( FormValues.getCurrentTime() + "sending byte query")
            for (i in 0 until attempts )
            {
                SendMessage( message )
                val answer = readBytes()
                Thread.sleep( delayAnswerRead.toLong() )
                if ( answer != null )
                {
                    println( FormValues.getCurrentTime() + " ...success")
                    return Pair ( true, answer.toTypedArray() )
                }
            }
        } catch ( e : Exception )
        {
            println( FormValues.getCurrentTime() +
                    "При отправке запроса через порт возникла ошибка: ${e.message}")
        }
        val result = arrayOf<Byte>(0,1)
        return Pair(false,result)
    }
}