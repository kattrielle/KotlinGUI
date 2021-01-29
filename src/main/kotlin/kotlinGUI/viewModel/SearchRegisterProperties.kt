package kotlinGUI.viewModel

import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class SearchRegisterProperties {
    val baseDescription = SimpleStringProperty("Виброключ")

    val setpointSampleDescription = SimpleStringProperty("адрес")

    val setpointDescription = SimpleStringProperty("значение")

    val timeSetDescription = SimpleStringProperty("время установки")

    val timeUnsetDescription = SimpleStringProperty("время снятия")

    val weightDescription = SimpleStringProperty("вес")
}