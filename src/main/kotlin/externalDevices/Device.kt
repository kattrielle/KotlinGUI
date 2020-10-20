//package ExternalDevices
package externalDevices

import externalDevices.IPort

open class Device {
    protected open val maxIterations: Int = 1

    //protected open lateinit var port: IPort
    open lateinit var port: IPort
    //protected
    open lateinit var settings: PortSettings

}