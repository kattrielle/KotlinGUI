package registerCollection

import registerMapTikModscan.CellData

class DiscreteOutCollectionMapper() {
    var registerWriteDefence : CellData? = null

    val items = mutableListOf<DiscreteOutMapper>()

    constructor( baseCollection : DiscreteOutCollection ) : this()
    {
        registerWriteDefence = baseCollection.registerWriteDefence

        for( out in baseCollection.items )
        {
            items.add( DiscreteOutMapper( out ) )
        }
    }
}