package registerCollection

import externalDevices.devices.Modbus
import registerMapTikModscan.CellData
import registerMapTikModscan.ElementFormat
import registerMapTikModscan.ElementType

class DiscreteOut () {
    var values : CellData? = null //регистр выборки
    var setpointSample : CellData? = null //регистр хранения адреса (номера регистра) выборки для уставки
    var setpoint : CellData? = null //регистр величины уставки
    var timeSet : CellData? = null //регистр времени устаноки для уставки
    var timeUnset : CellData? = null //регистр времени снятия для уставки
    var weight : CellData? = null //регистр весового коэффициента для уставки
    val sample : MutableList<Double> = mutableListOf()

    constructor ( mapper : DiscreteOutMapper ) : this() {
        values = mapper.values //регистр выборки
        setpointSample = mapper.setpointSample //регистр хранения адреса (номера регистра) выборки для уставки
        setpoint = mapper.setpoint //регистр величины уставки
        timeSet = mapper.timeSet //регистр времени устаноки для уставки
        timeUnset = mapper.timeUnset //регистр времени снятия для уставки
        weight = mapper.weight //регистр весового коэффициента для уставки
    }

    // @TODO может быть, есть смысл вынести дескрипшн и регистр в CellData?ЧТо тогда делать с null?
    val descriptionValues : String
        get() = values?.name ?: ""

    val descriptionSetpointSample : String
        get() = setpointSample?.name ?: ""

    val descriptionSetpoint : String
        get() = setpoint?.name ?: ""

    val descriptionTimeSet : String
        get() = timeSet?.name ?: ""

    val descriptionTimeUnset : String
        get() = timeUnset?.name ?: ""

    val descriptionWeight : String
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

    var valueSetpointSample = 0

    var valueSetpoint = 0.0

    var valueTimeSet = 0

    var valueTimeUnset = 0

    var valueWeight = 0

    var isUsed = true

    fun getSampleValue( device: Modbus ) : Pair< Boolean, Double >
    {
        return getValue( values, device)
    }

    fun getSample( n : Int, device : Modbus) : List<Double>
    {
        val result = mutableListOf<Double>()

        for ( i in 0 until n )
        {
            val ( success, sampleVal ) = getSampleValue ( device )
            if ( success )
            {
                result.add( sampleVal )
            }
        }
        return result
    }

    fun getDiscreteOutValues( device : Modbus ) : Boolean
    {
        if ( !device.OpenConnection() )
        {
            return false
        }
        if ( !readSetpointSampleValue( device ) )
        {
            return false
        }
        if ( valueSetpointSample != 0 ) {
            isUsed = true
            if ( !readSetpointValue( device ) )
            {
                return false
            }
            if ( !readTimeSetValue( device ) )
            {
                return false
            }
            if ( !readTimeUnsetValue( device ) )
            {
                return false
            }
            if ( !readWeightValue( device ) )
            {
                return false
            }
        } else {
            isUsed = false
        }
        return true
    }

    fun writeDiscreteOutValues( device : Modbus ) : Boolean
    {
        var result = true
        result = result && writeSetpointSampleValue( device )
        result = result && writeSetpointValue( valueSetpoint, device )
        result = result && writeTimeSetValue( valueTimeSet, device )
        result = result && writeTimeUnsetValue( valueTimeUnset, device )
        result = result && writeWeightValue( valueWeight, device )
        return result
    }

    fun writeSetpointValue( value : Double, device : Modbus ) : Boolean
    {
        return writeHoldingValue( value, device, setpoint?.format!!, registerSetpoint )
    }

    fun readSetpointValue( device : Modbus ) : Boolean
    {
        val ( success, result ) = getValue(setpoint, device)
        valueSetpoint = result
        return success
    }

    fun writeSetpointSampleValue ( device : Modbus ) : Boolean
    {
        return writeHoldingValue( findSetpointSampleValue(), device,
                setpointSample?.format!!, registerSetpointSample )
    }

    fun readSetpointSampleValue( device : Modbus ) : Boolean
    {
        val ( success, result) = getValue( setpointSample, device )
        valueSetpointSample = result.toInt()
        return success
    }

    fun writeTimeSetValue( value : Int, device: Modbus ) : Boolean
    {
        return writeHoldingValue( value.toDouble(), device, timeSet?.format!!, registerTimeSet)
    }

    fun readTimeSetValue( device : Modbus ) : Boolean
    {
        val ( success, result ) = getValue(timeSet, device)
        valueTimeSet = result.toInt()
        return success
    }

    fun writeTimeUnsetValue( value : Int, device : Modbus ) : Boolean
    {
        return writeHoldingValue( value.toDouble(), device, timeUnset?.format!!, registerTimeUnset)
    }

    fun readTimeUnsetValue( device : Modbus ) : Boolean
    {
        val ( success, result ) = getValue(timeUnset, device)
        valueTimeUnset = result.toInt()
        return success
    }

    fun writeWeightValue ( value : Int, device : Modbus ) : Boolean
    {
        return writeHoldingValue( value.toDouble(), device, weight?.format!!, registerWeigth )
    }

    fun readWeightValue( device : Modbus ) : Boolean
    {
        val ( success, result ) = getValue(weight, device)
        valueWeight = result.toInt()
        return success
    }

    private fun findSetpointSampleValue() : Double {
        return if ( isUsed )
        {
            registerValues.toDouble()
        } else {
            0.0
        }
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

    private fun getValue( register: CellData?, device : Modbus )
    : Pair<Boolean, Double>
    {
        return when (register?.type ) {
            ElementType.InputRegister ->
                getInputValue(register, device)
            ElementType.HoldingRegister ->
                getHoldingValue(register, device)
            else -> Pair( false, Double.NEGATIVE_INFINITY )
        }
    }

    private fun getHoldingValue(register: CellData?, device : Modbus )
    : Pair<Boolean, Double>
    {
        return when (register?.format ) {
            ElementFormat.Int -> {
                val (success, value) = device.GetOneHoldingValue(register?.address + 1)
                Pair( success, value.toDouble() )
            }
            ElementFormat.Float -> {
                val (success, value) = device.GetHoldingSwFloat(register?.address + 1)
                Pair( success, value.toDouble() )
            }
            ElementFormat.swFloat -> {
                val (success, value ) = device.GetHoldingFloat(register?.address + 1)
                Pair( success, value.toDouble() )
            }
            else ->
                Pair( false, 0.0 )
        }
    }

     private fun getInputValue( register : CellData?, device : Modbus )
     : Pair<Boolean, Double>
     {
         return when (register?.format ) {
             ElementFormat.Int -> {
                 val (success, value) = device.GetOneInputValue(register?.address + 1)
                 Pair( success, value.toDouble() )
             }
             ElementFormat.Float -> {
                 val (success, value) = device.GetInputSwFloat(register?.address + 1)
                 Pair( success, value.toDouble() )
             }
             ElementFormat.swFloat -> {
                 val (success, value ) = device.GetInputFloat(register?.address + 1)
                 Pair( success, value.toDouble() )
             }
             else ->
                 Pair( false, 0.0 )
         }
     }
}