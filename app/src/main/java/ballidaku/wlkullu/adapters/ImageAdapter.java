package ballidaku.wlkullu.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ballidaku.wlkullu.R;

/**
 * @author Paresh Mayani (@pareshmayani)
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder>
{

    private ArrayList<String> mImagesList;
    private Context mContext;
    private ArrayList<Boolean> isSelectedList;

    public ImageAdapter(Context context, ArrayList<String> imageList)
    {
        mContext = context;
        isSelectedList = new ArrayList<>();
        mImagesList = new ArrayList<String>();
        this.mImagesList = imageList;

        for (int i = 0; i < mImagesList.size() ; i++)
        {
            isSelectedList.add(false);
        }

    }

    public ArrayList<String> getCheckedItems()
    {
        ArrayList<String> mTempArry = new ArrayList<String>();

        for (int i = 0; i < mImagesList.size(); i++)
        {
            if (isSelectedList.get(i))
            {
                mTempArry.add(mImagesList.get(i));
            }
        }

        return mTempArry;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

  /*  CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener()
    {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
        }
    };*/

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_gallery_images, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)
    {

        String imageUrl = mImagesList.get(position);

        //Log.e("ImageAdapter","path "+imageUrl);

        Glide.with(mContext)
                .load("file://" + imageUrl)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.imageView);


    }

    @Override
    public int getItemCount()
    {
        return mImagesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        public ImageView imageViewFrame;
        public ImageView imageView;

        public MyViewHolder(View view)
        {
            super(view);

            imageViewFrame = (ImageView) view.findViewById(R.id.imageViewFrame);
            imageView = (ImageView) view.findViewById(R.id.imageView);

            imageViewFrame.setVisibility(View.GONE);

            //imageViewFrame.setTag(false);

            imageView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //boolean isSelected = (boolean) imageViewFrame.getTag();
                    boolean isSelected = isSelectedList.get(getAdapterPosition());


                    if (isSelected)
                    {
                        imageViewFrame.setVisibility(View.GONE);
                        isSelectedList.set(getAdapterPosition(),false);
                    }
                    else
                    {
                        imageViewFrame.setVisibility(View.VISIBLE);
                        isSelectedList.set(getAdapterPosition(),true);
                    }
                }
            });

        }
    }

}
