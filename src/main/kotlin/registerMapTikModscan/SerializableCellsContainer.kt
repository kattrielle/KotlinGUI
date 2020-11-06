package registerMapTikModscan

import com.fasterxml.jackson.annotation.JsonProperty

class SerializableCellsContainer {
    @JsonProperty("CellsArray") var сellsArray: Array<CellData> = emptyArray()
    @JsonProperty("AdaptersArray") var adaptersArray: Array<AdapterData> = emptyArray()
    @JsonProperty("TabsArray") var tabsArray: Array<TabsData> = emptyArray()
    @JsonProperty("GuiData") var guiData: RegMapGuiData? = null
}