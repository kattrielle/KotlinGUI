package kotlinGUI

import kotlinGUI.viewModel.DiscreteOutCellsProperties
import kotlinGUI.viewModel.RegisterDescriptions
import tornadofx.*

class ShowDiscreteOutFragment : Fragment() {
    private val cells : DiscreteOutCellsProperties by param()

    override val root = fieldset( cells.discreteOut.descriptionValues ) {
        field("Выборка: ${cells.discreteOut.descriptionValues}") {
            textfield( cells.sampleRegister )
        }
        field("Тип регистра") {
            combobox( cells.sampleType, RegisterDescriptions.types )
        }
        field( "Формат регистра" ) {
            combobox( cells.sampleFormat, RegisterDescriptions.formats )
        }
        field("Уставка - адрес выборки: ${cells.discreteOut.descriptionSetpointSample}") {
            textfield(cells.setpointSampleRegister)
        }
        field("Тип регистра") {
            combobox( cells.setpointSampleType, RegisterDescriptions.types )
        }
        field( "Формат регистра" ) {
            combobox( cells.setpointSampleFormat, RegisterDescriptions.formats )
        }
        field("Уставка - величина срабатывания: ${cells.discreteOut.descriptionSetpoint}") {
            textfield(cells.setpointRegister)
        }
        field("Тип регистра") {
            combobox( cells.setpointType, RegisterDescriptions.types )
        }
        field( "Формат регистра" ) {
            combobox( cells.setpointFormat, RegisterDescriptions.formats )
        }
        field("Уставка - время срабатывания: ${cells.discreteOut.descriptionTimeSet}") {
            textfield(cells.timeSetRegister)
        }
        field("Тип регистра") {
            combobox( cells.timeSetType, RegisterDescriptions.types )
        }
        field( "Формат регистра" ) {
            combobox( cells.timeSetFormat, RegisterDescriptions.formats )
        }
        field("Уставка - время снятия: ${cells.discreteOut.descriptionTimeUnset}") {
            textfield(cells.timeUnsetRegister)
        }
        field("Тип регистра") {
            combobox( cells.timeUnsetType, RegisterDescriptions.types )
        }
        field( "Формат регистра" ) {
            combobox( cells.timeUnsetFormat, RegisterDescriptions.formats )
        }
        field("Уставка - весовой коэффициент: ${cells.discreteOut.descriptionWeight}") {
            textfield(cells.weightRegister)
        }
        field("Тип регистра") {
            combobox( cells.weightType, RegisterDescriptions.types )
        }
        field( "Формат регистра" ) {
            combobox( cells.weightFormat, RegisterDescriptions.formats )
        }
    }
}