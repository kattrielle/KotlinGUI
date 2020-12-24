package registerCollection

import externalDevices.devices.Modbus
import javafx.beans.property.*
import registerMapTikModscan.CellData
import registerMapTikModscan.ElementFormat
import registerMapTikModscan.ElementType
import tornadofx.getProperty
import tornadofx.property

class DiscreteOut () {
    var values : CellData? = null //регистр выборки
    var setpointSample : CellData? = null //регистр хранения адреса (номера регистра) выборки для уставки
    var setpoint : CellData? = null //регистр величины уставки
    var timeSet : CellData? = null //регистр времени устаноки для уставки
    var timeUnset : CellData? = null //регистр времени снятия для уставки
    var weight : CellData? = null //регистр весового коэффициента для уставки
    lateinit var sample : List<Double>

    constructor ( mapper : DiscreteOutMapper ) : this() {
        values = mapper.values //регистр выборки
        setpointSample = mapper.setpointSample //регистр хранения адреса (номера регистра) выборки для уставки
        setpoint = mapper.setpoint //регистр величины уставки
        timeSet = mapper.timeSet //регистр времени устаноки для уставки
        timeUnset = mapper.timeUnset //регистр времени снятия для уставки
        weight = mapper.weight //регистр весового коэффициента для уставки
    }

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

    var valueSetpointSample by property( 0 )
    fun valueSetpointSampleProperty() = getProperty( DiscreteOut:: valueSetpointSample )

    var valueSetpoint by property( 0.0 )
    fun valueSetpointProperty() = getProperty( DiscreteOut::valueSetpoint )

    var valueTimeSet by property( 0 )
    fun valueTimeSetProperty() = getProperty( DiscreteOut::valueTimeSet )

    var valueTimeUnset by property( 0 )
    fun valueTimeUnsetProperty() = getProperty( DiscreteOut::valueTimeUnset )

    var valueWeight by property( 0 )
    fun valueWeightProperty() = getProperty( DiscreteOut:: valueWeight )

    fun getSample( n : Int, device : Modbus) : List<Double>
    {
        val result = mutableListOf<Double>()

        for ( i in 0 until n )
        {
            val sampleVal = getValue( values, device)
            if ( sampleVal != Double.NEGATIVE_INFINITY )
            {
                result.add( sampleVal )
            }
        }
        return result
    }

    fun getDiscreteOutValues( device : Modbus )
    {
        valueSetpointSample = getValue( setpointSample, device ).toInt()
        valueSetpoint = getValue( setpoint, device )
        valueTimeSet = getValue ( timeSet, device ).toInt()
        valueTimeUnset = getValue ( timeUnset, device ).toInt()
        valueWeight = getValue ( weight, device ).toInt()

        /*valueSetpointSample = 1
        Thread.sleep(500)
        valueSetpoint = 2.0
        Thread.sleep(500)
        valueTimeSet = 3
        Thread.sleep(500)
        valueTimeUnset = 4
        Thread.sleep(500)
        valueWeight = 5
        Thread.sleep(500)*/
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

    private fun getValue( register: CellData?, device : Modbus ) : Double
    {
        return when (register?.type ) {
            ElementType.InputRegister ->
                return getInputValue(register, device)
            ElementType.HoldingRegister ->
                return getHoldingValue(register, device)
            else -> return Double.NEGATIVE_INFINITY
        }
    }

    private fun getHoldingValue(register: CellData?, device : Modbus ) : Double
    {
        return when (register?.format ) {
            ElementFormat.Int -> {
                val (_, value) = device.GetOneHoldingValue(register?.address + 1)
                value.toDouble()
            }
            ElementFormat.Float -> {
                val (_, value) = device.GetHoldingSwFloat(register?.address + 1)
                value.toDouble()
            }
            ElementFormat.swFloat -> {
                val (_, value ) = device.GetHoldingFloat(register?.address + 1)
                value.toDouble()
            }
            else ->
                0.0
        }
    }

     private fun getInputValue( register : CellData?, device : Modbus ) : Double
     {
         return when (register?.format ) {
             ElementFormat.Int -> {
                 val (_, value) = device.GetOneInputValue(register?.address + 1)
                 value.toDouble()
             }
             ElementFormat.Float -> {
                 val (_, value) = device.GetInputSwFloat(register?.address + 1)
                 value.toDouble()
             }
             ElementFormat.swFloat -> {
                 val (_, value ) = device.GetInputFloat(register?.address + 1)
                 value.toDouble()
             }
             else ->
                 0.0
         }
     }
}