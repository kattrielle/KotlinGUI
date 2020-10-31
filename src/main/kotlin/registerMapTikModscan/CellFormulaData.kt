package registerMapTikModscan

import com.fasterxml.jackson.annotation.JsonProperty

class CellFormulaData {
    @JsonProperty("Kcoeff") var kCoeff = 0.0
    @JsonProperty("Scoeff") var sCoeff = 0.0
    @JsonProperty("DigitsAfterPointCount") var digitsAfterPointCount = 0 // Не используется, но нужен тут для совместимости со старыми картами регистров

}