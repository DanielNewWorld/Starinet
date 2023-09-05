package ua.zp.starinet.starinet_02072015.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import ua.zp.starinet.starinet_02072015.model.DBEMail;
import ua.zp.starinet.starinet_02072015.network.EMailNetwork;

/**
 * Created by root on 30.07.15.
 */

public class GetEMailRequest extends RetrofitSpiceRequest<DBEMail, EMailNetwork> {

    private int mdbUserID;
    private String mEMail;
    private int mStatus;

    public GetEMailRequest(int dbuserid, String email, int status) {
        super(DBEMail.class, EMailNetwork.class);
        mdbUserID = dbuserid;
        mEMail = email;
        mStatus = status;
    }

    @Override
    public DBEMail loadDataFromNetwork() throws Exception {
        return getService().GetWallpapers(mdbUserID, mEMail, mStatus);
    }
}
