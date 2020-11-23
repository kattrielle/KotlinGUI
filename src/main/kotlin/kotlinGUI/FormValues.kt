package kotlinGUI

import externalDevices.devices.ModbusRTU
import externalDevices.settings.SettingsModbusRTU
import registerCollection.DiscreteOut
import registerMapTikModscan.CellData
import registerMapTikModscan.SerializableCellsContainer
import java.io.File

class FormValues {
    companion object {
        var settings = SettingsModbusRTU( "" )

        var device = ModbusRTU( "" )

        var setpoints = DiscreteOutCollection()

        lateinit var file : File

        lateinit var tikModscanMap : SerializableCellsContainer

        fun getRegisterMapDescriptions() : List<String>
        {
            val descriptions = mutableListOf<String>()
            tikModscanMap?.сellsArray.forEach { descriptions.add( it.name ) }

            return descriptions
        }

        fun findRegister( name : String ) : CellData
        {
            return tikModscanMap?.сellsArray.find { it.name == name } ?: CellData()
        }

        fun findSetpoint( name : String ) : DiscreteOut?
        {
            return setpoints.items.find { it?.descriptionValues == name }
        }
    }
}

class DiscreteOutCollection
{
    val items = mutableListOf<DiscreteOut>()
}