package saveableProperties

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import externalDevices.settings.SettingsModbusRTU
import kotlinGUI.FormValues
import java.io.File

class PropertiesSaver {
    companion object {
        fun LoadProperties()
        {
            val fileProperties = "properties.xml"
            try {
                val xmlMapper = XmlMapper()
                FormValues.savedProperties = xmlMapper.readValue( File( fileProperties ), SaveableProperties::class.java )
                FormValues.settings = SettingsModbusRTU( FormValues.savedProperties.selectedPortNum )
            } catch ( e : Exception) {
                println( FormValues.getCurrentTime() + "An error occured at properties reading: ${e.message}" )
            }
        }

        fun SaveProperties()
        {
            val fileProperties = "properties.xml"
            try {
                val xmlMapper = XmlMapper()
                xmlMapper.writeValue( File( fileProperties ), FormValues.savedProperties )
            } catch ( e : Exception) {
                println( FormValues.getCurrentTime() + "An error occured at properties saving: ${e.message}" )
            }
        }
    }
}