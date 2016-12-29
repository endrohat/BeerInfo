package com.indraneel.beerinfo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.indraneel.beerinfo.R;

/**
 * Parent class of all the activities. Used for common init stuff
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected boolean useToolbar() {
        return true;
    }

    protected boolean isChildActivity() {
        return false;
    }

    @Override
    public void setContentView(int layoutResID) {
        View containerView = getLayoutInflater().inflate(R.layout.toolbar, null);
        View view = getLayoutInflater().inflate(layoutResID, (ViewGroup) containerView);

        configureToolbar(view);
        super.setContentView(containerView);
    }

    private void configureToolbar(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (mToolbar != null) {
            if (useToolbar()) {
                setSupportActionBar(mToolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(isChildActivity());
            } else {
                mToolbar.setVisibility(View.GONE);
            }
        }
    }
}
