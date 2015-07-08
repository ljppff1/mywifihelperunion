
package com.example.videotest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.webdesign688.shot360.R;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class SurfaceViewTestActivity extends Activity implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, OnBufferingUpdateListener,
        OnClickListener {

    /**
     *
     */
    private SurfaceView surfaceView;

    /**
     * surfaceView閹绢厽鏂侀幒褍鍩�
     */
    private SurfaceHolder surfaceHolder;

    /**
     * 閹绢厽鏂侀幒褍鍩楅弶锟�
     */
    private SeekBar seekBar;

    /**
     * 閺嗗倸浠犻幘顓熸杹閹稿鎸�
     */
    private Button playButton;

    /**
     * 闁插秵鏌婇幘顓熸杹閹稿鎸�
     */
    private Button replayButton;

    /**
     * 閹搭亜娴橀幐澶愭尦
     */
    private Button screenShotButton;

    /**
     * 閺�鐟板綁鐟欏棝顣舵径褍鐨琤utton
     */
    private Button videoSizeButton;

    /**
     * 閸旂姾娴囨潻娑樺閺勫墽銇氶弶锟�
     */
    private ProgressBar progressBar;

    /**
     * 閹绢厽鏂佺憴鍡涱暥
     */
    private MediaPlayer mediaPlayer;

    /**
     * 鐠佹澘缍嶈ぐ鎾冲閹绢厽鏂侀惃鍕秴缂冿拷
     */
    private int playPosition = -1;

    /**
     * seekBar閺勵垰鎯侀懛顏勫З閹锋牕濮�
     */
    private boolean seekBarAutoFlag = false;

    /**
     * 鐟欏棝顣堕弮鍫曟？閺勫墽銇�
     */
    private TextView vedioTiemTextView;

    /**
     * 閹绢厽鏂侀幀缁樻闂傦拷
     */
    private String videoTimeString;

    private long videoTimeLong;

    /**
     * 閹绢厽鏂佺捄顖氱窞
     */
    private String pathString;

    /**
     * 鐏炲繐绠烽惃鍕啍鎼达箑鎷版妯哄
     */
    private int screenWidth, screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_view_test);
        // 閼惧嘲褰囩仦蹇撶閻ㄥ嫬顔旀惔锕�鎷版妯哄
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        initViews();
    }

    public void initViews() {
        String path = null;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 鐎涙ê婀懢宄板絿婢舵牠鍎撮弬鍥︽鐠侯垰绶�
            path = Environment.getExternalStorageDirectory().getPath();
        } else {
            // 娑撳秴鐡ㄩ崷銊ㄥ箯閸欐牕鍞撮柈銊ョ摠閸岋拷
            path = Environment.getDataDirectory().getPath();
        }
        
        pathString =getIntent().getStringExtra("PATHSTRING");
        // 閸掓繂顬婇崠鏍ㄥ付娴狅拷
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        playButton = (Button) findViewById(R.id.button_play);
        replayButton = (Button) findViewById(R.id.button_replay);
        vedioTiemTextView = (TextView) findViewById(R.id.textView_showTime);
        screenShotButton = (Button) findViewById(R.id.button_screenShot);
        videoSizeButton = (Button) findViewById(R.id.button_videoSize);
        // 鐠佸墽鐤唖urfaceHolder
        surfaceHolder = surfaceView.getHolder();
        // 鐠佸墽鐤咹older缁鐎�,鐠囥儳琚崹瀣�冪粈绨妘rfaceView閼奉亜绻佹稉宥囶吀閻炲棛绱︾�涙ê灏�,閾忕晫鍔ч幓鎰仛鏉╁洦妞傞敍灞肩稻閺堬拷婵傚�熺箷閺勵垵顪呯拋鍓х枂
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 鐠佸墽鐤唖urface閸ョ偠鐨�
        surfaceHolder.addCallback(new SurfaceCallback());

    }

    // SurfaceView閻ㄥ垻allBack
    private class SurfaceCallback implements SurfaceHolder.Callback {
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        public void surfaceCreated(SurfaceHolder holder) {
            // surfaceView鐞氼偄鍨卞锟�
            // 鐠佸墽鐤嗛幘顓熸杹鐠у嫭绨�
            playVideo();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // surfaceView闁匡拷濮ｏ拷,閸氬本妞傞柨锟藉В涔礶diaPlayer
            if (null != mediaPlayer) {
                mediaPlayer.release();
                mediaPlayer = null;
            }

        }

    }

    /**
     * 閹绢厽鏂佺憴鍡涱暥
     */
    public void playVideo() {
        // 閸掓繂顬婇崠鏈歟diaPlayer
        mediaPlayer = new MediaPlayer();
        // 闁插秶鐤唌ediaPaly,瀵ら缚顔呴崷銊ュ灥婵绮ediaplay缁斿宓嗙拫鍐暏閵嗭拷
        mediaPlayer.reset();
        // 鐠佸墽鐤嗘竟浼寸叾閺佸牊鐏�
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        // 鐠佸墽鐤嗛幘顓熸杹鐎瑰本鍨氶惄鎴濇儔
        mediaPlayer.setOnCompletionListener(this);
        // 鐠佸墽鐤嗘刊鎺嶇秼閸旂姾娴囩�瑰本鍨氭禒銉ユ倵閸ョ偠鐨熼崙鑺ユ殶閵嗭拷
        mediaPlayer.setOnPreparedListener(this);
        // 闁挎瑨顕ら惄鎴濇儔閸ョ偠鐨熼崙鑺ユ殶
        mediaPlayer.setOnErrorListener(this);
        // 鐠佸墽鐤嗙紓鎾崇摠閸欐ê瀵查惄鎴濇儔
        mediaPlayer.setOnBufferingUpdateListener(this);
        Uri uri = Uri.parse("http://192.168.1.5:8080/");
        try {
            // mediaPlayer.reset();
            mediaPlayer.setDataSource(pathString);
            // mediaPlayer.setDataSource(this, uri);
            // mediaPlayer.setDataSource(SurfaceViewTestActivity.this, uri);
            // 鐠佸墽鐤嗗鍌涱劄閸旂姾娴囩憴鍡涱暥閿涘苯瀵橀幏顑胯⒈缁夊秵鏌熷锟� prepare()閸氬本顒為敍瀹瞨epareAsync()瀵倹顒�
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "閸旂姾娴囩憴鍡涱暥闁挎瑨顕ら敍锟�", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 鐟欏棝顣堕崝鐘烘祰鐎瑰本鐦惄鎴濇儔
     * 
     * @param mp
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        // 瑜版捁顬呮０鎴濆鏉炶棄鐣В鏇氫簰閸氬函绱濋梾鎰閸旂姾娴囨潻娑樺閺夛拷
        progressBar.setVisibility(View.GONE);
        // 閸掋倖鏌囬弰顖氭儊閺堝绻氱�涙娈戦幘顓熸杹娴ｅ秶鐤�,闂冨弶顒涚仦蹇撶閺冨娴嗛弮璁圭礉閻ｅ矂娼扮悮顐﹀櫢閺傜増鐎鐚寸礉閹绢厽鏂佹担宥囩枂娑撱垹銇戦妴锟�
        if (Constants.playPosition >= 0) {
            mediaPlayer.seekTo(Constants.playPosition);
            Constants.playPosition = -1;
            // surfaceHolder.unlockCanvasAndPost(Constants.getCanvas());
        }
        seekBarAutoFlag = true;
        // 鐠佸墽鐤嗛幒褍鍩楅弶锟�,閺�鎯ф躬閸旂姾娴囩�瑰本鍨氭禒銉ユ倵鐠佸墽鐤嗛敍宀勬Щ濮濄垼骞忛崣鏉噀tDuration()闁挎瑨顕�
        seekBar.setMax(mediaPlayer.getDuration());
        // 鐠佸墽鐤嗛幘顓熸杹閺冨爼妫�
        videoTimeLong = mediaPlayer.getDuration();
        videoTimeString = getShowTime(videoTimeLong);
        vedioTiemTextView.setText("00:00:00/" + videoTimeString);
        // 鐠佸墽鐤嗛幏鏍уЗ閻╂垵鎯夋禍瀣╂
        seekBar.setOnSeekBarChangeListener(new SeekBarChangeListener());
        // 鐠佸墽鐤嗛幐澶愭尦閻╂垵鎯夋禍瀣╂
        // 闁插秵鏌婇幘顓熸杹
        replayButton.setOnClickListener(SurfaceViewTestActivity.this);
        // 閺嗗倸浠犻崪灞炬尡閺�锟�
        playButton.setOnClickListener(SurfaceViewTestActivity.this);
        // 閹搭亜娴橀幐澶愭尦
        screenShotButton.setOnClickListener(SurfaceViewTestActivity.this);
        // 鐟欏棝顣舵径褍鐨�
        videoSizeButton.setOnClickListener(SurfaceViewTestActivity.this);
        // 閹绢厽鏂佺憴鍡涱暥
        mediaPlayer.start();
        // 鐠佸墽鐤嗛弰鍓с仛閸掓澘鐫嗛獮锟�
        mediaPlayer.setDisplay(surfaceHolder);
        // 瀵拷閸氼垳鍤庣粙锟� 閸掗攱鏌婃潻娑樺閺夛拷
        new Thread(runnable).start();
        // 鐠佸墽鐤唖urfaceView娣囨繃瀵旈崷銊ョ潌楠炴洑绗�
        mediaPlayer.setScreenOnWhilePlaying(true);
        surfaceHolder.setKeepScreenOn(true);

    }

    /**
     * 濠婃垵濮╅弶鈥冲綁閸栨牜鍤庣粙锟�
     */
    private Runnable runnable = new Runnable() {

        public void run() {
            // TODO Auto-generated method stub
            // 婢х偛濮炵�电懓绱撶敮鍝ユ畱閹规洝骞忛敍宀勬Щ濮濄垹婀崚銈嗘焽mediaPlayer.isPlaying閻ㄥ嫭妞傞崐娆欑礉閹额櫐llegalStateException瀵倸鐖�
            try {
                while (seekBarAutoFlag) {
                    /*
                     * mediaPlayer娑撳秳璐熺粚杞扮瑬婢跺嫪绨锝呮躬閹绢厽鏂侀悩鑸碉拷浣规閿涘奔濞囨潻娑樺閺夆剝绮撮崝銊ｏ拷锟�
                     * 闁俺绻冮幐鍥х暰缁鎮曢惃鍕煙瀵繐鍨介弬鐠礶diaPlayer闂冨弶顒涢悩鑸碉拷浣稿絺閻㈢喍绗夋稉锟介懛锟�
                     */

                    if (null != SurfaceViewTestActivity.this.mediaPlayer
                            && SurfaceViewTestActivity.this.mediaPlayer.isPlaying()) {
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * seekBar閹锋牕濮╅惄鎴濇儔缁拷
     * 
     * @author shenxiaolei
     */
    @SuppressWarnings("unused")
    private class SeekBarChangeListener implements OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // TODO Auto-generated method stub
            if (progress >= 0) {
                // 婵″倹鐏夐弰顖滄暏閹撮攱澧滈崝銊﹀珛閸斻劍甯舵禒璁圭礉閸掓瑨顔曠純顔款瀰妫版垼鐑︽潪顑撅拷锟�
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
                // 鐠佸墽鐤嗚ぐ鎾冲閹绢厽鏂侀弮鍫曟？
                vedioTiemTextView.setText(getShowTime(progress) + "/" + videoTimeString);
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub

        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub

        }

    }

    /**
     * 閹稿鎸抽悙鐟板毊娴滃娆㈤惄鎴濇儔
     */
    public void onClick(View v) {
        // TODO Auto-generated method stub
        // 闁插秵鏌婇幘顓熸杹
        if (v == replayButton) {
            // mediaPlayer娑撳秶鈹栭敍灞藉灟閻╁瓨甯寸捄瀹犳祮
            if (null != mediaPlayer) {
                // MediaPlayer閸滃矁绻樻惔锔芥蒋闁�熺儲鏉烆剙鍩屽锟芥慨瀣╃秴缂冿拷
                mediaPlayer.seekTo(0);
                seekBar.setProgress(0);
                // 婵″倹鐏夋稉宥咁樀娴滃孩鎸遍弨鍓уЦ閹緤绱濋崚娆忕磻婵鎸遍弨锟�
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
            } else {
                // 娑撹櫣鈹栭崚娆撳櫢閺傛媽顔曠純鐢縠diaPlayer
                playVideo();
            }

        }
        // 閹绢厽鏂侀妴浣规畯閸嬫粍瀵滈柦锟�
        if (v == playButton) {
            if (null != mediaPlayer) {
                // 濮濓絽婀幘顓熸杹
                if (mediaPlayer.isPlaying()) {
                    Constants.playPosition = mediaPlayer.getCurrentPosition();
                    // seekBarAutoFlag = false;
                    mediaPlayer.pause();
                    playButton.setText("开始");
                } else {
                    if (Constants.playPosition >= 0) {
                        // seekBarAutoFlag = true;
                        mediaPlayer.seekTo(Constants.playPosition);
                        mediaPlayer.start();
                        playButton.setText("暂停");
                        Constants.playPosition = -1;
                    }
                }

            }
        }
        // 鐟欏棝顣堕幋顏勬禈
        if (v == screenShotButton) {
            if (null != mediaPlayer) {
                // 鐟欏棝顣跺锝呮躬閹绢厽鏂侀敍锟�
                if (mediaPlayer.isPlaying()) {
                    // 閼惧嘲褰囬幘顓熸杹娴ｅ秶鐤�
                    Constants.playPosition = mediaPlayer.getCurrentPosition();
                    // 閺嗗倸浠犻幘顓熸杹
                    mediaPlayer.pause();
                    //
                    playButton.setText("暂停");
                }
                // 鐟欏棝顣堕幋顏勬禈
                savaScreenShot(Constants.playPosition);
            } else {
                Toast.makeText(SurfaceViewTestActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        }
        if (v == videoSizeButton) {
            // 鐠嬪啰鏁ら弨鐟板綁婢堆冪毈閻ㄥ嫭鏌熷▔锟�
            changeVideoSize();
        }
    }

    /**
     * 閹绢厽鏂佺�瑰本鐦惄鎴濇儔
     * 
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        // 鐠佸墽鐤唖eeKbar鐠哄疇娴嗛崚鐗堟付閸氬簼缍呯純锟�
        seekBar.setProgress(Integer.parseInt(String.valueOf(videoTimeLong)));
        // 鐠佸墽鐤嗛幘顓熸杹閺嶅洩顔囨稉绡篴lse
        seekBarAutoFlag = false;
    }

    /**
     * 鐟欏棝顣剁紓鎾崇摠婢堆冪毈閻╂垵鎯�,瑜版捁顬呮０鎴炴尡閺�鍙ヤ簰閸氾拷 閸︹暞tarted閻樿埖锟戒椒绱扮拫鍐暏
     */
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        // TODO Auto-generated method stub
        // percent 鐞涖劎銇氱紓鎾崇摠閸旂姾娴囨潻娑樺閿涳拷0娑撶儤鐥呭锟芥慨瀣剁礉100鐞涖劎銇氶崝鐘烘祰鐎瑰本鍨氶敍灞芥躬閸旂姾娴囩�瑰本鍨氭禒銉ユ倵娑旂喍绱版稉锟介惄纾嬬殶閻劏顕氶弬瑙勭《
        Log.e("text", "onBufferingUpdate-->" + percent);
        // 閸欘垯浜掗弽瑙勫祦婢堆冪毈閻ㄥ嫬褰夐崠鏍ㄦ降
    }

    /**
     * 闁挎瑨顕ら惄鎴濇儔
     * 
     * @param mp
     * @param what
     * @param extra
     * @return
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Toast.makeText(this, "MEDIA_ERROR_UNKNOWN", Toast.LENGTH_SHORT).show();
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Toast.makeText(this, "MEDIA_ERROR_SERVER_DIED", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        switch (extra) {
            case MediaPlayer.MEDIA_ERROR_IO:
                Toast.makeText(this, "MEDIA_ERROR_IO", Toast.LENGTH_SHORT).show();
                break;
            case MediaPlayer.MEDIA_ERROR_MALFORMED:
                Toast.makeText(this, "MEDIA_ERROR_MALFORMED", Toast.LENGTH_SHORT).show();
                break;
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Toast.makeText(this, "MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK",
                        Toast.LENGTH_SHORT).show();
                break;
            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                Toast.makeText(this, "MEDIA_ERROR_TIMED_OUT", Toast.LENGTH_SHORT).show();
                break;
            case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                Toast.makeText(this, "MEDIA_ERROR_UNSUPPORTED", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    /**
     * 娴犲孩娈忛崑婊�鑵戦幁銏狀樉
     */
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // 閸掋倖鏌囬幘顓熸杹娴ｅ秶鐤�
        if (Constants.playPosition >= 0) {

            if (null != mediaPlayer) {
                seekBarAutoFlag = true;
                mediaPlayer.seekTo(Constants.playPosition);
                mediaPlayer.start();
            } else {
                playVideo();
            }

        }
    }

    /**
     * 妞ょ敻娼版径鍕艾閺嗗倸浠犻悩鑸碉拷锟�
     */
    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (null != mediaPlayer && mediaPlayer.isPlaying()) {
                Constants.playPosition = mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();
                seekBarAutoFlag = false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 閸欐垹鏁撶仦蹇撶閺冨娴嗛弮鎯扮殶閻拷
     */
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        if (null != mediaPlayer) {
            // 娣囨繂鐡ㄩ幘顓熸杹娴ｅ秶鐤�
            Constants.playPosition = mediaPlayer.getCurrentPosition();
        }
    }

    /**
     * 鐏炲繐绠烽弮瀣祮鐎瑰本鍨氶弮鎯扮殶閻拷
     */
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onRestoreInstanceState(savedInstanceState);

    }
public void onConfigurationChanged(Configuration newConfig) {
    // TODO Auto-generated method stub
    super.onConfigurationChanged(newConfig);
}
    /**
     * 鐏炲繐绠烽柨锟藉В浣规鐠嬪啰鏁�
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 閻㈠彉绨琈ediaPlay闂堢偛鐖堕崡鐘垫暏鐠у嫭绨敍灞惧娴犮儱缂撶拋顔肩潌楠炴洖缍嬮崜宄歝tivity闁匡拷濮ｄ焦妞傞敍灞藉灟閻╁瓨甯撮柨锟藉В锟�
        try {
            if (null != SurfaceViewTestActivity.this.mediaPlayer) {
                // 閹绘劕澧犻弽鍥х箶娑撶alse,闂冨弶顒涢崷銊瀰妫版垵浠犲銏℃閿涘瞼鍤庣粙瀣╃矝閸︺劏绻嶇悰灞伙拷锟�
                seekBarAutoFlag = false;
                // 婵″倹鐏夊锝呮躬閹绢厽鏂侀敍灞藉灟閸嬫粍顒涢妴锟�
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                Constants.playPosition = -1;
                // 闁插﹥鏂乵ediaPlayer
                SurfaceViewTestActivity.this.mediaPlayer.release();
                SurfaceViewTestActivity.this.mediaPlayer = null;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 鏉烆剚宕查幘顓熸杹閺冨爼妫�
     * 
     * @param milliseconds 娴肩姴鍙嗗В顐ゎ瀾閸婏拷
     * @return 鏉╂柨娲� hh:mm:ss閹存潟m:ss閺嶇厧绱￠惃鍕殶閹癸拷
     */
    @SuppressLint("SimpleDateFormat")
    public String getShowTime(long milliseconds) {
        // 閼惧嘲褰囬弮銉ュ坊閸戣姤鏆�
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        SimpleDateFormat dateFormat = null;
        // 閸掋倖鏌囬弰顖氭儊婢堆傜艾60閸掑棝鎸撻敍灞筋渾閺嬫粌銇囨禍搴℃皑閺勫墽銇氱亸蹇旀閵嗗倽顔曠純顔芥）閺堢喐鐗稿锟�
        if (milliseconds / 60000 > 60) {
            dateFormat = new SimpleDateFormat("hh:mm:ss");
        } else {
            dateFormat = new SimpleDateFormat("mm:ss");
        }
        return dateFormat.format(calendar.getTime());
    }

    /**
     * 娣囨繂鐡ㄧ憴鍡涱暥閹搭亜娴�.鐠囥儲鏌熷▔鏇炲涧閼宠姤鏁幐浣规拱閸︽媽顬呮０鎴炴瀮娴狅拷
     * 
     * @param time鐟欏棝顣惰ぐ鎾冲娴ｅ秶鐤�
     */
    public void savaScreenShot(long time) {
        // 閺嶅洩顔囬弰顖氭儊娣囨繂鐡ㄩ幋鎰
        boolean isSave = false;
        // 閼惧嘲褰囬弬鍥︽鐠侯垰绶�
        String path = null;
        // 閺傚洣娆㈤崥宥囆�
        String fileName = null;
        if (time >= 0) {
            try {
                Uri uri = Uri
                        .parse("http://192.168.1.5:8080/");
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(pathString);
                // 閼惧嘲褰囩憴鍡涱暥閻ㄥ嫭鎸遍弨鐐拷缁樻闂�鍨礋娴ｅ秳璐熷В顐ゎ瀾
                String timeString = mediaMetadataRetriever
                        .extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                // 鏉烆剚宕查弽鐓庣础娑撳搫浜曠粔锟�
                long timelong = Long.parseLong(timeString) * 1000;
                // 鐠侊紕鐣昏ぐ鎾冲鐟欏棝顣堕幋顏勫絿閻ㄥ嫪缍呯純锟�
                long index = (time * timelong) / mediaPlayer.getDuration();
                // 閼惧嘲褰囪ぐ鎾冲鐟欏棝顣堕幐鍥х暰娴ｅ秶鐤嗛惃鍕焻閸ワ拷,閺冨爼妫块崣鍌涙殶閻ㄥ嫬宕熸担宥嗘Ц瀵邦喚顬�,閸嬫矮绨�*1000婢跺嫮鎮�
                // 缁楊兛绨╂稉顏勫棘閺侀璐熼幐鍥х暰娴ｅ秶鐤嗛敍灞惧壈閹繃甯存潻鎴犳畱娴ｅ秶鐤嗛幋顏勬禈
                Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(time * 1000,
                        MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                // 闁插﹥鏂佺挧鍕爱
                mediaMetadataRetriever.release();
                // 閸掋倖鏌囨径鏍劥鐠佹儳顦窼D閸椻剝妲搁崥锕�鐡ㄩ崷锟�
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 鐎涙ê婀懢宄板絿婢舵牠鍎撮弬鍥︽鐠侯垰绶�
                    path = Environment.getExternalStorageDirectory().getPath();
                } else {
                    // 娑撳秴鐡ㄩ崷銊ㄥ箯閸欐牕鍞撮柈銊ョ摠閸岋拷
                    path = Environment.getDataDirectory().getPath();
                }
                // 鐠佸墽鐤嗛弬鍥︽閸氬秶袨 閿涘奔浜掓禍瀣╂濮ｎ偆顬戞稉鍝勬倳缁夛拷
                fileName = Calendar.getInstance().getTimeInMillis() + ".jpg";
                // 鐠佸墽鐤嗘穱婵嗙摠閺傚洣娆�
                File file = new File(path + "/shen/" + fileName);

                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                bitmap.compress(CompressFormat.JPEG, 100, fileOutputStream);
                isSave = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 娣囨繂鐡ㄩ幋鎰娴犮儱鎮楅敍灞界潔缁�鍝勬禈閻楋拷
            if (isSave) {
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT));
                imageView.setImageBitmap(BitmapFactory.decodeFile(path + "/shen/" + fileName));
                new AlertDialog.Builder(this).setView(imageView).show();
            }
        }

    }

    /**
     * 閺�鐟板綁鐟欏棝顣堕惃鍕▔缁�鍝勩亣鐏忓骏绱濋崗銊ョ潌閿涘瞼鐛ラ崣锝忕礉閸愬懎顔�
     */
    public void changeVideoSize() {
        // 閺�鐟板綁鐟欏棝顣舵径褍鐨�
        String videoSizeString = videoSizeButton.getText().toString();
        // 閼惧嘲褰囩憴鍡涱暥閻ㄥ嫬顔旀惔锕�鎷版妯哄
        int width = mediaPlayer.getVideoWidth();
        int height = mediaPlayer.getVideoHeight();
        // 婵″倹鐏夐幐澶愭尦閺傚洤鐡ф稉铏圭崶閸欙絽鍨拋鍓х枂娑撹櫣鐛ラ崣锝喣佸锟�
        if ("暂停".equals(videoSizeString)) {
            /*
             * 婵″倹鐏夋稉鍝勫弿鐏炲繑膩瀵繐鍨弨閫涜礋闁倸绨查崘鍛啇閻ㄥ嫸绱濋崜宥嗗絹閺勵垵顬呮０鎴濐啍妤傛ê鐨禍搴＄潌楠炴洖顔旀姗堢礉婵″倹鐏夋径褌绨�逛粙鐝� 閹存垳婊戠憰浣镐粵缂傗晜鏂�
             * 婵″倹鐏夌憴鍡涱暥閻ㄥ嫬顔旀妯哄閺堝绔撮弬閫涚瑝濠娐ゅ喕閹存垳婊戠亸杈渽鏉╂稖顢戠紓鈺傛杹. 婵″倹鐏夌憴鍡涱暥閻ㄥ嫬銇囩亸蹇涘厴濠娐ゅ喕鐏忚京娲块幒銉啎缂冾喖鑻熺仦鍛厬閺勫墽銇氶妴锟�
             */
            if (width > screenWidth || height > screenHeight) {
                // 鐠侊紕鐣婚崙鍝勵啍妤傛娈戦崐宥嗘殶
                float vWidth = (float) width / (float) screenWidth;
                float vHeight = (float) height / (float) screenHeight;
                // 閼惧嘲褰囬張锟芥径褏娈戦崐宥嗘殶閸婄》绱濋幐澶娿亣閺佹澘锟借壈绻樼悰宀�缂夐弨锟�
                float max = Math.max(vWidth, vHeight);
                // 鐠侊紕鐣婚崙铏圭級閺�鎯с亣鐏忥拷,閸欐牗甯存潻鎴犳畱濮濓絽锟斤拷
                width = (int) Math.ceil((float) width / max);
                height = (int) Math.ceil((float) height / max);
            }
            // 鐠佸墽鐤哠urfaceView閻ㄥ嫬銇囩亸蹇撹嫙鐏炲懍鑵戦弰鍓с仛
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width,
                    height);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            surfaceView.setLayoutParams(layoutParams);
            videoSizeButton.setText("开始");
        } else if ("开始".equals(videoSizeString)) {
            // 鐠佸墽鐤嗛崗銊ョ潌
            // 鐠佸墽鐤哠urfaceView閻ㄥ嫬銇囩亸蹇撹嫙鐏炲懍鑵戦弰鍓с仛
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(screenWidth,
                    screenHeight);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            surfaceView.setLayoutParams(layoutParams);
            videoSizeButton.setText("暂停");
        }
    }
}
