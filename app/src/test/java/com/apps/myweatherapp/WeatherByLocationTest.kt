package com.apps.myweatherapp




import com.apps.myweatherapp.location.model.WeatherModel
import com.apps.myweatherapp.location.presenter.WeatherPresenterImpl
import com.apps.myweatherapp.location.presenter.WeatherViewContract
import com.apps.myweatherapp.network.NetworkRepository
import com.shon.projects.payappmodel.utils.glide.networking.ApiClient
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response


@RunWith(MockitoJUnitRunner::class)
class WeatherByLocationTest {

    @InjectMocks
    private lateinit var presenter: WeatherPresenterImpl

    @Mock
    private lateinit var repository: NetworkRepository

    @Mock
    private lateinit var api: ApiClient


    @Mock
    private  lateinit var viewContract: WeatherViewContract

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this) // required for the "@Mock" annotations
       // repository = NetworkRepository(api)

        // Make presenter a mock while using mock repository and viewContract created above
        presenter = Mockito.spy(WeatherPresenterImpl(viewContract, repository))
    }


    @Test
    fun searchGitHubRepos_noQuery() {
        val searchQuery: String? = null
        // Trigger
        presenter.fetchWeather(null, null)
        // Validation
        Mockito.verify(repository, Mockito.times(0)).loadWeatherData(null, null, presenter)
    }

    @Test
    fun searchGitHubRepos() {
        val searchQuery = "some query"
        // Trigger
        presenter.fetchWeather(3.10,4.12)
        // Validation
        Mockito.verify(repository, Mockito.times(1)).loadWeatherData(3.10, 4.12, presenter)
    }

    @Test
    fun handleGitHubResponse_Success() {
        val response = Mockito.mock(Response::class.java)
        val searchResponse: WeatherModel = Mockito.mock(WeatherModel::class.java)
        Mockito.doReturn(true).`when`(response).isSuccessful
        Mockito.doReturn(searchResponse).`when`(response).body()

        // trigger
        presenter.handleWeatherResponse(response as Response<WeatherModel>)
        // validation
        Mockito.verify(viewContract, Mockito.times(1))
            .displayWeatherResults(searchResponse)
    }

    @Test
    fun handleGitHubResponse_Failure() {
        val response = Mockito.mock(Response::class.java)
        Mockito.doReturn(false).`when`(response).isSuccessful
        // trigger
        presenter.handleWeatherResponse(response as Response<WeatherModel>)
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
        presenter.handleWeatherResponse(response as Response<WeatherModel>)
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