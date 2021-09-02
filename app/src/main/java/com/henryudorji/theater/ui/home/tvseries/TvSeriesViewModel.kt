package com.henryudorji.theater.ui.home.tvseries

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.henryudorji.theater.data.model.MovieResponse
import com.henryudorji.theater.data.repository.MovieRepository
import com.henryudorji.theater.utils.NetworkManager
import com.henryudorji.theater.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class TvSeriesViewModel @Inject constructor (
    private val repository: MovieRepository,
    private val networkManager: NetworkManager
    ) : ViewModel() {

    private val networkObserver = networkManager.observeConnectionStatus

    private val _popularTvSeriesLiveData = MutableLiveData<Resource<MovieResponse>>()
    val popularTvSeriesLiveData: LiveData<Resource<MovieResponse>> = _popularTvSeriesLiveData

    private val _onTheAirLiveData = MutableLiveData<Resource<MovieResponse>>()
    val onTheAirLiveData: LiveData<Resource<MovieResponse>> = _onTheAirLiveData

    private val _topRatedTvSeriesLiveData = MutableLiveData<Resource<MovieResponse>>()
    val topRatedTvSeriesLiveData: LiveData<Resource<MovieResponse>> = _topRatedTvSeriesLiveData

    private val _airingTodayLiveData = MutableLiveData<Resource<MovieResponse>>()
    val airingTodayLiveData: LiveData<Resource<MovieResponse>> = _airingTodayLiveData

    val page = 1

    init {
        getAiringToday(page)
        getPopularTvSeries(page)
        getOnTheAirTvSeries(page)
        getTopRatedTvSeries(page)
    }

    private fun getAiringToday(page: Int) = viewModelScope.launch(Dispatchers.IO) {
        _airingTodayLiveData.postValue(Resource.Loading())
        if (networkObserver.value == true) {
            try {
                val airingToday = repository.getAiringTodayTvSeries(page)
                if (airingToday.isSuccessful) {
                    airingToday.body()?.let { tvSeriesResponse ->
                        _airingTodayLiveData.postValue(Resource.Success(tvSeriesResponse))
                    }
                }else  _airingTodayLiveData.postValue(
                    Resource.Error(airingToday.errorBody().toString())
                )
            } catch (e: Exception) {
                _airingTodayLiveData.postValue(Resource.Error(e.localizedMessage))
            }
        }else {
            _airingTodayLiveData.postValue(Resource.Error(
                "internet not available, check connection"
            ))
        }
    }

    private fun getTopRatedTvSeries(page: Int) = viewModelScope.launch(Dispatchers.IO) {
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

    private fun getOnTheAirTvSeries(page: Int) = viewModelScope.launch(Dispatchers.IO) {
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

    private fun getPopularTvSeries(page: Int) = viewModelScope.launch(Dispatchers.IO) {
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