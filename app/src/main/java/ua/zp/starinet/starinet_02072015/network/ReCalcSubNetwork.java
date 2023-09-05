package ua.zp.starinet.starinet_02072015.network;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import ua.zp.starinet.starinet_02072015.model.ArrayListDBReCalcSub;


/**
 * Created by root on 30.07.15.
 */

public interface ReCalcSubNetwork {
    @GET("/recalcscript.php")
    ArrayListDBReCalcSub GetWallpapers(@Query("dbID") int dbid,
                                       @Query("status") int status,
                                       @Query("dbAccountID") String dbAccountID,
                                       @Query("dbFIOReCalc") String dbFIOReCalc,
                                       @Query("dbTelReCalc") String dbTelReCalc,
                                       @Query("edtHomeString") String edtHomeString,
                                       @Query("edtEndString") String edtEndString,
                                       @Query("edtComment") String edtComment
    );

    @POST("/recalcscript.php")
    Void PostWallpapers(@Query("param1") double quantity, @Query("param2") String name, @Body ArrayListDBReCalcSub data);
}
