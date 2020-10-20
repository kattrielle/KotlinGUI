package externalDevices

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ModbusRTUTest {

    @Test fun testCreateModbusRTU()
    {
        val modbusSensor = ModbusRTU()
        assertEquals( 1, ( modbusSensor.settings as SettingsModbusRTU ).address )
        assertEquals( 9600, ( modbusSensor.settings as SettingsModbusRTU ).baudRate )
    }

    @Test fun testCreateModbusRTUParams()
    {
        val modbusSensor = ModbusRTU( 5, 1200 )
        assertEquals( 5, ( modbusSensor.settings as SettingsModbusRTU ).address )
        assertEquals( 1200, ( modbusSensor.settings as SettingsModbusRTU ).baudRate )
    }

    @Test fun testReadHoldingValueReg1Val1()
    {
        val modbusSensor = ModbusRTU()
        val messageToSend: Array<Byte> = arrayOf(0x01, 0x03, 0x00, 0x00, 0x00, 0x01, 0x84.toByte(), 0x0A)
        val messageToRead: Array<Byte> = arrayOf(0x01, 0x03, 0x02, 0x00, 0x01, 0x79, 0x84.toByte())
        modbusSensor.port = PortCOMMock( messageToSend, messageToRead)
        val ( messageCorrect, outValue ) = modbusSensor.GetOneHoldingValue(1)

        assertTrue( messageCorrect, "SendMessage")
        assertEquals(1, outValue, "OutValue" )
    }

    @Test fun testReadHoldingValueReg14Val396() {
        val modbusSensor = ModbusRTU()
        val messageToSend: Array<Byte> = arrayOf(0x01, 0x03, 0x00, 0x0D, 0x00, 0x01, 0x15, 0xC9.toByte())
        val messageToRead: Array<Byte> = arrayOf(0x01, 0x03, 0x02, 0x01, 0x8C.toByte(), 0xB8.toByte(), 0x71.toByte())

        modbusSensor.port = PortCOMMock( messageToSend, messageToRead)
        val ( messageCorrect, outValue ) = modbusSensor.GetOneHoldingValue(14)

        assertTrue( messageCorrect, "SendMessage")
        assertEquals(396, outValue, "OutValue" )
    }

    @Test fun testSetHoldingValueReg14Val400() {
        val modbusSensor = ModbusRTU()
        val messageToSend: Array<Byte> = arrayOf(0x01, 0x06, 0x00, 0x0D, 0x01, 0x90.toByte(), 0x19, 0xF5.toByte())
        val messageToRead: Array<Byte> = arrayOf(0x01, 0x06, 0x00, 0x0D, 0x01, 0x90.toByte(), 0x19, 0xF5.toByte())

        modbusSensor.port = PortCOMMock( messageToSend, messageToRead)
        val operationCorrect = modbusSensor.SetHoldingValue(14, 400)

        assertTrue( operationCorrect, "Check success")
    }

    @Test fun testReadHoldingSwFloatReg52Val1337() {
        val modbusSensor = ModbusRTU()
        val messageToSend: Array<Byte> = arrayOf(0x01, 0x03, 0x00, 0x33, 0x00, 0x02, 0x34, 0x04)
        val messageToRead: Array<Byte> = arrayOf(0x01, 0x03, 0x04, 0x44, 0xA7.toByte(), 0x20, 0x00, 0x46, 0xE0.toByte())

        modbusSensor.port = PortCOMMock( messageToSend, messageToRead)
        val ( messageCorrect, outValue ) = modbusSensor.GetHoldingSwFloat( 52 )

        assertTrue( messageCorrect, "Check message" )
        assertEquals( 1337f, outValue, "Result value" )
    }

    @Test fun testReadHoldingFloatReg52Val1e19() {
        val modbusSensor = ModbusRTU()
        val messageToSend: Array<Byte> = arrayOf(0x01, 0x03, 0x00, 0x33, 0x00, 0x02, 0x34, 0x04)
        val messageToRead: Array<Byte> = arrayOf(0x01, 0x03, 0x04, 0x44, 0xA7.toByte(), 0x20, 0x00, 0x46, 0xE0.toByte())

        modbusSensor.port = PortCOMMock( messageToSend, messageToRead)
        val ( messageCorrect, outValue ) = modbusSensor.GetHoldingFloat( 52 )

        assertTrue( messageCorrect, "Check message" )
        assertEquals( 1.0864737E-19f, outValue, "Result value" )
    }

    @Test fun testReadMultipleHoldings() {
        val modbusSensor = ModbusRTU()
        val messageToSend: Array<Byte> = arrayOf(0x01, 0x03, 0x00, 0x00, 0x00, 0x03, 0x05, 0xCB.toByte())
        val messageToRead: Array<Byte> = arrayOf(0x01, 0x03, 0x06, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x1C, 0xB5.toByte())

        modbusSensor.port = PortCOMMock( messageToSend, messageToRead)
        val ( messageCorrect, outValue ) = modbusSensor.GetHoldingValue( 1, 3 )
        val answerEqual = outValue contentEquals arrayOf(1, 0, 0)

        assertTrue( messageCorrect, "Check message")
        assertTrue( answerEqual, "Result Values")
    }

    @Test fun testReadMultipleInputs() {
        val modbusSensor = ModbusRTU()
        val messageToSend: Array<Byte> = arrayOf(0x01, 0x04, 0x00, 0x00, 0x00, 0x03, 0xB0.toByte(), 0x0B)
        val messageToRead: Array<Byte> = arrayOf(0x01, 0x04, 0x06, 0x00, 0x03, 0x00, 0x1E, 0x7F, 0xFF.toByte(), 0x24, 0xE5.toByte())

        modbusSensor.port = PortCOMMock( messageToSend, messageToRead)
        val ( messageCorrect, outValue ) = modbusSensor.GetInputValue( 1, 3 )
        val answerEqual = outValue contentEquals arrayOf(3, 30, 32767)

        assertTrue( messageCorrect, "Check message")
        assertTrue( answerEqual, "Result Values")
    }

    @Test fun testSetMultipleHoldingValues() {
        val modbusSensor = ModbusRTU()
        val messageToSend: Array<Byte> = arrayOf(0x01, 0x04, 0x00, 0x00, 0x00, 0x03, 0xB0.toByte(), 0x0B)
        val messageToRead: Array<Byte> = arrayOf(0x01, 0x04, 0x06, 0x00, 0x03, 0x00, 0x1E, 0x7F, 0xFF.toByte(), 0x24, 0xE5.toByte())

        modbusSensor.port = PortCOMMock( messageToSend, messageToRead)
        val success = modbusSensor.SetMultipleHoldingValues( 1, arrayOf(1,2,3))

        assertTrue( success, "sending Values" )
    }

    @Test fun testSetHoldingFloatReg52Val1337() {
        val modbusSensor = ModbusRTU()
        val messageToSend: Array<Byte> = arrayOf(0x01, 0x10, 0x00, 0x33, 0x00, 0x02, 0x04, 0x20, 0x00, 0x44,
                0xA7.toByte(), 0xC9.toByte(), 0xD4.toByte())
        val messageToRead: Array<Byte> = arrayOf(0x01, 0x10, 0x00, 0x33, 0x00, 0x02, 0xB1.toByte(), 0xC7.toByte())

        modbusSensor.port = PortCOMMock( messageToSend, messageToRead)
        val success = modbusSensor.SetHoldingFloat(52, 1337f)

        assertTrue( success, "sending Values" )
    }

    @Test fun testSetHoldingSwFloatReg52Val1337() {
        val modbusSensor = ModbusRTU()
        val messageToSend: Array<Byte> = arrayOf(0x01, 0x04, 0x00, 0x00, 0x00, 0x03, 0xB0.toByte(), 0x0B)
        val messageToRead: Array<Byte> = arrayOf(0x01, 0x04, 0x06, 0x00, 0x03, 0x00, 0x1E, 0x7F, 0xFF.toByte(), 0x24, 0xE5.toByte())

        modbusSensor.port = PortCOMMock( messageToSend, messageToRead)
        val success = modbusSensor.SetHoldingSwFloat(52, 1337f)

        assertTrue( success, "sending Values" )
    }

}