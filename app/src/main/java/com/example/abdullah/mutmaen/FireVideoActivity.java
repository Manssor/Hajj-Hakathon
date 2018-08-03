package com.example.abdullah.mutmaen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import java.util.Random;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

public class FireVideoActivity extends AppCompatActivity {

    private RtcEngine mRtcEngine;
    private IRtcEngineEventHandler mRtcEventHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_video);
        mRtcEventHandler = new IRtcEngineEventHandler() {


            @Override
            public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
                Log.i("uid video",uid+"");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setupRemoteVideo(uid);
                    }
                });
            }


        };
        initializeAgoraEngine();
    }

    private void initializeAgoraEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), "a812dcaab57543b880d7fd329783726a", mRtcEventHandler);
            joinChannel();
            setupLocalVideo();
            setupVideoProfile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupVideoProfile() {
        mRtcEngine.enableVideo();
        mRtcEngine.setVideoProfile(Constants.VIDEO_PROFILE_360P, false);
    }

    private void setupLocalVideo() {
        FrameLayout container = (FrameLayout) findViewById(R.id.online_video_view_container);
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        surfaceView.setZOrderMediaOverlay(true);
        container.addView(surfaceView);
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_ADAPTIVE, 0));
        FrameLayout container2 = (FrameLayout) findViewById(R.id.local_video_view_container);
        SurfaceView surfaceView2 = RtcEngine.CreateRendererView(getBaseContext());
        surfaceView2.setZOrderMediaOverlay(true);
        container2.addView(surfaceView2);
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView2, VideoCanvas.RENDER_MODE_FIT, 0));
    }

    private void joinChannel() {
        mRtcEngine.joinChannel(null, "aye", "Extra Optional Data", new Random().nextInt(10000000)+1); // if you do not specify the uid, Agora will assign one.
    }

    private void setupRemoteVideo(int uid) {
        FrameLayout container = (FrameLayout) findViewById(R.id.remote_video_view_container);

        if (container.getChildCount() >= 1) {
            return;
        }

        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        container.addView(surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_ADAPTIVE, uid));
        surfaceView.setTag(uid);

    }

    private void leaveChannel() {
        mRtcEngine.leaveChannel();
    }
}
