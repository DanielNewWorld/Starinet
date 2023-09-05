package ua.zp.starinet.starinet_02072015.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import ua.zp.starinet.starinet_02072015.model.ArrayListDBInfoSub;
import ua.zp.starinet.starinet_02072015.network.InfoSubNetwork;

/**
 * Created by root on 19.10.15.
 */
public class GetInfoSubRequest extends RetrofitSpiceRequest<ArrayListDBInfoSub, InfoSubNetwork> {
    private String intHome;
    private String intEnd;
    private String intAccountID;

    public GetInfoSubRequest(String homesub, String endsub, String accountid) {
        super(ArrayListDBInfoSub.class, InfoSubNetwork.class);
        intHome = homesub;
        intEnd = endsub;
        intAccountID = accountid;
    }

    @Override
    public ArrayListDBInfoSub loadDataFromNetwork() throws Exception {
        return getService().GetWallpapers(intHome, intEnd, intAccountID);
    }
}
