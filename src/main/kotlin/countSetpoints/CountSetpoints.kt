package countSetpoints

import kotlin.math.pow
import kotlin.math.sqrt

class CountSetpoints {
    companion object {
        fun count(
                variant: CountSetpointVariant,
                values: List<Double>,
                n: Double
        ) : Double
        {
            println( variant )
            return when ( variant )
            {
                CountSetpointVariant.Average ->
                    countAverage( values, n )
                CountSetpointVariant.Maximum ->
                    countMaximum( values, n )
                CountSetpointVariant.AverageMSD ->
                    countAverageMSD( values, n )
                CountSetpointVariant.MaximumMSD ->
                    countMaximumMSD( values, n )
            }
        }

        private fun countAverage(values: List<Double>, n: Double) : Double
        {
            val average = average ( values )
            return average + average * n / 100.0
        }

        private fun countMaximum(values: List<Double>, n: Double) : Double
        {
            val max = maximum( values )
            return max + max * n / 100.0
        }

        private fun countAverageMSD(values: List<Double>, n: Double) : Double
        {
            return average (values ) + n * countMSD( values )
        }

        private fun countMaximumMSD(values: List<Double>, n: Double) : Double
        {
            return maximum( values ) + n * countMSD( values )
        }

        private fun average( values : List<Double> ) : Double
        {
            return values.sum() / values.size
        }

        private fun maximum( values : List<Double> ) : Double
        {
            return values.maxOrNull() ?: 0.0
        }

        private fun countMSD( values: List<Double> ) : Double
        {
            val average = average ( values )
            var sum = 0.0
            values.forEach { sum += (it - average).pow(2.0) }
            sum /= values.size
            return sqrt( sum )
        }
    }
}