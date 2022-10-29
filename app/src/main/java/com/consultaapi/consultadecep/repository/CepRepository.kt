package com.consultaapi.consultadecep.repository

import com.consultaapi.consultadecep.model.UserAddressModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {
    @GET("{cep}/json")
    fun getData(@Path("cep") cep: String): Call<UserAddressModel>
}