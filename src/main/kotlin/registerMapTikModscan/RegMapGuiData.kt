package registerMapTikModscan

import com.fasterxml.jackson.annotation.JsonProperty

class RegMapGuiData {
    @JsonProperty("DataGridColsWidths") var dataGridColsWidths: Array<Int> = emptyArray()
    @JsonProperty("CoeffsShowing") var coeffsShowing: Boolean = false
    @JsonProperty("FormatShowing") var formatShowing: Boolean = false
    @JsonProperty("ColorsShowing") var colorsShowing: Boolean = false
}