package ua.zp.starinet.starinet_02072015.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import ua.zp.starinet.starinet_02072015.model.ArrayListDBTicketsAll;
import ua.zp.starinet.starinet_02072015.network.TicketsAllNetwork;


/**
 * Created by root on 19.10.15.
 */
public class GetTicketsAllRequest extends RetrofitSpiceRequest<ArrayListDBTicketsAll, TicketsAllNetwork> {
    private int mID;

    public GetTicketsAllRequest(int dbID) {
        super(ArrayListDBTicketsAll.class, TicketsAllNetwork.class);
        mID = dbID;
    }

    @Override
    public ArrayListDBTicketsAll loadDataFromNetwork() throws Exception {
        return getService().GetWallpapers(mID);
    }
}
