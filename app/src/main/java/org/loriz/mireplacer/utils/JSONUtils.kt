package com.appdoit.kotlin.appdoit4.helpers

import android.util.Log
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.*

/**
 * JSON utils class
 *
 * Created by giannideplano on 12/05/15.
 */
object JSONUtils {

    private val TAG = JSONUtils::class.simpleName

    private var mapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())

    fun toMap(json: String): Map<String, Any>? {
        try {
            return mapper.readValue<Map<String, Any>>(json, object : TypeReference<Map<String, Any>>() {

            })
        } catch (e: IOException) {
            Log.e(TAG, "Unable to parse JSON", e)
            return null
        }

    }

    fun toList(json: String): List<Map<String, Any>>? {
        try {
            return mapper.readValue<List<Map<String, Any>>>(json, object : TypeReference<List<Map<String, Any>>>() {

            })
        } catch (e: IOException) {
            Log.e(TAG, "Unable to parse JSON", e)
            return null
        }

    }

    fun toStringList(json: String): List<String>? {
        try {
            return mapper.readValue<List<String>>(json, object : TypeReference<List<String>>() {

            })
        } catch (e: IOException) {
            Log.e(TAG, "Unable to parse JSON", e)
            return null
        }

    }


    fun <T> toObject(json: String, clazz: Class<T>): T? {
        try {
            return mapper.readValue(json, clazz)
        } catch (e: IOException) {
            Log.e(TAG, "Unable to parse JSON", e)
            return null
        }

    }

    fun toMapList(json: String): List<Map<String, Any>>? {
        try {
            return mapper.readValue<List<Map<String, Any>>>(json, object : TypeReference<List<Map<String, Any>>>() {

            })
        } catch (e: IOException) {
            Log.e(TAG, "Unable to parse JSON", e)
            return null
        }

    }

    /**
     * PARSE FROM INPUT STREAM
     */

    fun toMap(inputStream: InputStream): Map<String, Any>? {
        try {
            return mapper.readValue<Map<String, Any>>(inputStream, object : TypeReference<Map<String, Any>>() {

            })
        } catch (e: IOException) {
            Log.e(TAG, "Unable to parse JSON", e)
            return null
        }

    }


    fun toMapList(inputStream: InputStream): List<Map<String, Any>>? {
        try {
            return mapper.readValue<List<Map<String, Any>>>(inputStream, object : TypeReference<List<Map<String, Any>>>() {

            })
        } catch (e: IOException) {
            Log.e(TAG, "Unable to parse JSON", e)
            return null
        }

    }

    fun toList(inputStream: InputStream): List<Any>? {
        try {
            return mapper.readValue<List<Any>>(inputStream, object : TypeReference<List<String>>() {

            })
        } catch (e: IOException) {
            Log.e(TAG, "Unable to parse JSON", e)
            return null
        }

    }

    fun <T> toObject(inputStream: InputStream, clazz: Class<T>): T? {
        try {
            return mapper.readValue(inputStream, clazz)
        } catch (e: IOException) {
            Log.e(TAG, "Unable to parse JSON", e)
            return null
        }

    }

    /**
     * PARSE FROM File
     */

    fun toMap(file: File): Map<String, Any>? {
        try {
            return mapper.readValue<Map<String, Any>>(file, object : TypeReference<Map<String, Any>>() {

            })
        } catch (e: IOException) {
            Log.e(TAG, "Unable to parse JSON", e)
            return null
        }

    }

    fun toMapList(file: File): List<Map<String, Any>>? {
        try {
            return mapper.readValue<List<Map<String, Any>>>(file, object : TypeReference<List<Map<String, Any>>>() {

            })
        } catch (e: IOException) {
            Log.e(TAG, "Unable to parse JSON", e)
            return null
        }

    }


    fun toList(file: File): List<Any>? {
        try {
            return mapper.readValue<List<Any>>(file, object : TypeReference<List<String>>() {

            })
        } catch (e: IOException) {
            Log.e(TAG, "Unable to parse JSON", e)
            return null
        }

    }

    fun <T> toObject(file: File, clazz: Class<T>): T? {
        try {
            return mapper.readValue(file, clazz)
        } catch (e: IOException) {
            Log.e(TAG, "Unable to parse JSON", e)
            return null
        }

    }

    /**
     * PARSE FROM URL
     */

    fun toMap(url: URL): Map<String, Any>? {
        try {
            return mapper.readValue<Map<String, Any>>(url, object : TypeReference<Map<String, Any>>() {

            })
        } catch (e: IOException) {
            Log.e(TAG, "Unable to parse JSON", e)
            return null
        }

    }

    fun toMapList(url: URL): List<Map<String, Any>>? {
        try {
            return mapper.readValue<List<Map<String, Any>>>(url, object : TypeReference<List<Map<String, Any>>>() {

            })
        } catch (e: IOException) {
            Log.e(TAG, "Unable to parse JSON", e)
            return null
        }

    }

    fun<T> toListT(jsonString: String): List<T>? {
        try {
            return mapper.readValue<List<T>>(jsonString, object : TypeReference<List<T>>() {

            })
        } catch (e: IOException) {
            Log.e(TAG, "Unable to parse JSON", e)
            return null
        }

    }


    fun toList(url: URL): List<Any>? {
        try {
            return mapper.readValue<List<Any>>(url, object : TypeReference<List<String>>() {

            })
        } catch (e: IOException) {
            Log.e(TAG, "Unable to parse JSON", e)
            return null
        }

    }

    fun <T> toObject(url: URL, clazz: Class<T>): T? {
        try {
            return mapper.readValue(url, clazz)
        } catch (e: IOException) {
            Log.e(TAG, "Unable to parse JSON", e)
            return null
        }

    }

    /**
     * TO JSON
     */

    fun toJson(`object`: Any): String? {
        try {
            return mapper.writeValueAsString(`object`)
        } catch (e: IOException) {
            Log.e(TAG, "Unable to convert object to JSON", e)
            return null
        }

    }


    /**
     * MAP TO OBJECT
     */

    fun fromMapToObject(map: Map<*, *>, clazz: Class<*>): Any {
        return mapper.convertValue(map, clazz)
    }

    /**
     * OBJECT TO MAP
     */

    fun fromObjectToMap(`object`: Any): Map<String, Any> {
        val tr = object : TypeReference<HashMap<*, *>>() {}
        return mapper.readValue(mapper.writeValueAsString(`object`), tr)
    }

    fun <T> convert(map: Map<*, *>, clazz: Class<T>): T {
        return mapper.convertValue(map, clazz)
    }

    /**
     * JSON TO OBJECT LIST
     */
    fun <T> fromJSONtoObjList(type: TypeReference<T>, jsonPacket: String): T? {
        var data: T? = null

        try {
            data = ObjectMapper().readValue<T>(jsonPacket, type)
        } catch (e: Exception) {
            data = null
        }

        return data
    }

}
