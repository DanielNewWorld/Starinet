package ua.zp.starinet.starinet_02072015.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import ua.zp.starinet.starinet_02072015.model.ArrayListDBTicketsView;
import ua.zp.starinet.starinet_02072015.network.TicketsViewNetwork;


/**
 * Created by root on 19.10.15.
 */
public class GetTicketsViewRequest extends RetrofitSpiceRequest<ArrayListDBTicketsView, TicketsViewNetwork> {
    private String mTiketsID;

    public GetTicketsViewRequest(String dbTicketsID) {
        super(ArrayListDBTicketsView.class, TicketsViewNetwork.class);
        mTiketsID = dbTicketsID;
    }

    @Override
    public ArrayListDBTicketsView loadDataFromNetwork() throws Exception {
        return getService().GetWallpapers(mTiketsID);
    }
}
