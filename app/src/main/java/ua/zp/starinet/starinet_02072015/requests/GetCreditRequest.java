package ua.zp.starinet.starinet_02072015.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import ua.zp.starinet.starinet_02072015.model.DBCredit;
import ua.zp.starinet.starinet_02072015.network.CreditNetwork;


/**
 * Created by root on 30.07.15.
 */

public class GetCreditRequest extends RetrofitSpiceRequest<DBCredit, CreditNetwork> {

    private String mAccount;
    private String mBalance;

    public GetCreditRequest(String account, String balance) {
        super(DBCredit.class, CreditNetwork.class);
        mAccount = account;
        mBalance = balance;
    }

    @Override
    public DBCredit loadDataFromNetwork() throws Exception {
        return getService().GetWallpapers(mAccount, mBalance);
    }
}
