package externalDevices.ports

import java.io.PrintWriter
import java.net.Socket

class PortTCP( IP : String, port : Int) : IPort {
    override var isOpen: Boolean
        get() = TODO("Not yet implemented")
        set(value) {}

    override var delayAnswerRead: Int
        get() = TODO("Not yet implemented")
        set(value) {}

    override var delayAnswerWrite: Int
        get() = TODO("Not yet implemented")
        set(value) {}

    private var socket = Socket( IP, port )

    override fun OpenPort(): Boolean {
        if ( isOpen )
        {
            return true
        }
        println("opening...")
        return true
    }

    override fun ClosePort(): Boolean {
        return true
    }

    override fun SendMessage(message: Array<Byte>): Boolean {

        return false
    }

    override fun SendMessage(message: String): Boolean {
        PrintWriter(socket.outputStream, true).write(message)
        return false
    }

    override fun SendQuery(message: String): Pair<Boolean, String> {
        TODO("Not yet implemented")
    }

    override fun SendQuery(message: Array<Byte>, answerLen: Int, attempts: Int): Pair<Boolean, Array<Byte>> {
        TODO("Not yet implemented")
    }
}