package ballidaku.wlkullu.mainScreens.activities;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

import ballidaku.wlkullu.R;
import ballidaku.wlkullu.adapters.VideoAdapter;
import ballidaku.wlkullu.data.UPacketFactory;
import ballidaku.wlkullu.myUtilities.CommonMethods;
import ballidaku.wlkullu.myUtilities.GridSpacingItemDecoration;
import ballidaku.wlkullu.utils.SharedPreferencesUtil;
import ballidaku.wlkullu.video.SendPlayVideo;
import sunmi.ds.DSKernel;
import sunmi.ds.callback.ICheckFileCallback;
import sunmi.ds.callback.IConnectionCallback;
import sunmi.ds.callback.IReceiveCallback;
import sunmi.ds.callback.ISendCallback;
import sunmi.ds.data.DSData;
import sunmi.ds.data.DSFile;
import sunmi.ds.data.DSFiles;
import sunmi.ds.data.DataPacket;

public class VideoActivity extends AppCompatActivity /*implements View.OnClickListener*/
{

    Context context;

    //private Button play;
    private DSKernel mDSKernel;
    private long taskId_sendVideo;
    private TextView state;
    private final static String SHOW_VIDEO_ID = "SHOW_VIDEO_ID";
    private SendPlayVideo mSendPlayVideo;



    RecyclerView recyclerViewVideos;
    VideoAdapter videoAdapter;


    DataPacket dsPacket;
    JSONObject jsonObject;

    //String videoPath="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        context =this;

        initSDK();

        setUpViews();

        initJson();
    }

    private void initJson() {
        jsonObject = new JSONObject();
        try {
            jsonObject.put("title", "WILDLIFE DIVISION KULLU");
            jsonObject.put("content", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setUpViews()
    {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_back);


        findViewById(R.id.textViewStop).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dsPacket = UPacketFactory.buildShowText(
                        DSKernel.getDSDPackageName(), jsonObject.toString(), callback);
                mDSKernel.sendData(dsPacket);
            }
        });

        recyclerViewVideos=(RecyclerView)findViewById(R.id.recyclerViewVideos) ;

        videoAdapter= new VideoAdapter(context,getAllMedia());


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 4);
        recyclerViewVideos.setLayoutManager(mLayoutManager);
        recyclerViewVideos.addItemDecoration(new GridSpacingItemDecoration(4, CommonMethods.getInstance().dpToPx(context, 10), true));
        recyclerViewVideos.setItemAnimator(new DefaultItemAnimator());
        recyclerViewVideos.setAdapter(videoAdapter);



        videoAdapter.setOnItemClickListener(new VideoAdapter.MyClickListener()
        {
            @Override
            public void onItemClick(int position, View v)
            {

               // Log.e("ANNN",videoAdapter.getVideoPath(position));
                mSendPlayVideo.sendVideoFile(videoAdapter.getVideoPath(position));
            }
        });




       // play = (Button) this.findViewById(R.id.play);
        state = (TextView) this.findViewById(R.id.state);
      //  play.setOnClickListener(this);
        mSendPlayVideo = new SendPlayVideo(this, mDSKernel, SHOW_VIDEO_ID, iSendCallback, new SendPlayVideo.PlayIng()
        {
            @Override
            public void playing()
            {
            }
        });

        ArrayList<String> a=getAllMedia();
        for (int i = 0; i <a.size() ; i++)
        {
            Log.e("Paths",""+a.get(i));
        }

    }

    public ArrayList<String> getAllMedia() {
        HashSet<String> videoItemHashSet = new HashSet<>();
        String[] projection = { MediaStore.Video.VideoColumns.DATA ,MediaStore.Video.Media.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        try {
            cursor.moveToFirst();
            do{
                videoItemHashSet.add((cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))));
            }while(cursor.moveToNext());

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<String> downloadedList = new ArrayList<>(videoItemHashSet);
        return downloadedList;
    }


    private void initSDK()
    {
        mDSKernel = DSKernel.newInstance();
        mDSKernel.init(this, mConnCallback);
        mDSKernel.addReceiveCallback(mReceiveCallback);

        mDSKernel.addConnCallback(iConnectionCallback);
    }

    @Override
    protected void onDestroy()
    {

        mDSKernel.onDestroy();

        super.onDestroy();
    }

    IConnectionCallback mConnCallback = new IConnectionCallback()
    {// SDK link status callback
        @Override
        public void onDisConnect()
        {

        }

        @Override
        public void onConnected(final ConnState state)
        {
            runOnUiThread(new Runnable()
            {

                @Override
                public void run()
                {
                    switch (state)
                    {
                        case AIDL_CONN:
                            //Connect with local service
                            break;
                        case VICE_SERVICE_CONN:
                            //And sub-screen service connection
                            break;
                        case VICE_APP_CONN:
                            //And sub-screen app connection smooth


                            break;

                        default:
                            break;
                    }

                }
            });

        }
    };

    IReceiveCallback mReceiveCallback = new IReceiveCallback()
    {//  Receive the callback of the secondary screen data

        @Override
        public void onReceiveFile(DSFile arg0)
        {

        }

        @Override
        public void onReceiveFiles(DSFiles dsFiles)
        {

        }

        @Override
        public void onReceiveData(DSData data)
        {

        }

        @Override
        public void onReceiveCMD(DSData arg0)
        {

        }
    };

    private IConnectionCallback iConnectionCallback = new IConnectionCallback()
    {
        @Override
        public void onDisConnect()
        {

        }

        @Override
        public void onConnected(final ConnState connState)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    switch (connState)
                    {
                        case VICE_SERVICE_CONN:
                            long fileId = SharedPreferencesUtil.getLong(VideoActivity.this, SHOW_VIDEO_ID);
                            if (fileId != -1L)
                                checkVideo(fileId, SHOW_VIDEO_ID);
                            break;
                    }
                }
            });
        }
    };

    private void checkVideo(long fileId, final String key)
    {

        mDSKernel.checkFileExist(fileId, new ICheckFileCallback()
        {
            @Override
            public void onCheckFail()
            {
                Toast.makeText(VideoActivity.this, key + " file does not exist", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResult(boolean b)
            {
                if (!b)
                {
                    SharedPreferencesUtil.put(VideoActivity.this, key, -1L);
                }
                else
                {
                    Toast.makeText(VideoActivity.this, key + "File exists", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private SendPlayVideo.ISendCallback iSendCallback = new SendPlayVideo.ISendCallback()
    {
        @Override
        public void onSendSuccess(long l)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    state.setText("Status : Success");
                }
            });
        }

        @Override
        public void onSendFail(int i, String s)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    state.setText("Status : Failure");
                }
            });

        }

        @Override
        public void onSendProcess(final long l, final long l1)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    state.setText("Status : Success      Current Progress： " + l1 + "  Total Progress： " + l);
                }
            });

        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:

                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private ISendCallback callback = new ISendCallback() {

        @Override
        public void onSendFail(int arg0, String arg1) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    state.setText("发送失败!");
                }
            });
        }

        @Override
        public void onSendProcess(long arg0, long arg1) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onSendSuccess(long arg0) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    state.setText("发送成功!");
                }
            });
        }

    };
}
