package ua.zp.starinet.starinet_02072015.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import ua.zp.starinet.starinet_02072015.model.DBTicketsOnlyInfo;
import ua.zp.starinet.starinet_02072015.network.TicketsOnlyInfoNetwork;


/**
 * Created by root on 19.10.15.
 */
public class GetTicketsOnlyInfoRequest extends RetrofitSpiceRequest<DBTicketsOnlyInfo, TicketsOnlyInfoNetwork> {
    private String mTicketsID;
    private int mTicketsStatus;

    public GetTicketsOnlyInfoRequest(String dbTicketsID, int dbTicketsStatus) {
        super(DBTicketsOnlyInfo.class, TicketsOnlyInfoNetwork.class);
        mTicketsID = dbTicketsID;
        mTicketsStatus = dbTicketsStatus;
    }

    @Override
    public DBTicketsOnlyInfo loadDataFromNetwork() throws Exception {
        return getService().GetWallpapers(mTicketsID, mTicketsStatus);
    }
}
