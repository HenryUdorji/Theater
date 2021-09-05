package com.henryudorji.theater.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.henryudorji.theater.data.model.cast.CastResponse
import com.henryudorji.theater.data.model.detail.MovieDetailResponse
import com.henryudorji.theater.data.model.detail.TvSeriesDetailResponse
import com.henryudorji.theater.data.model.movie.MovieResponse
import com.henryudorji.theater.data.model.review.ReviewResponse
import com.henryudorji.theater.data.model.tv.TvSeriesResponse
import com.henryudorji.theater.data.model.video.VideoResponse
import com.henryudorji.theater.data.repository.MovieRepository
import com.henryudorji.theater.utils.Constants.MOVIE
import com.henryudorji.theater.utils.Constants.TV_SERIES
import com.henryudorji.theater.utils.NetworkManager
import com.henryudorji.theater.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor (
    private val repository: MovieRepository,
    private val networkManager: NetworkManager
) : ViewModel() {
    private val networkObserver = networkManager.observeConnectionStatus

    private val _movieDetailLiveData = MutableLiveData<Resource<MovieDetailResponse>>()
    val movieDetailLiveData: LiveData<Resource<MovieDetailResponse>> = _movieDetailLiveData

    private val _tvSeriesDetailLiveData = MutableLiveData<Resource<TvSeriesDetailResponse>>()
    val tvSeriesDetailLiveData: LiveData<Resource<TvSeriesDetailResponse>> = _tvSeriesDetailLiveData

    private val _trailerLiveData = MutableLiveData<Resource<VideoResponse>>()
    val trailerLiveData: LiveData<Resource<VideoResponse>> = _trailerLiveData

    private val _reviewsLiveData = MutableLiveData<Resource<ReviewResponse>>()
    val reviewsLiveData: LiveData<Resource<ReviewResponse>> = _reviewsLiveData

    private val _castLiveData = MutableLiveData<Resource<CastResponse>>()
    val castLiveData: LiveData<Resource<CastResponse>> = _castLiveData

    private val _moviesRecommendationLiveData = MutableLiveData<Resource<MovieResponse>>()
    val moviesRecommendationLiveData: LiveData<Resource<MovieResponse>> = _moviesRecommendationLiveData

    private val _tvSeriesRecommendationLiveData = MutableLiveData<Resource<TvSeriesResponse>>()
    val tvSeriesRecommendationLiveData: LiveData<Resource<TvSeriesResponse>> = _tvSeriesRecommendationLiveData

    private val page: Int = 1



    fun getMovieDetail(movieID: Int) = viewModelScope.launch(Dispatchers.IO) {
        _movieDetailLiveData.postValue(Resource.Loading())
        if (networkObserver.value == true) {
            try {
                val response = repository.getMovieDetails(movieID)
                if (response.isSuccessful) {
                    response.body()?.let { detailResponse ->
                        _movieDetailLiveData.postValue(Resource.Success(detailResponse))

                        if (detailResponse.video) {
                            getTrailerVideos(movieID, MOVIE)
                        }
                        getCast(movieID, MOVIE)
                        getReviews(movieID, MOVIE)
                        getMovieRecommendation(movieID)
                    }
                }else  {
                    _movieDetailLiveData.postValue(Resource.Error(response.errorBody().toString()))
                }
            }catch (e: Exception) {
                _movieDetailLiveData.postValue(Resource.Error(e.localizedMessage))
            }
        }else {
            _movieDetailLiveData.postValue(Resource.Error(
                "internet not available, check connection"
            ))
        }
    }

    fun getTvSeriesDetail(tvSeriesID: Int) = viewModelScope.launch(Dispatchers.IO) {
        _tvSeriesDetailLiveData.postValue(Resource.Loading())
        if (networkObserver.value == true) {
            try {
                val response = repository.getTvSeriesDetails(tvSeriesID)
                if (response.isSuccessful) {
                    response.body()?.let { detailResponse ->
                        _tvSeriesDetailLiveData.postValue(Resource.Success(detailResponse))

                        getTrailerVideos(movieID = tvSeriesID, TV_SERIES)
                        getCast(movieID = tvSeriesID, TV_SERIES)
                        getReviews(movieID = tvSeriesID, TV_SERIES)
                        getTvSeriesRecommendation(tvSeriesID)
                    }
                }else  {
                    _tvSeriesDetailLiveData.postValue(Resource.Error(response.errorBody().toString()))
                }
            }catch (e: Exception) {
                _tvSeriesDetailLiveData.postValue(Resource.Error(e.localizedMessage))
            }
        }else {
            _tvSeriesDetailLiveData.postValue(Resource.Error(
                "internet not available, check connection"
            ))
        }
    }

    private fun getCast(movieID: Int, type: Int) = viewModelScope.launch(Dispatchers.IO) {
        _castLiveData.postValue(Resource.Loading())
        try {
            val response = if (type == MOVIE) {
                repository.getMovieCast(movieID, page)
            }else repository.getTvSeriesCast(tvSeriesID = movieID, page)

            if (response.isSuccessful) {
                response.body()?.let { castResponse ->
                    _castLiveData.postValue(Resource.Success(castResponse))
                }
            }else {
                _castLiveData.postValue(Resource.Error("Unable to load Cast, try again"))
            }
        }catch (e: Exception) {
            _castLiveData.postValue(Resource.Error(e.localizedMessage))
        }
    }

    private fun getReviews(movieID: Int, type: Int) = viewModelScope.launch(Dispatchers.IO) {
        _reviewsLiveData.postValue(Resource.Loading())
        try {
            val response = if (type == MOVIE) {
                repository.getMovieReview(movieID, page)
            }else repository.getTvSeriesReview(tvSeriesID = movieID, page)

            if (response.isSuccessful) {
                response.body()?.let { reviewResponse ->
                    _reviewsLiveData.postValue(Resource.Success(reviewResponse))
                }
            }else {
                _reviewsLiveData.postValue(Resource.Error("Unable to load Reviews, try again"))
            }
        }catch (e: Exception) {
            _reviewsLiveData.postValue(Resource.Error(e.localizedMessage))
        }
    }

    private fun getMovieRecommendation(movieID: Int) = viewModelScope.launch(Dispatchers.IO){
        _moviesRecommendationLiveData.postValue(Resource.Loading())
        try {
            val response = repository.getMovieRecommendation(movieID)

            if (response.isSuccessful) {
                response.body()?.let { movieResponse ->
                    _moviesRecommendationLiveData.postValue(Resource.Success(movieResponse))
                }
            }else {
                _moviesRecommendationLiveData.postValue(Resource.Error("Unable to load " +
                        "Recommendations, try again"))
            }
        }catch (e: Exception) {
            _moviesRecommendationLiveData.postValue(Resource.Error(e.localizedMessage))
        }
    }

    private fun getTvSeriesRecommendation(tvSeriesID: Int) = viewModelScope.launch(Dispatchers.IO){
        _tvSeriesRecommendationLiveData.postValue(Resource.Loading())
        try {
            val response = repository.getTvSeriesRecommendation(tvSeriesID)

            if (response.isSuccessful) {
                response.body()?.let { tvSeriesResponse ->
                    _tvSeriesRecommendationLiveData.postValue(Resource.Success(tvSeriesResponse))
                }
            }else {
                _tvSeriesRecommendationLiveData.postValue(Resource.Error("Unable to load " +
                        "Recommendations, try again"))
            }
        }catch (e: Exception) {
            _tvSeriesRecommendationLiveData.postValue(Resource.Error(e.localizedMessage))
        }
    }

    private fun getTrailerVideos(movieID: Int, type: Int) = viewModelScope.launch(Dispatchers.IO) {
        _trailerLiveData.postValue(Resource.Loading())
        try {
            val response = if (type == MOVIE) {
                repository.getMovieTrailer(movieID)
            }else repository.getTvSeriesTrailer(tvSeriesID = movieID)

            if (response.isSuccessful) {
                response.body()?.let { videoResponse ->
                    _trailerLiveData.postValue(Resource.Success(videoResponse))
                }
            }else {
                _trailerLiveData.postValue(Resource.Error("Unable to load Trailer, try again"))
            }
        }catch (e: Exception) {
            _trailerLiveData.postValue(Resource.Error(e.localizedMessage))
        }
    }

}