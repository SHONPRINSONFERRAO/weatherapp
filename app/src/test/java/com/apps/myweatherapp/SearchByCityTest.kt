package com.apps.myweatherapp

import com.apps.myweatherapp.network.NetworkRepository
import com.apps.myweatherapp.search.model.CityDataModel
import com.apps.myweatherapp.search.presenter.SearchPresenterImpl
import com.apps.myweatherapp.search.presenter.SearchViewContract
import com.shon.projects.payappmodel.utils.glide.networking.ApiClient
import net.bytebuddy.matcher.ElementMatchers.any
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class SearchByCityTest {

    @InjectMocks
    private lateinit var presenter: SearchPresenterImpl

    @Mock
    private lateinit var repository: NetworkRepository

    @Mock
    private lateinit var api: ApiClient

    @Mock
    private  lateinit var viewContract: SearchViewContract

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this) // required for the "@Mock" annotations
        // repository = NetworkRepository(api)

        // Make presenter a mock while using mock repository and viewContract created above
        presenter = Mockito.spy(SearchPresenterImpl(viewContract, repository))
    }


    @Test
    fun searchGitHubRepos_noQuery() {
        val searchQuery: String? = null
        // Trigger
        presenter.fetchSearchResults(null)
        // Validation
        Mockito.verify(repository, Mockito.times(0)).loadCityData("", presenter)
    }

    @Test
    fun searchGitHubRepos() {
        val searchQuery = "some query"
        // Trigger
        presenter.fetchSearchResults("dubai")
        // Validation
        Mockito.verify(repository, Mockito.times(1)).loadCityData("dubai", presenter)
    }

    @Test
    fun handleGitHubResponse_Success() {
        val response = Mockito.mock(Response::class.java)
        val searchResponse: CityDataModel = Mockito.mock(CityDataModel::class.java)
        Mockito.doReturn(true).`when`(response).isSuccessful
        Mockito.doReturn(searchResponse).`when`(response).body()

        // trigger
        presenter.handleSearchWeatherResponse(response as Response<CityDataModel>)
        // validation
        Mockito.verify(viewContract, Mockito.times(1))
            .displaySearchResults(searchResponse)
    }

    @Test
    fun handleGitHubResponse_Failure() {
        val response = Mockito.mock(Response::class.java)
        Mockito.doReturn(false).`when`(response).isSuccessful
        // trigger
        presenter.handleSearchWeatherResponse(response as Response<CityDataModel>)
        // validation
        Mockito.verify(viewContract, Mockito.times(1))
            .displayError(Mockito.anyString())
    }

    @Test
    fun handleGitHubResponse_Failure_null_body() {
        val response = Mockito.mock(Response::class.java)
        // validation
        //Mockito.doReturn(null).`when`(response).body()
        // trigger
        presenter.handleSearchWeatherResponse(response as Response<CityDataModel>)
        // validation
        Mockito.verify(viewContract, Mockito.times(1))
            .displayError(Mockito.anyString())
    }

    @Test
    fun handleGitHubError() {
        presenter.handleError("exe")
        Mockito.verify(viewContract, Mockito.times(1)).displayError("exe")
    }

    @Test
    fun handleGitHubError_wrong_assert() {
        presenter.handleError("exe")
        Mockito.verify(viewContract, Mockito.times(0)).displayError("testfail")
    }

}