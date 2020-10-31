package registerMapTikModscan

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.cfg.MapperConfig
import com.fasterxml.jackson.databind.introspect.AnnotatedField
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod


class TikModscanNamingStrategy : PropertyNamingStrategy() {
    override fun nameForField(
            config: MapperConfig<*>?,
            field: AnnotatedField?,
            defaultName: String?
    ): String {
        return super.nameForField(config, field, defaultName)
    }

    override fun nameForGetterMethod(
            config: MapperConfig<*>?,
            method: AnnotatedMethod?,
            defaultName: String?
    ): String {
        return super.nameForGetterMethod(config, method, defaultName)
    }

    override fun nameForSetterMethod(
            config: MapperConfig<*>?,
            method: AnnotatedMethod?,
            defaultName: String?
    ): String {
        TODO("Find how to save naming as-is")
        //val test = method?.name?.drop(3).toString()
        //return method?.name?.drop(3).toString()
    }
}