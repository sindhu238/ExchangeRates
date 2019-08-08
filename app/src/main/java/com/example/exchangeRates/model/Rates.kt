package com.example.exchangeRates.model

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.json.Json

@Serializable
data class ConversionRates(
    val base: String,
    @Serializable(with = RateSer::class) val rates: List<sam>
)

@Serializable
data class sam(val name: String, val rate: Double)

@Serializable
data class Rate(
    val AUD: Double,
    val BGN: Double
)

@Serializer(forClass = Rate::class)
object RateSer: KSerializer<List<sam>> {

    override fun serialize(encoder: Encoder, obj: List<sam>) {

    }

    override val descriptor: SerialDescriptor
        get() = StringDescriptor.withName("blahhh")

    @UseExperimental(ImplicitReflectionSerializer::class)
    override fun deserialize(decoder: Decoder): List<sam> =
        (Json.parse(decoder.decode()) as? Rate)?.let {
        listOf(sam(it.AUD.toString(), it.AUD))
    }!!
}