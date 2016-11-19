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
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseActivity;
import com.online.factory.factoryonline.customview.CustomDialog;
import com.online.factory.factoryonline.data.local.SharePreferenceKey;
import com.online.factory.factoryonline.databinding.ActivityPublishRentalBinding;
import com.online.factory.factoryonline.models.Area;
import com.online.factory.factoryonline.models.exception.ValidateException;
import com.online.factory.factoryonline.models.post.Publish;
import com.online.factory.factoryonline.modules.album.AlbumActivity;
import com.online.factory.factoryonline.modules.album.fragment.PhotoSelected.PhotoSelectedFragment;
import com.online.factory.factoryonline.modules.publishRental.PrePay.PrePayActivity;
import com.online.factory.factoryonline.modules.publishRental.RentType.RentTypeActivity;
import com.online.factory.factoryonline.modules.publishRental.area.AreaActivity;
import com.online.factory.factoryonline.modules.publishRental.tag.TagActivity;
import com.online.factory.factoryonline.utils.GeoHash;
import com.online.factory.factoryonline.utils.Saver;
import com.online.factory.factoryonline.utils.Validate;
import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by cwenhui on 2016/10/19.
 */
public class PublishRentalActivity extends BaseActivity<PublishRentalContract.View, PublishRentalPresenter> implements PublishRentalContract.View, OnGetGeoCoderResultListener {
    public static final String REQUEST_CODE = "requestCode";
    public static final int TO_PHOTO_WALL = 68;
    public static final int TO_PHOTO_SELECTED = 69;
    private static final int TO_TAGS_SELECTED = 59;
    private static final int TO_AREA_SELECTED = 49;
    private static final int TO_RENT_TYPE_ACTIVITY = 39;
    private static final int TO_PRE_PAY_ACTIVITY = 29;
    private ActivityPublishRentalBinding mBinding;
    public static final int TO_PHOTO_WALL_PICK_IMAGE = 78;
    public static final int ALBUM_ACTIVITY_RESULT_OK = 79;

    @Inject
    PublishRentalPresenter mPresenter;
    private List<String> mSelectedImage = new ArrayList<>();
    private List<String> imageKeys = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private GeoCoder geoCoder;
    private Publish publish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_publish_rental);

        initView();
        mBinding.setView(this);

        ViewTreeObserver observer = getWindow().getDecorView().getViewTreeObserver();
        observer.addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                Timber.e("focus changed");
            }
        });

        // 获取反地理编码对象
        geoCoder = GeoCoder.newInstance();
        // 设置查询结果监听者
        geoCoder.setOnGetGeoCodeResultListener(this);
    }

    private void initView() {
        Publish publish = Saver.getSerializableObject(SharePreferenceKey.PUBLISH);
        if (publish != null) {
            mPresenter.getArea(publish.getArea_id());

            mBinding.etDetailAddress.setText(publish.getAddress());
            if (publish.getRange() != 0) {
                mBinding.etRange.setText(publish.getRange() + "");
            }
            if (publish.getPrice() != 0) {
                mBinding.etPrice.setText(publish.getPrice() + "");
            }
            mBinding.etTitle.setText(publish.getTitle());
            mBinding.etDescription.setText(publish.getDescription());
            mBinding.etContactName.setText(publish.getContact_name());
            mBinding.etContactNum.setText(publish.getContact_num());
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

        mBinding.ivSelectedImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PublishRentalActivity.this, AlbumActivity.class);
                intent.putStringArrayListExtra(PhotoSelectedFragment.UPLOADED_PHOTO, (ArrayList<String>)
                        mSelectedImage);
                intent.putStringArrayListExtra(PhotoSelectedFragment.IMAGE_KEYS, (ArrayList<String>) imageKeys);
                intent.putExtra(REQUEST_CODE, TO_PHOTO_SELECTED);
                startActivityForResult(intent, TO_PHOTO_WALL_PICK_IMAGE);
            }
        });
        mBinding.ivSelectedImg.setClickable(false);
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

    public void pickMore() {
        Intent intent = new Intent(this, AlbumActivity.class);
        intent.putStringArrayListExtra(PhotoSelectedFragment.UPLOADED_PHOTO, (ArrayList<String>)
                mSelectedImage);
        intent.putStringArrayListExtra(PhotoSelectedFragment.IMAGE_KEYS, (ArrayList<String>) imageKeys);
        intent.putExtra(REQUEST_CODE, TO_PHOTO_WALL);
        startActivityForResult(intent, TO_PHOTO_WALL_PICK_IMAGE);
    }

    public void openPublishArea() {
        Intent intent = AreaActivity.getStartIntent(this);
        intent.putExtra(AreaActivity.SELECTED_AREA, mBinding.tvArea.getText().toString());
        startActivityForResult(intent, TO_AREA_SELECTED);
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    public void openRentTypePage() {
        Intent intent = RentTypeActivity.getStartIntent(this);
        intent.putExtra(RentTypeActivity.SELECTED_RENT_TYPE, mBinding.tvRentType.getText().toString());
        startActivityForResult(intent, TO_RENT_TYPE_ACTIVITY);
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    public void openPrePay() {
        Intent intent = PrePayActivity.getStartIntent(this);
        intent.putExtra(PrePayActivity.SELECTED_PRE_PAY, mBinding.tvPrePay.getText().toString());
        startActivityForResult(intent, TO_PRE_PAY_ACTIVITY);
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    public void openTagPage() {
        Intent intent = TagActivity.getStartIntent(this);
//        intent.pu
        startActivityForResult(intent, TO_TAGS_SELECTED);
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }
    public void submit() {
        checkContent();
//        Publish publish =
//        if (publish != null) {
//            mPresenter.publishMessage(publish);
//        }
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
        Saver.saveSerializableObject(publish, SharePreferenceKey.PUBLISH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO_PHOTO_WALL_PICK_IMAGE && resultCode == ALBUM_ACTIVITY_RESULT_OK && data != null) {
            mSelectedImage.clear();
            mSelectedImage.addAll(data.getStringArrayListExtra(PhotoSelectedFragment.UPLOADED_PHOTO));
            imageKeys.clear();
            imageKeys.addAll(data.getStringArrayListExtra(PhotoSelectedFragment.IMAGE_KEYS));
            loadSelectedImage();
        } else if (requestCode == TO_TAGS_SELECTED && resultCode == RESULT_OK && data != null) {
//            tags.add(data.getStringArrayListExtra());
        } else if (requestCode == TO_AREA_SELECTED && resultCode == RESULT_OK && data != null) {
            Area area = (Area) data.getSerializableExtra(AreaActivity.SELECTED_AREA);
            mBinding.tvArea.setText(area.getName());
            mBinding.tvArea.setTag(area.getId());
        } else if (requestCode == TO_RENT_TYPE_ACTIVITY && resultCode == RESULT_OK && data != null) {
            String rentType = data.getStringExtra(RentTypeActivity.SELECTED_RENT_TYPE);
            mBinding.tvRentType.setText(rentType);
        } else if (requestCode == TO_PRE_PAY_ACTIVITY && resultCode == RESULT_OK && data != null) {
            String prePay = data.getStringExtra(PrePayActivity.SELECTED_PRE_PAY);
            mBinding.tvPrePay.setText(prePay);
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
            mBinding.llPickMore.setVisibility(View.VISIBLE);
            mBinding.tvPickMore.setText(mSelectedImage.size() + "/9");
            mBinding.ivSelectedImg.setClickable(true);

        }
    }

    private Publish checkContent() {
        publish = new Publish();
        try {
            publish.setTitle(checkCont(mBinding.etTitle, "请输入标题"));
            publish.setDescription(checkCont(mBinding.etDescription, "请输入描述内容"));
            publish.setAddress(checkCont(mBinding.etDetailAddress, "请输入详细地址"));
            publish.setRange(Float.parseFloat(checkCont(mBinding.etRange, "请输入面积")));
            publish.setPrice(Float.parseFloat(checkCont(mBinding.etPrice, "请输入价格")));
            publish.setContact_name(checkCont(mBinding.etContactName, "请输入联系人姓名"));
            publish.setPre_pay(checkCont(mBinding.tvPrePay, "请选择您需要的押金金额"));
            publish.setRent_type(checkCont(mBinding.tvRentType, "请选择出租方式"));

            String phoneNum = checkCont(mBinding.etContactNum, "请输入联系电话");
            Validate.validatePhoneNum(phoneNum);
            publish.setContact_num(phoneNum);
            if (!TextUtils.isEmpty(checkCont(mBinding.tvArea, "请选择发布的镇区"))) {
                publish.setArea_id((Integer) mBinding.tvArea.getTag());
            }
            tags.add("123");
            tags.add("456");
            tags.add("789");
            if (tags.size() > 0) {
                publish.setTags(tags.toArray(new String[tags.size()]));
            } else {
                throw new Exception("请选择您需要的标签");
            }
            if (mSelectedImage.size() > 0) {
                publish.setPics(imageKeys.toArray(new String[imageKeys.size()]));
            } else {
                throw new Exception("请选择您需要的厂房图片");
            }
            publish.setCity_id(305);
            getGeoHash("东莞", mBinding.tvArea.getText().toString() + mBinding.etDetailAddress.getText().toString());
            return publish;
        } catch (ValidateException e) {
            Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
            return null;
        } catch (NumberFormatException nfe) {
            Toast.makeText(this, "请输入正确的面积或价格", Toast.LENGTH_SHORT).show();
            return null;
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public String checkCont(TextView textView, String exception) throws Exception {
        if (TextUtils.isEmpty(textView.getText())) {
            throw new Exception(exception);
        }
        return textView.getText().toString();
    }

    @NonNull
    private Publish savePublish() {
        Publish publish = new Publish();
        if (mBinding.tvArea.getTag() != null) {
            int areaId = (int) mBinding.tvArea.getTag();
            publish.setArea_id(areaId);
        }

        String title = mBinding.etTitle.getText().toString();
        String description = mBinding.etDescription.getText().toString();
        String area = mBinding.tvArea.getText().toString();
        String address = mBinding.etDetailAddress.getText().toString();
        String range = mBinding.etRange.getText().toString();
        String price = mBinding.etPrice.getText().toString();
        String contactName = mBinding.etContactName.getText().toString();
        String contactNum = mBinding.etContactNum.getText().toString();

        if (!TextUtils.isEmpty(address)) {
            publish.setAddress(address);
        }
        if (!TextUtils.isEmpty(range)) {
            publish.setRange(Float.parseFloat(range));
        }
        if (!TextUtils.isEmpty(price)) {
            publish.setPrice(Float.parseFloat(price));
        }
        if (!TextUtils.isEmpty(title)) {
            publish.setTitle(title);
        }
        if (!TextUtils.isEmpty(description)) {
            publish.setDescription(description);
        }
        if (!TextUtils.isEmpty(contactName)) {
            publish.setContact_num(contactName);
        }
        if (!TextUtils.isEmpty(contactNum)) {
            try {
                Validate.validatePhoneNum(contactNum);
                publish.setContact_num(contactNum);
            } catch (ValidateException e) {
                CustomDialog.createAlertDialog(this, "请输入正确的手机号", contactNum).show();
                e.printStackTrace();
            }
        }
        publish.setPics(mSelectedImage.toArray(new String[mSelectedImage.size()]));
        // TODO: 2016/11/15 是否需要保存imagekeys?
        return publish;
    }

    @Override
    public void setArea(String name) {
        mBinding.tvArea.setText(name);
    }

    public void getGeoHash(String city, String address) {
        if (TextUtils.isEmpty(city) || TextUtils.isEmpty(address)) {
            return;
        }
        GeoCodeOption geoCodeOption = new GeoCodeOption();
        geoCodeOption.address(address);
        geoCodeOption.city(city);
        geoCoder.geocode(geoCodeOption);
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if (geoCodeResult != null) {
            double longitude = geoCodeResult.getLocation().longitude;
            double latitude = geoCodeResult.getLocation().latitude;
            GeoHash geoHash = new GeoHash();
            publish.setGeohash(geoHash.encode(latitude, longitude));
            mPresenter.publishMessage(publish);
        }
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
    }

    @Override
    protected void onDestroy() {
        geoCoder.destroy();
        super.onDestroy();
    }
}
