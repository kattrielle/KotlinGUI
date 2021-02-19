package kotlinGUI.styles

import javafx.scene.paint.Color
import tornadofx.*

class VisibleBorder : Stylesheet() {
    companion object {
        val rightBorderVisible by cssclass()

        val changedCellHighlight by cssclass()
    }

    init {
        rightBorderVisible {
            borderColor += box(Color.TRANSPARENT, Color.LIGHTGREY, Color.TRANSPARENT, Color.TRANSPARENT)
        }

        changedCellHighlight {
            tableRowCell {
                selected {
                    backgroundColor += Color.RED
                }
            }
        }
    }
}