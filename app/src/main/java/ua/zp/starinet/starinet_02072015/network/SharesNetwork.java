package ua.zp.starinet.starinet_02072015.network;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import ua.zp.starinet.starinet_02072015.model.ArrayListDBShares;


/**
 * Created by root on 30.07.15.
 */

public interface SharesNetwork {
    @GET("/shares_script.php")
    ArrayListDBShares GetWallpapers(@Query("dbID") int dbID
    );

    @POST("/shares_script.php")
    Void PostWallpapers(@Query("param1") double quantity, @Query("param2") String name, @Body ArrayListDBShares data);
}
