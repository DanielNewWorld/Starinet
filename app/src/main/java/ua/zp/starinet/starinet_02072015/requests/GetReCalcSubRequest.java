package ua.zp.starinet.starinet_02072015.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import ua.zp.starinet.starinet_02072015.model.ArrayListDBReCalcSub;
import ua.zp.starinet.starinet_02072015.network.ReCalcSubNetwork;

/**
 * Created by root on 19.10.15.
 */
public class GetReCalcSubRequest extends RetrofitSpiceRequest<ArrayListDBReCalcSub, ReCalcSubNetwork> {
    private int mdbid;
    private int mstatus;
    private String mdbAccountID;
    private String mdbFIOReCalc;
    private String mdbTelReCalc;
    private String medtHomeString;
    private String medtEndString;
    private String medtComment;

    public GetReCalcSubRequest(int dbid, int status, String dbAccountID, String dbFIOReCalc, String dbTelReCalc, String edtHomeString, String edtEndString, String edtComment) {
        super(ArrayListDBReCalcSub.class, ReCalcSubNetwork.class);
        mdbid = dbid;
        mstatus = status;
        mdbAccountID = dbAccountID;
        mdbFIOReCalc = dbFIOReCalc;
        mdbTelReCalc = dbTelReCalc;
        medtHomeString = edtHomeString;
        medtEndString = edtEndString;
        medtComment = edtComment;
    }

    @Override
    public ArrayListDBReCalcSub loadDataFromNetwork() throws Exception {
        return getService().GetWallpapers(mdbid, mstatus, mdbAccountID, mdbFIOReCalc, mdbTelReCalc, medtHomeString, medtEndString, medtComment);
    }
}
