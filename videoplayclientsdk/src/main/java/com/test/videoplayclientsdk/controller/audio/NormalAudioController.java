package com.test.videoplayclientsdk.controller.audio;

import android.annotation.TargetApi;
import android.media.AudioRecord;
import android.util.Log;

import com.test.videoplayclientsdk.configuration.AudioConfiguration;
import com.test.videoplayclientsdk.audio.AudioProcessor;
import com.test.videoplayclientsdk.audio.AudioUtils;
import com.test.videoplayclientsdk.audio.OnAudioEncodeListener;
import com.test.videoplayclientsdk.constant.SopCastConstant;
import com.test.videoplayclientsdk.utils.SopCastLog;

/**
 * @Title: NormalAudioController
 * @Package com.laifeng.sopcastsdk.controller.audio
 * @Description:
 * @Author Jim
 * @Date 16/9/14
 * @Time 下午12:53
 * @Version
 */
public class NormalAudioController implements IAudioController {
    private OnAudioEncodeListener mListener;
    private AudioRecord mAudioRecord;
    private AudioProcessor mAudioProcessor;
    private boolean mMute;
    private AudioConfiguration mAudioConfiguration;

    public NormalAudioController() {
        mAudioConfiguration = AudioConfiguration.createDefault();
    }

    public void setAudioConfiguration(AudioConfiguration audioConfiguration) {
        mAudioConfiguration = audioConfiguration;
    }

    public void setAudioEncodeListener(OnAudioEncodeListener listener) {
        mListener = listener;
    }

    public void start() {
        SopCastLog.d(SopCastConstant.TAG, "Audio Recording start");
        mAudioRecord = AudioUtils.getAudioRecord(mAudioConfiguration);
        try {
            mAudioRecord.startRecording();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAudioProcessor = new AudioProcessor(mAudioRecord, mAudioConfiguration);
        mAudioProcessor.setAudioHEncodeListener(mListener);
        mAudioProcessor.start();
        mAudioProcessor.setMute(mMute);
    }

    public void stop() {
        SopCastLog.d(SopCastConstant.TAG, "Audio Recording stop");
        if(mAudioProcessor != null) {
            mAudioProcessor.stopEncode();
        }
        if(mAudioRecord != null) {
            try {
                mAudioRecord.stop();
                mAudioRecord.release();
                mAudioRecord = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void pause() {
        SopCastLog.d(SopCastConstant.TAG, "Audio Recording pause");
        try {
            if (mAudioRecord != null) {
                mAudioRecord.stop();
            }
            if (mAudioProcessor != null) {
                mAudioProcessor.pauseEncode(true);
            }
        }catch (Exception e){
            Log.e("NormalAudioController","" + e.toString());
        }
    }

    public void resume() {
        SopCastLog.d(SopCastConstant.TAG, "Audio Recording resume");
        if(mAudioRecord != null) {
            mAudioRecord.startRecording();
        }
        if (mAudioProcessor != null) {
            mAudioProcessor.pauseEncode(false);
        }
    }

    public void mute(boolean mute) {
        SopCastLog.d(SopCastConstant.TAG, "Audio Recording mute: " + mute);
        mMute = mute;
        if(mAudioProcessor != null) {
            mAudioProcessor.setMute(mMute);
        }
    }

    @Override
    @TargetApi(16)
    public int getSessionId() {
        if(mAudioRecord != null) {
            return mAudioRecord.getAudioSessionId();
        } else {
            return -1;
        }
    }
}
