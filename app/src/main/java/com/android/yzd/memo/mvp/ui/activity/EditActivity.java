package com.android.yzd.memo.mvp.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.yzd.memo.R;
import com.android.yzd.memo.bean.God;
import com.android.yzd.memo.mvp.model.evenbus.EventCenter;
import com.android.yzd.memo.mvp.presenter.impl.EditAImpl;
import com.android.yzd.memo.widget.spinner.NiceSpinner;
import com.android.yzd.memo.mvp.ui.view.EditAView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

import butterknife.Bind;

public class EditActivity extends BaseActivity implements EditAView {

    private static final int SUCCESS = 1;
    private static final int ERROR = 0;
    @Bind(R.id.common_toolbar) Toolbar mToolBar;
    @Bind(R.id.spinner) NiceSpinner mSpinner;
    @Bind(R.id.title_edit_text) MaterialEditText mTitleEdt;
    @Bind(R.id.userName) MaterialEditText mUserNameEdt;
    @Bind(R.id.passWord) MaterialEditText mPassWordEdt;
    @Bind(R.id.eye) CheckBox mEyeChB;
    @Bind(R.id.timeTextView) TextView mTimeTextView;
    private EditAImpl mEditImpl;
    private MenuItem menuItem;
    private AlertDialog alertDialog;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEditImpl = new EditAImpl(this, this);
        mEditImpl.onCreate(savedInstanceState);
        mEditImpl.getIntent(getIntent());
        mEyeChB.setOnCheckedChangeListener(mEditImpl);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override public boolean onPrepareOptionsMenu(Menu menu) {
        menuItem = menu.getItem(0);
        setItemMenuVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mEditImpl.onOptionItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onEventComing(EventCenter eventCenter) {

    }

    @Override protected int getContentView() {
        return R.layout.activity_edit;
    }

    @Override protected void initToolbar() {
        initToolBar(mToolBar);
        setToolBarTitle(R.string.create_mode);
    }

    @Override protected boolean isApplyTranslucency() {
        return true;
    }

    @Override protected boolean isApplyButterKnife() {
        return true;
    }

    @Override
    protected boolean isApplyEventBus() {
        return false;
    }

    @Override public void initSpinner(List<String> data) {
        mSpinner.attachDataSource(data);
        mSpinner.setOnItemSelectedListener(mEditImpl);
    }

    @Override public void initCreateModel() {
        mTitleEdt.requestFocus();
        showKeyBoard();
        addEdtChangeListener();
    }

    private void addEdtChangeListener() {
        mTitleEdt.addTextChangedListener(mEditImpl);
        mUserNameEdt.addTextChangedListener(mEditImpl);
        mPassWordEdt.addTextChangedListener(mEditImpl);
    }

    @Override public void initEditModel() {

    }

    @Override
    public void initViewModel(God god, int positionType) {
        hideKeyBoard();
        mTitleEdt.setText(god.getTitle());
        mUserNameEdt.setText(god.getUserName());
        mPassWordEdt.setText(god.getPassWord());
        mPassWordEdt.setTransformationMethod(HideReturnsTransformationMethod
                .getInstance());
        mTitleEdt.setOnFocusChangeListener(mEditImpl);
        mUserNameEdt.setOnFocusChangeListener(mEditImpl);
        mPassWordEdt.setOnFocusChangeListener(mEditImpl);
        mEyeChB.setChecked(false);
        mSpinner.setSelectedIndex(positionType);
        addEdtChangeListener();
    }


    @Override public String getTitleName() {
        return mTitleEdt.getText().toString().trim();
    }

    @Override public String getUserName() {
        return mUserNameEdt.getText().toString().trim();
    }

    @Override public String getPassWord() {
        return mPassWordEdt.getText().toString().trim();
    }

    @Override
    public void setTime(String time) {
        mTimeTextView.setVisibility(View.VISIBLE);
        mTimeTextView.setText("创建于：" + time);
    }

    @Override public void showSnackToast(String msg) {
        Snackbar.make(mToolBar, msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override public void setItemMenuVisible(boolean visible) {
        menuItem.setVisible(visible);
    }

    @Override
    public void finishActivity() {
        setResult(SUCCESS);
        finish();
    }

    @Override
    public void setPassWordVisible(boolean visible) {
        if (visible) {
            mPassWordEdt.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
        } else {
            mPassWordEdt.setTransformationMethod(HideReturnsTransformationMethod
                    .getInstance());
        }
        mPassWordEdt.setSelection(getPassWord().length());
    }

    private void showKeyBoard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override public void hideKeyBoard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mPassWordEdt.getWindowToken(), 0);
    }

    @Override
    public void setToolBarTitle(int resId) {
        mToolBar.setTitle(getResources().getString(resId));
    }

    @Override
    public void showSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("密码还未保存，是否先保存在退出");
        builder.setPositiveButton("保存", mEditImpl);
        builder.setNegativeButton("取消", mEditImpl);
        alertDialog = builder.show();
    }

    @Override
    public void hideSaveDialog() {
        if (null != alertDialog) {
            alertDialog.dismiss();
        }
    }
}
