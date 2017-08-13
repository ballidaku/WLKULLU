package ballidaku.wlkullu.video;

import android.content.Context;
import android.util.Log;

import ballidaku.wlkullu.data.DataModel;
import ballidaku.wlkullu.data.UPacketFactory;
import ballidaku.wlkullu.utils.SharedPreferencesUtil;
import sunmi.ds.DSKernel;
/*import sunmi.ds.data.DataModel;
import sunmi.ds.data.UPacketFactory;
import sunmi.dsc.utils.SharedPreferencesUtil;*/

/**
 * 项目名称：DSC
 * 类描述：
 * 创建人：Abtswiath丶lxy
 * 创建时间：2016/10/19 10:43
 * 修改人：longx
 * 修改时间：2016/10/19 10:43
 * 修改备注：
 */
public class SendPlayVideo {

    private Context mContext;
    private DSKernel mDSKernel;
    private long taskId_sendVideo;
    private String cacheKey;
    private ISendCallback iSendCallback;
    private PlayIng playIng;

    public SendPlayVideo(Context mContext, DSKernel mDSKernel, String cacheKey, ISendCallback iSendCallback, PlayIng playIng){
        this.mContext = mContext;
        this.mDSKernel = mDSKernel;
        this.cacheKey = cacheKey;
        this.iSendCallback = iSendCallback;
        this.playIng = playIng;
    }


    public void sendVideoFile(String videoPath){
        taskId_sendVideo = SharedPreferencesUtil.getLong(mContext, cacheKey);
        /*if (taskId_sendVideo != -1L) {
            showVideo();
            return;
        }*/


       // Log.e("Inside",""+videoPath);

//        taskId_sendVideo = mDSKernel.sendFile(DSKernel.getDSDPackageName(), Environment.getExternalStorageDirectory().getPath() + "/video.mp4", new sunmi.ds.callback.ISendCallback() {
        taskId_sendVideo = mDSKernel.sendFile(DSKernel.getDSDPackageName(), /*Environment.getExternalStorageDirectory().getPath() + */videoPath, new sunmi.ds.callback.ISendCallback() {
            @Override
            public void onSendSuccess(long l) {
                iSendCallback.onSendSuccess(l);
                SharedPreferencesUtil.put( mContext, cacheKey, taskId_sendVideo);
                showVideo();
            }

            @Override
            public void onSendFail(int i, String s) {
                iSendCallback.onSendFail(i,s);
            }

            @Override
            public void onSendProcess(final long l,final long l1) {
                iSendCallback.onSendProcess(l,l1);
            }
        });
    }


    private void showVideo(){
        String json = UPacketFactory.createJson(DataModel.VIDEO,"");

        Log.e("json",""+json);

        mDSKernel.sendCMD(DSKernel.getDSDPackageName(), json, taskId_sendVideo, null);
        playIng.playing();
    }

    public interface ISendCallback{
        void onSendProcess(final long l, final long l1);
        void onSendFail(int i, String s);
        void onSendSuccess(long l);
    }

    public interface PlayIng{
        void playing();
    }

}
