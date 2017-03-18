package com.guoyi.github.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.guoyi.github.Constants;
import com.guoyi.github.R;
import com.guoyi.github.adapter.CustomLanguageAdapter;
import com.guoyi.github.bean.Language;
import com.guoyi.github.helper.OnStartDragListener;
import com.guoyi.github.helper.SimpleItemTouchHelperCallback;
import com.guoyi.github.utils.LanguageHelper;
import com.guoyi.github.utils.rxbus2.RxBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Credit on 2017/3/17.
 */

public class CustomLanguageFragment extends BaseSwipeFragement implements OnStartDragListener {

    private int MODE;

    public static CustomLanguageFragment newInstance(int mode) {
        Bundle bundle = new Bundle();
        bundle.putInt("mode", mode);
        CustomLanguageFragment fragment = new CustomLanguageFragment();
        fragment.setShowLayoutManager(CustomLanguageFragment.GRID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            MODE = bundle.getInt("mode", Constants.LANGUAGE_CUSTOM_MOVE);
        }
    }


    @Override
    public void replaceTheme() {
        super.replaceTheme();
        parentActivity.setToolBar(toolbar, getString(MODE == Constants.LANGUAGE_CUSTOM_MOVE ? R.string.custom_language : R.string.remove_language));
        toolbar.setNavigationOnClickListener(b -> {
            if (dataHasChange() && MODE == Constants.LANGUAGE_CUSTOM_DELETE) {
                onBack();
            } else {
                parentActivity.onBackPressed();
            }
        });
    }


    private ItemTouchHelper mItemTouchHelper;

    private CustomLanguageAdapter customLanguageAdapter;

    @Override
    public void initAdapter() {
        customLanguageAdapter = new CustomLanguageAdapter(mContext, MODE, this);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(customLanguageAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recylerView);
        adapter = customLanguageAdapter;

    }

    @Override
    protected void initEvent() {

    }

    int dataSize = 0;

    @Override
    public void onResume() {
        super.onResume();
        save = false;
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                log("后退》》》》》》》》");
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK && MODE == Constants.LANGUAGE_CUSTOM_DELETE) {
                    log("dataHasChange 成功");
                    if (dataHasChange()) {
                        onBack();
                        return true;
                    }
                }
                return false;
            }
        });

        if (adapter != null) {
            List<Language> languages = new ArrayList<>(LanguageHelper.getInstance().getSelectedLanguages());
            dataSize = languages.size();
            adapter.changeData(languages);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        getView().setFocusableInTouchMode(false);
        getView().setOnKeyListener(null);
    }

    public void onBack() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("放弃修改数据？");
        builder.setNegativeButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                saveData();
                parentActivity.onBackPressed();
            }
        });

        builder.setNeutralButton("不保存", (d, i) -> {
            d.dismiss();
            parentActivity.onBackPressed();
        });

        builder.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_custom_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        boolean isRemoveMode = MODE == Constants.LANGUAGE_CUSTOM_DELETE;
        menu.findItem(R.id.action_add).setVisible(!isRemoveMode);
        menu.findItem(R.id.action_remove).setVisible(!isRemoveMode);
        menu.findItem(R.id.action_done).setVisible(isRemoveMode);
        menu.findItem(R.id.action_save).setVisible(!isRemoveMode);
    }

    private void switchMode(int mode) {
        this.MODE = mode;
        customLanguageAdapter.setMode(mode);
        /**
         * 重新绘制OptionsMenu
         */
        parentActivity.invalidateOptionsMenu();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                parentActivity.switchFragment(4, 2);
                return true;
            case R.id.action_remove:
                parentActivity.switchFragment(3, 2);
                return true;
            case R.id.action_done:
            case R.id.action_save:
                saveData();
                parentActivity.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean save = false;

    public void saveData() {
        save = true;
        LanguageHelper.getInstance().setSelectedLanguages(adapter.getmData());
        RxBus.get()
                .withKey(Constants.LANGUAGE_DATA_CHANGE)
                .send(MODE);
    }

    public boolean dataHasChange() {
        if (adapter == null || save) {
            return false;
        }
        return !(adapter.getmData().size() == dataSize);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
