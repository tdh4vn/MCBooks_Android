package vn.mcbooks.mcbooks.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import vn.mcbooks.mcbooks.intef.ILogout;

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
}
