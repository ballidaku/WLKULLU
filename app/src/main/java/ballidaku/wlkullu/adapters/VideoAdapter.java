package ballidaku.wlkullu.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import ballidaku.wlkullu.R;
import ballidaku.wlkullu.myUtilities.CommonMethods;

/**
 * Created by sharanpalsingh on 26/07/17.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder>
{

    private String LOG_TAG = "VideoAdapter";

    private ArrayList<String> arrayList;


    private VideoAdapter.MyClickListener myClickListener;

    private Context context;

    public VideoAdapter(Context context,ArrayList<String> arrayList)
    {
        this.arrayList = arrayList;
        this.context = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {

        ImageView imageView;


        public ViewHolder(final View itemView)
        {

            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    myClickListener.onItemClick(getAdapterPosition(),itemView);
                }
            });
        }
    }

    public interface MyClickListener
    {
        public void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(VideoAdapter.MyClickListener myClickListener)
    {
        this.myClickListener = myClickListener;
    }

    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_video_item, parent, false);

        return new VideoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoAdapter.ViewHolder holder, int position)
    {
        CommonMethods.getInstance().setImage(context,arrayList.get(position),holder.imageView);
    }

    @Override
    public int getItemCount()
    {
        return arrayList.size();
    }


    public String getVideoPath(int position)
    {
       return arrayList.get(position);
    }


}