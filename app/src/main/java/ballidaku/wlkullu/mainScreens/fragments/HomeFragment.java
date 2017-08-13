package ballidaku.wlkullu.mainScreens.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ballidaku.wlkullu.R;
import ballidaku.wlkullu.adapters.HomeFragmentAdapter;
import ballidaku.wlkullu.dataModels.TitleModel;
import ballidaku.wlkullu.mainScreens.activities.ChildActivity;
import ballidaku.wlkullu.myUtilities.CommonMethods;
import ballidaku.wlkullu.myUtilities.GridSpacingItemDecoration;
import ballidaku.wlkullu.myUtilities.MyConstants;
import ballidaku.wlkullu.myUtilities.MyDatabase;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sharanpalsingh on 16/07/17.
 */

public class HomeFragment extends Fragment implements View.OnClickListener
{

    String TAG = HomeFragment.class.getSimpleName();

    View view = null;

    Context context;

    HomeFragment homeFragment;


    @BindView(R.id.recycleViewHome)
    RecyclerView recycleViewHome;


   public HomeFragmentAdapter homeFragmentAdapter;

    ArrayList<TitleModel> mainList = new ArrayList<>();


   public  MyDatabase myDatabase;


    public HomeFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_home, container, false);

            context = getActivity();

            homeFragment= this;

            myDatabase =new MyDatabase(context);


            ButterKnife.bind(this, view);

            setUpViews();

        }

        return view;
    }

    public void setUpViews()
    {

        refreshMainList();


        homeFragmentAdapter = new HomeFragmentAdapter(context,homeFragment, mainList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        recycleViewHome.setLayoutManager(mLayoutManager);
        recycleViewHome.addItemDecoration(new GridSpacingItemDecoration(3, CommonMethods.getInstance().dpToPx(context, 10), true));
        recycleViewHome.setItemAnimator(new DefaultItemAnimator());
        recycleViewHome.setAdapter(homeFragmentAdapter);


        homeFragmentAdapter.setOnItemClickListener(new HomeFragmentAdapter.MyClickListener()
        {
            @Override
            public void onItemClick(int position, View v)
            {

                Intent intent = new Intent(getActivity(), ChildActivity.class);
                intent.putExtra(MyConstants.TITLE_ID, mainList.get(position).getId());
                startActivity(intent);
            }
        });

    }

    public void refreshMainList()
    {
        mainList = myDatabase.getAllTitlesData();

        Collections.sort(mainList, new Comparator<TitleModel>() {
            @Override
            public int compare(TitleModel s1, TitleModel s2) {
                return s1.getTitle().compareToIgnoreCase(s2.getTitle());
            }
        });
    }

    public void addTitle(String name)
    {
        myDatabase.addTitle(name);

        refreshMainList();

        homeFragmentAdapter.addItem(mainList);
    }


    @Override
    public void onClick(View v)
    {


        switch (v.getId())
        {
            /*case R.id.fab:

                Intent intent=new Intent(context, AddBankDetails.class);
                startActivityForResult(intent,ADD_DETAILS_RESPONSE);


                break;*/
        }
    }


}
