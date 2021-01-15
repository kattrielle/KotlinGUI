package kotlinGUI

import externalDevices.devices.ModbusRTU
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import jssc.SerialPortList
import kotlinGUI.viewModel.SettingsModel
import tornadofx.*

class FormSettings: View("Параметры") {
    private val dataComConnection : DataComConnection by inject()
    private val settingsModel = SettingsModel( dataComConnection )

    override val root = form {
        fieldset("Устройство") {
            field("Порт:") {
                combobox( dataComConnection.selectPort, SerialPortList.getPortNames().toList() )
            }
            field("Адрес устройства:") {
                textfield( settingsModel.deviceAddress ) {
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

                        }
                    }
                }
            }

        }
        fieldset("Параметры соединения") {
            field("Скорость:") {
                combobox( dataComConnection.selectBaudrate, dataComConnection.baudrate )
            }
            field( "Чётность:" ) {
                combobox( dataComConnection.selectParity, dataComConnection.parity )
            }
            field("Биты данных:") {
                combobox( dataComConnection.selectDataBits, dataComConnection.dataBits )
            }
            field("Стоп-бит:") {
                combobox( dataComConnection.selectStopBits, dataComConnection.stopBits )
            }
            field("Задержка чтения") {
                textfield( settingsModel.selectDelayAnswerRead ) {
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

                        }
                    }
                }

            }
            field("Задержка записи"){
                textfield( settingsModel.selectDelayAnswerWrite ) {
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

                        }
                    }
                }
            }
        }
        hbox {
            button("OK")
            {
                action {
                    settingsModel.commit()
                    FormValues.settings = dataComConnection.setModbusParameters()
                    FormValues.device = ModbusRTU( FormValues.settings )
                    close()
                }
                defaultButtonProperty().bind( focusedProperty() )
            }
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