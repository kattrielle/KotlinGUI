package kotlinGUI.viewModel

import tornadofx.ItemViewModel

class CountSetpointModel( properties : CountSetpointProperties )
    : ItemViewModel<CountSetpointProperties> (properties)
{
    val sampleLen = bind( CountSetpointProperties::sampleLenProperty )
    val selectCount = bind( CountSetpointProperties::selectCountProperty )
    val countDescription = bind( CountSetpointProperties::countDescriptionProperty )
    val percent = bind( CountSetpointProperties::percentProperty )
}