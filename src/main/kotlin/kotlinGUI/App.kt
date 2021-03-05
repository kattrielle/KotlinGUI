/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package kotlinGUI

import javafx.stage.Stage
import tornadofx.*
import kotlinGUI.styles.VisibleBorder
import saveableProperties.PropertiesSaver

fun main(args: Array<String>) {
    PropertiesSaver.LoadProperties()
    launch<VisualApplication>(args)
}

class VisualApplication: App(MainForm::class, VisibleBorder::class)
{
    override fun start(stage: Stage) {
        stage.minWidth = 750.0
        stage.minHeight = 500.0
        stage.maxWidth = 835.0
        stage.maxHeight = 655.0

        super.start(stage)
    }
}

