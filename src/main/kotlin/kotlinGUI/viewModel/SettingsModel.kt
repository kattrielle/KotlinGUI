package kotlinGUI.viewModel

import kotlinGUI.DataComConnection
import tornadofx.ItemViewModel
import javax.xml.crypto.Data

class SettingsModel(properties: DataComConnection) :
    ItemViewModel<DataComConnection>(properties)
{
    val deviceAddress = bind( DataComConnection::deviceAddress )
    val selectDelayAnswerRead = bind( DataComConnection::selectDelayAnswerRead )
    val selectDelayAnswerWrite = bind( DataComConnection::selectDelayAnswerWrite )
}