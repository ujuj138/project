package activitytest.example.com.atry.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import activitytest.example.com.atry.interfaces.IPlayControl;
import activitytest.example.com.atry.interfaces.IPlayerViewControl;
import activitytest.example.com.atry.presenter.PlayPresenter;

/**
 * Created by liwenxuan on 2019/3/18.
 */

public class PlayerServices extends Service {
    private PlayPresenter mPlayPresenter;


    @Override
    public void onCreate() {
        super.onCreate();
        if(mPlayPresenter==null) {
            mPlayPresenter = new PlayPresenter();
        }
    }

    public IBinder onBind(Intent intent){
        return  mPlayPresenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayPresenter=null;
    }
}
