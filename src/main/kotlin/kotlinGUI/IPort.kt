//package ExternalDevices
package kotlinGUI

interface IPort {

    //var settings : IPortSettings
    var isOpen : Boolean //как сделать доступным только Гет?
    
    fun OpenPort() : Boolean
    fun ClosePort() : Boolean
    fun SendMessage(message: String, delay: Int) : Boolean //i really need delay?
    fun SendMessage(message: Array<Byte>, delay: Int) : Boolean
    fun SendQuery(message:String): Pair<Boolean, String>
    fun SendQuery(message: Array<Byte>, delay:Int, 
        answerLen: Int, attempts: Int): Pair<Boolean,Array<Byte>>
}