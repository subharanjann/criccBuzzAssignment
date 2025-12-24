package com.assignment.weatherassignment.domain.useCase

import com.assignment.weatherassignment.data.api.model.CityResponseItem
import com.assignment.weatherassignment.domain.repository.WeatherRepository
import com.assignment.weatherassignment.util.Resource

internal class SearchCitiesUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(
        query: String
    ): Resource<List<CityResponseItem>> {
        return repository.searchCities(query)
    }
}
