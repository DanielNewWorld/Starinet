package ua.zp.starinet.starinet_02072015.network;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import ua.zp.starinet.starinet_02072015.model.DBFriend;

/**
 * Created by root on 30.07.15.
 */

public interface FriendNetwork {
    @GET("/friendscript.php")
    DBFriend GetWallpapers(@Query("dbAccountID") String dbAccountID,
                           @Query("accountIDOplata") String accountIDOplata,
                           @Query("oplata") Integer oplata,
                           @Query("comment") String comment
    );

    @POST("/friendscript.php")
    Void PostWallpapers(@Query("param1") double quantity, @Query("param2") String name, @Body DBFriend data);
}
