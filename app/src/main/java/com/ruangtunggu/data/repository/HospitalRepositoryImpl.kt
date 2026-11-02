package com.ruangtunggu.data.repository

import com.ruangtunggu.data.model.Hospital
import com.ruangtunggu.data.model.Polyclinic
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HospitalRepositoryImpl @Inject constructor() : HospitalRepository {

    private val polyclinics = listOf(
        Polyclinic(id = "poli_umum", hospitalId = "rs_01", nama = "Umum", icon = "medical_services"),
        Polyclinic(id = "poli_anak", hospitalId = "rs_01", nama = "Anak", icon = "child_care"),
        Polyclinic(id = "poli_gigi", hospitalId = "rs_01", nama = "Gigi", icon = "dentistry"),
        Polyclinic(id = "poli_kandungan", hospitalId = "rs_01", nama = "Kandungan", icon = "pregnant_woman"),
        Polyclinic(id = "poli_mata", hospitalId = "rs_01", nama = "Mata", icon = "visibility"),
        Polyclinic(id = "poli_penyakit_dalam", hospitalId = "rs_01", nama = "Penyakit Dalam", icon = "healing")
    )

    private val hospitals = listOf(
        Hospital(
            id = "rs_01",
            nama = "RS Sehat Sidoarjo",
            alamat = "Jl. Raya Candi No. 45, Sidoarjo",
            latitude = -7.4721,
            longitude = 112.7153,
            rating = 4.8f
        ),
        Hospital(
            id = "rs_02",
            nama = "RS Harapan Bunda",
            alamat = "Jl. Pahlawan No. 12, Sidoarjo",
            latitude = -7.4560,
            longitude = 112.7180,
            rating = 4.8f
        ),
        Hospital(
            id = "rs_03",
            nama = "RSUD Sidoarjo",
            alamat = "Jl. Mojopahit No. 667, Sidoarjo",
            latitude = -7.4612,
            longitude = 112.7121,
            rating = 4.6f
        ),
        Hospital(
            id = "rs_04",
            nama = "RS Siti Khadijah",
            alamat = "Jl. Raya Taman No. 1, Sepanjang",
            latitude = -7.3489,
            longitude = 112.6978,
            rating = 4.7f
        )
    )

    override suspend fun getHospitals(query: String?): Result<List<Hospital>> {
        delay(300)
        return if (query.isNullOrBlank()) {
            Result.success(hospitals)
        } else {
            val filtered = hospitals.filter {
                it.nama.contains(query, ignoreCase = true) ||
                it.alamat.contains(query, ignoreCase = true)
            }
            Result.success(filtered)
        }
    }

    override suspend fun getNearbyHospitals(lat: Double, lng: Double): Result<List<Hospital>> {
        delay(300)
        return Result.success(hospitals)
    }

    override suspend fun getPolyclinics(hospitalId: String): Result<List<Polyclinic>> {
        delay(200)
        return Result.success(polyclinics)
    }

    override suspend fun getPopularPolyclinics(): Result<List<Polyclinic>> {
        delay(200)
        return Result.success(polyclinics.take(4)) // Umum, Anak, Gigi, Kandungan
    }
}

