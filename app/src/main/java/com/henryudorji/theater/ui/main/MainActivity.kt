package com.henryudorji.theater.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.henryudorji.theater.R
import com.henryudorji.theater.data.repository.MovieRepository
import com.henryudorji.theater.databinding.ActivityMainBinding
import com.henryudorji.theater.utils.Constants.SNACKBAR_LENGHT_LONG
import com.henryudorji.theater.utils.NetworkManager
import com.henryudorji.theater.utils.showSnackBar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var movieRepository: MovieRepository
    private lateinit var viewModel: MoviesViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Theater)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movieRepository = MovieRepository()
        val factory = MoviesViewModelFactory(movieRepository, application)
        viewModel = ViewModelProvider(this, factory).get(MoviesViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        //handleNetworkChanges()
    }

    private fun handleNetworkChanges() {
        NetworkManager.getNetworkInfo(applicationContext).observe(this) { isConnected ->
            if (!isConnected) binding.root.showSnackBar(getString(R.string.internet_not_available), SNACKBAR_LENGHT_LONG) else Unit
        }
    }
}