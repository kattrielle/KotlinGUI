package kotlinGUI

import registerCollection.DiscreteOut
import registerMapTikModscan.CellData
import tornadofx.*

class FormSelectRegisters : View( "Задание параметров цифровых выходов" )
{
    override val root = gridpane {
        /*val registers = FormValues.tikModscanMap?.сellsArray.asList().asObservable()
        tableview ( registers ) {
            readonlyColumn("Регистр", CellData::address)
            readonlyColumn("Название", CellData::name)
        }*/

        vbox {
            button("Close") {
                action {
                    println( FormValues.setpoints.items.first().descriptionValues)
                    find(MainForm::class).reloadForm()
                    close()
                }
            }
            button("+") {
                action {
                    FormValues.setpoints.items.add( DiscreteOut())
                    this@vbox.add(DiscreteOutFragment(FormValues.setpoints.items.last()))
                }
            }
        }

    }
}