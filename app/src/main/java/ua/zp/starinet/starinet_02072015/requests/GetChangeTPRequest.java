package ua.zp.starinet.starinet_02072015.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import ua.zp.starinet.starinet_02072015.model.DBChangeTP;
import ua.zp.starinet.starinet_02072015.network.ChangeTPNetwork;

/**
 * Created by root on 30.07.15.
 */

public class GetChangeTPRequest extends RetrofitSpiceRequest<DBChangeTP, ChangeTPNetwork> {

    private String mAccount;
    private String mChangeTP;

    public GetChangeTPRequest(String account, String changetp) {
        super(DBChangeTP.class, ChangeTPNetwork.class);
        mAccount = account;
        mChangeTP = changetp;
    }

    @Override
    public DBChangeTP loadDataFromNetwork() throws Exception {
        return getService().GetWallpapers(mAccount, mChangeTP);
    }
}
