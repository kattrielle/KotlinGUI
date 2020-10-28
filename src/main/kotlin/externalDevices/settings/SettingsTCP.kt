package externalDevices.settings

class SettingsTCP( var IP: String, var port: Int ) : PortSettings() {
    override val description: String
        get() = "$IP: $port"

    override fun CheckName(): Boolean = IP.isNotEmpty()
}