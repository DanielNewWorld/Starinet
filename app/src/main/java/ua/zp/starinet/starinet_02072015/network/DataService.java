package ua.zp.starinet.starinet_02072015.network;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

public class DataService extends RetrofitGsonSpiceService {
    private final static String BASE_URL = "http://10.20.200.19";

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    protected String getServerUrl() {
        return BASE_URL;
    }

}