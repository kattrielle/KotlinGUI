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