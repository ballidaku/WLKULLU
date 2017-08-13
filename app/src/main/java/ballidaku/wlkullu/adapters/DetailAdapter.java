package ballidaku.wlkullu.adapters;

/**
 * Created by sharanpalsingh on 20/07/17.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ballidaku.wlkullu.R;
import ballidaku.wlkullu.dataModels.DetailModel;
import ballidaku.wlkullu.mainScreens.activities.DetailActivity;
import ballidaku.wlkullu.mainScreens.activities.GalleryImages;
import ballidaku.wlkullu.myUtilities.CommonMethods;
import ballidaku.wlkullu.myUtilities.MyDialogs;
import ballidaku.wlkullu.myUtilities.MyTagHandler;


/**
 * Created by brst-pc93 on 7/20/17.
 */

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.MyViewHolder>
{
    String TAG = DetailAdapter.class.getSimpleName();

    Context context;

    private List<DetailModel> detailAdapterList;

    public int myVisibilityDelete = View.GONE;
    public int myVisibilityEdit = View.GONE;


    public DetailAdapter(Context context, List<DetailModel> detailAdapterList)
    {
        this.context = context;
        this.detailAdapterList = detailAdapterList;


    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView textViewHeading;
        TextView textView;
        TextView textViewImages;
        TextView textViewVideos;

        ImageView imageViewVideo;
        ImageView imageViewVideoPlay;

        GridView horizontalListViewMain;
        ImageView imageViewDelete;
        ImageView imageViewEdit;

        public MyViewHolder(View view)
        {
            super(view);

            textViewHeading = (TextView) view.findViewById(R.id.textViewHeading);
            textView = (TextView) view.findViewById(R.id.textView);
            textViewImages = (TextView) view.findViewById(R.id.textViewImages);
            textViewVideos = (TextView) view.findViewById(R.id.textViewVideos);


            imageViewVideo = (ImageView) view.findViewById(R.id.imageViewVideo);
            imageViewVideoPlay = (ImageView) view.findViewById(R.id.imageViewVideoPlay);

            horizontalListViewMain = (GridView) view.findViewById(R.id.horizontalListViewMain);
            //horizontalListViewMain.setExpanded(true);

            imageViewDelete = (ImageView) view.findViewById(R.id.imageViewDelete);
            imageViewEdit = (ImageView) view.findViewById(R.id.imageViewEdit);

            imageViewDelete.setOnClickListener(this);
            imageViewEdit.setOnClickListener(this);


            imageViewVideoPlay.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try {
                        String path= "file://"+detailAdapterList.get(getAdapterPosition()).getVideoPath();
                        // mVideosPath[i] = /storage/emulated/0/Movies/test.mp4

                        Intent mVideoWatch = new Intent(Intent.ACTION_VIEW);
                        mVideoWatch.setDataAndType(Uri.parse(path),"video/mp4");
                        context.startActivity(mVideoWatch);
                    }
                    catch(Exception e)
                    {
                        Log.e(TAG,e.getMessage());
                    }
                }
            });
        }

        @Override
        public void onClick(View v)
        {
            // myClickListener.onItemClick(getAdapterPosition(), v);

            switch (v.getId())
            {
                case R.id.imageViewDelete:

                    deleteItem(getAdapterPosition());

                    break;


                case R.id.imageViewEdit:

                    editItem(getAdapterPosition());

                    break;
            }
        }
    }



    public void deleteItem(int index)
    {
        ((DetailActivity) context).myDatabase.deleteDetailRecord(detailAdapterList.get(index).getTitleId());

        detailAdapterList.remove(index);

        myVisibilityDelete = View.GONE;
        // notifyItemRemoved(index);
        notifyDataSetChanged();


        //((HomeFragment) fragment).myDatabase.deleteTitleRecord(detailAdapterList.get(index).getId());
    }


    EditText editTextHeading;
    EditText editTextInfo;

    View.OnClickListener onUpdateClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            String heading = editTextHeading.getText().toString().trim();
            String info = editTextInfo.getText().toString().trim();

            MyDialogs.getInstance().dialog.dismiss();

            updateData(heading, info, MyDialogs.getInstance().imagePathList, (int) editTextHeading.getTag(),MyDialogs.getInstance().videoImagePath);
        }
    };

    View.OnClickListener onImageUpdateClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {

            Intent intent=new Intent(context,GalleryImages.class);
            ((DetailActivity)context).startActivityForResult(intent,200);
        }
    };

    View.OnClickListener onVideoUpdateClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {

            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            ((DetailActivity)context).startActivityForResult(Intent.createChooser(intent,"Select Video"),69);
        }
    };

    private void editItem(int adapterPosition)
    {
        EditText[] editText = (EditText[]) MyDialogs.getInstance().addChangeValues(context, 2, "Update Data", "Update", onUpdateClickListener, onImageUpdateClickListener,onVideoUpdateClickListener);
        editTextHeading = editText[0];
        editTextInfo = editText[1];

        editTextHeading.setHint("Enter Heading...");
        editTextHeading.setTag(adapterPosition);
        editTextHeading.setText(detailAdapterList.get(adapterPosition).getHeading());
        editTextInfo.setText(detailAdapterList.get(adapterPosition).getText());

        List<String> imagePathList = detailAdapterList.get(adapterPosition).getImagePathList();
        if (imagePathList != null && imagePathList.size() > 0)
        {
            MyDialogs.getInstance().setImages(imagePathList);
        }


        String videoPath=detailAdapterList.get(adapterPosition).getVideoPath().trim();

        if(!videoPath.isEmpty())
        {
            MyDialogs.getInstance().setVideoImage(context, videoPath);

        }

    }


    @Override
    public DetailAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_detail_item, parent, false);

        return new DetailAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DetailAdapter.MyViewHolder holder, int position)
    {
        DetailModel detailModel = detailAdapterList.get(position);

        holder.textViewHeading.setText(detailModel.getHeading());

        holder.imageViewDelete.setVisibility(myVisibilityDelete);
        holder.imageViewEdit.setVisibility(myVisibilityEdit);

        String text = detailModel.getText();

        Log.e(TAG,text);
        if (!text.isEmpty())
        {
            //holder.textView.setText(Html.fromHtml(detailModel.getText()));
            //holder.textView.setText(text);
//            holder.textView.setText(Html.fromHtml(text.replaceAll(" ", "&nbsp;").replaceAll("\n","<br />")));
            holder.textView.setText(Html.fromHtml(text.replaceAll("\n","<br />"),null, new MyTagHandler()));
        }
        else
        {
            holder.textView.setVisibility(View.GONE);
        }


       if(detailModel.getImagePathList() != null && detailModel.getImagePathList().size()>0)
       {
           holder.horizontalListViewMain.setVisibility(View.VISIBLE);
           holder.textViewImages.setVisibility(View.VISIBLE);

           HorizontalListViewAdapter horizontalListViewAdapter = new HorizontalListViewAdapter(context, detailModel.getImagePathList());
           holder.horizontalListViewMain.setAdapter(horizontalListViewAdapter);


           int a = detailModel.getImagePathList().size();
           if (a > 5)
           {
               setGridViewHeightBasedOnChildren(holder.horizontalListViewMain, 5);

           }
       }
       else
       {
           holder.horizontalListViewMain.setVisibility(View.GONE);
           holder.textViewImages.setVisibility(View.GONE);
       }



        if(detailModel.getVideoPath() != null && !detailModel.getVideoPath().trim().isEmpty())
        {
            CommonMethods.getInstance().setImage(context, detailModel.getVideoPath().trim(),holder.imageViewVideo);
            holder.textViewVideos.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.imageViewVideo.setVisibility(View.GONE);
            holder.imageViewVideoPlay.setVisibility(View.GONE);
            holder.textViewVideos.setVisibility(View.GONE);
        }

    }

    public void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        BaseAdapter listAdapter = (BaseAdapter) gridView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        float x = 1;
        if( items > columns ){
            x = items/columns;
            rows = (int) (x + 1);
           // totalHeight *= rows;
        }
        int abc=rows*200;
       totalHeight = CommonMethods.getInstance().dpToPx(context,abc);

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);

    }


    @Override
    public int getItemCount()
    {
        return detailAdapterList.size();
    }


    @Override
    public int getItemViewType(int position)
    {
        return super.getItemViewType(position);
    }


    public void addData(String titleId,String childId, String heading, String info, List<String> imagePathList,String videoPath)
    {
        DetailModel detailModel = new DetailModel();
        detailModel.setHeading(heading);
        detailModel.setTitleId(titleId);
        detailModel.setChildId(childId);
        detailModel.setImagePathList(imagePathList);
        detailModel.setVideoPath(videoPath);
        detailModel.setText(info);

        ((DetailActivity) context).myDatabase.addDetailData(detailModel);

        detailAdapterList.add(detailModel);

        doneEditDelete();

        ((DetailActivity) context).recyclerViewDetails.scrollToPosition(detailAdapterList.size() - 1);

    }

    public void updateData(String heading, String info, List<String> imagePathList, int position,String videoPath)
    {
        DetailModel detailModel = new DetailModel();
        detailModel.setDetailId(detailAdapterList.get(position).getDetailId());
        detailModel.setChildId(detailAdapterList.get(position).getChildId());
        detailModel.setTitleId(detailAdapterList.get(position).getTitleId());
        detailModel.setHeading(heading);
        detailModel.setImagePathList(imagePathList);
        detailModel.setVideoPath(videoPath);
        detailModel.setText(info);

        ((DetailActivity) context).myDatabase.updateDetailRecord(detailModel);

        detailAdapterList.set(position, detailModel);

        myVisibilityEdit = View.GONE;
        notifyDataSetChanged();
    }

    public void showDeleteIcon()
    {
        myVisibilityDelete = View.VISIBLE;
        myVisibilityEdit = View.GONE;
        notifyDataSetChanged();
    }

    public void showEditIcon()
    {
        myVisibilityEdit = View.VISIBLE;
        myVisibilityDelete = View.GONE;
        notifyDataSetChanged();
    }

    public void doneEditDelete()
    {
        myVisibilityDelete = View.GONE;
        myVisibilityEdit = View.GONE;
        notifyDataSetChanged();
    }


}