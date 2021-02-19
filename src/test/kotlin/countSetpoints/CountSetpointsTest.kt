package countSetpoints

import kotlin.test.Test
import kotlin.test.assertEquals

class CountSetpointsTest {
    @Test fun testCountSetpointMaximum()
    {
        val sample = listOf( 7.0, 8.0, 7.5, 7.7, 8.5 )
        val result = CountSetpoints.count( CountSetpointVariant.Maximum, sample, 10.0 )

        assertEquals( 9.35, result )
    }

    @Test fun testCountSetpointAverage()
    {
        val sample = listOf( 7.0, 8.0, 7.5, 7.7, 8.5 )
        val result = CountSetpoints.count( CountSetpointVariant.Average, sample, 10.0 )

        assertEquals( 8.514, result )
    }

    @Test fun testCountSetpointMaximumMSD()
    {
        val sample = listOf( 7.0, 8.0, 7.5, 7.7, 8.5 )
        val result = CountSetpoints.count( CountSetpointVariant.MaximumMSD, sample, 1.0 )

        assertEquals( 9.000399840127873, result )
    }

    @Test fun testCountSetpointAverageMSD()
    {
        val sample = listOf( 7.0, 8.0, 7.5, 7.7, 8.5 )
        val result = CountSetpoints.count( CountSetpointVariant.AverageMSD, sample, 1.0 )

        assertEquals( 8.240399840127873, result )
    }

    @Test fun testToDoubleFun()
    {
        assertEquals( 1.5, "1.5".toDoubleOrNull() )
        assertEquals( null, "text".toDoubleOrNull() )
        assertEquals( 5.0, "5".toDouble() )
        assertEquals( null, "1,3".toDoubleOrNull() )
        assertEquals( null, "12hello3".toDoubleOrNull() )
    }
}