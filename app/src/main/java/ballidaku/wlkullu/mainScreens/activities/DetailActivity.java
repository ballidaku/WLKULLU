package ballidaku.wlkullu.mainScreens.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ballidaku.wlkullu.R;
import ballidaku.wlkullu.adapters.DetailAdapter;
import ballidaku.wlkullu.dataModels.DetailModel;
import ballidaku.wlkullu.frontScreens.BaseActivity;
import ballidaku.wlkullu.myUtilities.CommonMethods;
import ballidaku.wlkullu.myUtilities.MyConstants;
import ballidaku.wlkullu.myUtilities.MyDatabase;
import ballidaku.wlkullu.myUtilities.MyDialogs;


/**
 * Created by brst-pc93 on 7/20/17.
 */

public class DetailActivity extends BaseActivity
{

    String TAG = DetailActivity.class.getSimpleName();

    Context context;


    Toolbar toolbar;

    TextView textViewTitle;

    public RecyclerView recyclerViewDetails;

    DetailAdapter detailAdapter;

    List<DetailModel> detailsModelArrayList = new ArrayList<>();

    String titleId;
    String childId;

    public MyDatabase myDatabase;


    public DetailActivity()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        context = this;

        titleId = getIntent().getStringExtra(MyConstants.TITLE_ID);
        childId = getIntent().getStringExtra(MyConstants.CHILD_ID);

        myDatabase = new MyDatabase(context);

        refreshList();

        setUpViews();
    }


    private void setUpViews()
    {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_back);


        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTitle.setText(myDatabase.getChildName(childId));



        recyclerViewDetails = (RecyclerView) findViewById(R.id.recyclerViewDetails);

       /* DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewDetails.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewDetails.addItemDecoration(dividerItemDecoration);*/


        detailAdapter = new DetailAdapter(context, detailsModelArrayList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerViewDetails.setLayoutManager(mLayoutManager);
        recyclerViewDetails.setItemAnimator(new DefaultItemAnimator());
        recyclerViewDetails.setAdapter(detailAdapter);


    }

    public void refreshList()
    {
        detailsModelArrayList=myDatabase.getAllDetailData(childId);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
    }

    EditText editTextHeading;
    EditText editTextInfo;

    View.OnClickListener onAddClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            String heading = editTextHeading.getText().toString().trim();

            String info = editTextInfo.getText().toString();
            Log.e(TAG,"info  "+info);

            MyDialogs.getInstance().dialog.dismiss();

            detailAdapter.addData(titleId,childId,heading, info, MyDialogs.getInstance().imagePathList,MyDialogs.getInstance().videoImagePath);
        }
    };

    View.OnClickListener onImageClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {

            Intent intent=new Intent(context,GalleryImages.class);
            startActivityForResult(intent,200);
        }
    };

    View.OnClickListener onVideoClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {

            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Video"),69);
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

            case R.id.add:

                EditText[] editText = (EditText[]) MyDialogs.getInstance().addChangeValues(context, 2, "Add Data", "ADD", onAddClickListener, onImageClickListener,onVideoClickListener);
                editTextHeading = editText[0];
                editTextInfo = editText[1];

                editTextHeading.setHint("Enter Heading...");

                return true;

           /* case R.id.addImage:

                Intent intent = new Intent();
                intent.setType("image*//*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);


                return true;

            case R.id.addVideo:

                Intent intent2 = new Intent();
                intent2.setType("video*//*");
                intent2.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent2, "Select Video"), 2);


                return true;*/


            case R.id.edit:

                detailAdapter.showEditIcon();

                return true;


            case R.id.delete:

                detailAdapter.showDeleteIcon();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (resultCode == RESULT_OK)
        {

            if (requestCode == 69)
            {
                Uri selectedImageUri = data.getData();

                Log.e("VideoPath", "" + CommonMethods.getInstance().getRealPathFromURI_API19(context, selectedImageUri, "Video"));

                MyDialogs.getInstance().setVideoImage(context, CommonMethods.getInstance().getRealPathFromURI_API19(context, selectedImageUri, "Video"));

            }
            else if (requestCode == 2)
            {
                Uri currImageURI = data.getData();

                Log.e("VideoUri", currImageURI + "");
                //Log.e("VideoPath", "" + getRealPathFromURI_API19(context, currImageURI, "Video"));

                //detailAdapter.addVideo(getRealPathFromURI_API19(context, currImageURI,"Video"));
                // detailAdapter.addVideo(currImageURI);

            }
            else if(requestCode == 200)
            {
                String images=data.getStringExtra(MyConstants.IMAGES);

                List<String> myList =Arrays.asList(images.replace("[","").replace("]","").split(","));

               // Log.e("ZZ",""+myList);

                MyDialogs.getInstance().setImages(myList);
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        int count = getSupportFragmentManager().getBackStackEntryCount();


        if (detailAdapter.myVisibilityDelete == 0 || detailAdapter.myVisibilityEdit == 0)
        {
            detailAdapter.doneEditDelete();
        }
        else if (count == 1)
        {
            finish();
        }
        else
        {
            super.onBackPressed();
        }

    }


}