//package ExternalDevices
package kotlinGUI

open class Device {
    protected open val maxIterations: Int = 1

    protected open lateinit var port: IPort


}