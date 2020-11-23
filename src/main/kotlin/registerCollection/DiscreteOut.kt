package registerCollection

import externalDevices.devices.Modbus
import registerMapTikModscan.CellData
import registerMapTikModscan.ElementFormat
import registerMapTikModscan.ElementType

class DiscreteOut {
    var values : CellData? = null
    var setpoint : CellData? = null
    var timeSet : CellData? = null
    var timeUnset : CellData? = null

    var descriptionValues : String = ""
    get() = values?.name ?: ""

    var descriptionSetpoint : String = ""
        get() = setpoint?.name ?: ""

    var descriptionTimeSet : String = ""
        get() = timeSet?.name ?: ""

    var descriptionTimeUnset : String = ""
        get() = timeUnset?.name ?: ""

    var registerValues : Int
        get() = values?.address?.plus(1) ?: 0
        set(value) {values?.address = value - 1 }

    var registerSetpoint : Int
        get() = setpoint?.address?.plus(1) ?: 0
        set(value) { setpoint?.address = value - 1 }

    var registerTimeSet : Int
        get() = timeSet?.address?.plus(1) ?: 0
        set(value) { timeSet?.address = value - 1 }

    var registerTimeUnset : Int
        get() = timeUnset?.address?.plus(1) ?: 0
        set(value) { timeUnset?.address = value - 1 }

    fun getSample( n : Int, device : Modbus) : List<Double>
    {
        val result = mutableListOf<Double>()

        for ( i in 0 until n )
        {
            when (values?.type ) {
                ElementType.InputRegister ->
                    result.add(getInputValue(device))
                ElementType.HoldingRegister ->
                    result.add(getHoldingValue(device))
                else -> {}
            }
        }
        return result
    }

    fun writeSetpointValue( value : Double, device : Modbus ) : Boolean
    {
        return writeHoldingValue( value, device, setpoint?.format!!, registerSetpoint )
    }

    fun writeTimeSetValue( value : Int, device: Modbus ) : Boolean
    {
        return writeHoldingValue( value.toDouble(), device, timeSet?.format!!, registerTimeSet)
    }

    fun writeTimeUnsetValue( value : Int, device : Modbus ) : Boolean
    {
        return writeHoldingValue(value.toDouble(),device,timeUnset?.format!!, registerTimeUnset)
    }

    private fun writeHoldingValue(
            value: Double,
            device: Modbus,
            format: ElementFormat,
            register: Int
    ) : Boolean
    {
        return when (format ) {
            ElementFormat.Int -> {
                device.SetHoldingValue(register, value.toInt())
            }
            ElementFormat.Float -> {
                device.SetHoldingSwFloat( register, value.toFloat() )
            }
            ElementFormat.swFloat -> {
                device.SetHoldingFloat(register, value.toFloat() )
            }
            else -> false
        }
    }

    private fun getHoldingValue(device : Modbus ) : Double
    {
        return when (values?.format ) {
            ElementFormat.Int -> {
                val (_, value) = device.GetOneHoldingValue(registerValues)
                value.toDouble()
            }
            ElementFormat.Float -> {
                val (_, value) = device.GetHoldingSwFloat(registerValues)
                value.toDouble()
            }
            ElementFormat.swFloat -> {
                val (_, value ) = device.GetHoldingFloat(registerValues)
                value.toDouble()
            }
            else ->
                0.0
        }
    }

     private fun getInputValue( device : Modbus ) : Double
     {
         return when (values?.format ) {
             ElementFormat.Int -> {
                 val (_, value) = device.GetOneInputValue(registerValues)
                 value.toDouble()
             }
             ElementFormat.Float -> {
                 val (_, value) = device.GetInputSwFloat(registerValues)
                 value.toDouble()
             }
             ElementFormat.swFloat -> {
                 val (_, value ) = device.GetInputFloat(registerValues)
                 value.toDouble()
             }
             else ->
                 0.0
         }
     }
}