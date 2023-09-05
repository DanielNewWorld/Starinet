package ua.zp.starinet.starinet_02072015.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import ua.zp.starinet.starinet_02072015.model.DBTicketsUpdate;
import ua.zp.starinet.starinet_02072015.network.TicketsUpdateNetwork;


/**
 * Created by root on 30.07.15.
 */

public class GetTicketsUpdateRequest extends RetrofitSpiceRequest<DBTicketsUpdate, TicketsUpdateNetwork> {

    private String mID;
    private int mticketsStatus;
    private int mticketsPriority;

    public GetTicketsUpdateRequest(String id, int ticketsstatus, int ticketspriority) {
        super(DBTicketsUpdate.class, TicketsUpdateNetwork.class);
        mID = id;
        mticketsStatus = ticketsstatus;
        mticketsPriority = ticketspriority;
    }

    @Override
    public DBTicketsUpdate loadDataFromNetwork() throws Exception {
        return getService().GetWallpapers(mID, mticketsStatus, mticketsPriority);
    }
}
