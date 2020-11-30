package serialization

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import registerCollection.DiscreteOutCollection
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
        val filepath = "/home/kate/repos/KotlinGUI/src/test/resources/setpointMap.xml"
        val xmlMapper = XmlMapper()
        val map = xmlMapper.readValue(
                File(filepath),
                DiscreteOutCollection::class.java)

        assertEquals(2, map.registerNumDefence )
        assertEquals("Пароль ABCD", map.descriptionWriteDefence )

        assertEquals( 1, map.items.size )

        val setpoint = map.items.first()

        //assertNotNull( setpoint.values )
        assertEquals("ось X скорость СКЗ мм/c", setpoint.descriptionValues )
        assertEquals( 1009, setpoint.registerValues )
        assertEquals( ElementFormat.swFloat, setpoint.values?.format )
        assertEquals( ElementType.InputRegister, setpoint.values?.type )

        //assertNotNull( setpoint.setpointSample )
        assertEquals( "Виброключ 1, адрес", setpoint.descriptionSetpointSample )
        assertEquals( 41, setpoint.registerSetpointSample )
        assertEquals( ElementFormat.Int, setpoint.setpointSample?.format )
        assertEquals( ElementType.HoldingRegister, setpoint.setpointSample?.type )

        //assertNotNull( setpoint.setpoint )
        assertEquals( "Виброключ 1, значение", setpoint.descriptionSetpoint )
        assertEquals( 42, setpoint.registerSetpoint )
        assertEquals( ElementFormat.swFloat, setpoint.setpoint?.format )
        assertEquals( ElementType.HoldingRegister, setpoint.setpoint?.type )

        //assertNotNull( setpoint.timeSet)
        assertEquals( "Виброключ 1, время установки", setpoint.descriptionTimeSet )
        assertEquals( 44, setpoint.registerTimeSet )
        assertEquals( ElementFormat.Int, setpoint.timeSet?.format )
        assertEquals( ElementType.HoldingRegister, setpoint.timeSet?.type )

        //assertNotNull( setpoint.timeUnset, "time Unset" )
        assertEquals( "Виброключ 1, время снятия", setpoint.descriptionTimeUnset )
        assertEquals( 45, setpoint.registerTimeUnset )
        assertEquals( ElementFormat.Int, setpoint.timeUnset?.format )
        assertEquals( ElementType.HoldingRegister, setpoint.timeUnset?.type )

        //assertNotNull( setpoint.weight, "weight" )
        assertEquals( "Виброключ 1, вес", setpoint.descriptionWeight )
        assertEquals( 46, setpoint.registerWeigth )
        assertEquals( ElementFormat.Int, setpoint.weight?.format )
        assertEquals( ElementType.HoldingRegister, setpoint.weight?.type )
    }
}