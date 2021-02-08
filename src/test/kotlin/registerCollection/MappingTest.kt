package registerCollection

import registerMapTikModscan.*
import kotlin.test.Test
import kotlin.test.assertEquals

class MappingTest {
    @Test fun testCreateMapperFromDiscreteOut()
    {
        val setpoint = createDiscreteOut()
        val map = DiscreteOutMapper( setpoint )

        assertEquals( setpoint.values?.name, map.values?.name )
        assertEquals( setpoint.values?.type, map.values?.type )
        assertEquals( setpoint.values?.format, map.values?.format )
        assertEquals( setpoint.values?.address, map.values?.address )
        assertEquals( setpoint.setpointSample?.name, map.setpointSample?.name )
        assertEquals( setpoint.setpointSample?.type, map.setpointSample?.type )
        assertEquals( setpoint.setpointSample?.format, map.setpointSample?.format )
        assertEquals( setpoint.setpointSample?.address, map.setpointSample?.address )
        assertEquals( setpoint.setpoint?.name, map.setpoint?.name )
        assertEquals( setpoint.setpoint?.type, map.setpoint?.type )
        assertEquals( setpoint.setpoint?.format, map.setpoint?.format )
        assertEquals( setpoint.setpoint?.address, map.setpoint?.address )
        assertEquals( setpoint.timeSet?.name, map.timeSet?.name )
        assertEquals( setpoint.timeSet?.type, map.timeSet?.type )
        assertEquals( setpoint.timeSet?.format, map.timeSet?.format )
        assertEquals( setpoint.timeSet?.address, map.timeSet?.address )
        assertEquals( setpoint.timeUnset?.name, map.timeUnset?.name )
        assertEquals( setpoint.timeUnset?.type, map.timeUnset?.type )
        assertEquals( setpoint.timeUnset?.format, map.timeUnset?.format )
        assertEquals( setpoint.timeUnset?.address, map.timeUnset?.address )
        assertEquals( setpoint.weight?.name, map.weight?.name )
        assertEquals( setpoint.weight?.type, map.weight?.type )
        assertEquals( setpoint.weight?.format, map.weight?.format )
        assertEquals( setpoint.weight?.address, map.weight?.address )
    }

    @Test fun testCreateMapperFromDiscreteOutCollection()
    {
        val collection = DiscreteOutCollection()
        collection.registerWriteDefence =  CellData()
        with ( collection.registerWriteDefence ) {
            this?.name = "samples"
            this?.type = ElementType.HoldingRegister
            this?.format = ElementFormat.Int
            this?.address = 100
        }
        collection.items.add( createDiscreteOut() )

        val map = DiscreteOutCollectionMapper( collection )

        assertEquals( collection.registerWriteDefence?.name, map.registerWriteDefence?.name )
        assertEquals( collection.registerWriteDefence?.type, map.registerWriteDefence?.type )
        assertEquals( collection.registerWriteDefence?.format, map.registerWriteDefence?.format )
        assertEquals( collection.registerWriteDefence?.address, map.registerWriteDefence?.address )
        assertEquals( 1, map.items.size )
        assertEquals( collection.items[0].values?.name, map.items[0].values?.name )
        assertEquals( collection.items[0].setpointSample?.name, map.items[0].setpointSample?.name )
        assertEquals( collection.items[0].setpoint?.name, map.items[0].setpoint?.name )
        assertEquals( collection.items[0].timeSet?.name, map.items[0].timeSet?.name )
        assertEquals( collection.items[0].timeUnset?.name, map.items[0].timeUnset?.name )
        assertEquals( collection.items[0].weight?.name, map.items[0].weight?.name )
    }

    private fun createDiscreteOut() : DiscreteOut
    {
        val result = DiscreteOut()
        result.values = CellData()
        with ( result.values ) {
            this?.name = "samples"
            this?.type = ElementType.InputRegister
            this?.format = ElementFormat.Int
            this?.address = 1
        }
        result.setpointSample = CellData()
        with ( result.setpointSample ) {
            this?.name = "setpointSample"
            this?.type = ElementType.HoldingRegister
            this?.format = ElementFormat.Int
            this?.address = 1
        }
        result.setpoint = CellData()
        with ( result.setpoint ) {
            this?.name = "setpoint"
            this?.type = ElementType.HoldingRegister
            this?.format = ElementFormat.swFloat
            this?.address = 2
        }
        result.timeSet = CellData()
        with ( result.timeSet ) {
            this?.name = "timeSet"
            this?.type = ElementType.HoldingRegister
            this?.format = ElementFormat.Int
            this?.address = 4
        }
        result.timeUnset = CellData()
        with ( result.timeUnset ) {
            this?.name = "timeUnset"
            this?.type = ElementType.HoldingRegister
            this?.format = ElementFormat.Int
            this?.address = 5
        }
        result.weight = CellData()
        with ( result.weight ) {
            this?.name = "weight"
            this?.type = ElementType.HoldingRegister
            this?.format = ElementFormat.Int32
            this?.address = 7
        }

        return result
    }
}