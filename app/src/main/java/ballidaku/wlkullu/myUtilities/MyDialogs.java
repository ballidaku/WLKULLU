package ballidaku.wlkullu.myUtilities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import ballidaku.wlkullu.R;

/**
 * Created by sharanpalsingh on 16/07/17.
 */

public class MyDialogs<T> implements View.OnClickListener
{

    public Dialog dialog;

    ImageView imageView;
    ImageView imageViewVideo;
    ImageView imageViewVideoPlay;
    // public String imagePath = null;

    // LinearLayout linear;

    public List<String> imagePathList;

    public String videoImagePath = "";

    Context context;


    HorizontalListView horizontalListView;

    public static MyDialogs instance = new MyDialogs();

    public static MyDialogs getInstance()
    {
        return instance;
    }

    public T addChangeValues(Context context, int i, String heading, String positiveButtonName, View.OnClickListener changeHeadingListener, View.OnClickListener onImageClickListener, View.OnClickListener onVideoClickListener)
    {

        this.context = context;

        dialog = new Dialog(context, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_main);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (i == 2)
        {
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        }
        else
        {
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.TOP);
        }

        LinearLayout linearlayoutButtons = (LinearLayout) dialog.findViewById(R.id.linearlayoutButtons);

        TextView textViewTitle = (TextView) dialog.findViewById(R.id.textViewTitle);
        EditText editText = (EditText) dialog.findViewById(R.id.editText);

        imageView = (ImageView) dialog.findViewById(R.id.imageView);
        imageView.setOnClickListener(onImageClickListener);

        imageViewVideo = (ImageView) dialog.findViewById(R.id.imageViewVideo);

        imageViewVideo.setOnClickListener(onVideoClickListener);

        imageViewVideoPlay = (ImageView) dialog.findViewById(R.id.imageViewVideoPlay);
        imageViewVideoPlay.setVisibility(View.GONE);

        //imagePath = null;
        imagePathList = null;
        videoImagePath = "";

        EditText editTextInfo = (EditText) dialog.findViewById(R.id.editTextInfo);

       /* HorizontalScrollView horizontal_scroll = (HorizontalScrollView) dialog.findViewById(R.id.horizontal_scroll);
        linear = (LinearLayout) dialog.findViewById(R.id.linear);*/

        horizontalListView = (HorizontalListView) dialog.findViewById(R.id.horizontalListView);
        horizontalListView.setVisibility(View.GONE);

        textViewTitle.setText(heading);

        dialog.findViewById(R.id.textViewNegative).setOnClickListener(this);
        TextView textViewPositive = (TextView) dialog.findViewById(R.id.textViewPositive);
        textViewPositive.setText(positiveButtonName);
        textViewPositive.setOnClickListener(changeHeadingListener);

        if (i == 1)
        {
            imageView.setVisibility(View.GONE);
            imageViewVideo.setVisibility(View.GONE);
            editTextInfo.setVisibility(View.GONE);
        }
        else
        {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 400);
            linearlayoutButtons.setLayoutParams(params);
        }


        dialog.show();

        EditText[] editTexts = {editText, editTextInfo};

        return (T) (i == 1 ? editTexts[0] : editTexts);

    }

    public void setImages(List<String> imagePathList)
    {
        this.imagePathList = imagePathList;
        // imageView.setVisibility(View.GONE);
        horizontalListView.setVisibility(View.VISIBLE);

        horizontalListView.setAdapter(new MyAdapter(imagePathList));

    }

    public void setVideoImage(Context context, String videoImagePath)
    {
        this.videoImagePath = videoImagePath;

        CommonMethods.getInstance().setImage(context, videoImagePath, imageViewVideo);

        imageViewVideoPlay.setVisibility(View.VISIBLE);
    }

    class MyAdapter extends BaseAdapter
    {
        LayoutInflater inflter;

        List<String> list;

        MyAdapter(List<String> list)
        {
            this.list = list;

            Log.e("list", "" + list.size());
            inflter = (LayoutInflater.from(context));
        }

        @Override
        public int getCount()
        {
            return list.size();
        }

        @Override
        public Object getItem(int position)
        {
            return null;
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {


            View view = inflter.inflate(R.layout.custom_image, parent, false);//set layout for displaying items
            ImageView imageViewGallery = (ImageView) view.findViewById(R.id.imageViewGallery);//get id for image view
           /* Bitmap bitmap = BitmapFactory.decodeFile(new File(list.get(position)).getAbsolutePath());

            imageViewGallery.setImageBitmap(bitmap);*/

            CommonMethods.getInstance().setImage(context, list.get(position), imageViewGallery);

            return view;
        }
    }




    /*public void setImage(Context context, String imagePath)
    {
        this.imagePath = imagePath;
        CommonMethods.getInstance().setImage(context, imagePath, imageView);
    }*/


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.textViewNegative:

                dialog.dismiss();

                break;


        }
    }


    public void zoomableImageViewDialog(Context context, String path)
    {
        dialog = new Dialog(context, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_zoomable_image);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);


        ZoomableImageView zoomableImageView = (ZoomableImageView) dialog.findViewById(R.id.imageViewZoomable);
        TextView textView = (TextView) dialog.findViewById(R.id.textView);


        File imgFile = new File(path.trim());
        if (imgFile.exists())
        {

            textView.setText("PATH" + imgFile);
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            zoomableImageView.setImageBitmap(bitmap);
        }

        //CommonMethods.getInstance().setImage(context, path, zoomableImageView);


        dialog.show();

    }


    public EditText checkPasswordDialog(final Context context, View.OnClickListener onClickListener)
    {
        dialog = new Dialog(context, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_password);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);


        final EditText editText = (EditText) dialog.findViewById(R.id.editText);

        TextView textViewNegative = (TextView) dialog.findViewById(R.id.textViewNegative);
        textViewNegative.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

        TextView textViewPositive = (TextView) dialog.findViewById(R.id.textViewPositive);
        textViewPositive.setOnClickListener(onClickListener);

        dialog.show();


        return editText;

    }


}
