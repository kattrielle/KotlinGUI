package externalDevices.settings

class SettingsModbusRTU( name: String ) : SettingsCOM( name ) {
    var address:Byte = 1
    var delayAnswerRead: Int = 100
    var delayAnswerWrite: Int = 100
}