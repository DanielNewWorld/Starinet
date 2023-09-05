package ua.zp.starinet.starinet_02072015.network;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import ua.zp.starinet.starinet_02072015.model.ArrayListDBInfoSub;


/**
 * Created by root on 30.07.15.
 */

public interface InfoSubNetwork {
    @GET("/infoscript.php")
    ArrayListDBInfoSub GetWallpapers(@Query("dbDataHome") String intHome,
                                      @Query("dbDataEnd") String intEnd,
                                      @Query("dbAccountID") String intAccountID
    );

    @POST("/infoscript.php")
    Void PostWallpapers(@Query("param1") double quantity, @Query("param2") String name, @Body ArrayListDBInfoSub data);
}
