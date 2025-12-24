package com.assignment.weatherassignment.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.weatherassignment.data.api.model.CityResponseItem
import com.assignment.weatherassignment.data.local.model.WeatherData
import com.assignment.weatherassignment.domain.useCase.GetCachedWeatherUseCase
import com.assignment.weatherassignment.domain.useCase.GetWeatherByCityUseCase
import com.assignment.weatherassignment.domain.useCase.GetWeatherByLocationUseCase
import com.assignment.weatherassignment.domain.useCase.SearchCitiesUseCase
import com.assignment.weatherassignment.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

internal class WeatherViewModel(
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val getWeatherByCityUseCase: GetWeatherByCityUseCase,
    private val getWeatherByLocationUseCase: GetWeatherByLocationUseCase,
    private val getCachedWeatherUseCase: GetCachedWeatherUseCase

) : ViewModel(){

    private val _citySearchResult = MutableLiveData<Resource<List<CityResponseItem>>>()
    val citySearchResult: LiveData<Resource<List<CityResponseItem>>>  = _citySearchResult

    private val _weatherResult = MutableLiveData<Resource<List<WeatherData>>>()
    val weatherResult: LiveData<Resource<List<WeatherData>>>  = _weatherResult

    private val _isOnline = MutableLiveData<Boolean>()
    val isOnline: LiveData<Boolean> = _isOnline


    private val searchQuery = MutableStateFlow("")

    init {
        observeCitySearch()
        loadCachedWeather()
    }


    fun onCityQueryChanged(query: String) {
        searchQuery.value = query
    }

    private fun observeCitySearch() {
        viewModelScope.launch {
            searchQuery
                .debounce(400)
                .distinctUntilChanged()
                .filter { it.length >= 2 }
                .collectLatest { query ->
                    _citySearchResult.postValue(Resource.Loading())
                    val result = searchCitiesUseCase(query)
                    _citySearchResult.postValue(result)
                }
        }
    }

    fun fetchWeatherByCity(cityName: String) {
        viewModelScope.launch {

            if (!hasCachedData()) {
                _weatherResult.postValue(Resource.Loading())
            }

            val result = getWeatherByCityUseCase(cityName)

            when (result) {
                is Resource.Success -> {
                    _weatherResult.postValue(result)
                }

                is Resource.Error -> {
                    if (!hasCachedData()) {
                        _weatherResult.postValue(result)
                    }
                }

                else -> Unit
            }
        }
    }


    private fun loadCachedWeather() {
        viewModelScope.launch {
            _weatherResult.postValue(Resource.Loading())
            val result = getCachedWeatherUseCase()
            _weatherResult.postValue(result)
        }
    }

    private fun hasCachedData(): Boolean {
        return _weatherResult.value is Resource.Success
    }


    fun fetchWeatherByLocation(lat: Double, lon: Double) {
        viewModelScope.launch {

            if (!hasCachedData()) {
                _weatherResult.postValue(Resource.Loading())
            }

            val result = getWeatherByLocationUseCase(lat, lon)

            when (result) {
                is Resource.Success -> {
                    _weatherResult.postValue(result)
                }

                is Resource.Error -> {
                    if (!hasCachedData()) {
                        _weatherResult.postValue(result)
                    }
                }

                else -> Unit
            }
        }
    }

//netwrk stuff
fun updateNetworkStatus(isOnline: Boolean) {
    _isOnline.postValue(isOnline)
}

}