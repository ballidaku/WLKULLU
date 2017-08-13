package ballidaku.wlkullu.mainScreens.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ballidaku.wlkullu.BaseApp;
import ballidaku.wlkullu.R;
import ballidaku.wlkullu.frontScreens.BaseActivity;
import ballidaku.wlkullu.mainScreens.fragments.HomeFragment;
import ballidaku.wlkullu.myUtilities.CommonMethods;
import ballidaku.wlkullu.myUtilities.MyDialogs;
import ballidaku.wlkullu.myUtilities.MySharedPreference;
import ballidaku.wlkullu.utils.AidlUtil;
import ballidaku.wlkullu.utils.BytesUtil;
import sunmi.sunmiui.dialog.DialogCreater;
import sunmi.sunmiui.dialog.EditTextDialog;

public class MainActivity extends BaseActivity
{

    Context context;

    BaseApp baseApp;

    EditText dialogEditText;
    TextView textViewTitle;

    int fromWhere;


    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        baseApp = (BaseApp) getApplication();

        setContentView(R.layout.activity_main);

        context = this;

        setUpViews();


        CommonMethods.getInstance().switchFragment(context,fragment = new HomeFragment());

    }

    private void setUpViews()
    {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textViewTitle=(TextView)findViewById(R.id.textViewTitle);

        refreshHeading();
    }

    private void refreshHeading()
    {
        textViewTitle.setText(MySharedPreference.getInstance().getHeading(context));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public void onBackPressed()
    {
        int count = getSupportFragmentManager().getBackStackEntryCount();


        if( ((HomeFragment)fragment).homeFragmentAdapter.myVisibilityDelete == 0 ||  ((HomeFragment)fragment).homeFragmentAdapter.myVisibilityEdit == 0)
        {
            ((HomeFragment)fragment).homeFragmentAdapter.doneEditDelete();
        }
        else if (count == 1)
        {
            finish();
        }
        else
        {
            super.onBackPressed();
        }

    }

    EditTextDialog mEditTextDialog;

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.changeTitle:

                dialogEditText = (EditText) MyDialogs.getInstance().addChangeValues(context,1,"Change Heading","SAVE",changeHeadingListener,null,null);
                dialogEditText.setHint("Enter heading here");
                fromWhere=1;

                break;

            case R.id.addTitle:

                ((HomeFragment)fragment).homeFragmentAdapter.doneEditDelete();;

                dialogEditText = (EditText) MyDialogs.getInstance().addChangeValues(context,1,"Add Title","ADD",changeHeadingListener,null,null);
                dialogEditText.setHint("Enter title name here");
                fromWhere=2;

                break;


            case R.id.deleteTitle:

               ((HomeFragment)fragment).homeFragmentAdapter.showDeleteIcon();;

                break;


            case R.id.print:

                mEditTextDialog = DialogCreater.createEditTextDialog(this, "Cancel", "Print", "Please enter the text", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mEditTextDialog.cancel();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = mEditTextDialog.getEditText().getText().toString();
                        AidlUtil.getInstance().sendRawData(BytesUtil.getBytesFromHexString(text));
                        mEditTextDialog.cancel();
                    }
                }, null);
                mEditTextDialog.show();

                break;

            case R.id.editTitle:

                ((HomeFragment)fragment).homeFragmentAdapter.showEditIcon();;


                break;

            case R.id.secondScreen:

                startActivity(new Intent(context,VideoActivity.class));


                break;

        }

        return super.onOptionsItemSelected(item);
    }



    View.OnClickListener changeHeadingListener=new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {

            MyDialogs.getInstance().dialog.dismiss();
            String heading = dialogEditText.getText().toString().trim();

            if(!heading.isEmpty())
            {
                if(fromWhere == 1)
                {
                    MySharedPreference.getInstance().saveHeading(context, heading);
                    refreshHeading();
                }
                else
                {
                    ((HomeFragment)fragment).addTitle(heading);
                }
            }
        }
    };

}
