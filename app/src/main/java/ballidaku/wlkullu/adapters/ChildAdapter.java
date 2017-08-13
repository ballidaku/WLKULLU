package ballidaku.wlkullu.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ballidaku.wlkullu.R;
import ballidaku.wlkullu.dataModels.ChildModel;
import ballidaku.wlkullu.mainScreens.activities.ChildActivity;
import ballidaku.wlkullu.myUtilities.MyDialogs;

/**
 * Created by sharanpalsingh on 26/07/17.
 */

public class ChildAdapter  extends RecyclerView.Adapter<ChildAdapter.ViewHolder>
{

    private String LOG_TAG = "HomeFragmentAdapter";

    private List<ChildModel> arrayList;


    private ChildAdapter.MyClickListener myClickListener;

    private Context context;

    public int myVisibilityDelete = View.GONE;
    public int myVisibilityEdit = View.GONE;


    public ChildAdapter(Context context, List<ChildModel> arrayList)
    {

        this.arrayList = arrayList;
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

    public void setOnItemClickListener(ChildAdapter.MyClickListener myClickListener)
    {
        this.myClickListener = myClickListener;
    }

    @Override
    public ChildAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_home_fragment_item, parent, false);

        return new ChildAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChildAdapter.ViewHolder holder, int position)
    {
        holder.textViewItemName.setText(arrayList.get(position).getChildText());

        holder.imageViewDelete.setVisibility(myVisibilityDelete);
        holder.imageViewEdit.setVisibility(myVisibilityEdit);
    }


    public void addItem(List<ChildModel> arrayList)
    {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    public void deleteItem(int index)
    {
        ((ChildActivity) context).myDatabase.deleteChildRecord(arrayList.get(index).getChildId());

        arrayList.remove(index);
//        notifyItemRemoved(index);

        doneEditDelete();

    }


    EditText dialogEditText;

    private void editItem(int adapterPosition)
    {

        dialogEditText = (EditText) MyDialogs.getInstance().addChangeValues(context,1, "Edit Title", "DONE", changeChildListener,null,null);
        dialogEditText.setText(arrayList.get(adapterPosition).getChildText());
        dialogEditText.setTag(adapterPosition);
    }

    View.OnClickListener changeChildListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {

            MyDialogs.getInstance().dialog.dismiss();
            String heading = dialogEditText.getText().toString().trim();

            if (!heading.isEmpty())
            {
                arrayList.get((int) dialogEditText.getTag()).setChildText(heading);

                doneEditDelete();

                ((ChildActivity) context).myDatabase.updateChildRecord(arrayList.get((int) dialogEditText.getTag()));
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