package com.online.factory.factoryonline.modules.personalInfo.fragments.personalInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.customview.CustomDialog;
import com.online.factory.factoryonline.databinding.FragmentPersonalInfoBinding;
import com.online.factory.factoryonline.models.User;
import com.online.factory.factoryonline.modules.login.LogOutState;
import com.online.factory.factoryonline.modules.login.LoginContext;
import com.online.factory.factoryonline.modules.personalInfo.fragments.modifyName.ModifyNameFragment;
import com.online.factory.factoryonline.modules.personalInfo.fragments.modifyPwd.ModifyPwdFragment;
import com.online.factory.factoryonline.utils.StatusBarUtils;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.inject.Inject;

/**
 * 作者: GIndoc
 * 日期: 2016/12/3 16:47
 * 作用:
 */

public class PersonalInfoFragment extends BaseFragment<PersonalInfoContract.View, PersonalInfoPresenter> implements PersonalInfoContract.View {
    private static final int MODIFY_NAME_REQUEST_CODE = 199;
    private static final int MODIFY_PWD_REQUEST_CODE = 200;
    private static final int SELECT_IMAGE_REQUEST_CODE = 201;
    private FragmentPersonalInfoBinding mBinding;

    @Inject
    PersonalInfoPresenter mPresenter;

    @Inject
    ModifyNameFragment modifyNameFragment;

    @Inject
    ModifyPwdFragment modifyPwdFragment;

    @Inject
    LoginContext mLoginContext;
    private User user;

    @Inject
    public PersonalInfoFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected PersonalInfoPresenter createPresent() {
        return mPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentPersonalInfoBinding.inflate(inflater);
        mBinding.setView(this);
        mBinding.setPresenter(mPresenter);
        StatusBarUtils.from(getActivity())
                //白底黑字状态栏
                .setLightStatusBar(true)
                //设置toolbar,actionbar等view
                .setActionbarView(mBinding.rlTopBar)
                .process();
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getUser();
    }

    public void modifyName() {
        Bundle bundle = new Bundle();
//        String name = user.getUserName() == null ? "" : user.getUserName();
//        bundle.putString(ModifyNameFragment.USER_NAME, name);
        bundle.putString(ModifyNameFragment.USER_NAME, user.getUserName());
        modifyNameFragment.setArguments(bundle);
        startForResult(modifyNameFragment, MODIFY_NAME_REQUEST_CODE);
    }

    public void modifyPwd() {
        Bundle bundle = new Bundle();
        bundle.putString(ModifyPwdFragment.PHONE_NUM, user.getPhone_num());
        modifyPwdFragment.setArguments(bundle);
        startForResult(modifyPwdFragment, MODIFY_PWD_REQUEST_CODE);
    }

    public void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE);
    }

    public void finish() {
        getActivity().finish();
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        mPresenter.getUser();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContext().getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            mPresenter.uploadImage(picturePath);
        }
    }

    @Override
    public void showLoading() {
        CustomDialog.createLoadingDialog(getContext()).show();
    }

    @Override
    public void hideLoading() {
        CustomDialog.dismissDialog();
    }

    @Override
    public void refreshWhenLogOut() {
        Toast.makeText(getContext(), "注销成功", Toast.LENGTH_SHORT).show();
        mLoginContext.setmState(new LogOutState());
        getActivity().finish();
    }

    @Override
    public void showUser(User user) {
        this.user = user;
        mBinding.setUser(user);
    }

    @Override
    public void unLogin() {
        Toast.makeText(getContext(), "当前未登陆，请先登录", Toast.LENGTH_SHORT).show();
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.unLogin)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getActivity().finish();
                    }
                }).create().show();
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {

    }
}
