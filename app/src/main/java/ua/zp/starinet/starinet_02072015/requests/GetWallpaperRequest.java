package ua.zp.starinet.starinet_02072015.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import ua.zp.starinet.starinet_02072015.model.DbItem;
import ua.zp.starinet.starinet_02072015.network.INetwork;

/**
 * Created by root on 30.07.15.
 */

public class GetWallpaperRequest extends RetrofitSpiceRequest<DbItem, INetwork> {

    private String mLogin;
    private String mPassword;

    public GetWallpaperRequest(String login, String password) {
        super(DbItem.class, INetwork.class);
        mLogin = login;
        mPassword = password;
    }

    @Override
    public DbItem loadDataFromNetwork() throws Exception {
        return getService().GetWallpapers(mLogin, mPassword);
    }
}
