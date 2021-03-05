package kotlinGUI

import externalDevices.devices.ModbusRTU
import externalDevices.settings.SettingsModbusRTU
import kotlinGUI.viewModel.DiscreteOutModel
import kotlinGUI.viewModel.DiscreteOutProperties
import registerCollection.DiscreteOut
import registerCollection.DiscreteOutCollection
import kotlinGUI.viewModel.DiscreteOutViewProperties
import registerMapTikModscan.CellData
import registerMapTikModscan.SerializableCellsContainer
import saveableProperties.SaveableProperties
import tornadofx.asObservable
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.math.ceil

class FormValues {
    companion object {
        var savedProperties = SaveableProperties()

        var settings = SettingsModbusRTU( "" )

        var device = ModbusRTU( "" )

        var setpoints = DiscreteOutCollection()

        val discreteOutProperties = mutableListOf<DiscreteOutViewProperties>()

        val discreteOutTableViewProperties = mutableListOf<DiscreteOutProperties>().asObservable()

        //val discreteOutModel = mutableListOf<DiscreteOutModel>().asObservable()

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
            return setpoints.items.find { it.descriptionValues == name }
        }

        fun countSampleTime( sampleLen : Int ) : Int
        {
            val count = setpoints.items.filter { it.isUsed }.count()
            return ceil(sampleLen * count * ( ( settings.delayAnswerRead / 1000.0 ) +
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

        fun updateDiscreteOutTableViewPropertiesList()
        {
            while ( discreteOutTableViewProperties.size < setpoints.items.size )
            {
                val index = discreteOutTableViewProperties.size
                discreteOutTableViewProperties.add( DiscreteOutProperties( setpoints.items[ index ] ) )
                //discreteOutModel.add( DiscreteOutModel( discreteOutTableViewProperties[index] ) )
            }
        }

        fun updateDiscreteOutProperties()
        {
            discreteOutTableViewProperties.forEach { it.updateProperties() }
        }

        fun updateDiscreteOutValues()
        {
            discreteOutTableViewProperties.forEach { it.updateBaseValues() }
        }

        fun clearDiscreteOutCollection()
        {
            setpoints.items.clear()
            setpoints.registerWriteDefence = null
            discreteOutTableViewProperties.clear()
            //discreteOutModel.clear()
        }

    }
}