package kotlinGUI

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

    protected open fun CreateMessageWrite( // Создание пакета множественной записи (0х10 функция)
            register: Int, //Стартовый регистр
            len: Int, //Количество отправляемых значений
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
        return emptyArray()
    }

    protected fun CountAnswerLength( //Подсчёт длины получаемого ответа, чистых значений
            type: Int, //Отправленная на устройство функция
            length: Int //Число запрошенных регистров
    ) : Int //Число значимых байт ответа
    {
        return when ( type ) {
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
                for (i in 0..((responseBytes - answerStart) / 2 - skipAtEnd)) {
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
            delayAnswer: Int //Время ожидания ответа
    ) : Pair<Boolean,Boolean> //Значение логического регистра
    {
        val (succeed, answer) = GetCoilValue( register, 1, delayAnswer )
        return if (succeed) {
            Pair(succeed, answer[0])
        } else {
            Pair(succeed,false)
        }
    }

    fun GetCoilValue( //Запрос coil-регистров ( 0х01 )
            register: Int, //Стартовый регистр
            length: Int, //Требуемое число регистров
            delayAnswer: Int //Время ожидания ответа
    ) : Pair<Boolean,Array<Boolean>> //Набор значений логических регистров
    {
        val (succeed,answer) = GetValue(READMANY_COIL, register, length, delayAnswer)
        return return Pair(succeed,ConvertToFlagValues(length,answer))
    }

    fun GetOneDiscreteInputValue( //Запрос 1 дискретного входа ( 0х02 )
            register: Int, //Запрашиваемый регистр
            delayAnswer: Int //Время ожидания ответа
    ) : Pair<Boolean, Boolean> //Значение логического регистра
    {
        val (succeed, answer) = GetDiscreteInputValue( register, 1, delayAnswer )
        return if (succeed) {
            Pair(succeed, answer[0])
        } else {
            Pair(succeed,false)
        }
    }

    fun GetDiscreteInputValue( //Запрос дискретных входов ( 0х02 )
            register: Int, //Стартовый регистр
            length: Int, //Требуемое число регистров
            delayAnswer: Int //Время ожидания ответа
    ) :Pair<Boolean, Array<Boolean>> //Набор значений дискретных входов
    {
        val (succeed, answer) = GetValue(READMANY_DISCRETEINPUTS,register, length, delayAnswer)
        return Pair(succeed,ConvertToFlagValues(length,answer))
    }

    fun GetOneHoldingValue( //Запрос 1 регистра хранения ( 0х03 )
            register: Int, //Запрашиваемый регистр
            delayAnswer: Int //Время ожидания ответа
    ) : Pair<Boolean,Int> //Значение регистра
    {
        val (succeed, answer) = GetHoldingValue(register, 1, delayAnswer)
        return if (succeed) {
            Pair(succeed,answer[0])
        } else {
            Pair(succeed, 0)
        }
    }

    fun GetHoldingValue( //Запрос регистров хранения ( 0х03 )
            register: Int, //Стартовый регистр
            length: Int, //Требуемое число регистров
            delayAnswer: Int //Время ожидания ответа
    ) : Pair<Boolean,Array<Int>> //Набор значений регистров
    {
        return GetValue(READMANY_HOLDING, register, length, delayAnswer)
    }

    fun GetOneInputValue( //Запрос 1 регистра ввода ( 0х04 )
            register: Int, //Запрашиваемый регистр
            delayAnswer: Int //Время ожидания ответа
    ) : Pair<Boolean,Int> //Значение регистра
    {
        val (succeed, answer) = GetInputValue(register, 1, delayAnswer)
        return if (succeed) {
            Pair(succeed, answer[0])
        } else {
            Pair(succeed, 0)
        }
    }

    fun GetInputValue( //Запрос регистров ввода ( 0х04 )
            register: Int, //Стартовый регистр
            length: Int, //Требуемое число регистров
            delayAnswer: Int //Время ожидания ответа
    ) : Pair<Boolean, Array<Int>> //Набор значений регистров
    {
        return GetValue(READMANY_INPUT, register, length, delayAnswer)
    }

    fun GetFloat( //Запрос float-значения из регистра
             funct: Byte, //Отправленная на устройство функция
             register: Int, //Запрашиваемый регистр
             delayAnswer: Int //Время ожидания ответа
    ): Pair<Boolean,Float> {
        val (succeed, answer) = GetClearAnswer(funct, register, 2, delayAnswer)
        return if (succeed) {
            var tmp: ByteArray = ByteArray(4) { 0 }
            for (i: Int in 0..2) {
                tmp[2 * i] = answer[answerStart + 2 * i + 1]
                tmp[2 * i + 1] = answer[answerStart + 2 * i]
            }
            Pair(succeed, ByteBuffer.wrap(tmp).order(ByteOrder.LITTLE_ENDIAN).float)
        } else {
            //logging of unsuccessful reading
            Pair(succeed, 0F)
        }
    }

    fun GetSwFloat( //Запрос switched-float-значения из регистра
            funct: Byte, //Отправленная на устройство функция
            register: Int, //Запрашиваемый регистр
            delayAnswer: Int //Время ожидания ответа
    ):Pair<Boolean,Float> {
        val (succeed, answer) = GetClearAnswer(funct, register, 2, delayAnswer)
        return if (succeed) {
            var tmp: ByteArray = ByteArray(4) { 0 }
            for (i: Int in 0..2) {
                tmp[2 * i] = answer[answerStart + 2 * i]
                tmp[2 * i + 1] = answer[answerStart + 2 * i + 1]
            }
            Pair(succeed, ByteBuffer.wrap(tmp).order(ByteOrder.LITTLE_ENDIAN).float)
        } else {
            //logging of unsuccessful reading
            Pair(succeed, 0F)
        }
    }

    fun GetHoldingFloat( //Запрос float-значения из регистра хранения
            register: Int, //Запрашиваемый регистр
            delayAnswer: Int //Время ожидания ответа
    ):Pair<Boolean,Float>
    {
        return GetFloat( READMANY_HOLDING, register, delayAnswer )
    }

    fun GetInputFloat( //Запрос float-значения из регистра ввода
            register: Int, //Запрашиваемый регистр
            delayAnswer: Int //Время ожидания ответа
    ):Pair<Boolean,Float>
    {
        return GetFloat( READMANY_INPUT, register, delayAnswer )
    }

    fun GetHoldingSwFloat( //Запрос switched-float-значения из регистра хранения
            register: Int, //Запрашиваемый регистр
            delayAnswer: Int //Время ожидания ответа
    ):Pair<Boolean,Float>
    {
        return GetSwFloat( READMANY_HOLDING, register, delayAnswer )
    }

    fun GetInputSwFloat( //Запрос switched-float-значения из регистра ввода
            register: Int, //Запрашиваемый регистр
            delayAnswer: Int //Время ожидания ответа
    ):Pair<Boolean,Float>
    {
        return GetSwFloat( READMANY_INPUT, register, delayAnswer )
    }

    fun GetClearAnswer( //Получение чистого ответа устройства (без убирания доп. части пакета)
            funct: Byte, //Отправленная на устройство функция
            register: Int, //Стартовый регистр
            length: Int, //Число регистров
            delayAnswer: Int //Время ожидания ответа
    ) : Pair< Boolean, Array<Byte>> //Успешность операции & Ответ устройства
    {
        val message = CreateMessage(funct, register, length )
        val answerLength = CountAnswerLength( funct.toInt(), length )
        return port.SendQuery(message, delayAnswer,
                answerStart + 2 * skipAtEnd + answerLength, maxIterations)
    }

    fun GetValue( // Запрос значений с устройства
            funct: Byte, //Отправленная на устройство функция
            register: Int, //Стартовый регистр
            length: Int, //Число регистров
            delayAnswer: Int //Время ожидания ответа
    ) : Pair<Boolean, Array<Int>> //Успешность операции & Набор значений регистров
    {
        val answerLength = CountAnswerLength( funct.toInt(), length)
        val (succeed, answer) = GetClearAnswer( funct, register, length, delayAnswer )
        return Pair(succeed, ConvertBytes( answer, funct, answerLength ))
    }

    fun SetFlag( //Установка (запись значения) coil-регистра
            register: Int, //Изменяемый регистр
            value: Boolean, //Отправляемый флаг
            delayAnswer: Int //Время ожидания ответа
    ):Boolean //Ответ устройства на операцию записи
    {
        val flag = if (value) WRITEVALUE_TRUE else WRITEVALUE_FALSE
        return SetValue(WRITEONE_FLAG, register, flag, delayAnswer)
    }

    fun SetHoldingValue( //Установка (запись значения) регистра хранения
            register: Int, //Изменяемый регистр
            value: Int, //Отправляемое значение
            delayAnswer: Int //Время ожидания ответа
    ):Boolean //Ответ устройства на операцию записи
    {
        return SetValue( WRITEONE_HOLDING, register, value, delayAnswer )
    }

    open fun SetValue( //Отправка пакета записи
            funct: Byte, //Отправляемая на устройство функция
            register: Int, //Изменяемый регистр
            value: Int, //Отправляемое значение
            delayAnswer: Int //Время ожидания ответа
    ):Boolean //Ответ устройства на операцию записи
    {
        return false
    }

    open fun SetMultipleHoldingValues( //Отправка пакета множественной записи регистров хранения (0х10)
            register: Int, //Стартовый регистр
            values: Array<Byte>, //Отправляемое значение
            delayAnswer: Int //Время ожидания ответа
    ):Boolean //Ответ устройства на операцию записи
    {
        return false
    }

    fun SetMultipleHoldingValues( //Отправка пакета множественной записи регистров хранения (0х10)
            register: Int, //Стартовый регистр
            values: Array<Int>, //Отправляемое значение
            delayAnswer: Int //Время ожидания ответа
    ):Boolean //Ответ устройства на операцию записи
    {
        val registerPoints = ConvertToBytes(values)
        return SetMultipleHoldingValues(register, registerPoints, delayAnswer)
    }

    fun SetHoldingFloat( //отправка на устройство float-величины
            register: Int, //адрес регистра
            value: Float, //записываемое значение
            delayAnswer: Int //время ожидания ответа
    ) : Boolean //Ответ устройства на операцию записи
    {
        //int[] tmp = new int[ 2 ];
        //tmp[ 0 ] = (int) BitConverter.ToInt16( BitConverter.GetBytes( value ), 0 );
        //tmp[ 1 ] = (int) BitConverter.ToInt16( BitConverter.GetBytes( value ), 2 );
        val tmp: Array<Byte> = Array(4) { 0 }
        return SetMultipleHoldingValues(register, tmp, delayAnswer)
    }

    fun SetHoldingSwFloat( //отправка на устройство float-величины
            register: Int, //адрес регистра
            value: Float, //записываемое значение
            delayAnswer: Int //время ожидания ответа
    ) : Boolean //Ответ устройства на операцию записи
    {
        //int[] tmp = new int[ 2 ];
        //tmp[ 0 ] = (int) BitConverter.ToInt16( BitConverter.GetBytes( value ), 2 );
        //tmp[ 1 ] = (int) BitConverter.ToInt16( BitConverter.GetBytes( value ), 0 );
        //return SetMultipleValue( address, register, tmp, sleep );
        val tmp: Array<Byte> = Array(4) { 0 }
        return SetMultipleHoldingValues(register, tmp, delayAnswer)
    }
}