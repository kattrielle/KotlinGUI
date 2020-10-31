package registerMapTikModscan

import com.fasterxml.jackson.annotation.JsonProperty

class AdapterData {
    @JsonProperty("FullAdapterInf") var fullAdapterInf: String = ""
    @JsonProperty("Devices") var devices: Array<Device> = emptyArray()
    @JsonProperty("DefaultDeviceAdress") var defaultDeviceAddress = 0
}