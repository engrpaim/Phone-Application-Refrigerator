// RetrofitClient.kt
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object trofitClient {
    private const val BASE_URL = "http://192.166.20.99/"

    val instance: Esp32Service by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        retrofit.create(Esp32Service::class.java)
    }
}
