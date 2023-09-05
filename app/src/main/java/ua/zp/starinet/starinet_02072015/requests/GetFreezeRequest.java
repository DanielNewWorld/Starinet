package ua.zp.starinet.starinet_02072015.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import ua.zp.starinet.starinet_02072015.model.DBFreeze;
import ua.zp.starinet.starinet_02072015.network.FreezeNetwork;

/**
 * Created by root on 30.07.15.
 */

public class GetFreezeRequest extends RetrofitSpiceRequest<DBFreeze, FreezeNetwork> {

    private String mAccountIP;
    private int mchekStatusUPDATE;
    private double mdbBalanceUPDATE;

    public GetFreezeRequest(String accountip, int chekStatusUPDATE, double dbBalanceUPDATE) {
        super(DBFreeze.class, FreezeNetwork.class);
        mAccountIP = accountip;
        mchekStatusUPDATE = chekStatusUPDATE;
        mdbBalanceUPDATE = dbBalanceUPDATE;
    }

    @Override
    public DBFreeze loadDataFromNetwork() throws Exception {
        return getService().GetWallpapers(mAccountIP, mchekStatusUPDATE, mdbBalanceUPDATE);
    }
}
