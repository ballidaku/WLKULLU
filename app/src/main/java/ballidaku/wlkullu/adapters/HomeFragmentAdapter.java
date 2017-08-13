package ballidaku.wlkullu.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ballidaku.wlkullu.R;
import ballidaku.wlkullu.dataModels.TitleModel;
import ballidaku.wlkullu.mainScreens.fragments.HomeFragment;
import ballidaku.wlkullu.myUtilities.MyDialogs;


/**
 * Created by sharanpalsingh on 12/09/16.
 */
public class HomeFragmentAdapter extends RecyclerView.Adapter<HomeFragmentAdapter.ViewHolder>
{

    private String LOG_TAG = "HomeFragmentAdapter";

    private ArrayList<TitleModel> arrayList;


    private MyClickListener myClickListener;

    private Context context;

    public int myVisibilityDelete = View.GONE;
    public int myVisibilityEdit = View.GONE;

    Fragment fragment;


    public HomeFragmentAdapter(Context context, Fragment fragment, ArrayList<TitleModel> arrayList)
    {

        this.arrayList = arrayList;
        this.fragment = fragment;
        this.context = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        TextView textViewItemName;

        ImageView imageViewDelete;
        ImageView imageViewEdit;


        public ViewHolder(final View itemView)
        {

            super(itemView);

            textViewItemName = (TextView) itemView.findViewById(R.id.textViewItemName);
            imageViewDelete = (ImageView) itemView.findViewById(R.id.imageViewDelete);
            imageViewEdit = (ImageView) itemView.findViewById(R.id.imageViewEdit);


            imageViewDelete.setOnClickListener(this);
            imageViewEdit.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    myClickListener.onItemClick(getAdapterPosition(),itemView);
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

    public interface MyClickListener
    {
        public void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(MyClickListener myClickListener)
    {
        this.myClickListener = myClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_home_fragment_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.textViewItemName.setText(arrayList.get(position).getTitle());

        holder.imageViewDelete.setVisibility(myVisibilityDelete);
        holder.imageViewEdit.setVisibility(myVisibilityEdit);
    }

//    public void addItem(String dataObj, int index) {
//
//        //userBankDataModelsList.add(index, dataObj);
//
//        notifyItemInserted(index);
//    }

    public void addItem(ArrayList<TitleModel> arrayList)
    {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    public void deleteItem(int index)
    {
        ((HomeFragment) fragment).myDatabase.deleteTitleRecord(arrayList.get(index).getId());


        arrayList.remove(index);
//        notifyItemRemoved(index);

        doneEditDelete();


    }


    EditText dialogEditText;

    private void editItem(int adapterPosition)
    {

        dialogEditText = (EditText) MyDialogs.getInstance().addChangeValues(context,1, "Edit Title", "DONE", changeHeadingListener,null,null);
        dialogEditText.setText(arrayList.get(adapterPosition).getTitle());
        dialogEditText.setTag(adapterPosition);
    }

    View.OnClickListener changeHeadingListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {

            MyDialogs.getInstance().dialog.dismiss();
            String heading = dialogEditText.getText().toString().trim();

            if (!heading.isEmpty())
            {
                arrayList.get((int) dialogEditText.getTag()).setTitle(heading);

                doneEditDelete();

                ((HomeFragment) fragment).myDatabase.updateTitleRecord(arrayList.get((int) dialogEditText.getTag()));
            }
        }
    };


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

    @Override
    public int getItemCount()
    {
        return arrayList.size();
    }


}