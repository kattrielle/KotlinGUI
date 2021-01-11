package countSetpoints

class CountSetpointsDescriptions {

    companion object {
        private val averageName = "Среднее"
        private val maxName = "Максимум"
        private val averageMSTName = "СКО от среднего"
        private val maxMSTName = "СКО от максимума"

        fun getCountTypesList() : List<String>
        {
            return listOf(averageName,
                    maxName,
                    averageMSTName,
                    maxMSTName)
        }

        fun getCountDescription( name : String? ) : String
        {
            return when ( name )
            {
                averageName -> "Расчёт: среднее значение + n%"
                maxName -> "Расчёт: максимальное значение + n%"
                averageMSTName -> "Расчёт: среднее значение + n * СКО"
                maxMSTName -> "Расчёт: максимальное значение + n * СКО"
                else -> ""
            }
        }

        fun selectCountType( name : String ) : CountSetpointVariant
        {
            return when ( name )
            {
                averageName -> CountSetpointVariant.Average
                maxName -> CountSetpointVariant.Maximum
                averageMSTName -> CountSetpointVariant.AverageMSD
                maxMSTName -> CountSetpointVariant.MaximumMSD
                else -> CountSetpointVariant.Maximum
            }
        }
    }
}