package ua.zp.starinet.starinet_02072015.network;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import ua.zp.starinet.starinet_02072015.model.DBTicketsUpdate;


/**
 * Created by root on 30.07.15.
 */

public interface TicketsUpdateNetwork {
    @GET("/tickets_update_script.php")
    DBTicketsUpdate GetWallpapers(@Query("ticketsID") String ticketsID,
                                  @Query("ticketsStatus") int ticketsStatus,
                                  @Query("ticketsPriority") int ticketsPriority);

    @POST("/tickets_update_script.php")
    Void PostWallpapers(@Query("param1") double quantity, @Query("param2") String name, @Body DBTicketsUpdate data);
}
