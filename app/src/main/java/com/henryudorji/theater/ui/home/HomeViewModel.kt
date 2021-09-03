package com.henryudorji.theater.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.henryudorji.theater.data.model.movie.MovieResponse
import com.henryudorji.theater.data.model.tv.TvSeriesResponse
import com.henryudorji.theater.data.repository.MovieRepository
import com.henryudorji.theater.utils.NetworkManager
import com.henryudorji.theater.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor (
    private val repository: MovieRepository,
    private val networkManager: NetworkManager
) : ViewModel() {

    private val networkObserver = networkManager.observeConnectionStatus

    private val _popularMoviesLiveData = MutableLiveData<Resource<MovieResponse>>()
    val popularMoviesLiveData: LiveData<Resource<MovieResponse>> = _popularMoviesLiveData

    private val _upcomingMoviesLiveData = MutableLiveData<Resource<MovieResponse>>()
    val upcomingMoviesLiveData: LiveData<Resource<MovieResponse>> = _upcomingMoviesLiveData

    private val _topRatedMoviesLiveData = MutableLiveData<Resource<MovieResponse>>()
    val topRatedLiveData: LiveData<Resource<MovieResponse>> = _topRatedMoviesLiveData

    //TV SERIES
    private val _popularTvSeriesLiveData = MutableLiveData<Resource<TvSeriesResponse>>()
    val popularTvSeriesLiveData: LiveData<Resource<TvSeriesResponse>> = _popularTvSeriesLiveData

    private val _onTheAirLiveData = MutableLiveData<Resource<TvSeriesResponse>>()
    val onTheAirLiveData: LiveData<Resource<TvSeriesResponse>> = _onTheAirLiveData

    private val _topRatedTvSeriesLiveData = MutableLiveData<Resource<TvSeriesResponse>>()
    val topRatedTvSeriesLiveData: LiveData<Resource<TvSeriesResponse>> = _topRatedTvSeriesLiveData

    val page = 1


    fun getTopRatedMovies(page: Int) = viewModelScope.launch(Dispatchers.IO) {
        _topRatedMoviesLiveData.postValue(Resource.Loading())
        if (networkObserver.value == true) {
            try {
                val topRatedMoviesResponse = repository.getTopRatedMovies(page)
                if (topRatedMoviesResponse.isSuccessful) {
                    topRatedMoviesResponse.body()?.let { movieResponse ->
                        _topRatedMoviesLiveData.postValue(Resource.Success(movieResponse))
                    }
                }else  _topRatedMoviesLiveData.postValue(
                    Resource.Error(topRatedMoviesResponse.errorBody().toString())
                )
            } catch (e: Exception) {
                _topRatedMoviesLiveData.postValue(Resource.Error(e.localizedMessage))
            }
        }else {
            _topRatedMoviesLiveData.postValue(Resource.Error(
                "internet not available, check connection"
            ))
        }
    }

    fun getUpcomingMovies(page: Int) = viewModelScope.launch(Dispatchers.IO) {
        _upcomingMoviesLiveData.postValue(Resource.Loading())
        if (networkObserver.value == true) {
            try {
                val upcomingMoviesResponse = repository.getPopularMovies(page)
                if (upcomingMoviesResponse.isSuccessful) {
                    upcomingMoviesResponse.body()?.let { movieResponse ->
                        _upcomingMoviesLiveData.postValue(Resource.Success(movieResponse))
                    }
                }else  _upcomingMoviesLiveData.postValue(
                    Resource.Error(upcomingMoviesResponse.errorBody().toString())
                )
            } catch (e: Exception) {
                _upcomingMoviesLiveData.postValue(Resource.Error(e.localizedMessage))
            }
        }else {
            _upcomingMoviesLiveData.postValue(Resource.Error(
                "internet not available, check connection"
            ))
        }

    }

    fun getPopularMovies(page: Int) = viewModelScope.launch(Dispatchers.IO) {
        _popularMoviesLiveData.postValue(Resource.Loading())
        if (networkObserver.value == true) {
            try {
                val popularMoviesResponse = repository.getPopularMovies(page)
                if (popularMoviesResponse.isSuccessful) {
                    popularMoviesResponse.body()?.let { movieResponse ->
                        _popularMoviesLiveData.postValue(Resource.Success(movieResponse))
                    }
                }else  _popularMoviesLiveData.postValue(
                    Resource.Error(popularMoviesResponse.errorBody().toString())
                )
            } catch (e: Exception) {
                _popularMoviesLiveData.postValue(Resource.Error(e.localizedMessage))
            }
        }else {
            _popularMoviesLiveData.postValue(Resource.Error(
                "internet not available, check connection"
            ))
        }
    }

    fun getTopRatedTvSeries(page: Int) = viewModelScope.launch(Dispatchers.IO) {
        _topRatedTvSeriesLiveData.postValue(Resource.Loading())
        if (networkObserver.value == true) {
            try {
                val topRatedTvSeriesResponse = repository.getTopRatedTvSeries(page)
                if (topRatedTvSeriesResponse.isSuccessful) {
                    topRatedTvSeriesResponse.body()?.let { tvSeriesResponse ->
                        _topRatedTvSeriesLiveData.postValue(Resource.Success(tvSeriesResponse))
                    }
                }else  _topRatedTvSeriesLiveData.postValue(
                    Resource.Error(topRatedTvSeriesResponse.errorBody().toString())
                )
            } catch (e: Exception) {
                _topRatedTvSeriesLiveData.postValue(Resource.Error(e.localizedMessage))
            }
        }else {
            _topRatedTvSeriesLiveData.postValue(Resource.Error(
                "internet not available, check connection"
            ))
        }
    }

    fun getOnTheAirTvSeries(page: Int) = viewModelScope.launch(Dispatchers.IO) {
        _onTheAirLiveData.postValue(Resource.Loading())
        if (networkObserver.value == true) {
            try {
                val onTheAirResponse = repository.getOnTheAir(page)
                if (onTheAirResponse.isSuccessful) {
                    onTheAirResponse.body()?.let { movieResponse ->
                        _onTheAirLiveData.postValue(Resource.Success(movieResponse))
                    }
                }else  _onTheAirLiveData.postValue(
                    Resource.Error(onTheAirResponse.errorBody().toString())
                )
            } catch (e: Exception) {
                _onTheAirLiveData.postValue(Resource.Error(e.localizedMessage))
            }
        }else {
            _onTheAirLiveData.postValue(Resource.Error(
                "internet not available, check connection"
            ))
        }

    }

    fun getPopularTvSeries(page: Int) = viewModelScope.launch(Dispatchers.IO) {
        _popularTvSeriesLiveData.postValue(Resource.Loading())
        if (networkObserver.value == true) {
            try {
                val popularTvSeriesResponse = repository.getPopularTvSeries(page)
                if (popularTvSeriesResponse.isSuccessful) {
                    popularTvSeriesResponse.body()?.let { movieResponse ->
                        _popularTvSeriesLiveData.postValue(Resource.Success(movieResponse))
                    }
                }else  _popularTvSeriesLiveData.postValue(
                    Resource.Error(popularTvSeriesResponse.errorBody().toString())
                )
            } catch (e: Exception) {
                _popularTvSeriesLiveData.postValue(Resource.Error(e.localizedMessage))
            }
        }else {
            _popularTvSeriesLiveData.postValue(Resource.Error(
                "internet not available, check connection"
            ))
        }
    }
}