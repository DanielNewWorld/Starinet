package ua.zp.starinet.starinet_02072015.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import ua.zp.starinet.starinet_02072015.model.DBPass;
import ua.zp.starinet.starinet_02072015.network.PassNetwork;

/**
 * Created by root on 30.07.15.
 */

public class GetPassRequest extends RetrofitSpiceRequest<DBPass, PassNetwork> {

    private int mID;
    private String mNewPass;

    public GetPassRequest(int id, String newpass) {
        super(DBPass.class, PassNetwork.class);
        mID = id;
        mNewPass = newpass;
    }

    @Override
    public DBPass loadDataFromNetwork() throws Exception {
        return getService().GetWallpapers(mID, mNewPass);
    }
}
