package ua.zp.starinet.starinet_02072015.network;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import ua.zp.starinet.starinet_02072015.model.DBByPin;

/**
 * Created by root on 30.07.15.
 */

public interface ByPinNetwork {
    @GET("/bypinscript.php")
    DBByPin GetWallpapers(@Query("dbNomer") String byPinNomer,
                          @Query("dbPin") String byPin,
                          @Query("dbIP") String byIP,
                          @Query("dbAccountID") String byAccountID,
                          @Query("dbLogin") String byLogin,
                          @Query("dbBalance") double byBalance
    );

    @POST("/bypinscript.php")
    Void PostWallpapers(@Query("param1") double quantity, @Query("param2") String name, @Body DBByPin data);
}
