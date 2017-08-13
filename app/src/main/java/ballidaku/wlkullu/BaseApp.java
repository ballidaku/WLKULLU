package ballidaku.wlkullu;

import android.app.Application;
import android.content.res.AssetManager;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import ballidaku.wlkullu.utils.AidlUtil;

public class BaseApp extends Application
{
    private boolean isAidl;

    public boolean isAidl() {
        return isAidl;
    }

    public void setAidl(boolean aidl) {
        isAidl = aidl;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isAidl = true;
        AidlUtil.getInstance().connectPrinterService(this);

       // initAssets();
    }

    private void initAssets() {
        AssetManager assetManager = getAssets();
        InputStream inputStream = null;
        FileOutputStream fos = null;
        try {
            String fileNames[] = assetManager.list("custom_img");
            File file = new File(Environment.getExternalStorageDirectory().getPath());
            String sdFileNames[] = file.list();
            for (int i = 0; i < fileNames.length; i++) {
                boolean hasFile = false;
                for (String fileName : sdFileNames) {
                    if (fileName.equals(fileNames[i])) {
                        hasFile = true;
                        break;
                    }
                }
                if (hasFile) continue;
                inputStream = getClass().getClassLoader().getResourceAsStream("assets/custom_img/" + fileNames[i]);
                fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory().getPath() + "/" + fileNames[i]));
                int len = 0;
                byte[] buffer = new byte[1024];
                while ((len = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                inputStream.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}