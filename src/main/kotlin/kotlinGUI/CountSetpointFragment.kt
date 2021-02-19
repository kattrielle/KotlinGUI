package kotlinGUI

import countSetpoints.CountSetpointsDescriptions
import javafx.beans.InvalidationListener
import javafx.beans.property.SimpleDoubleProperty
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.scene.control.Alert
import javafx.scene.input.KeyCode
import javafx.scene.layout.Priority
import tornadofx.*
import javafx.scene.input.KeyEvent
import javafx.scene.paint.Color
import kotlinGUI.styles.VisibleBorder
import kotlinGUI.viewModel.CountSetpointModel
import kotlinGUI.viewModel.CountSetpointProperties
import kotlinGUI.viewModel.DiscreteOutProperties
import tornadofx.Stylesheet.Companion.tableRowCell
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
        maxWidth = Double.MAX_VALUE
        hgrow = Priority.ALWAYS
    }

    private val buttonSaveSetpoints = button("Записать уставки") {
        action {
            writeSetpoints()
            find(MainForm::class).reloadForm()
        }
    }

    private val textfieldCountParameter = textfield(countModel.percent) {
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

    /*private val table2 = tableview( FormValues.discreteOutModel ) {
        column("Значение уставки", DiscreteOutModel::valueSetpoint ).makeEditable()
    }*/

    private val table = tableview( FormValues.discreteOutTableViewProperties ) {
        //isEditable = true

        column("", DiscreteOutProperties::isUsedProperty ).useCheckbox()
        readonlyColumn("Выборка", DiscreteOutProperties::descriptionValues)
        readonlyColumn("Регистр выборки", DiscreteOutProperties::registerValues)
        // @todo нужна ли всё-таки колонка хранящегося в датчике адреса выборка на уставке? Или отображать только её?
        column("Значение уставки",DiscreteOutProperties::valueSetpointProperty ).makeEditable()
        column("Время установки", DiscreteOutProperties::valueTimeSetProperty).makeEditable()
        column("Время снятия", DiscreteOutProperties::valueTimeUnsetProperty).makeEditable()
        column( "Вес", DiscreteOutProperties::valueWeightProperty ).makeEditable()

        maxHeight( Double.MAX_VALUE )
        maxWidth( Double.MAX_VALUE )
        vgrow = Priority.ALWAYS
        hgrow = Priority.ALWAYS

        var isEditing = false
        enableCellEditing()
        regainFocusAfterEdit()
        setOnKeyPressed {
            if (it.code == KeyCode.ENTER && !isEditing) {
                isEditing = true
            }
            println("key pressed: " + it.character)
        }
        setOnKeyTyped {
            if (selectedCell != null && it.character.isNotEmpty() && !isEditing) {
                edit(selectedCell!!.row, selectedCell!!.tableColumn)
                isEditing = true
                println( "i`m here and char is: " + it.character )
            }
        }

        onEditCommit {
            isEditing = false
            //addClass( VisibleBorder.changedCellHighlight )
            /*style {
                backgroundColor += Color.RED }*/
        }
        // In case user selected another cell before commit
        selectionModel.selectedCells.addListener(InvalidationListener {
            isEditing = false
        } )

    }

/*
    fun <T> EventTarget.exceltableview(items: ObservableList<T>? = null, op: TableView<T>.() -> Unit = {}) =
        TableView<T>().attachTo(this, op) {
            println("exceltableview")
            if (items != null) {
                if (items is SortedFilteredList<T>) items.bindTo(it)
                else it.items = items
            }
            it.enableExcelBehaviour()
        }

    // TableView.enableExcelBehaviour
    fun <T> TableView<T>.enableExcelBehaviour() {
        enableCellEditing()
        regainFocusAfterEdit()
        addEventHandler(KeyEvent.KEY_PRESSED) { keyEvent ->
            println("first level")
            // For editing cell without pressing enter first
            if (keyEvent.code.isValidInput()) {
                println("hello")
                if (editingCell == null) {
                    val currentSelectedCell = selectedCell
                    if (currentSelectedCell != null && currentSelectedCell.tableColumn.isEditable) {
                        println("trying to edit column")
                        edit(currentSelectedCell.row, currentSelectedCell.tableColumn)
                    }
                }
            }
        }
    }
*/
    private fun KeyCode.isValidInput(): Boolean {
        return isDigitKey
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
                        textProperty().addListener { _, _, _ ->
                            countModel.commit( countModel.sampleLen ) }
                    }
                }
                field("Предварительное время чтения, мин") {
                    textfield( countSetpointProperties.sampleTimeProperty )
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
                    {
                        addEventFilter( KeyEvent.KEY_PRESSED ) { event ->
                            if (event.code == KeyCode.ENTER) {
                                textfieldCountParameter.requestFocus()
                            }
                        }
                    }
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
                    add( textfieldCountParameter )
                }
                button("Расчёт") {
                    action {
                        countSetpoints()
                    }
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
        hbox {
            //add( table2 )
            add(table)
            //table.exceltableview( FormValues.setpoints.items )
            isFillHeight = true
            maxWidth = Double.MAX_VALUE
            maxHeight = Double.MAX_VALUE
            hgrow = Priority.ALWAYS
            vgrow = Priority.ALWAYS

            gridpaneConstraints {
                columnRowIndex(0,1)
                columnSpan = 5
            }
        }

        setMinSize(600.0,500.0)
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
            } else {
                //alert( Alert.AlertType.INFORMATION, "Success")
                println(FormValues.getCurrentTime() + "getting parameters is done")
            }
            getPropertiesProgress.set( 0.0 )
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
            } else {
                println(FormValues.getCurrentTime() + "getting samples is done")
            }
            getSampleProgress.set (0.0)
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
            FormValues.updateDiscreteOutValues()
            if ( !FormValues.setpoints.writeParameterValues(
                    FormValues.device, writePropertiesProgress) )
            {
                println(FormValues.getCurrentTime() + "writing parameters failed")
            } else {
                println(FormValues.getCurrentTime() + "writing parameters is done")
            }
            writePropertiesProgress.set(0.0)
            FormValues.device.CloseConnection()
            setButtonsStatus( false )
        }
    }

    private fun countSetpoints()
    {
        if ( countModel.selectCount.value != null ) {
            FormValues.setpoints.countSetpointValues(
                    countModel.selectCount.value, countModel.percent.value)
            if ( FormValues.setpoints.items.any { it.isUsed && it.valueSetpoint < 0.5 } )
            {
                alert(Alert.AlertType.INFORMATION, "", "Проверьте величины уставок\n"
                        + "Часть уставок имеет маленькие значения" )
            }
        }

    }

    private fun setButtonsStatus( isDisabled : Boolean )
    {
        buttonLoadSamples.isDisable = isDisabled
        buttonLoadSetpoints.isDisable = isDisabled
        buttonSaveSetpoints.isDisable = isDisabled
    }

}