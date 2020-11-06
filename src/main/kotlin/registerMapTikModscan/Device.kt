package registerMapTikModscan

import com.fasterxml.jackson.annotation.JsonProperty

class Device {
    @JsonProperty("Adress") var address = 0
    @JsonProperty("Name") var name: String = ""
}