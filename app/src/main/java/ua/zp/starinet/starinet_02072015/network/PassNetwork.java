package ua.zp.starinet.starinet_02072015.network;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import ua.zp.starinet.starinet_02072015.model.DBPass;

/**
 * Created by root on 30.07.15.
 */

public interface PassNetwork {
    @GET("/passscript.php")
    DBPass GetWallpapers(@Query("dbID") int dbID, @Query("dbNewPass") String dbNewPass);

    @POST("/passscript.php")
    Void PostWallpapers(@Query("param1") double quantity, @Query("param2") String name, @Body DBPass data);
}
