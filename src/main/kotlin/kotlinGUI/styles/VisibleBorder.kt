package kotlinGUI.styles

import javafx.scene.paint.Color
import tornadofx.*

class VisibleBorder : Stylesheet() {
    companion object {
        val rightBorderVisible by cssclass()
    }

    init {
        rightBorderVisible {
            borderColor += box(Color.TRANSPARENT, Color.LIGHTGREY, Color.TRANSPARENT, Color.TRANSPARENT)
        }
    }
}