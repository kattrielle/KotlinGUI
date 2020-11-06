package externalDevices

import externalDevices.ports.IPort

class PortCOMMock(private val messageToGetString : String) : IPort {
    private var messageToWriteBytes : Array<Byte> = emptyArray()
    private var messageToGetBytes : Array<Byte> = emptyArray()

    constructor( _messageToWriteBytes : Array<Byte>,
                 _messageToGetBytes : Array<Byte> ) : this("") {
        messageToWriteBytes = _messageToWriteBytes
        messageToGetBytes = _messageToGetBytes
    }

    override var isOpen: Boolean = true
        get() = true

    override var delayAnswerRead: Int = 100
    override var delayAnswerWrite: Int = 100

    override fun OpenPort(): Boolean = true
    override fun ClosePort(): Boolean = true

    override fun SendMessage(message: String): Boolean = true
    override fun SendMessage(message: Array<Byte>): Boolean = true

    override fun SendQuery(message: String): Pair<Boolean, String> {
        return Pair( true, messageToGetString )
    }

    override fun SendQuery(message: Array<Byte>, answerLen: Int, attempts: Int): Pair<Boolean, Array<Byte>> {
        //val messageCorrect = message.equals( messageToWriteBytes )
        val messageCorrect = message contentEquals messageToWriteBytes
        return Pair( messageCorrect, messageToGetBytes )
    }
}