package registerMapTikModscan

import com.fasterxml.jackson.annotation.JsonProperty

class TabsData {
    @JsonProperty("Tag") var tag: String = ""
    @JsonProperty("ColumnsWidths") var columnsWidths: Array<Int> = emptyArray()
}