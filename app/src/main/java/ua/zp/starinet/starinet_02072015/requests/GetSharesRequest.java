package ua.zp.starinet.starinet_02072015.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import ua.zp.starinet.starinet_02072015.model.ArrayListDBShares;
import ua.zp.starinet.starinet_02072015.network.SharesNetwork;


/**
 * Created by root on 19.10.15.
 */
public class GetSharesRequest extends RetrofitSpiceRequest<ArrayListDBShares, SharesNetwork> {
    private int mdbID;

    public GetSharesRequest(int dbID) {
        super(ArrayListDBShares.class, SharesNetwork.class);
        mdbID = dbID;
    }

    @Override
    public ArrayListDBShares loadDataFromNetwork() throws Exception {
        return getService().GetWallpapers(mdbID);
    }
}
