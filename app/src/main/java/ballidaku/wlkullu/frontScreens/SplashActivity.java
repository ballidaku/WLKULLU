package ballidaku.wlkullu.frontScreens;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;

import ballidaku.wlkullu.R;
import ballidaku.wlkullu.mainScreens.activities.MainActivity;
import ballidaku.wlkullu.myUtilities.AbsRuntimeMarshmallowPermission;
import ballidaku.wlkullu.myUtilities.MyDialogs;
import ballidaku.wlkullu.myUtilities.MySharedPreference;

public class SplashActivity extends AbsRuntimeMarshmallowPermission
{


    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = this;


        String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        requestAppPermissions(permission, R.string.permission, 54);
    }

    @Override
    public void onPermissionGranted(int requestCode)
    {
        if (requestCode == 54)
        {
            countDownTimer.start();
        }
    }

    EditText editTextPassword;
    CountDownTimer countDownTimer = new CountDownTimer(5000, 1000)
    {

        public void onTick(long millisUntilFinished)
        {

        }

        public void onFinish()
        {

            if(!MySharedPreference.getInstance().isLogined(context))
            {
                editTextPassword = MyDialogs.getInstance().checkPasswordDialog(context, onClickListener);
            }
            else
            {
                goToNextScreen();
            }


        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if(editTextPassword.getText().toString().trim().equals("ballidaku"))
            {
                MySharedPreference.getInstance().setLogin(context,true);
                goToNextScreen();
            }
            else
            {
                finish();
            }
        }

    };


    public void goToNextScreen()
    {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        finish();
    }
}
