package ballidaku.wlkullu.myUtilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import ballidaku.wlkullu.R;

/**
 * Created by sharanpalsingh on 16/07/17.
 */

public class CommonMethods
{

    String TAG=CommonMethods.class.getSimpleName();

    public static CommonMethods instance = new CommonMethods();

    public static CommonMethods getInstance()
    {
        return instance;
    }


    /**
     * Converting dp to pixel
     */
    public int dpToPx(Context context, int dp)
    {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public void switchFragment(Context context, Fragment toWhere)
    {

        FragmentTransaction fragmentTransaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_body, toWhere);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }


    public void setImage(Context context, String imagePath, ImageView imageView)
    {
        //Picasso.with(context).load("file://"+imagePath) /*.resize(96, 96).centerCrop()*/.into(imageView);

        Log.e(TAG,"path "+imagePath);

        Glide.with(context)
                .load("file://" + imagePath.trim())
                .centerCrop()
                /*.placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)*/
                .into(imageView);




    }

    @SuppressLint("NewApi")
    public String getRealPathFromURI_API19(Context context, Uri uri, String imageVideo)
    {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Uri u;
        if (imageVideo.equals(MyConstants.IMAGE))
        {
            u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else
        {
            u = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }

        Cursor cursor = context.getContentResolver().query(u, column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst())
        {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }





}
