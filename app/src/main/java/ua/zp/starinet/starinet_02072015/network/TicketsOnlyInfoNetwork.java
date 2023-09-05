package ua.zp.starinet.starinet_02072015.network;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import ua.zp.starinet.starinet_02072015.model.DBTicketsOnlyInfo;


/**
 * Created by root on 30.07.15.
 */

public interface TicketsOnlyInfoNetwork {
    @GET("/ticketsviewonlyscript.php")
    DBTicketsOnlyInfo GetWallpapers(@Query("ticketsID") String dbticketsID,
                                    @Query("ticketsStatus") int ticketsStatus
    );

    @POST("/ticketsviewonlyscript.php")
    Void PostWallpapers(@Query("param1") double quantity, @Query("param2") String name, @Body DBTicketsOnlyInfo data);
}
