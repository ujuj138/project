package activitytest.example.com.atry.interfaces;

/**
 * Created by liwenxuan on 2019/3/18.
 */

public interface IPlayControl {

    //播放状态量
    int PLAY_STATE_PLAYER=1;
    int PLAY_STATE_PAUSE=2;
    int PLAY_STATE_STOP=3;

    void registerViewControllerr(IPlayerViewControl viewControlr);

    void unregisterViewController();

    void  playOrPause();



    void  stopPlay();

    void seekTo(int seek);
    //设置播放进度
}
