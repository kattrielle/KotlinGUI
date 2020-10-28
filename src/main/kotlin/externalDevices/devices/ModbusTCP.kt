package externalDevices.devices

import externalDevices.settings.SettingsTCP
import externalDevices.ports.PortTCP

class ModbusTCP(IP: String, portConnection: Int ) : Modbus() {
    init{
        settings = SettingsTCP( IP, portConnection )
        port = PortTCP( IP, portConnection )
    }

    private val LEN_WRITEMESSAGE_TCP : Int = 6
    private val LEN_MULTIPLEWRITE_TCP : Int = 7

    override val answerStart: Int = 9
    override val skipAtEnd: Int = 0
    override val maxIterations: Int = 1

    override fun CreateMessage(funct: Byte, register: Int, value: Int): Array<Byte> {
        val result: Array<Byte> = Array(LEN_WRITEMESSAGE_TCP) { 0 }
        result[0] = 0
        result[1] = funct
        var tmp = ConvertValueToBytes(register - 1)
        result[2] = tmp[1]
        result[3] = tmp[0]
        tmp = ConvertValueToBytes(value)
        result[4] = tmp[1]
        result[5] = tmp[0]
        return result
    }

    override fun CreateMessageMultipleWrite(register: Int, values: Array<Byte>
    ): Array<Byte> {
        val result = Array<Byte>(LEN_MULTIPLEWRITE_TCP + 2 * values.size) { 0 }
        result[0] = 0
        result[1] = WRITEMANY_HOLDING
        var tmp = ConvertValueToBytes(register - 1)
        result[2] = tmp[1]
        result[3] = tmp[0]
        tmp = ConvertValueToBytes( values.size / 2 )
        result[4] = tmp[1]
        result[5] = tmp[0]
        result[6] = values.size.toByte()
        for (i in 0 until values.size / 2) {
            result[LEN_MULTIPLEWRITE_TCP + 2 * i] = values[2 * i + 1]
            result[LEN_MULTIPLEWRITE_TCP + 2 * i + 1] = values[2 * i]
        }
        return result
    }

    override fun SetValue(funct: Byte, register: Int, value: Int): Boolean {
        val message = CreateMessage( funct, register, value )
        val (succeed, response ) = port.SendQuery( message, answerStart + 2, maxIterations )
        //надо ли обрабатывать response?
        return succeed
    }

    override fun SetMultipleHoldingValues(register: Int, values: Array<Byte>): Boolean {
        val message = CreateMessageMultipleWrite( register, values )
        val ( succeed, response ) = port.SendQuery( message,
                answerStart + values.size, maxIterations )
        //надо ли обрабатывать response?
        return succeed
    }
}