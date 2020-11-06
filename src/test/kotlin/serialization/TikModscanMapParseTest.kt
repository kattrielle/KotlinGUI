package serialization

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import registerMapTikModscan.*
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class TikModscanMapParseTest {

    @Test fun testParseCellData()
    {
        val filepath = "/home/kate/repos/KotlinGUI/src/test/resources/regMap.xml"
        val xmlMapper = XmlMapper()
        val map = xmlMapper.readValue(
                File(filepath),
                SerializableCellsContainer::class.java)

        assertEquals( ElementType.HoldingRegister, map.сellsArray[0].type, "0:type" )
        assertEquals( ElementFormat.Int, map.сellsArray[0].format, "0:format" )
        assertEquals( ElementStatus.Value, map.сellsArray[0].status, "0:status" )
        assertEquals( ElementPresentation.Dec, map.сellsArray[0].represent, "0:presentation" )
        assertEquals( 1, map.сellsArray[0].address, "0:addr" )
        assertEquals(0, map.сellsArray[0].adapterId, "0: adapter" )
        assertEquals( 1, map.сellsArray[0].deviceAddress, "0:devAddr" )
        assertEquals( 50.toString(), map.сellsArray[0].value, "0:value" )
        assertEquals( true, map.сellsArray[0].isHaveData, "0:isHaveData" )
        assertEquals( "Квитирование", map.сellsArray[0].name, "0: name" )
        assertEquals( "Вкладка 1", map.сellsArray[0].tag, "0:tag" )
        assertEquals( 1, map.сellsArray[0].isEnabled, "0:isEnabled" )
    }
}