package kotlinGUI.viewModel

import tornadofx.ItemViewModel

class SettingsModel(properties: DataComConnection) :
    ItemViewModel<DataComConnection>(properties)
{
    val deviceAddress = bind( DataComConnection::deviceAddress )
    val selectDelayAnswerRead = bind( DataComConnection::selectDelayAnswerRead )
    val selectDelayAnswerWrite = bind( DataComConnection::selectDelayAnswerWrite )
}