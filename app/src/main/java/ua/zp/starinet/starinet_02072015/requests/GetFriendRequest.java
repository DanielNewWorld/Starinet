package ua.zp.starinet.starinet_02072015.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import ua.zp.starinet.starinet_02072015.model.DBFriend;
import ua.zp.starinet.starinet_02072015.network.FriendNetwork;

/**
 * Created by root on 30.07.15.
 */

public class GetFriendRequest extends RetrofitSpiceRequest<DBFriend, FriendNetwork> {

    private String mAccountID;
    private String mAccountIDOplata;
    private Integer mOplata;
    private String mComment;

    public GetFriendRequest(String dbaccount, String accountidoplata, Integer oplata, String comment) {
        super(DBFriend.class, FriendNetwork.class);
        mAccountID = dbaccount;
        mAccountIDOplata = accountidoplata;
        mOplata = oplata;
        mComment = comment;
    }

    @Override
    public DBFriend loadDataFromNetwork() throws Exception {
        return getService().GetWallpapers(mAccountID, mAccountIDOplata, mOplata, mComment);
    }
}
