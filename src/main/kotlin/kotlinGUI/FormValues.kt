package kotlinGUI

import externalDevices.devices.ModbusRTU
import externalDevices.settings.SettingsModbusRTU
import registerCollection.DiscreteOut
import registerCollection.DiscreteOutCollection
import registerCollection.DiscreteOutViewProperties
import registerMapTikModscan.CellData
import registerMapTikModscan.SerializableCellsContainer
import java.io.File
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class FormValues {
    companion object {
        var settings = SettingsModbusRTU( "" )

        var device = ModbusRTU( "" )

        var setpoints = DiscreteOutCollection()

        val discreteOutProperties = mutableListOf<DiscreteOutViewProperties>()

        lateinit var file : File

        lateinit var tikModscanMap : SerializableCellsContainer

        fun getRegisterMapDescriptions() : List<String>
        {
            val descriptions = mutableListOf<String>()
            tikModscanMap?.сellsArray.forEach { descriptions.add(it.name.toString()) }

            return descriptions
        }

        fun findRegister( name : String ) : CellData
        {
            println( getCurrentTime() + "searching register \"$name\"")
            return tikModscanMap?.сellsArray.find { it.name == name } ?: CellData()
        }

        fun findSetpoint( name : String ) : DiscreteOut?
        {
            return setpoints.items.find { it?.descriptionValues == name }
        }

        fun getCurrentTime() : String
        {
            return DateTimeFormatter
                    .ofPattern("dd.MM.yyyy HH:mm:ss.SSSSSS ")
                    .withZone(ZoneOffset.ofHours(5))
                    .format(Instant.now())
        }
    }
}