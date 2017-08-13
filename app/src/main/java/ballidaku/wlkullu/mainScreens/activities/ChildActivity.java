package ballidaku.wlkullu.mainScreens.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ballidaku.wlkullu.R;
import ballidaku.wlkullu.adapters.ChildAdapter;
import ballidaku.wlkullu.dataModels.ChildModel;
import ballidaku.wlkullu.myUtilities.CommonMethods;
import ballidaku.wlkullu.myUtilities.GridSpacingItemDecoration;
import ballidaku.wlkullu.myUtilities.MyConstants;
import ballidaku.wlkullu.myUtilities.MyDatabase;
import ballidaku.wlkullu.myUtilities.MyDialogs;

public class ChildActivity extends AppCompatActivity
{

    Context context;


    Toolbar toolbar;

    TextView textViewTitle;

    String titleId = "";

    public MyDatabase myDatabase;

    ChildAdapter childAdapter;

    RecyclerView recycleViewChild;

    EditText dialogEditText;


    List<ChildModel> childModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        context = this;


        titleId = getIntent().getStringExtra(MyConstants.TITLE_ID);

        myDatabase = new MyDatabase(context);

        refreshList();

        setUpViews();

    }


    public void refreshList()
    {
        childModelArrayList = myDatabase.getAllChildData(titleId);

        Collections.sort(childModelArrayList, new Comparator<ChildModel>() {
            @Override
            public int compare(ChildModel s1, ChildModel s2) {
                return s1.getChildText().compareToIgnoreCase(s2.getChildText());
            }
        });
    }

    private void setUpViews()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_back);


        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTitle.setText(myDatabase.getTitle(titleId));


        childAdapter = new ChildAdapter(context, childModelArrayList);

        recycleViewChild = (RecyclerView) findViewById(R.id.recycleViewChild);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 3);
        recycleViewChild.setLayoutManager(mLayoutManager);
        recycleViewChild.addItemDecoration(new GridSpacingItemDecoration(3, CommonMethods.getInstance().dpToPx(context, 10), true));
        recycleViewChild.setItemAnimator(new DefaultItemAnimator());
        recycleViewChild.setAdapter(childAdapter);


        childAdapter.setOnItemClickListener(new ChildAdapter.MyClickListener()
        {
            @Override
            public void onItemClick(int position, View v)
            {

                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(MyConstants.TITLE_ID, childModelArrayList.get(position).getTitleId());
                intent.putExtra(MyConstants.CHILD_ID, childModelArrayList.get(position).getChildId());
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.child_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {

            case android.R.id.home:

                finish();

                return true;

            case R.id.add:

                childAdapter.doneEditDelete();

                dialogEditText = (EditText) MyDialogs.getInstance().addChangeValues(context, 1, "Add Title", "ADD", changeHeadingListener, null, null);
                dialogEditText.setHint("Enter title name here");


                break;


            case R.id.edit:

                childAdapter.showEditIcon();

                break;

            case R.id.delete:

                childAdapter.showDeleteIcon();

                break;


        }

        return super.onOptionsItemSelected(item);
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
                myDatabase.addChild(titleId,heading);

                refreshList();

                childAdapter.addItem(childModelArrayList);

            }
        }
    };
}
