package kotlinGUI.viewModel

import countSetpoints.CountSetpointsDescriptions
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import kotlinGUI.FormValues
import tornadofx.*

class CountSetpointProperties {
    val sampleLenProperty = SimpleIntegerProperty()
    var sampleLen by sampleLenProperty

    val sampleTimeProperty = SimpleIntegerProperty()

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

        sampleLenProperty.onChange {
            sampleTimeProperty.value = FormValues.countSampleTime( sampleLenProperty.value )
        }
    }
}