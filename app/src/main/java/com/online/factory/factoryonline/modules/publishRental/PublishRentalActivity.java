package com.online.factory.factoryonline.modules.publishRental;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.databinding.ActivityPublishRentalBinding;
import com.online.factory.factoryonline.models.post.Publish;
import com.online.factory.factoryonline.modules.album.AlbumActivity;
import com.online.factory.factoryonline.modules.album.fragment.PhotoSelected.PhotoSelectedFragment;
import com.online.factory.factoryonline.utils.Saver;
import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

/**
 * Created by cwenhui on 2016/10/19.
 */
public class PublishRentalActivity extends BaseActivity<PublishRentalContract.View, PublishRentalPresenter> implements PublishRentalContract.View {
    public static final String REQUEST_CODE = "requestCode";
    public static final int TO_PHOTO_WALL = 68;
    public static final int TO_PHOTO_SELECTED = 69;
    private ActivityPublishRentalBinding mBinding;
    public static final int TO_PHOTO_WALL_PICK_IMAGE = 78;
    public static final int ALBUM_ACTIVITY_RESULT_OK = 79;

    @Inject
    PublishRentalPresenter mPresenter;
    private List<String> mSelectedImage = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_publish_rental);

        initView();
        mBinding.setView(this);

        initToolBar();
    }

    private void initView() {
        Publish publish = Saver.getPublish();
        if (publish != null) {
            mBinding.tvDistrict.setText(publish.getDistrict_id() + "");
            mBinding.etAddress.setText(publish.getAddress());
            if (publish.getRange() != 0) {
                mBinding.etRange.setText(publish.getRange() + "");
            }
            if (publish.getPrice() != 0) {
                mBinding.etPrice.setText(publish.getPrice() + "");
            }
            mBinding.etTitle.setText(publish.getTitle());
            mBinding.tvDescription.setText(publish.getDescription());
            mBinding.etContactName.setText(publish.getContact_name());
            mBinding.etContactNum.setText(publish.getContact_num() + "");
            mSelectedImage.clear();
            mSelectedImage.addAll(Arrays.asList(publish.getPics()));
            ViewTreeObserver observer = mBinding.ivSelectedImg.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    loadSelectedImage();
                    return true;
                }
            });
       }
    }

    private void initToolBar() {
        mBinding.toolbar.setTitle("");
        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
    }

    @Override
    protected PublishRentalPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {
    }

    public void toAlbumActivity() {
        Intent intent = new Intent(this, AlbumActivity.class);
        startActivityForResult(intent, TO_PHOTO_WALL_PICK_IMAGE);
    }

    public void pickMore(){
        Intent intent = new Intent(this, AlbumActivity.class);
        intent.putStringArrayListExtra(PhotoSelectedFragment.SELECTED_PHOTO, (ArrayList<String>)
                mSelectedImage);
        intent.putExtra(REQUEST_CODE, TO_PHOTO_WALL);
        startActivityForResult(intent, TO_PHOTO_WALL_PICK_IMAGE);
    }

    public void viewSelectedImage(){
        Intent intent = new Intent(this, AlbumActivity.class);
        intent.putStringArrayListExtra(PhotoSelectedFragment.SELECTED_PHOTO, (ArrayList<String>)
                mSelectedImage);
        intent.putExtra(REQUEST_CODE, TO_PHOTO_SELECTED);
        startActivityForResult(intent, TO_PHOTO_WALL_PICK_IMAGE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    public void savePublishRental() {
        Publish publish = savePublish();
        Saver.savePublish(publish);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO_PHOTO_WALL_PICK_IMAGE && resultCode == ALBUM_ACTIVITY_RESULT_OK && data != null) {
            mSelectedImage.clear();
            mSelectedImage.addAll(data.getStringArrayListExtra(PhotoSelectedFragment.SELECTED_PHOTO));
            loadSelectedImage();
        }
    }

    private void loadSelectedImage() {
        if (mSelectedImage.size() > 0) {
            File file = new File(mSelectedImage.get(0));
            Picasso.with(this).load(file)
                    .placeholder(R.drawable.demo)
                    .resize(mBinding.ivSelectedImg.getMeasuredWidth(), mBinding.ivSelectedImg.getMeasuredHeight())
                    .config(Bitmap.Config.RGB_565)
                    .into(mBinding.ivSelectedImg);
            mBinding.ivPickImg.setVisibility(View.GONE);
            mBinding.tvPickImg.setVisibility(View.GONE);
            mBinding.ivPicMore.setVisibility(View.VISIBLE);
        }
    }

    @NonNull
    private Publish savePublish() {
        Publish publish = new Publish();
        if (mBinding.tvDistrict.getTag() != null) {
            int districtId = (int) mBinding.tvDistrict.getTag();
            publish.setDistrict_id(districtId);
        }
        String address = mBinding.etAddress.getText().toString();
        String range = mBinding.etRange.getText().toString();
        String price = mBinding.etPrice.getText().toString();
        String title = mBinding.etTitle.getText().toString();
        String description = mBinding.tvDescription.getText().toString();
        String contactName = mBinding.etContactName.getText().toString();
        String contactNum = mBinding.etContactNum.getText().toString();
        String pattern = "[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";
        Pattern r = Pattern.compile(pattern);
        if (TextUtils.isEmpty(address)) {
            publish.setAddress(address);
        }
        Matcher m = r.matcher(range);
        if (m.find() && TextUtils.isEmpty(range)) {
            publish.setRange(Float.parseFloat(range));
        }
        m = r.matcher(price);
        if (m.find() && TextUtils.isEmpty(price)) {
            publish.setPrice(Float.parseFloat(price));
        }
        if (TextUtils.isEmpty(title)) {
            publish.setTitle(title);
        }
        if (TextUtils.isEmpty(description)) {
            publish.setDescription(description);
        }
        if (TextUtils.isEmpty(contactName)) {
            publish.setContact_num(contactName);
        }
        m = r.matcher(contactNum);
        if (m.find() && TextUtils.isEmpty(contactNum)) {
            publish.setContact_num(contactNum);
        }
        publish.setPics((String[]) mSelectedImage.toArray(new String[mSelectedImage.size()]));
        return publish;
    }
}
