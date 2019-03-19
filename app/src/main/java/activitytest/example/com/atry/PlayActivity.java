package activitytest.example.com.atry;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import activitytest.example.com.atry.interfaces.IPlayControl;
import activitytest.example.com.atry.interfaces.IPlayerViewControl;
import activitytest.example.com.atry.services.PlayerServices;

import static activitytest.example.com.atry.interfaces.IPlayControl.PLAY_STATE_PAUSE;
import  static  activitytest.example.com.atry.interfaces.IPlayControl.PLAY_STATE_PLAYER;
import static activitytest.example.com.atry.interfaces.IPlayControl.PLAY_STATE_STOP;

/**
 * Created by liwenxuan on 2019/3/18.
 */

public class PlayActivity extends Activity {
    private static  final String TAG="PlayerActivity";
    private  SeekBar mSeekBar;
    private  Button mPlayOrPause;
    private  Button mClose;
    private  PlayerConnection mPlayerConnection;
    private IPlayControl mIPlayControl;
    private  boolean isUserTouchProgressBar=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initView();
        initEvent();
        initService();
        initBindService();
    }

    private void initService() {
        startService(new Intent(this,PlayerServices.class));
    }

    //服务不可以长期运行
    private void initBindService() {
        Intent intent =new Intent(this, PlayerServices.class);
        if( mPlayerConnection ==null){
            mPlayerConnection =new PlayerConnection();
        }

        bindService(intent,mPlayerConnection,BIND_AUTO_CREATE);
    }
    private  class PlayerConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
             mIPlayControl=(IPlayControl)service;
            mIPlayControl.registerViewControllerr(mIPlayerViewControl);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIPlayControl=null;
        }
    }

    private void initEvent() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isUserTouchProgressBar=true;

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isUserTouchProgressBar=false;
                int touchProgress=seekBar.getProgress();
                if(mIPlayControl!=null){
                    mIPlayControl.seekTo(seekBar.getProgress());
                }

            }
        });
        mPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIPlayControl!=null){
                    mIPlayControl.playOrPause();
                }
            }
        });
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIPlayControl!=null) {
                    mIPlayControl.stopPlay();
                }
            }
        });
    }


    private void initView() {
        mSeekBar=(SeekBar)this.findViewById(R.id.seekbar_bar);
        mPlayOrPause =(Button)this.findViewById(R.id.play_pause_btn);
        mClose=(Button)this.findViewById(R.id.close_btn);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPlayerConnection!=null){
            mIPlayControl.unregisterViewController();
           unbindService(mPlayerConnection);
        }
    }
    private IPlayerViewControl mIPlayerViewControl = new IPlayerViewControl() {
        @Override
        public void onPlayStateChange(int state) {
            //根据播放类型修改
            switch (state){
                case PLAY_STATE_PLAYER:
                    //修改按钮
                    mPlayOrPause.setText("暂停");/*本处未来变成圆形播放键*/
                    break;
                case PLAY_STATE_PAUSE:
                case PLAY_STATE_STOP:
                    mPlayOrPause.setText("播放");
                    break;
            }
        }

        @Override
        public void onSeekChage(int seek) {
//改变播放进度，条件为，当用户的手触摸到进度条时不更新
            if(isUserTouchProgressBar){
                mSeekBar.setProgress(seek);
            }
        }
    };
}
