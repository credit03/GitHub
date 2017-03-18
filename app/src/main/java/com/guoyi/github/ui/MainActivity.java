package com.guoyi.github.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.guoyi.github.Constants;
import com.guoyi.github.R;
import com.guoyi.github.ui.fragment.AboutFragment;
import com.guoyi.github.ui.fragment.BaseFragment;
import com.guoyi.github.ui.fragment.CustomLanguageFragment;
import com.guoyi.github.ui.fragment.FavortFragment;
import com.guoyi.github.ui.fragment.HomeFragment;
import com.guoyi.github.ui.fragment.LanguageAddFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseAcitvity
        implements NavigationView.OnNavigationItemSelectedListener {


    @InjectView(R.id.nav_view)
    NavigationView navView;
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.content)
    FrameLayout content;
    private ActionBarDrawerToggle toggle;

    private FragmentManager fragmentManager;


    private BaseFragment[] fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        navView.setNavigationItemSelectedListener(this);
        fragmentManager = getSupportFragmentManager();
        fragments = new BaseFragment[]{
                HomeFragment.newInstance(),
                FavortFragment.newInstance(),
                CustomLanguageFragment.newInstance(Constants.LANGUAGE_CUSTOM_MOVE),
                CustomLanguageFragment.newInstance(Constants.LANGUAGE_CUSTOM_DELETE),
                LanguageAddFragment.newInstance(),
                AboutFragment.newInstance()
        };


        switchFragment(0);


    }


    public void setToolBar(@NonNull Toolbar toolBar, String title) {
        setSupportActionBar(toolBar);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            if (TextUtils.isEmpty(title)) {
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(false);
                toggle = new ActionBarDrawerToggle(
                        this, drawerLayout, toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawerLayout.setDrawerListener(toggle);
                toggle.syncState();
            } else {
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(title);
            }

        }

    }

    private int currentIndex = -1;

    private int backIndex = -1;

    public void switchFragment(int page, int back) {
        this.backIndex = back;
        switchFragment(page);
    }


    public void switchFragment(int page) {

        if (page == currentIndex) {
            return;
        }
        BaseFragment fragment = fragments[page];
        fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
        currentIndex = page;
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (currentIndex < 1) {
                super.onBackPressed();
            } else {
                if (backIndex != -1) {
                    switchFragment(backIndex);
                    backIndex = -1;
                } else {
                    switchFragment(0);
                }
            }
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_home:
                switchFragment(0);
                break;
            case R.id.action_favos:
                switchFragment(1);
                break;
            case R.id.action_custom:
                switchFragment(2);
                break;
            case R.id.action_add:
                switchFragment(4);
                break;
            case R.id.action_remove:
                switchFragment(3);
                break;
            case R.id.action_theme:
                ThemeDialog dialog = new ThemeDialog();
                dialog.show(getSupportFragmentManager(), "theme");
                break;
            case R.id.action_about:
                switchFragment(5);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        if (fragments != null) {
            for (BaseFragment b : fragments) {
                if (b != null) {
                    b.destroyData();
                }
            }
        }
        fragments = null;
        super.onDestroy();
    }
}
