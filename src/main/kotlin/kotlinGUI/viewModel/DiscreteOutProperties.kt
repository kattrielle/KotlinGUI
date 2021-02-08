package kotlinGUI.viewModel

import registerCollection.DiscreteOut
import tornadofx.*

class DiscreteOutProperties( val discreteOut : DiscreteOut) {
    val descriptionValues
        get() = discreteOut.descriptionValues

    val registerValues
        get() = discreteOut.registerValues

    var valueSetpointSample by property( 0 )
    fun valueSetpointSampleProperty() = getProperty( DiscreteOutProperties:: valueSetpointSample )

    var valueSetpoint by property( 0.0 )
    fun valueSetpointProperty() = getProperty( DiscreteOutProperties::valueSetpoint )

    var valueTimeSet by property( 0 )
    fun valueTimeSetProperty() = getProperty( DiscreteOutProperties::valueTimeSet )

    var valueTimeUnset by property( 0 )
    fun valueTimeUnsetProperty() = getProperty( DiscreteOutProperties::valueTimeUnset )

    var valueWeight by property( 0 )
    fun valueWeightProperty() = getProperty( DiscreteOutProperties:: valueWeight )

    var isUsed by property( true )
    fun isUsedProperty() = getProperty( DiscreteOutProperties::isUsed )

    init {
        isUsedProperty().onChange {
            if ( !isUsed ) {
                valueSetpoint = 0.0
                valueTimeSet = 0
                valueTimeUnset = 0
                valueWeight = 0
            }

        }
    }

    fun updateProperties()
    {
        valueSetpointSample = discreteOut.valueSetpointSample
        valueSetpoint = discreteOut.valueSetpoint
        valueTimeSet = discreteOut.valueTimeSet
        valueTimeUnset = discreteOut.valueTimeUnset
        valueWeight = discreteOut.valueWeight
        isUsed = discreteOut.isUsed
    }

    fun updateBaseValues()
    {
        discreteOut.valueSetpointSample = valueSetpointSample
        discreteOut.valueSetpoint = valueSetpoint
        discreteOut.valueTimeSet = valueTimeSet
        discreteOut.valueTimeUnset = valueTimeUnset
        discreteOut.valueWeight = valueWeight
        discreteOut.isUsed = isUsed
    }
}