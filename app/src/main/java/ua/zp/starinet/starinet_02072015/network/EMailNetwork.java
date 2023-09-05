package ua.zp.starinet.starinet_02072015.network;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import ua.zp.starinet.starinet_02072015.model.DBEMail;

/**
 * Created by root on 30.07.15.
 */

public interface EMailNetwork {
    @GET("/emailscript.php")
    DBEMail GetWallpapers(@Query("dbUserID") int dbuserid,
                           @Query("email") String email,
                           @Query("status") int status
    );

    @POST("/emailscript.php")
    Void PostWallpapers(@Query("param1") double quantity, @Query("param2") String name, @Body DBEMail data);
}
