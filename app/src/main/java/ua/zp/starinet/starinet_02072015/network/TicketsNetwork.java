package ua.zp.starinet.starinet_02072015.network;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import ua.zp.starinet.starinet_02072015.model.DBTickets;

/**
 * Created by root on 30.07.15.
 */

public interface TicketsNetwork {
    @GET("/ticketsscript.php")
    DBTickets GetWallpapers(@Query("dbID") int dbID,
                            @Query("dbSubject") String dbSubject,
                            @Query("dbAccountID") String dbAccountID,
                            @Query("dbDepartment") String dbDepartment,
                            @Query("dbPriority") int dbPriority,
                            @Query("dbText") String dbText);

    @POST("/ticketsscript.php")
    Void PostWallpapers(@Query("param1") double quantity, @Query("param2") String name, @Body DBTickets data);
}
