package registerCollection

import externalDevices.devices.Modbus
import registerMapTikModscan.CellData

class DiscreteOutCollection {
    var registerWriteDefence : CellData? = null

    var descriptionWriteDefence : String = ""
        get() = registerWriteDefence?.name ?: ""

    var registerNumDefence : Int
        get() = registerWriteDefence?.address?.plus(1) ?: 0
        set(value) { registerWriteDefence?.address = value - 1 }

    val items = mutableListOf<DiscreteOut>()

    fun unlockWrite( device : Modbus ) : Boolean
    {
        return device.SetHoldingValue( registerNumDefence, 0xABCD )
    }

    fun lockWrite( device : Modbus ) : Boolean
    {
        return  device.SetHoldingValue( registerNumDefence, 0 )
    }
}