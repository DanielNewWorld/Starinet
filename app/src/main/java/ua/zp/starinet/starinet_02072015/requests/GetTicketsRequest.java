package ua.zp.starinet.starinet_02072015.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import ua.zp.starinet.starinet_02072015.model.DBTickets;
import ua.zp.starinet.starinet_02072015.network.TicketsNetwork;


/**
 * Created by root on 30.07.15.
 */

public class GetTicketsRequest extends RetrofitSpiceRequest<DBTickets, TicketsNetwork> {

    private int mID;
    private String mSubject;
    private String mAccountID;
    private String mDepartment;
    private int mPriority;
    private String mText;

    public GetTicketsRequest(int id, String subject, String accountID, String dbdepartment, int dbpriority, String dbText) {
        super(DBTickets.class, TicketsNetwork.class);
        mID = id;
        mSubject = subject;
        mAccountID = accountID;
        mDepartment = dbdepartment;
        mPriority = dbpriority;
        mText = dbText;
    }

    @Override
    public DBTickets loadDataFromNetwork() throws Exception {
        return getService().GetWallpapers(mID, mSubject, mAccountID, mDepartment, mPriority, mText);
    }
}
