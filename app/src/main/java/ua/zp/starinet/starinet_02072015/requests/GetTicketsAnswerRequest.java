package ua.zp.starinet.starinet_02072015.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import ua.zp.starinet.starinet_02072015.model.DBTicketsAnswer;
import ua.zp.starinet.starinet_02072015.network.TicketsAnswerNetwork;


/**
 * Created by root on 30.07.15.
 */

public class GetTicketsAnswerRequest extends RetrofitSpiceRequest<DBTicketsAnswer, TicketsAnswerNetwork> {

    private int mID;
    private String mdbticketsID;
    private String mText;

    public GetTicketsAnswerRequest(int id, String dbticketsID, String dbText) {
        super(DBTicketsAnswer.class, TicketsAnswerNetwork.class);
        mID = id;
        mdbticketsID = dbticketsID;
        mText = dbText;
    }

    @Override
    public DBTicketsAnswer loadDataFromNetwork() throws Exception {
        return getService().GetWallpapers(mID, mdbticketsID, mText);
    }
}
