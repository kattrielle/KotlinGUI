package externalDevices.devices

import kotlinGUI.FormValues
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.ceil

open class Modbus : Device() {
    val READMANY_COIL : Byte = 0x01
    val READMANY_DISCRETEINPUTS : Byte = 0x02
    val READMANY_HOLDING : Byte = 0x03
    val READMANY_INPUT : Byte = 0x04
    val WRITEONE_FLAG : Byte = 0x05
    val WRITEONE_HOLDING : Byte = 0x06
    val WRITEMANY_HOLDING : Byte = 0x10

    val WRITEVALUE_TRUE : Int = 0xFF00
    val WRITEVALUE_FALSE : Int = 0x0000

    protected open val answerStart : Int = 0 //начало значимой части ответа
    protected open val skipAtEnd : Int = 0 //сколько ячеек ответа в конце незначимы

    protected open fun CreateMessage( // Создание пакета для отправки на устройство
            funct: Byte, //Отправляемая на устройство Modbus-функция
            register: Int, //Стартовый регистр
            value: Int //Параметр (записываемое значение или число считываемых байт)
    ) : Array<Byte> //Сгенерированный пакет для отправки на внешнее устройство, без множественой записи
    {
        return emptyArray()
    }

    protected open fun CreateMessageMultipleWrite( // Создание пакета множественной записи (0х10 функция)
            register: Int, //Стартовый регистр
            values: Array<Byte> //Набор значений для записи
    ) : Array<Byte> //Сгенерированный пакет для отправки на внешнее устройство
    {
        return emptyArray()
    }

    private fun ConvertToFlagValues(
            numOfFlags: Int,
            answer: Array<Int>
    ): Array<Boolean>
    {
        var result: Array<Boolean?> = arrayOfNulls( numOfFlags )
        for (i in 0 until numOfFlags) {
            //result[i] = Convert.ToBoolean(answer[i / 8] % Math.Pow(2, i + 1) as Int)
            result[i] = ( answer[ i / 8 ] ) as Boolean
        }
        return result.filterNotNull().toTypedArray()
    }

    private fun ConvertToBytes( //массив покета байт
            values:Array<Int> //Набор величин, которые требуется отправить в регистры
    ):Array<Byte>
    {
        val result: Array<Byte> = Array( values.size * 2) {0}
        val byteBuffer = ByteBuffer.allocate(2)
        for ( i in values.indices) {
            byteBuffer.putShort(values[i].toShort())
            result[2 * i + 1] = byteBuffer[0]
            result[2 * i] = byteBuffer[1]
            byteBuffer.clear()
        }
        return result
    }

    protected fun ConvertValueToBytes( value:Int ) : Array<Byte> {
        val result: Array<Byte> = Array(2) { 0 }
        val byteBuffer = ByteBuffer.allocate(2)
        byteBuffer.putShort(value.toShort())
        result[1] = byteBuffer[0]
        result[0] = byteBuffer[1]
        return result
    }

    private fun CountAnswerLength( //Подсчёт длины получаемого ответа, чистых значений
            funct: Int, //Отправленная на устройство функция
            length: Int //Число запрошенных регистров
    ) : Int //Число значимых байт ответа
    {
        return when ( funct ) {
            READMANY_DISCRETEINPUTS.toInt(), READMANY_COIL.toInt() ->
                ceil( length.toDouble() / 8.0 ).toInt()
            READMANY_HOLDING.toInt(), READMANY_INPUT.toInt() ->
                2 * length
            else ->
                0
        }
    }

    private fun ConvertBytes( //Перевод ответа из массива байт
            response: Array<Byte>,
            funct: Byte,
            answerLength: Int
    ) : Array<Int> {
        val responseBytes = response.size
        var result: Array<Int>
        when (funct) {
            READMANY_HOLDING, READMANY_INPUT -> {
                result = Array(answerLength / 2) { 0 }
                val tmp = ByteArray(2) { 0 }
                for (i in 0 until (responseBytes - answerStart) / 2 - skipAtEnd) {
                    tmp[0] = response[1 + i * 2 + answerStart]
                    tmp[1] = response[i * 2 + answerStart]
                    result[i] = ByteBuffer.wrap(tmp).order(ByteOrder.LITTLE_ENDIAN).short.toInt()
                }
            }
            READMANY_COIL, READMANY_DISCRETEINPUTS -> {
                result = Array(answerLength) { 0 }
                for (i in 0..(responseBytes - answerLength) / 2) {
                    result[i] = response[i * 2 + answerStart].toInt()
                }
            }
            else ->
                result = Array(1) { 0 }
        }
        return result
    }

    fun GetOneCoilValue( //Запрос 1 coil-регистра ( 0х01 )
            register: Int, //Запрашиваемый регистр
    ) : Pair<Boolean,Boolean> //Значение логического регистра
    {
        val (succeed, answer) = GetCoilValue( register, 1 )
        return if (succeed) {
            println( FormValues.getCurrentTime() + "successfully got ${answer[0]}")
            Pair(succeed, answer[0])
        } else {
            println(FormValues.getCurrentTime() + "reading unsuccessful")
            Pair(succeed,false)
        }
    }

    fun GetCoilValue( //Запрос coil-регистров ( 0х01 )
            register: Int, //Стартовый регистр
            length: Int, //Требуемое число регистров
    ) : Pair<Boolean,Array<Boolean>> //Набор значений логических регистров
    {
        val (succeed,answer) = GetValue(READMANY_COIL, register, length)
        println( FormValues.getCurrentTime() + "getting coils: $succeed" )
        return Pair(succeed,ConvertToFlagValues(length,answer))
    }

    fun GetOneDiscreteInputValue( //Запрос 1 дискретного входа ( 0х02 )
            register: Int, //Запрашиваемый регистр
    ) : Pair<Boolean, Boolean> //Значение логического регистра
    {
        val (succeed, answer) = GetDiscreteInputValue( register, 1 )
        return if (succeed) {
            println( FormValues.getCurrentTime() + "successfully got ${answer[0]}")
            Pair(succeed, answer[0])
        } else {
            println(FormValues.getCurrentTime() + "reading unsuccessful")
            Pair(succeed,false)
        }
    }

    fun GetDiscreteInputValue( //Запрос дискретных входов ( 0х02 )
            register: Int, //Стартовый регистр
            length: Int, //Требуемое число регистров
    ) :Pair<Boolean, Array<Boolean>> //Набор значений дискретных входов
    {
        val (succeed, answer) = GetValue(READMANY_DISCRETEINPUTS,register, length)
        println( FormValues.getCurrentTime() + "getting discrete inputs: $succeed" )
        return Pair(succeed,ConvertToFlagValues(length,answer))
    }

    fun GetOneHoldingValue( //Запрос 1 регистра хранения ( 0х03 )
            register: Int, //Запрашиваемый регистр
    ) : Pair<Boolean,Int> //Значение регистра
    {
        val (succeed, answer) = GetHoldingValue(register, 1)
        return if (answer.isNotEmpty()) {
            println( FormValues.getCurrentTime() + "getting holding: ${answer[0]}")
            Pair(succeed,answer[0])
        } else {
            println(FormValues.getCurrentTime() + "reading unsuccessful")
            Pair(succeed, 0)
        }
    }

    fun GetHoldingValue( //Запрос регистров хранения ( 0х03 )
            register: Int, //Стартовый регистр
            length: Int, //Требуемое число регистров
    ) : Pair<Boolean,Array<Int>> //Набор значений регистров
    {
        return GetValue(READMANY_HOLDING, register, length)
    }

    fun GetOneInputValue( //Запрос 1 регистра ввода ( 0х04 )
            register: Int, //Запрашиваемый регистр
    ) : Pair<Boolean,Int> //Значение регистра
    {
        val (succeed, answer) = GetInputValue(register, 1)
        return if (succeed) {
            println( FormValues.getCurrentTime() + "getting input: ${answer[0]}")
            Pair(succeed, answer[0])
        } else {
            println(FormValues.getCurrentTime() + "reading unsuccessful")
            Pair(succeed, 0)
        }
    }

    fun GetInputValue( //Запрос регистров ввода ( 0х04 )
            register: Int, //Стартовый регистр
            length: Int, //Требуемое число регистров
    ) : Pair<Boolean, Array<Int>> //Набор значений регистров
    {
        return GetValue(READMANY_INPUT, register, length)
    }

    fun GetFloat( //Запрос float-значения из регистра
             funct: Byte, //Отправленная на устройство функция
             register: Int, //Запрашиваемый регистр
    ): Pair<Boolean,Float> {
        val (succeed, answer) = GetClearAnswer(funct, register, 2 )
        return if (succeed) {
            var tmp = ByteArray(4) { 0 }
            for (i: Int in 0 until 2) {
                tmp[2 * i] = answer[answerStart + 2 * i + 1]
                tmp[2 * i + 1] = answer[answerStart + 2 * i]
            }
            val result = ByteBuffer.wrap(tmp).order(ByteOrder.LITTLE_ENDIAN).float
            println( FormValues.getCurrentTime() + "getting float value: $result")
            Pair(succeed, result)
        } else {
            println(FormValues.getCurrentTime() + "reading unsuccessful")
            Pair(succeed, 0F)
        }
    }

    fun GetSwFloat( //Запрос switched-float-значения из регистра
            funct: Byte, //Отправленная на устройство функция
            register: Int, //Запрашиваемый регистр
    ):Pair<Boolean,Float> {
        val (succeed, answer) = GetClearAnswer(funct, register, 2 )
        return if (succeed) {
            val tmp = ByteArray(4) { 0 }
            for (i: Int in 0 until 2) {
                tmp[2 * i] = answer[answerStart + 2 * i]
                tmp[2 * i + 1] = answer[answerStart + 2 * i + 1]
            }
            val result = ByteBuffer.wrap(tmp).float
            println( FormValues.getCurrentTime() + "getting float value: $result")
            Pair(succeed, result )
        } else {
            println(FormValues.getCurrentTime() + "reading unsuccessful")
            Pair(succeed, 0F)
        }
    }

    fun GetHoldingFloat( //Запрос float-значения из регистра хранения
            register: Int, //Запрашиваемый регистр
    ):Pair<Boolean,Float>
    {
        return GetFloat( READMANY_HOLDING, register )
    }

    fun GetInputFloat( //Запрос float-значения из регистра ввода
            register: Int, //Запрашиваемый регистр
    ):Pair<Boolean,Float>
    {
        return GetFloat( READMANY_INPUT, register )
    }

    fun GetHoldingSwFloat( //Запрос switched-float-значения из регистра хранения
            register: Int, //Запрашиваемый регистр
    ):Pair<Boolean,Float>
    {
        return GetSwFloat( READMANY_HOLDING, register )
    }

    fun GetInputSwFloat( //Запрос switched-float-значения из регистра ввода
            register: Int, //Запрашиваемый регистр
    ):Pair<Boolean,Float>
    {
        return GetSwFloat( READMANY_INPUT, register )
    }

    fun GetClearAnswer( //Получение чистого ответа устройства (без убирания доп. части пакета)
            funct: Byte, //Отправленная на устройство функция
            register: Int, //Стартовый регистр
            length: Int, //Число регистров
    ) : Pair< Boolean, Array<Byte>> //Успешность операции & Ответ устройства
    {
        val message = CreateMessage(funct, register, length )
        val answerLength = CountAnswerLength( funct.toInt(), length )
        return port.SendQuery(message, answerStart + 2 * skipAtEnd + answerLength,
                maxIterations)
    }

    fun GetValue( // Запрос значений с устройства
            funct: Byte, //Отправленная на устройство функция
            register: Int, //Стартовый регистр
            length: Int, //Число регистров
    ) : Pair<Boolean, Array<Int>> //Успешность операции & Набор значений регистров
    {
        val answerLength = CountAnswerLength( funct.toInt(), length)
        val (succeed, answer) = GetClearAnswer( funct, register, length )
        println( FormValues.getCurrentTime() +
                "getting value from register $register with function $funct: $succeed")
        return Pair(succeed, ConvertBytes( answer, funct, answerLength ))
    }

    fun SetFlag( //Установка (запись значения) coil-регистра
            register: Int, //Изменяемый регистр
            value: Boolean, //Отправляемый флаг
    ):Boolean //Ответ устройства на операцию записи
    {
        val flag = if (value) WRITEVALUE_TRUE else WRITEVALUE_FALSE
        return SetValue(WRITEONE_FLAG, register, flag)
    }

    fun SetHoldingValue( //Установка (запись значения) регистра хранения
            register: Int, //Изменяемый регистр
            value: Int, //Отправляемое значение
    ):Boolean //Ответ устройства на операцию записи
    {
        println( FormValues.getCurrentTime() + "writing $value to holding $register" )
        return SetValue( WRITEONE_HOLDING, register, value )
    }

    open fun SetValue( //Отправка пакета записи
            funct: Byte, //Отправляемая на устройство функция
            register: Int, //Изменяемый регистр
            value: Int, //Отправляемое значение
    ):Boolean //Ответ устройства на операцию записи
    {
        return false
    }

    protected open fun SetMultipleHoldingValues( //Отправка пакета множественной записи регистров хранения (0х10)
            register: Int, //Стартовый регистр
            values: Array<Byte>, //Отправляемое значение
    ):Boolean //Ответ устройства на операцию записи
    {
        return false
    }

    fun SetMultipleHoldingValues( //Отправка пакета множественной записи регистров хранения (0х10)
            register: Int, //Стартовый регистр
            values: Array<Int>, //Отправляемое значение
    ):Boolean //Ответ устройства на операцию записи
    {
        val registerPoints = ConvertToBytes(values)
        return SetMultipleHoldingValues(register, registerPoints)
    }

    fun SetHoldingFloat( //отправка на устройство float-величины
            register: Int, //адрес регистра
            value: Float, //записываемое значение
    ) : Boolean //Ответ устройства на операцию записи
    {
        val tmp: Array<Byte> = Array(4) { 0 }
        val byteBuffer = ByteBuffer.allocate(4)
        byteBuffer.putFloat( value )
        for ( i in 0..1 ) {
            tmp[2 * i] = byteBuffer[2 * (1 - i) + 1]
            tmp[2 * i + 1] = byteBuffer[2 * (1 - i)]
        }
        println( FormValues.getCurrentTime() + "writing $value to holding $register" )
        return SetMultipleHoldingValues(register, tmp)
    }

    fun SetHoldingSwFloat( //отправка на устройство float-величины
            register: Int, //адрес регистра
            value: Float, //записываемое значение
    ) : Boolean //Ответ устройства на операцию записи
    {
        val tmp: Array<Byte> = Array(4) { 0 }
        val byteBuffer = ByteBuffer.allocate(4)
        byteBuffer.putFloat( value )
        for ( i in 0..1 ) {
            tmp[2 * i] = byteBuffer[2 * i + 1]
            tmp[2 * i + 1] = byteBuffer[2 * i]
        }
        println( FormValues.getCurrentTime() + "writing $value to holding $register" )
        return SetMultipleHoldingValues(register, tmp)
    }
}