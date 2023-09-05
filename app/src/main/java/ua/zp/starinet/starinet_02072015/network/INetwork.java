package ua.zp.starinet.starinet_02072015.network;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import ua.zp.starinet.starinet_02072015.model.DbItem;

/**
 * Created by root on 30.07.15.
 */

public interface INetwork {
    @GET("/starscript.php")
    DbItem GetWallpapers(@Query("dbLogin") String login, @Query("dbPass") String pass);

    @POST("/starscript.php")
    Void PostWallpapers(@Query("param1") double quantity, @Query("param2") String name, @Body DbItem data);
}
