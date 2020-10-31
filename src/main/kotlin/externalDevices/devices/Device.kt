package externalDevices.devices

import externalDevices.settings.PortSettings
import externalDevices.ports.IPort

open class Device {
    protected open val maxIterations: Int = 1

    //protected open lateinit var port: IPort
    open lateinit var port: IPort
    //protected
    open lateinit var settings: PortSettings

    open fun OpenConnection() : Boolean
    {
        return port.OpenPort()
    }

    open fun CloseConnection()
    {
        port.ClosePort()
    }

}