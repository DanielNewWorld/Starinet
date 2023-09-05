package ua.zp.starinet.starinet_02072015.network;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import ua.zp.starinet.starinet_02072015.model.DBTicketsAnswer;

/**
 * Created by root on 30.07.15.
 */

public interface TicketsAnswerNetwork {
    @GET("/tickets_answer_script.php")
    DBTicketsAnswer GetWallpapers(@Query("dbID") int dbID,
                                  @Query("ticketsID") String dbticketsID,
                                  @Query("dbText") String dbText);

    @POST("/tickets_answer_script.php")
    Void PostWallpapers(@Query("param1") double quantity, @Query("param2") String name, @Body DBTicketsAnswer data);
}
