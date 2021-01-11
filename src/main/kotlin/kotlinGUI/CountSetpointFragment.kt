package kotlinGUI

import countSetpoints.CountSetpointsDescriptions
import javafx.beans.property.SimpleDoubleProperty
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.scene.input.KeyCode
import javafx.scene.layout.Priority
import registerCollection.DiscreteOut
import tornadofx.*
import javafx.scene.input.KeyEvent
import kotlinGUI.styles.VisibleBorder
import kotlinGUI.viewModel.CountSetpointModel
import kotlinGUI.viewModel.CountSetpointProperties
import kotlin.concurrent.thread

class CountSetpointFragment : Fragment() {
    private val getPropertiesProgress = SimpleDoubleProperty()
    private val getSampleProgress = SimpleDoubleProperty()
    private val writePropertiesProgress = SimpleDoubleProperty()

    private val countSetpointProperties = CountSetpointProperties()
    private val countModel = CountSetpointModel( countSetpointProperties )

    private val buttonLoadSetpoints = button("Загрузить уставки") {
        action {
            readSetpoints()
        }
        defaultButtonProperty().bind( focusedProperty() )
    }

    private val buttonLoadSamples = button("Считать") {
        action {
            readSamples()
        }
        enableWhen( countModel.valid )
        maxWidth = Double.MAX_VALUE
        hgrow = Priority.ALWAYS
    }

    private val buttonSaveSetpoints = button("Записать уставки") {
        action {
            writeSetpoints()
        }
    }

    private val table = tableview( FormValues.setpoints.items ) {
        isEditable = true
        column("", DiscreteOut::isUsedProperty ).useCheckbox()
        readonlyColumn("Выборка", DiscreteOut::descriptionValues)
        readonlyColumn("Регистр выборки", DiscreteOut::registerValues)
        // @todo нужна ли всё-таки колонка хранящегося в датчике адреса выборка на уставке? Или отображать только её?
        column("Значение уставки",DiscreteOut::valueSetpointProperty ).makeEditable()
        column("Время установки", DiscreteOut::valueTimeSetProperty).makeEditable()
        column("Время снятия", DiscreteOut::valueTimeUnsetProperty).makeEditable()
        column( "Вес", DiscreteOut::valueWeightProperty ).makeEditable()

        maxHeight( Double.MAX_VALUE )
        maxWidth( Double.MAX_VALUE )
        vgrow = Priority.ALWAYS
        hgrow = Priority.ALWAYS

        gridpaneConstraints {
            columnRowIndex(0,1)
            columnSpan = 5
        }
    }

    override val root = gridpane {

        vbox {
            vbox {
                add( buttonLoadSetpoints )
                progressbar( getPropertiesProgress ) {
                    maxWidth = Double.MAX_VALUE
                    hgrow = Priority.ALWAYS
                }
            }
            addClass( VisibleBorder.rightBorderVisible )
            gridpaneConstraints {
                columnRowIndex(0, 0)
                padding = Insets(15.0)
            }
        }
        form {
            fieldset (labelPosition = Orientation.VERTICAL) {
                field("Размер выборки, запросов") {
                    textfield(countModel.sampleLen) {
                        validator {
                            if ( ! it!!.isInt()) {
                                error("Введено не число")
                            } else if ( it.toInt() <= 0 ) {
                                error("Выборка не может быть нулевой или отрицательной")
                            } else if ( it.toInt() > 2000 )
                            {
                                error("Слишком большая выборка")
                            } else null
                        }
                        addEventFilter( KeyEvent.KEY_PRESSED ) { event ->
                            if (event.code == KeyCode.ENTER) {
                                readSamples()
                            }
                        }
                    }
                }
                field("Примерное время чтения, с") {
                    textfield()
                    {
                        isEditable = false
                    }
                }
                vbox {
                    add( buttonLoadSamples )
                    progressbar(getSampleProgress) {
                        maxWidth = Double.MAX_VALUE
                        hgrow = Priority.ALWAYS
                    }
                }
                padding = Insets(5.0)
            }
            addClass( VisibleBorder.rightBorderVisible )
            gridpaneConstraints {
                columnRowIndex(1,0)
            }
        }
        form {
            fieldset {
                field("Вариант расчёта") {
                    combobox(countSetpointProperties.selectCountProperty,
                        CountSetpointsDescriptions.getCountTypesList())
                }
                padding = Insets( 0.0, 5.0, 0.0, 5.0 )
            }
            fieldset( labelPosition = Orientation.VERTICAL ) {
                field("Описание") {
                    textfield(countModel.countDescription) {
                        isEditable = false
                    }
                }
                padding = Insets( 0.0, 5.0, 0.0, 5.0 )
            }
            fieldset {
                field("Параметр n") {
                    textfield(countModel.percent) {
                        validator {
                            if ( ! it!!.isDouble() ) {
                                error("Введено не число")
                            } else null
                        }
                        addEventFilter( KeyEvent.KEY_PRESSED ) { event ->
                            if (event.code == KeyCode.ENTER) {
                                countSetpoints()
                            }
                        }
                    }
                }
                button("Расчёт") {
                    action {
                        countSetpoints()
                    }
                    enableWhen( countModel.valid )
                }
                padding = Insets( 0.0, 5.0, 0.0, 5.0 )
            }
            addClass( VisibleBorder.rightBorderVisible )
            gridpaneConstraints {
                columnRowIndex(2,0)
            }
        }
        vbox {
            vbox {
                add( buttonSaveSetpoints )
                progressbar( writePropertiesProgress ) {
                    maxWidth = Double.MAX_VALUE
                    hgrow = Priority.ALWAYS
                }
            }
            gridpaneConstraints {
                columnRowIndex(3,0)
                margin = Insets(15.0)
            }
        }
        vbox {
            gridpaneConstraints {
                columnRowIndex(4,0)
            }
        }
        add(table)
    }

    private fun readSetpoints()
    {
        thread {
            setButtonsStatus( true )
            println(FormValues.getCurrentTime() + "getting parameters begin")
            getPropertiesProgress.set(0.0)
            if ( !FormValues.setpoints.getParameterValues(
                    FormValues.device, getPropertiesProgress) )
            {
                //alert(Alert.AlertType.ERROR, "Reading Failed")
                println( FormValues.getCurrentTime() + "an error has occured at reading setpoint values" )
                getPropertiesProgress.set( 0.0 )
            } else {
                //alert( Alert.AlertType.INFORMATION, "Success")
                println(FormValues.getCurrentTime() + "getting parameters is done")
            }
            FormValues.device.CloseConnection()
            setButtonsStatus( false )
        }
    }

    private fun readSamples()
    {
        thread {
            setButtonsStatus( true )
            println(FormValues.getCurrentTime() + "getting sample begin")
            getSampleProgress.set (0.0)
            if ( !FormValues.setpoints.getSamples(
                            countModel.sampleLen.value,
                            FormValues.device, getSampleProgress) )
            {
                println(FormValues.getCurrentTime() + "getting samples is failed")
                getSampleProgress.set (0.0)
            } else {
                println(FormValues.getCurrentTime() + "getting samples is done")
            }
            FormValues.device.CloseConnection()
            setButtonsStatus( false )
        }
    }

    private fun writeSetpoints()
    {
        thread {
            setButtonsStatus( true )
            println(FormValues.getCurrentTime() + "writing parameters begin")
            writePropertiesProgress.set(0.0)
            if ( !FormValues.setpoints.writeParameterValues(
                    FormValues.device, writePropertiesProgress) )
            {
                println(FormValues.getCurrentTime() + "writing parameters failed")
                writePropertiesProgress.set(0.0)
            } else {
                println(FormValues.getCurrentTime() + "writing parameters is done")
            }
            FormValues.device.CloseConnection()
            setButtonsStatus( false )
        }
    }

    private fun countSetpoints()
    {
        if ( countModel.selectCount.value != null ) {
            FormValues.setpoints.countSetpointValues(
                    countModel.selectCount.value, countModel.percent.value)
        }
    }

    private fun setButtonsStatus( isDisabled : Boolean )
    {
        //buttonLoadSamples.isDisable = isDisabled
        //buttonLoadSetpoints.isDisable = isDisabled
        //buttonSaveSetpoints.isDisable = isDisabled
    }

}