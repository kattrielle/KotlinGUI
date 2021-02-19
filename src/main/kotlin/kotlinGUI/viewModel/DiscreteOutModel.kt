package kotlinGUI.viewModel

import tornadofx.ItemViewModel

class DiscreteOutModel( properties : DiscreteOutProperties ) : ItemViewModel<DiscreteOutProperties>(properties)
{
    val valueSetpoint = bind( DiscreteOutProperties::valueSetpointProperty )
}