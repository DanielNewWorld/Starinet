package ua.zp.starinet.starinet_02072015.network;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import ua.zp.starinet.starinet_02072015.model.DBCredit;

/**
 * Created by root on 30.07.15.
 */

public interface CreditNetwork {
    @GET("/creditscript.php")
    DBCredit GetWallpapers(@Query("dbAccountID") String dbaccountid,
                           @Query("dbCredit") String dbcredit);

    @POST("/creditscript.php")
    Void PostWallpapers(@Query("param1") double quantity, @Query("param2") String name, @Body DBCredit data);
}
