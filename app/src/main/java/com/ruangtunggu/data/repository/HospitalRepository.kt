package com.ruangtunggu.data.repository

import com.ruangtunggu.data.model.Hospital
import com.ruangtunggu.data.model.Polyclinic

interface HospitalRepository {
    suspend fun getHospitals(query: String? = null): Result<List<Hospital>>
    suspend fun getNearbyHospitals(lat: Double, lng: Double): Result<List<Hospital>>
    suspend fun getPolyclinics(hospitalId: String): Result<List<Polyclinic>>
    suspend fun getPopularPolyclinics(): Result<List<Polyclinic>>
}
