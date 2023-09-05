package ua.zp.starinet.starinet_02072015.network;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import ua.zp.starinet.starinet_02072015.model.DBChangeTP;

/**
 * Created by root on 30.07.15.
 */

public interface ChangeTPNetwork {
    @GET("/changetpscript.php")
    DBChangeTP GetWallpapers(@Query("dbAccountID") String login, @Query("dbChangeTP") String pass);

    @POST("/changetpscript.php")
    Void PostWallpapers(@Query("param1") double quantity, @Query("param2") String name, @Body DBChangeTP data);
}
