package registerCollection

import registerMapTikModscan.CellData

class DiscreteOutMapper() {
    var values : CellData? = null //регистр выборки
    var setpointSample : CellData? = null //регистр хранения адреса (номера регистра) выборки для уставки
    var setpoint : CellData? = null //регистр величины уставки
    var timeSet : CellData? = null //регистр времени устаноки для уставки
    var timeUnset : CellData? = null //регистр времени снятия для уставки
    var weight : CellData? = null //регистр весового коэффициента для уставки

    constructor( baseOut : DiscreteOut ) : this() {
        values = baseOut.values
        setpointSample = baseOut.setpointSample
        setpoint = baseOut.setpoint
        timeSet = baseOut.timeSet
        timeUnset = baseOut.timeUnset
        weight = baseOut.weight
    }
}