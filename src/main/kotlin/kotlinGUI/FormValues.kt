package kotlinGUI

import externalDevices.devices.ModbusRTU
import externalDevices.settings.SettingsModbusRTU
import registerCollection.DiscreteOut
import registerCollection.DiscreteOutCollection
import registerCollection.DiscreteOutViewProperties
import registerMapTikModscan.CellData
import registerMapTikModscan.SerializableCellsContainer
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.math.ceil

class FormValues {
    companion object {
        var settings = SettingsModbusRTU( "" )

        var device = ModbusRTU( "" )

        var setpoints = DiscreteOutCollection()

        val discreteOutProperties = mutableListOf<DiscreteOutViewProperties>()

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
            return tikModscanMap.сellsArray.find { it.name == name } ?: CellData()
        }

        fun findSetpoint( name : String ) : DiscreteOut?
        {
            return setpoints.items.find { it?.descriptionValues == name }
        }

        fun countSampleTime( sampleLen : Int ) : Int
        {
            return ceil(sampleLen * setpoints.items.count() * ( ( settings.delayAnswerRead / 1000.0 ) +
                    ( settings.delayAnswerWrite / 1000.0 ) +
                    ( 2 * 12 / 8.0 / settings.baudRate ) ) / 60.0 ) .toInt()
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