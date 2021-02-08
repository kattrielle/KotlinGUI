package serialization

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import registerCollection.DiscreteOutCollection
import registerCollection.DiscreteOutCollectionMapper
import registerMapTikModscan.ElementFormat
import registerMapTikModscan.ElementType
import registerMapTikModscan.SerializableCellsContainer
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SetpointMapParseTest {

    @Test fun testParseSetpointMap()
    {
        val filepath = "src/test/resources/setpointMap.xml"
        val xmlMapper = XmlMapper()
        val map = xmlMapper.readValue(
                File(filepath),
                DiscreteOutCollectionMapper::class.java)
        val collection = DiscreteOutCollection( map )

        assertEquals(2, collection.registerNumDefence )
        assertEquals("Пароль ABCD", collection.descriptionWriteDefence )

        assertEquals( 1, map.items.size )

        val setpoint = collection.items.first()

        assertEquals("ось X скорость СКЗ мм/c", setpoint.descriptionValues )
        assertEquals( 1009, setpoint.registerValues )
        assertEquals( ElementFormat.swFloat, setpoint.values?.format )
        assertEquals( ElementType.InputRegister, setpoint.values?.type )

        assertEquals( "Виброключ 1, адрес", setpoint.descriptionSetpointSample )
        assertEquals( 41, setpoint.registerSetpointSample )
        assertEquals( ElementFormat.Int, setpoint.setpointSample?.format )
        assertEquals( ElementType.HoldingRegister, setpoint.setpointSample?.type )

        assertEquals( "Виброключ 1, уставка", setpoint.descriptionSetpoint )
        assertEquals( 42, setpoint.registerSetpoint )
        assertEquals( ElementFormat.swFloat, setpoint.setpoint?.format )
        assertEquals( ElementType.HoldingRegister, setpoint.setpoint?.type )

        assertEquals( "Виброключ 1, время установки", setpoint.descriptionTimeSet )
        assertEquals( 44, setpoint.registerTimeSet )
        assertEquals( ElementFormat.Int, setpoint.timeSet?.format )
        assertEquals( ElementType.HoldingRegister, setpoint.timeSet?.type )

        assertEquals( "Виброключ 1, время снятия", setpoint.descriptionTimeUnset )
        assertEquals( 45, setpoint.registerTimeUnset )
        assertEquals( ElementFormat.Int, setpoint.timeUnset?.format )
        assertEquals( ElementType.HoldingRegister, setpoint.timeUnset?.type )

        assertEquals( "Виброключ 1, вес", setpoint.descriptionWeight )
        assertEquals( 46, setpoint.registerWeigth )
        assertEquals( ElementFormat.Int, setpoint.weight?.format )
        assertEquals( ElementType.HoldingRegister, setpoint.weight?.type )
    }
}