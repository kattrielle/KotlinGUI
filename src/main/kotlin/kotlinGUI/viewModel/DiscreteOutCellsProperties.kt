package kotlinGUI.viewModel

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import registerCollection.DiscreteOut

class DiscreteOutCellsProperties( val discreteOut : DiscreteOut) {
    val sampleRegister = SimpleIntegerProperty( discreteOut.registerValues )
    val sampleType = SimpleStringProperty(
            RegisterDescriptions.convertFromTypeToText( discreteOut.values?.type) )
    val sampleFormat = SimpleStringProperty(
            RegisterDescriptions.convertFromFormatToText( discreteOut.values?.format) )
    val setpointRegister = SimpleIntegerProperty( discreteOut.registerSetpoint )
    val setpointType = SimpleStringProperty(
            RegisterDescriptions.convertFromTypeToText( discreteOut.setpoint?.type) )
    val setpointFormat = SimpleStringProperty(
            RegisterDescriptions.convertFromFormatToText( discreteOut.setpoint?.format) )
    val setpointSampleRegister = SimpleIntegerProperty( discreteOut.registerSetpointSample )
    val setpointSampleType = SimpleStringProperty(
            RegisterDescriptions.convertFromTypeToText( discreteOut.setpointSample?.type) )
    val setpointSampleFormat = SimpleStringProperty(
            RegisterDescriptions.convertFromFormatToText( discreteOut.setpointSample?.format) )
    val timeSetRegister = SimpleIntegerProperty( discreteOut.registerTimeSet )
    val timeSetType = SimpleStringProperty(
            RegisterDescriptions.convertFromTypeToText( discreteOut.timeSet?.type) )
    val timeSetFormat = SimpleStringProperty(
            RegisterDescriptions.convertFromFormatToText( discreteOut.timeSet?.format) )
    val timeUnsetRegister = SimpleIntegerProperty( discreteOut.registerTimeUnset )
    val timeUnsetType = SimpleStringProperty(
            RegisterDescriptions.convertFromTypeToText( discreteOut.timeUnset?.type) )
    val timeUnsetFormat = SimpleStringProperty(
            RegisterDescriptions.convertFromFormatToText( discreteOut.timeUnset?.format) )
    val weightRegister = SimpleIntegerProperty( discreteOut.registerWeigth )
    val weightType = SimpleStringProperty(
            RegisterDescriptions.convertFromTypeToText( discreteOut.weight?.type) )
    val weightFormat = SimpleStringProperty(
            RegisterDescriptions.convertFromFormatToText( discreteOut.weight?.format) )

    fun saveParams()
    {
        discreteOut.registerValues = sampleRegister.value
        discreteOut.registerSetpoint = setpointRegister.value
        discreteOut.registerSetpointSample = setpointSampleRegister.value
        discreteOut.registerTimeSet = timeSetRegister.value
        discreteOut.registerTimeUnset = timeUnsetRegister.value
        discreteOut.registerWeigth = weightRegister.value

        discreteOut.values?.type = RegisterDescriptions.selectTypeFromTextLine( sampleType.value )
        discreteOut.setpoint?.type = RegisterDescriptions.selectTypeFromTextLine( setpointType.value )
        discreteOut.setpointSample?.type = RegisterDescriptions.selectTypeFromTextLine( setpointSampleType.value )
        discreteOut.timeSet?.type = RegisterDescriptions.selectTypeFromTextLine( timeSetType.value )
        discreteOut.timeUnset?.type = RegisterDescriptions.selectTypeFromTextLine( timeUnsetType.value )
        discreteOut.weight?.type = RegisterDescriptions.selectTypeFromTextLine( weightType.value )

        discreteOut.values?.format = RegisterDescriptions.selectFormatFromTextLine( sampleFormat.value )
        discreteOut.setpoint?.format = RegisterDescriptions.selectFormatFromTextLine( setpointFormat.value )
        discreteOut.setpointSample?.format = RegisterDescriptions.selectFormatFromTextLine( setpointSampleFormat.value )
        discreteOut.timeSet?.format = RegisterDescriptions.selectFormatFromTextLine( timeSetFormat.value )
        discreteOut.timeUnset?.format = RegisterDescriptions.selectFormatFromTextLine( timeUnsetFormat.value )
        discreteOut.weight?.format = RegisterDescriptions.selectFormatFromTextLine( weightFormat.value )
    }
}