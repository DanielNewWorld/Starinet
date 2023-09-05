package ua.zp.starinet.starinet_02072015.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import ua.zp.starinet.starinet_02072015.model.ArrayListDBDebitSub;
import ua.zp.starinet.starinet_02072015.network.DebitSubNetwork;


/**
 * Created by root on 19.10.15.
 */
public class GetDebitSubRequest extends RetrofitSpiceRequest<ArrayListDBDebitSub, DebitSubNetwork> {
    private String intHome;
    private String intEnd;
    private String intAccountID;

    public GetDebitSubRequest(String homesub, String endsub, String accountid) {
        super(ArrayListDBDebitSub.class, DebitSubNetwork.class);
        intHome = homesub;
        intEnd = endsub;
        intAccountID = accountid;
    }

    @Override
    public ArrayListDBDebitSub loadDataFromNetwork() throws Exception {
        return getService().GetWallpapers(intHome, intEnd, intAccountID);
    }
}
