package registerCollection

import countSetpoints.CountSetpoints
import countSetpoints.CountSetpointsDescriptions
import externalDevices.devices.Modbus
import javafx.beans.property.SimpleDoubleProperty
import kotlinGUI.FormValues
import registerMapTikModscan.CellData
import tornadofx.asObservable

class DiscreteOutCollection () {
    var registerWriteDefence : CellData? = null

    var descriptionWriteDefence : String = ""
        get() = registerWriteDefence?.name ?: ""

    var registerNumDefence : Int
        get() = registerWriteDefence?.address?.plus(1) ?: 0
        set(value) { registerWriteDefence?.address = value - 1 }

    val items = mutableListOf<DiscreteOut>()

    constructor( mapper : DiscreteOutCollectionMapper ) : this()
    {
        registerWriteDefence = mapper.registerWriteDefence
        for ( item in mapper.items )
        {
            items.add( DiscreteOut(item) )
        }
    }

    fun getParameterValues( device : Modbus, progress : SimpleDoubleProperty) : Boolean
    {
        if ( items.isEmpty() )
        {
            return false
        }
        for ( i in items.indices )
        {
            if ( !items[i].getDiscreteOutValues( device ) )
            {
                return false
            }
            progress.set( (i + 1).toDouble() / items.size )
        }
        FormValues.updateDiscreteOutProperties()
        return true
    }

    fun getSamples( n: Int, device : Modbus, progress : SimpleDoubleProperty ) : Boolean
    {
        if ( items.isEmpty() )
        {
            return false
        }
        items.forEach{ it.sample.clear() }
        for ( i in 0 until n )
        {
            items.filter { it.isUsed }.forEach{
                val ( success, value ) = it.getSampleValue( device )
                if ( ! success )
                {
                    return false
                }
                it.sample.add( value )
            }
            progress.set( (i+1).toDouble() / n )
        }
        return true
    }

    fun countSetpointValues( type : String, percent : Double )
    {
        for ( out in items.filter { it.isUsed } ) {
            out.valueSetpoint = CountSetpoints.count(
                    CountSetpointsDescriptions.selectCountType(type),
                    out.sample, percent )
        }
        FormValues.updateDiscreteOutProperties()
    }

    fun writeParameterValues ( device : Modbus, progress : SimpleDoubleProperty ) : Boolean
    {
        if ( !unlockWrite( device ) )
        {
            println( FormValues.getCurrentTime() + "failed to unlock device")
            return false
        }
        for ( i in items.indices )
        {
            if ( !items[ i ].writeDiscreteOutValues( device ) )
            {
                println( FormValues.getCurrentTime() + "failed at writing operation to ${i+1} register")
                return false
            }
            progress.set ( (i+1).toDouble() / items.size )
        }
        if ( !lockWrite( device ) )
        {
            println( FormValues.getCurrentTime() + "failed to lock device after writing operation" )
            return false
        }
        return true
    }

    fun unlockWrite( device : Modbus ) : Boolean
    {
        println(FormValues.getCurrentTime() + "writing open")
        return device.SetHoldingValue( registerNumDefence, 0xABCD )
    }

    fun lockWrite( device : Modbus ) : Boolean
    {
        println(FormValues.getCurrentTime() + "writing lock")
        return  device.SetHoldingValue( registerNumDefence, 0 )
    }
}