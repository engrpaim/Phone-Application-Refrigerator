import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded

interface Esp32Service {
    @FormUrlEncoded
    @POST("/refx/server.php") // Adjust endpoint path if needed
    fun sendData(
        @Field("param0") param0: String?,
        @Field("param1") param1: String?,
        @Field("time") time: String
    ): Call<String>
}
