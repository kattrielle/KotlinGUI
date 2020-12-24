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

    val items = mutableListOf<DiscreteOut>().asObservable()

    constructor( mapper : DiscreteOutCollectionMapper ) : this()
    {
        registerWriteDefence = mapper.registerWriteDefence
        for ( item in mapper.items )
        {
            items.add( DiscreteOut(item) )
        }
    }

    fun getParameterValues( device : Modbus, progress : SimpleDoubleProperty)
    {
        for ( i in items.indices )
        {
            items[i].getDiscreteOutValues( device )
            progress.set( (i + 1).toDouble() / items.size )
        }
    }

    fun getSamples( n: Int, device : Modbus, progress : SimpleDoubleProperty )
    {
        for ( i in items.indices )
        {
            items[i].sample = items[i].getSample(n, device)
            progress.set((i + 1 ).toDouble() / items.size)
        }
    }

    fun countSetpointValues( type : String, percent : Double )
    {
        for ( out in items ) {
            out.valueSetpoint = CountSetpoints.count(
                    CountSetpointsDescriptions.selectCountType(type),
                    out.sample, percent )
        }
    }

    fun writeParameterValues ( device : Modbus, progress : SimpleDoubleProperty )
    {
        unlockWrite( device )
        for ( i in items.indices )
        {
            items[ i ].writeDiscreteOutValues( device )
            progress.set ( (i+1).toDouble() / items.size )
        }
        lockWrite( device )
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