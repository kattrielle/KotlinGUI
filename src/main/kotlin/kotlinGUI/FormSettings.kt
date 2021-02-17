package kotlinGUI

import externalDevices.devices.ModbusRTU
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import jssc.SerialPortList
import kotlinGUI.viewModel.DataComConnection
import kotlinGUI.viewModel.SettingsModel
import tornadofx.*

class FormSettings: View("Параметры") {
    private val dataComConnection : DataComConnection by inject()
    private val settingsModel = SettingsModel( dataComConnection )

    private val comboboxDevice = combobox( dataComConnection.selectPort,
        SerialPortList.getPortNames().toList() ) {
        addEventFilter( KeyEvent.KEY_PRESSED ) { event ->
            if (event.code == KeyCode.ENTER) {
                textfieldAddress.requestFocus()
            }
        }
    }
    private val textfieldAddress = textfield( settingsModel.deviceAddress ) {
        validator {
            if ( ! it!!.isInt()) {
                error("Введено не число")
            } else if ( it.toInt() <= 0 ) {
                error("Адрес не может быть нулевым или отрицательным")
            } else if ( it.toInt() > 247 )
            {
                error("Максимально допустимый адрес устройства - 247")
            } else null
        }
        addEventFilter( KeyEvent.KEY_PRESSED ) { event ->
            if (event.code == KeyCode.ENTER) {
                comboboxBaudrate.requestFocus()
            }
        }
    }
    private val comboboxBaudrate = combobox( dataComConnection.selectBaudrate,
        dataComConnection.baudrate )
    {
        addEventFilter( KeyEvent.KEY_PRESSED ) { event ->
            if (event.code == KeyCode.ENTER) {
                comboboxParity.requestFocus()
            }
        }
    }
    private val comboboxParity = combobox( dataComConnection.selectParity,
        dataComConnection.parity )
    {
        addEventFilter( KeyEvent.KEY_PRESSED ) { event ->
            if (event.code == KeyCode.ENTER) {
                comboboxDataBits.requestFocus()
            }
        }
    }
    private val comboboxDataBits = combobox( dataComConnection.selectDataBits,
        dataComConnection.dataBits )
    {
        addEventFilter( KeyEvent.KEY_PRESSED ) { event ->
            if (event.code == KeyCode.ENTER) {
                comboboxStopBits.requestFocus()
            }
        }
    }
    private val comboboxStopBits = combobox( dataComConnection.selectStopBits,
        dataComConnection.stopBits )
    {
        addEventFilter( KeyEvent.KEY_PRESSED ) { event ->
            if (event.code == KeyCode.ENTER) {
                textfieldDelayRead.requestFocus()
            }
        }
    }
    private val textfieldDelayRead = textfield( settingsModel.selectDelayAnswerRead ) {
        validator {
            if ( ! it!!.isInt()) {
                error("Введено не число")
            } else if ( it.toInt() < 0 ) {
                error("Величина не может быть отрицательной")
            } else if ( it.toInt() > 10000 )
            {
                error("Слишком большое число")
            } else null
        }
        addEventFilter( KeyEvent.KEY_PRESSED ) { event ->
            if (event.code == KeyCode.ENTER) {
                textfieldDelayWrite.requestFocus()
            }
        }
    }
    private val textfieldDelayWrite = textfield( settingsModel.selectDelayAnswerWrite ) {
        validator {
            if ( ! it!!.isInt()) {
                error("Введено не число")
            } else if ( it.toInt() < 0 ) {
                error("Величина не может быть отрицательной")
            } else if ( it.toInt() > 10000 )
            {
                error("Слишком большое число")
            } else null
        }
        addEventFilter( KeyEvent.KEY_PRESSED ) { event ->
            if (event.code == KeyCode.ENTER) {
                buttonOk.requestFocus()
            }
        }
    }
    private val buttonOk = button("OK")
    {
        action {
            settingsModel.commit()
            FormValues.settings = dataComConnection.setModbusParameters()
            FormValues.device = ModbusRTU( FormValues.settings )
            close()
        }
        defaultButtonProperty().bind( focusedProperty() )
    }

    override val root = form {
        fieldset("Устройство") {
            field("Порт:") {
                add( comboboxDevice )
            }
            field("Адрес устройства:") {
                add( textfieldAddress )
            }
        }
        fieldset("Параметры соединения") {
            field("Скорость:") {
                add( comboboxBaudrate )
            }
            field( "Чётность:" ) {
                add( comboboxParity )
            }
            field("Биты данных:") {
                add( comboboxDataBits )
            }
            field("Стоп-бит:") {
                add( comboboxStopBits )
            }
            field("Задержка чтения") {
                add( textfieldDelayRead )
            }
            field("Задержка записи"){
                add( textfieldDelayWrite )
            }
        }
        hbox {
            add( buttonOk )
            button("Закрыть")
            {
                action {
                    close()
                }
                shortcut("Esc")
            }
        }
    }

    override fun onDock() {
        dataComConnection.updateModbusParametersFromDevice( FormValues.settings )
    }
}