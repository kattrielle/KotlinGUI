package externalDevices.devices

import externalDevices.settings.SettingsCOM
import externalDevices.settings.SettingsModbusRTU
import externalDevices.ports.PortCOM

class ModbusRTU ( portName: String ) : Modbus() {
    init{
        settings = SettingsModbusRTU( portName )
        port = PortCOM( settings as SettingsCOM )
    }
    private val LEN_WRITEMESSAGE_RTU: Int = 8

    override val maxIterations: Int = 25
    override val answerStart: Int = 3
    override val skipAtEnd: Int = 1

    constructor(portName: String, _address: Byte, _baudrate: Int) : this( portName ) {
        (settings as SettingsModbusRTU).address = _address
        (settings as SettingsModbusRTU).baudRate = _baudrate

        port = PortCOM( settings as SettingsCOM )
    }

    constructor( parametersCOM : SettingsModbusRTU ) : this( parametersCOM.name )
    {
        settings = parametersCOM

        port = PortCOM( settings as SettingsCOM )
    }

    private fun GetCRC(message: Array<Byte>) : Array<Byte>
    {
        val result: Array<Byte> = Array(2){0}
        var CRCFull: UShort = 0xFFFFu

        for (i in 0 until message.size - 2) {
            CRCFull = (CRCFull xor message[i].toUByte().toUShort()) as UShort
            for (j in 0..7) {
                val CRCLSB = CRCFull and 0x0001u
                CRCFull = ((CRCFull.toInt() shr 1).toUShort() and 0x7FFFu) as UShort
                if (CRCLSB == 1.toUShort()) CRCFull = (CRCFull xor 0xA001u) as UShort
            }
        }
        result[1] = ((CRCFull.toInt() shr 8) and 0xFF).toByte()
        result[0] = (CRCFull and 0xFFu).toByte()
        return result
    }

    override fun CreateMessage(funct: Byte, register: Int, value: Int): Array<Byte>
    {
        val result: Array<Byte> = Array(LEN_WRITEMESSAGE_RTU) { 0 }
        result[0] = (settings as SettingsModbusRTU).address
        result[1] = funct
        var tmp = ConvertValueToBytes(register - 1)
        result[2] = tmp[1]
        result[3] = tmp[0]
        tmp = ConvertValueToBytes(value)
        result[4] = tmp[1]
        result[5] = tmp[0]
        tmp = GetCRC(result)
        result[6] = tmp[0]
        result[7] = tmp[1]
        return result
    }

    override fun CreateMessageMultipleWrite(
            register: Int, values: Array<Byte>): Array<Byte> {
        val result = Array<Byte>( LEN_WRITEMESSAGE_RTU + 1 + values.size ) {0}
        result[ 0 ] = (settings as SettingsModbusRTU).address
        result[ 1 ] = WRITEMANY_HOLDING
        var tmp : Array<Byte> = ConvertValueToBytes( register - 1 )
        result[ 2 ] = tmp[ 1 ]
        result[ 3 ] = tmp[ 0 ]
        tmp = ConvertValueToBytes( values.size / 2 )
        result[ 4 ] = tmp[ 1 ]
        result[ 5 ] = tmp[ 0 ]
        result[ 6 ] = values.size.toByte()
        for ( i in 0 until values.size / 2)
        {
            result[ 7 + 2 * i ] = values[ 2 * i + 1 ]
            result[ 7 + 2 * i + 1 ] = values[ 2 * i ]
        }
        tmp = GetCRC( result );
        result[ result.size - 2 ] = tmp[ 0 ]
        result[ result.size - 1 ] = tmp[ 1 ]
        return result
    }

    override fun SetValue(funct: Byte, register: Int, value: Int): Boolean {
        val message = CreateMessage(funct, register, value)
        val (succeed, response) = port.SendQuery(message,
                LEN_WRITEMESSAGE_RTU, maxIterations)
        if ( !succeed)
        {
            return false
        }

        return if ( message contentEquals response )
        {
            true
        } else {
            GetOneHoldingValue(register) == Pair(true, value)
        }
    }

    override fun SetMultipleHoldingValues(register: Int, values: Array<Byte>): Boolean {
        val message = CreateMessageMultipleWrite( register, values )
        val ( succeed, response ) = port.SendQuery( message, LEN_WRITEMESSAGE_RTU, maxIterations )
        //надо ли обрабатывать response?
        return succeed
    }

}