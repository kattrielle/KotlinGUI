package externalDevices.ports

interface IPort {

    //var settings : IPortSettings
    var isOpen : Boolean //как сделать доступным только Гет?

    var delayAnswerRead: Int
    var delayAnswerWrite: Int
    
    fun OpenPort() : Boolean
    fun ClosePort() : Boolean
    fun SendMessage(message: String) : Boolean
    fun SendMessage(message: Array<Byte>) : Boolean
    fun SendQuery(message:String): Pair<Boolean, String>
    fun SendQuery(message: Array<Byte>, answerLen: Int, attempts: Int):
            Pair<Boolean,Array<Byte>>
}