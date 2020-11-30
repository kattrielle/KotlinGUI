package registerCollection

import externalDevices.devices.Modbus
import registerMapTikModscan.CellData
import registerMapTikModscan.ElementFormat
import registerMapTikModscan.ElementType

class DiscreteOut {
    var values : CellData? = null //регистр выборки
    var setpointSample : CellData? = null //регистр хранения адреса (номера регистра) выборки для уставки
    var setpoint : CellData? = null //регистр величины уставки
    var timeSet : CellData? = null //регистр времени устаноки для уставки
    var timeUnset : CellData? = null //регистр времени снятия для уставки
    var weight : CellData? = null //регистр весового коэффициента для уставки

    // @TODO может быть, есть смысл вынести дескрипшн и регистр в CellData?ЧТо тогда делать с null?
    var descriptionValues : String = ""
        get() = values?.name ?: ""

    var descriptionSetpointSample : String = ""
        get() = setpointSample?.name ?: ""

    var descriptionSetpoint : String = ""
        get() = setpoint?.name ?: ""

    var descriptionTimeSet : String = ""
        get() = timeSet?.name ?: ""

    var descriptionTimeUnset : String = ""
        get() = timeUnset?.name ?: ""

    var descriptionWeight : String = ""
        get() = weight?.name ?: ""

    var registerValues : Int
        get() = values?.address?.plus(1) ?: 0
        set(value) {values?.address = value - 1 }

    var registerSetpoint : Int
        get() = setpoint?.address?.plus(1) ?: 0
        set(value) { setpoint?.address = value - 1 }

    var registerSetpointSample : Int
        get() = setpointSample?.address?.plus(1) ?: 0
        set(value) { setpointSample?.address = value - 1 }

    var registerTimeSet : Int
        get() = timeSet?.address?.plus(1) ?: 0
        set(value) { timeSet?.address = value - 1 }

    var registerTimeUnset : Int
        get() = timeUnset?.address?.plus(1) ?: 0
        set(value) { timeUnset?.address = value - 1 }

    var registerWeigth : Int
        get() = weight?.address?.plus(1) ?: 0
        set(value) { weight?.address = value - 1 }

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

    fun writeSetpointSampleValue ( device : Modbus ) : Boolean
    {
        return writeHoldingValue( registerValues.toDouble(), device,
                setpointSample?.format!!, registerSetpointSample )
    }

    fun writeTimeSetValue( value : Int, device: Modbus ) : Boolean
    {
        return writeHoldingValue( value.toDouble(), device, timeSet?.format!!, registerTimeSet)
    }

    fun writeTimeUnsetValue( value : Int, device : Modbus ) : Boolean
    {
        return writeHoldingValue( value.toDouble(), device, timeUnset?.format!!, registerTimeUnset)
    }

    fun writeWeightValue ( value : Int, device : Modbus ) : Boolean
    {
        return writeHoldingValue( value.toDouble(), device, weight?.format!!, registerWeigth )
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