package kotlinGUI

import javafx.beans.property.SimpleStringProperty
import registerCollection.DiscreteOut
import tornadofx.*

class FormSelectRegisters : View( "Задание параметров цифровых выходов" )
{
    private val selectDefence = SimpleStringProperty()

    init {
        FormValues.setpoints.items.clear()

        selectDefence.onChange {
            FormValues.setpoints.registerWriteDefence =
                    FormValues.findRegister( selectDefence.value )
        }
    }

    override val root = gridpane {
        /*val registers = FormValues.tikModscanMap?.сellsArray.asList().asObservable()
        tableview ( registers ) {
            readonlyColumn("Регистр", CellData::address)
            readonlyColumn("Название", CellData::name)
        }*/
        vbox {
            button("Закрыть") {
                action {
                    find(MainForm::class).reloadForm()
                    close()
                }
            }
            vbox {
                text("Регистр защиты от записи")
                combobox( selectDefence, FormValues.getRegisterMapDescriptions())
            }
        }

        vbox {

            button("Добавить уставку") {
                action {
                    FormValues.setpoints.items.add( DiscreteOut())
                    this@vbox.add(DiscreteOutFragment(FormValues.setpoints.items.last()))
                }
            }
            gridpaneConstraints {
                columnRowIndex(1,0)
            }
        }

    }
}