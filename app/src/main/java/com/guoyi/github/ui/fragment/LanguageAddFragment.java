package com.guoyi.github.ui.fragment;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.guoyi.github.Constants;
import com.guoyi.github.R;
import com.guoyi.github.adapter.AddLanguageAdapter;
import com.guoyi.github.bean.Language;
import com.guoyi.github.utils.LanguageHelper;
import com.guoyi.github.utils.rxbus2.RxBus;

import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * Created by Credit on 2017/3/17.
 */

public class LanguageAddFragment extends BaseSwipeFragement {


    public static LanguageAddFragment newInstance() {
        LanguageAddFragment fragment = new LanguageAddFragment();
        fragment.setShowLayoutManager(CustomLanguageFragment.GRID);
        return fragment;
    }

    @Override
    public void replaceTheme() {
        super.replaceTheme();
        parentActivity.setToolBar(toolbar, getString(R.string.add_language));
        toolbar.setNavigationOnClickListener(b -> {
            if (dataHasChange()) {
                onBack();
            } else {
                parentActivity.onBackPressed();
            }
        });
    }

    private AddLanguageAdapter languageAdapter;

    @Override
    public void initAdapter() {
        languageAdapter = new AddLanguageAdapter(mContext);
        adapter = languageAdapter;
    }

    @Override
    public void onResume() {
        super.onResume();
        save = false;
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    if (dataHasChange()) {
                        onBack();
                        return true;
                    }
                }
                return false;
            }
        });

        if (adapter != null) {
            adapter.changeData(LanguageHelper.getInstance().getUnselectedLanguages());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        getView().setFocusableInTouchMode(false);
        getView().setOnKeyListener(null);
    }

    @Override
    protected void initEvent() {

    }

    public void onBack() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("放弃修改数据？");
        builder.setNegativeButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                saveSelectData();
                parentActivity.onBackPressed();
            }
        });
        builder.setNeutralButton("不保存", (d, i) -> {
            d.dismiss();
            cancelSelectData();
            parentActivity.onBackPressed();
        });
        builder.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_add_languagae_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                saveSelectData();
                parentActivity.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean save = false;

    ArrayList<Language> languages = new ArrayList<>();


    /**
     * 取消选择的数据
     */
    public void cancelSelectData() {
        if (adapter != null) {
            Observable.fromIterable(languageAdapter.getmData())
                    .filter(r -> {
                        return r.check;
                    }).subscribe(r -> {
                r.check = false;

            });
        }
    }

    /**
     * 判断是否有数据选择
     */
    public void checkSelectData() {
        if (adapter != null) {
            languages.clear();
            Observable.fromIterable(languageAdapter.getmData())
                    .filter(r -> {
                        return r.check;
                    }).subscribe(r -> {
                languages.add(r);

            });
        }
    }

    /**
     * 判断数据是否改变
     *
     * @return
     */
    public boolean dataHasChange() {
        checkSelectData();
        if (languages.size() > 0 && !save) {
            return true;
        }
        return false;
    }

    /**
     * 保存已选的数据
     */
    public void saveSelectData() {
        checkSelectData();
        save = true;
        if (languages.size() > 0) {
            LanguageHelper.getInstance().addSelectedLanguages(languages);
            cancelSelectData();
            RxBus.get()
                    .withKey(Constants.LANGUAGE_DATA_CHANGE)
                    .send(Constants.LANGUAGE_DATA_ADD);
        }

    }
}
