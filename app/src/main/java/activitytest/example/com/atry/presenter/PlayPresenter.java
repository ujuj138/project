package activitytest.example.com.atry.presenter;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.util.Log;

import java.io.IOException;

import activitytest.example.com.atry.interfaces.IPlayControl;
import activitytest.example.com.atry.interfaces.IPlayerViewControl;

import static android.content.ContentValues.TAG;
import static android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE;

/**
 * Created by liwenxuan on 2019/3/19.
 */

public class PlayPresenter extends Binder implements IPlayControl {
    public String path;//放置路径

    private IPlayerViewControl mViewController;
    private int mCurrentState=PLAY_STATE_STOP;
    private  MediaPlayer mMediaPlayer;
    @Override
    public void registerViewControllerr(IPlayerViewControl viewControlr) {
        this.mViewController=viewControlr;

    }

    @Override
    public void unregisterViewController() {
        mViewController=null;

    }

    @Override
    public void playOrPause() {
        if(mCurrentState==PLAY_STATE_STOP){
            initPlayer();

            try{
                mMediaPlayer.setDataSource(path);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
                mCurrentState=PLAY_STATE_PLAYER;/*
        未来输入MP3的PATH或者api
        */}catch (IOException e){
                e.printStackTrace();
            }

        }else  if(mCurrentState==PLAY_STATE_PLAYER){
            //当前播放时暂停
            if(mMediaPlayer!=null){
                mMediaPlayer.pause();
                mCurrentState=PLAY_STATE_PAUSE;

            }
        }else  if(mCurrentState==PLAY_STATE_PAUSE){
            if(mMediaPlayer!=null){
                mMediaPlayer.start();
                mCurrentState=PLAY_STATE_PLAYER;
            }
        }
        if(mViewController!=null) {
            mViewController.onPlayStateChange(mCurrentState);
        }
    }

    private void initPlayer() {
        if(mMediaPlayer!=null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //数据源
        }
    }

    @Override
    public void stopPlay() {
        if(mMediaPlayer!=null){
            mMediaPlayer.stop();
            mCurrentState=PLAY_STATE_STOP;

            //更新UI
            if(mViewController!=null){
                mViewController.onPlayStateChange(mCurrentState);
            }
            mMediaPlayer.release();
            mMediaPlayer=null;
        }

    }

    @Override
    public void seekTo(int seek) {

    }
}
