package com.consultaapi.consultadecep

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.consultaapi.consultadecep.databinding.ActivityMainBinding
import com.consultaapi.consultadecep.model.UserAddressModel
import com.consultaapi.consultadecep.repository.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnConsult.setOnClickListener { consult() }
    }

    private fun consult() {
        val cepCode = binding.etCepCode.text.toString()
        if(cepCode.length < 8 || cepCode.length > 8) {
            Toast.makeText(
                this,
                getString(R.string.erro_cep_with_wrong_number_of_digits),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            getCEP()
        }
    }

    private fun getCEP() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://viacep.com.br/ws/")
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getData(binding.etCepCode.text.toString())

        retrofitData.enqueue(object : Callback<UserAddressModel> {
            override fun onResponse(call: Call<UserAddressModel>, response: Response<UserAddressModel>) {
                if (response.isSuccessful && !response.body()?.cep.isNullOrBlank()) {
                    binding.tvCep.text = getString(R.string.cep, response.body()?.cep)
                    binding.tvLogradouro.text = getString(R.string.logradouro, response.body()?.logradouro)
                    binding.tvBairro.text = getString(R.string.bairro, response.body()?.bairro)
                    binding.tvLocalidade.text = getString(R.string.localidade, response.body()?.localidade)
                    binding.tvUf.text = getString(R.string.uf, response.body()?.uf)
                    binding.tvDdd.text = getString(R.string.ddd, response.body()?.ddd)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.consult_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<UserAddressModel>, t: Throwable) {
                Log.d("MainActivity", "ERROR ${t.message}")
            }
        })
    }
}