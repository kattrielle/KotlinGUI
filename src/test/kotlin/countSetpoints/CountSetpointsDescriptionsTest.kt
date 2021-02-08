package countSetpoints

import kotlin.test.Test
import kotlin.test.assertEquals

class CountSetpointsDescriptionsTest {
    @Test fun testCountTypesList()
    {
        val list = CountSetpointsDescriptions.getCountTypesList()

        assertEquals(4, list.size )
        assertEquals("Среднее", list[0] )
        assertEquals("Максимум", list[1] )
        assertEquals("СКО от среднего", list[2] )
        assertEquals("СКО от максимума", list[3] )
    }

    @Test fun testDescriptions()
    {
        assertEquals( "Расчёт: среднее значение + n%",
                CountSetpointsDescriptions.getCountDescription("Среднее") )
        assertEquals( "Расчёт: максимальное значение + n%",
                CountSetpointsDescriptions.getCountDescription("Максимум") )
        assertEquals( "Расчёт: среднее значение + n * СКО",
                CountSetpointsDescriptions.getCountDescription("СКО от среднего") )
        assertEquals( "Расчёт: максимальное значение + n * СКО",
                CountSetpointsDescriptions.getCountDescription("СКО от максимума") )
        assertEquals( "", CountSetpointsDescriptions.getCountDescription( "smth") )
    }

    @Test fun testSelectCountType()
    {
        assertEquals( CountSetpointVariant.Average,
                CountSetpointsDescriptions.selectCountType("Среднее") )
        assertEquals( CountSetpointVariant.Maximum,
                CountSetpointsDescriptions.selectCountType("Максимум") )
        assertEquals( CountSetpointVariant.AverageMSD,
                CountSetpointsDescriptions.selectCountType("СКО от среднего") )
        assertEquals( CountSetpointVariant.MaximumMSD,
                CountSetpointsDescriptions.selectCountType("СКО от максимума") )
        assertEquals( CountSetpointVariant.Maximum,
                CountSetpointsDescriptions.selectCountType( "smth") )
    }
}