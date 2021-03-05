package kotlinGUI

import kotlinGUI.viewModel.SearchRegisterProperties
import tornadofx.*

class FormSearchRegisterParameters : View("")
{
    private val descriptions : SearchRegisterProperties by param()

    override val root = form {
        fieldset {
            field( "Общее текстовое описание регистров для всей уставки" ) {
                textfield( descriptions.baseDescription )
            }
            field( "Общее текстовое описание регистров адреса выборки в уставке " ) {
                textfield( descriptions.setpointSampleDescription )
            }
            field( "Общее текстовое описание регистров значения уставки" ) {
                textfield( descriptions.setpointDescription )
            }
            field( "Общее текстовое описание регистров времени установки уставки" ) {
                textfield( descriptions.timeSetDescription )
            }
            field( "Общее текстовое описание регистров времени снятия уставки" ) {
                textfield( descriptions.timeUnsetDescription )
            }
            field( "Общее текстовое описание регистров весового коэффициента" ) {
                textfield( descriptions.weightDescription )
            }
        }
    }
}