package ua.zp.starinet.starinet_02072015.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import ua.zp.starinet.starinet_02072015.model.DBByPin;
import ua.zp.starinet.starinet_02072015.network.ByPinNetwork;

/**
 * Created by root on 19.10.15.
 */
public class GetByPinRequest extends RetrofitSpiceRequest<DBByPin, ByPinNetwork> {
    private String byPinNomer;
    private String byPin;
    private String byIP;
    private String byAccountID;
    private String byLogin;
    private double byBalance;

    public GetByPinRequest(String nomer, String pin, String ip, String accountid, String bylogin, double bybalance) {
        super(DBByPin.class, ByPinNetwork.class);
        byPinNomer = nomer;
        byPin = pin;
        byIP = ip;
        byAccountID = accountid;
        byLogin = bylogin;
        byBalance = bybalance;
    }

    @Override
    public DBByPin loadDataFromNetwork() throws Exception {
        return getService().GetWallpapers(byPinNomer, byPin, byIP, byAccountID, byLogin, byBalance);
    }
}
