package externalDevices

class SettingsModbusRTU : SettingsCOM() {
    var address:Byte = 1
    var delayAnswerRead: Int = 100
    var delayAnswerWrite: Int = 100
}