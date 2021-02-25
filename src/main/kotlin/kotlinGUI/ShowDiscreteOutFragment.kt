package kotlinGUI

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import kotlinGUI.viewModel.RegisterDescriptions
import registerCollection.DiscreteOut
import tornadofx.*

class ShowDiscreteOutFragment( val discreteOut : DiscreteOut ) : Fragment() {
    private val sampleRegister = SimpleIntegerProperty( discreteOut.registerValues )
    private val sampleType = SimpleStringProperty(
            RegisterDescriptions.convertFromTypeToText( discreteOut.values?.type) )
    private val sampleFormat = SimpleStringProperty(
            RegisterDescriptions.convertFromFormatToText( discreteOut.values?.format) )
    private val setpointRegister = SimpleIntegerProperty( discreteOut.registerSetpoint )
    private val setpointType = SimpleStringProperty(
            RegisterDescriptions.convertFromTypeToText( discreteOut.setpoint?.type) )
    private val setpointFormat = SimpleStringProperty(
            RegisterDescriptions.convertFromFormatToText( discreteOut.setpoint?.format) )
    private val setpointSampleRegister = SimpleIntegerProperty( discreteOut.registerSetpointSample )
    private val setpointSampleType = SimpleStringProperty(
            RegisterDescriptions.convertFromTypeToText( discreteOut.setpointSample?.type) )
    private val setpointSampleFormat = SimpleStringProperty(
            RegisterDescriptions.convertFromFormatToText( discreteOut.setpointSample?.format) )
    private val timeSetRegister = SimpleIntegerProperty( discreteOut.registerTimeSet )
    private val timeSetType = SimpleStringProperty(
            RegisterDescriptions.convertFromTypeToText( discreteOut.timeSet?.type) )
    private val timeSetFormat = SimpleStringProperty(
            RegisterDescriptions.convertFromFormatToText( discreteOut.timeSet?.format) )
    private val timeUnsetRegister = SimpleIntegerProperty( discreteOut.registerTimeUnset )
    private val timeUnsetType = SimpleStringProperty(
            RegisterDescriptions.convertFromTypeToText( discreteOut.timeUnset?.type) )
    private val timeUnsetFormat = SimpleStringProperty(
            RegisterDescriptions.convertFromFormatToText( discreteOut.timeUnset?.format) )
    private val weightRegister = SimpleIntegerProperty( discreteOut.registerWeigth )
    private val weightType = SimpleStringProperty(
            RegisterDescriptions.convertFromTypeToText( discreteOut.weight?.type) )
    private val weightFormat = SimpleStringProperty(
            RegisterDescriptions.convertFromFormatToText( discreteOut.weight?.format) )

    override val root = fieldset( discreteOut.descriptionValues ) {
        field("Выборка: ${discreteOut.descriptionValues}") {
            textfield(sampleRegister)
        }
        field("Тип регистра") {
            combobox( sampleType, RegisterDescriptions.types )
        }
        field( "Формат регистра" ) {
            combobox( sampleFormat, RegisterDescriptions.formats )
        }
        field("Уставка - адрес выборки: ${discreteOut.descriptionSetpointSample}") {
            textfield(setpointSampleRegister)
        }
        field("Тип регистра") {
            combobox( setpointSampleType, RegisterDescriptions.types )
        }
        field( "Формат регистра" ) {
            combobox( setpointSampleFormat, RegisterDescriptions.formats )
        }
        field("Уставка - величина срабатывания: ${discreteOut.descriptionSetpoint}") {
            textfield(setpointRegister)
        }
        field("Тип регистра") {
            combobox( setpointType, RegisterDescriptions.types )
        }
        field( "Формат регистра" ) {
            combobox( setpointFormat, RegisterDescriptions.formats )
        }
        field("Уставка - время срабатывания: ${discreteOut.descriptionTimeSet}") {
            textfield(timeSetRegister)
        }
        field("Тип регистра") {
            combobox( timeSetType, RegisterDescriptions.types )
        }
        field( "Формат регистра" ) {
            combobox( timeSetFormat, RegisterDescriptions.formats )
        }
        field("Уставка - время снятия: ${discreteOut.descriptionTimeUnset}") {
            textfield(timeUnsetRegister)
        }
        field("Тип регистра") {
            combobox( timeUnsetType, RegisterDescriptions.types )
        }
        field( "Формат регистра" ) {
            combobox( timeUnsetFormat, RegisterDescriptions.formats )
        }
        field("Уставка - весовой коэффициент: ${discreteOut.descriptionWeight}") {
            textfield(weightRegister)
        }
        field("Тип регистра") {
            combobox( weightType, RegisterDescriptions.types )
        }
        field( "Формат регистра" ) {
            combobox( weightFormat, RegisterDescriptions.formats )
        }
    }

    override fun onSave() {
        saveParams()
    }

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