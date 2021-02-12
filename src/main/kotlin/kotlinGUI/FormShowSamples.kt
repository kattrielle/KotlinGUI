package kotlinGUI

import javafx.scene.chart.NumberAxis
import tornadofx.*

class FormShowSamples : View("Отображение выборок") {
    override val root = vbox {
        linechart("", NumberAxis(), NumberAxis()) {
            for ( discreteOut in FormValues.setpoints.items ) {
                series( discreteOut.descriptionSetpointSample ) {
                    for ( i in discreteOut.sample.indices ) {
                        data( i, discreteOut.sample[i] )
                    }
                }
            }
        }
    }
}