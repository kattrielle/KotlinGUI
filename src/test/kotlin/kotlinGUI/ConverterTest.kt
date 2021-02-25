package kotlinGUI

import kotlinGUI.viewModel.ToDoubleConverter
import kotlinGUI.viewModel.ToIntConverter
import kotlin.test.Test
import kotlin.test.assertEquals

class ConverterTest {
    @Test fun testDoubleConverting()
    {
        assertEquals( 5.0, ToDoubleConverter.fromString("5") )
        assertEquals( 4.0, ToDoubleConverter.fromString("4.0") )
        assertEquals( 1.3, ToDoubleConverter.fromString("1,3") )
        assertEquals( 0.0, ToDoubleConverter.fromString("test") )
        assertEquals( 0.0, ToDoubleConverter.fromString("12xy") )
        assertEquals( "5.0", ToDoubleConverter.toString( 5.0 ) )
        assertEquals( "4.5", ToDoubleConverter.toString( 4.5 ) )
    }

    @Test fun testIntConverter()
    {
        assertEquals( 1, ToIntConverter.fromString("1") )
        assertEquals( 0, ToIntConverter.fromString("2.0") )
        assertEquals( 0, ToIntConverter.fromString("1f") )
        assertEquals( "1", ToIntConverter.toString(1) )
    }
}