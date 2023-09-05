package ua.zp.starinet.starinet_02072015.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import ua.zp.starinet.starinet_02072015.model.ArrayListDBInternetSub;
import ua.zp.starinet.starinet_02072015.network.InternetSubNetwork;

/**
 * Created by root on 19.10.15.
 */
public class GetInternetSubRequest extends RetrofitSpiceRequest<ArrayListDBInternetSub, InternetSubNetwork> {
    private String intHome;
    private String intEnd;
    private String intAccountID;

    public GetInternetSubRequest(String homesub, String endsub, String accountid) {
        super(ArrayListDBInternetSub.class, InternetSubNetwork.class);
        intHome = homesub;
        intEnd = endsub;
        intAccountID = accountid;
    }

    @Override
    public ArrayListDBInternetSub loadDataFromNetwork() throws Exception {
        return getService().GetWallpapers(intHome, intEnd, intAccountID);
    }
}
