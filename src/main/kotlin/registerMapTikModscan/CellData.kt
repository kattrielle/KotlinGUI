package registerMapTikModscan

import com.fasterxml.jackson.annotation.JsonProperty

class CellData {
    @JsonProperty("Type") var type: ElementType? = null
    @JsonProperty("Format") var format: ElementFormat? = null
    @JsonProperty("Status") var status: ElementStatus? = null
    @JsonProperty("Represent") var represent: ElementPresentation? = null
    @JsonProperty("Adress") var address = 0 //"чистый" адрес: номер регистра - 1
    @JsonProperty("AdapterId") var adapterId = 0
    @JsonProperty("DeviceAdress") var deviceAddress = 0
    @JsonProperty("Value") var value: String = ""
    @JsonProperty("isHaveData") var isHaveData: Boolean = false
    @JsonProperty("Name") var name: String = ""
    @JsonProperty("Tag") var tag: String = ""
    @JsonProperty("FormulaData") var formulaData: CellFormulaData? = null
    @JsonProperty("IsEnabled") var isEnabled = 0 // Int - for support old reg maps

}