package com.ruangtunggu.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object SplashRoute

@Serializable
object LoginRoute

@Serializable
object RegisterRoute

@Serializable
object ForgotPasswordRoute

@Serializable
data class BerandaRoute(
    val initialTab: String = "BERANDA"
)

@Serializable
data class CariRsPoliRoute(
    val initialPoliFilter: String = "Semua"
)

@Serializable
data class PilihPoliRoute(
    val hospitalId: String = "rs-sehat-sidoarjo",
    val hospitalName: String = "RS Sehat Sidoarjo"
)

@Serializable
data class PilihDokterJadwalRoute(
    val hospitalId: String = "rs-sehat-sidoarjo",
    val hospitalName: String = "RS Sehat Sidoarjo",
    val poliId: String = "poli-anak",
    val poliName: String = "Poli Anak",
    val poliShortName: String = "Anak"
)

@Serializable
data class KonfirmasiPendaftaranRoute(
    val hospitalId: String = "rs-sehat-sidoarjo",
    val hospitalName: String = "RS Sehat Sidoarjo",
    val poliId: String = "poli-anak",
    val poliName: String = "Poli Anak",
    val poliShortName: String = "Anak",
    val doctorId: String = "doc-abdul",
    val doctorName: String = "dr. Abdul, Sp.A",
    val dateLabel: String = "Kam, 22 Okt",
    val slotTime: String = "08:00"
)

@Serializable
object PendaftaranBerhasilRoute

@Serializable
object TiketQrRoute

@Serializable
object DatangKeLoketRoute

@Serializable
object ChatPetugasRoute

@Serializable
object NotifikasiRoute

@Serializable
object AturPengingatRoute

@Serializable
object BeriPenilaianRoute

@Serializable
object RescheduleJadwalRoute

@Serializable
object UbahProfilRoute
