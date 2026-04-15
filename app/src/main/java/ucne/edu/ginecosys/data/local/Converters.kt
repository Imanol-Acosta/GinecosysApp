package ucne.edu.ginecosys.data.local

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import ucne.edu.ginecosys.domain.model.ActivePregnancy
import ucne.edu.ginecosys.domain.model.Antecedents
import ucne.edu.ginecosys.domain.model.ObstetricHistory
import ucne.edu.ginecosys.domain.model.Pregnancy

object Converters {

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // ObstetricHistory
    fun obstetricHistoryToJson(obj: ObstetricHistory?): String? {
        obj ?: return null
        return moshi.adapter(ObstetricHistory::class.java).toJson(obj)
    }

    fun obstetricHistoryFromJson(json: String?): ObstetricHistory? {
        json ?: return null
        return try {
            moshi.adapter(ObstetricHistory::class.java).fromJson(json)
        } catch (_: Exception) { null }
    }

    // Antecedents
    fun antecedentsToJson(obj: Antecedents?): String? {
        obj ?: return null
        return moshi.adapter(Antecedents::class.java).toJson(obj)
    }

    fun antecedentsFromJson(json: String?): Antecedents? {
        json ?: return null
        return try {
            moshi.adapter(Antecedents::class.java).fromJson(json)
        } catch (_: Exception) { null }
    }

    // ActivePregnancy
    fun activePregnancyToJson(obj: ActivePregnancy?): String? {
        obj ?: return null
        return moshi.adapter(ActivePregnancy::class.java).toJson(obj)
    }

    fun activePregnancyFromJson(json: String?): ActivePregnancy? {
        json ?: return null
        return try {
            moshi.adapter(ActivePregnancy::class.java).fromJson(json)
        } catch (_: Exception) { null }
    }

    // List<Pregnancy>
    private val pregnancyListType = Types.newParameterizedType(List::class.java, Pregnancy::class.java)

    fun pregnanciesToJson(list: List<Pregnancy>?): String? {
        if (list.isNullOrEmpty()) return null
        return moshi.adapter<List<Pregnancy>>(pregnancyListType).toJson(list)
    }

    fun pregnanciesFromJson(json: String?): List<Pregnancy> {
        json ?: return emptyList()
        return try {
            moshi.adapter<List<Pregnancy>>(pregnancyListType).fromJson(json) ?: emptyList()
        } catch (_: Exception) { emptyList() }
    }

    // Generic Map<String, Any> converter for Supabase JSON fields
    private val mapType = Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java)

    fun mapToObstetricHistory(map: Map<String, Any>?): ObstetricHistory? {
        map ?: return null
        return ObstetricHistory(
            g = (map["g"] as? Number)?.toInt() ?: 0,
            p = (map["p"] as? Number)?.toInt() ?: 0,
            c = (map["c"] as? Number)?.toInt() ?: 0,
            a = (map["a"] as? Number)?.toInt() ?: 0
        )
    }

    fun mapToAntecedents(map: Map<String, Any>?): Antecedents? {
        map ?: return null
        return Antecedents(
            family = map["family"] as? String ?: "",
            pathological = map["pathological"] as? String ?: "",
            allergies = map["allergies"] as? String ?: "",
            surgical = map["surgical"] as? String ?: ""
        )
    }

    fun mapToActivePregnancy(map: Map<String, Any>?): ActivePregnancy? {
        map ?: return null
        return ActivePregnancy(
            weeks = (map["weeks"] as? Number)?.toInt() ?: 0,
            fpp = map["fpp"] as? String ?: "",
            risk = map["risk"] as? String ?: "Bajo",
            lastFHR = map["lastFHR"] as? String ?: ""
        )
    }

    @Suppress("UNCHECKED_CAST")
    fun mapListToPregnancies(list: List<Map<String, Any>>?): List<Pregnancy> {
        list ?: return emptyList()
        return list.map { map ->
            Pregnancy(
                id = map["id"] as? String ?: "",
                year = (map["year"] as? Number)?.toInt() ?: 0,
                outcome = map["outcome"] as? String ?: "",
                details = map["details"] as? String ?: ""
            )
        }
    }

    fun obstetricHistoryToMap(obj: ObstetricHistory?): Map<String, Any>? {
        obj ?: return null
        return mapOf("g" to obj.g, "p" to obj.p, "c" to obj.c, "a" to obj.a)
    }

    fun antecedentsToMap(obj: Antecedents?): Map<String, Any>? {
        obj ?: return null
        return mapOf(
            "family" to obj.family,
            "pathological" to obj.pathological,
            "allergies" to obj.allergies,
            "surgical" to obj.surgical
        )
    }

    fun activePregnancyToMap(obj: ActivePregnancy?): Map<String, Any>? {
        obj ?: return null
        return mapOf(
            "weeks" to obj.weeks,
            "fpp" to obj.fpp,
            "risk" to obj.risk,
            "lastFHR" to obj.lastFHR
        )
    }

    fun pregnanciesListToMap(list: List<Pregnancy>?): List<Map<String, Any>>? {
        if (list.isNullOrEmpty()) return null
        return list.map { p ->
            mapOf(
                "id" to p.id,
                "year" to p.year,
                "outcome" to p.outcome,
                "details" to p.details
            )
        }
    }
}
