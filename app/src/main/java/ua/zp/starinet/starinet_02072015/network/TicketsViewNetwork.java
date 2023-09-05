package ua.zp.starinet.starinet_02072015.network;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import ua.zp.starinet.starinet_02072015.model.ArrayListDBTicketsView;


/**
 * Created by root on 30.07.15.
 */

public interface TicketsViewNetwork {
    @GET("/tickets_view_only_text_script.php")
    ArrayListDBTicketsView GetWallpapers(@Query("ticketsID") String dbTicketsID
    );

    @POST("/tickets_view_only_text_script.php")
    Void PostWallpapers(@Query("param1") double quantity, @Query("param2") String name, @Body ArrayListDBTicketsView data);
}
