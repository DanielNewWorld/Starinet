package ua.zp.starinet.starinet_02072015.network;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import ua.zp.starinet.starinet_02072015.model.DBFreeze;

/**
 * Created by root on 30.07.15.
 */

public interface FreezeNetwork {
    @GET("/freezescript.php")
    DBFreeze GetWallpapers(@Query("dbAccountID") String accountid,
                           @Query("chekStatusUPDATE") int chekStatusUPDATE,
                           @Query("dbBalanceUPDATE") double dbBalanceUPDATE);

    @POST("/freezescript.php")
    Void PostWallpapers(@Query("param1") double quantity, @Query("param2") String name, @Body DBFreeze data);
}
