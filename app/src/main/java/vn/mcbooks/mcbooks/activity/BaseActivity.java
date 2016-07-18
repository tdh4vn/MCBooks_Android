package vn.mcbooks.mcbooks.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import vn.mcbooks.mcbooks.intef.ILogout;
import vn.mcbooks.mcbooks.singleton.ContentManager;

/**
 * Created by hungtran on 5/29/16.
 */
public class BaseActivity extends AppCompatActivity{
    static
    {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    public void openActivity(Class activityClass, Bundle bundle){
        Intent intent = new Intent(this, activityClass);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        saveData();
        super.onDestroy();
    }

    private void saveData() {
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput("content_mcbooks", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(ContentManager.getInstance());
            os.close();
            fos.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
