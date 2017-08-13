package ballidaku.wlkullu.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ballidaku.wlkullu.R;
import ballidaku.wlkullu.myUtilities.CommonMethods;
import ballidaku.wlkullu.myUtilities.MyDialogs;

/**
 * Created by sharanpalsingh on 25/07/17.
 */

public class HorizontalListViewAdapter extends BaseAdapter
{
    LayoutInflater inflter;

    List<String> list;

    Context context;

    HorizontalListViewAdapter(Context context, List<String> list)
    {
        this.list = list;
        this.context = context;
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
    public View getView(final int position, final View convertView, ViewGroup parent)
    {


        View view = inflter.inflate(R.layout.custom_main_images, parent, false);
        ImageView imageViewGallery = (ImageView) view.findViewById(R.id.imageViewGallery);

        //Log.e("NAv",""+list.get(position));

        FrameLayout linearLayout=(FrameLayout) view.findViewById(R.id.linearLayout);

        TextView textView = (TextView)view.findViewById(R.id.textView);

        ViewGroup.LayoutParams params = linearLayout.getLayoutParams();

        // Set the width of TextView widget (item of GridView)
        //params.width = CommonMethods.getInstance().dpToPx(context, 300);

        // Set the TextView height (GridView item/row equal height)
        params.height = CommonMethods.getInstance().dpToPx(context, 200);

        // Set the TextView layout parameters
        linearLayout.setLayoutParams(params);

        textView.setText("PATH"+list.get(position).trim());

        imageViewGallery.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.e("Clicked Image ",""+list.get(position));


                MyDialogs.getInstance().zoomableImageViewDialog(context,list.get(position));
            }
        });



        CommonMethods.getInstance().setImage(context, list.get(position).trim(), imageViewGallery);

        return view;
    }
}