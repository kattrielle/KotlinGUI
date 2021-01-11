package kotlinGUI.viewModel

import countSetpoints.CountSetpointsDescriptions
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class CountSetpointProperties {
    val sampleLenProperty = SimpleIntegerProperty()
    var sampleLen by sampleLenProperty

    val selectCountProperty = SimpleStringProperty()
    var selectCount by selectCountProperty

    val countDescriptionProperty = SimpleStringProperty()
    var countDescription by countDescriptionProperty

    val percentProperty = SimpleDoubleProperty()
    var percent by percentProperty

    init {
        selectCountProperty.onChange {
            countDescriptionProperty.value = CountSetpointsDescriptions.getCountDescription(
                    selectCountProperty.value )
        }
    }
}