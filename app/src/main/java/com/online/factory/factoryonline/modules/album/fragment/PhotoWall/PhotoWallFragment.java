package com.online.factory.factoryonline.modules.album.fragment.PhotoWall;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.base.BaseFragment;
import com.online.factory.factoryonline.customview.CustomDialog;
import com.online.factory.factoryonline.databinding.FragmentPhotoWallBinding;
import com.online.factory.factoryonline.databinding.ItemPhotowallTakePicBinding;
import com.online.factory.factoryonline.models.ImageFolderBean;
import com.online.factory.factoryonline.modules.album.AlbumActivity;
import com.online.factory.factoryonline.modules.album.fragment.PhotoFolder.PhotoFolderFragment;
import com.online.factory.factoryonline.modules.album.fragment.PhotoSelected.PhotoSelectedFragment;
import com.online.factory.factoryonline.modules.publishRental.PublishRentalActivity;
import com.online.factory.factoryonline.utils.FileUtils;
import com.online.factory.factoryonline.utils.ScanImageUtils;
import com.online.factory.factoryonline.utils.rx.RxSubscriber;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by cwenhui on 2016/10/19.
 */
public class PhotoWallFragment extends BaseFragment<PhotoWallContract.View, PhotoWallPresenter> implements
        PhotoWallContract.View/*, BaseRecyclerViewAdapter.OnItemClickListener*/ {
    public static final int TO_PHOTOFOLDER_FRAGMENT = 99;
    public static final String SELECTED_FOLDER_INDEX = "selectedFolderIndex";
    public static final int TO_PHOTOSELECTED_FRAGMENT = 98;
    private FragmentPhotoWallBinding mBinding;
    private ItemPhotowallTakePicBinding mTakePicBinding;
    /**
     * 拍照后图片的存储路径
     */
    private static String mImageCapturePath = "";
    @Inject
    PhotoWallPresenter mPresenter;

    @Inject
    PhotoWallAdapter mAdapter;

    @Inject
    PhotoFolderFragment photoFolderFragment;

    @Inject
    PhotoSelectedFragment photoSelectedFragment;

    @Inject
    public PhotoWallFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        mBinding = FragmentPhotoWallBinding.inflate(inflater);
        mTakePicBinding = ItemPhotowallTakePicBinding.inflate(inflater);

        mBinding.setView(this);
        mTakePicBinding.setView(this);

        int requestCode = getArguments().getInt(PublishRentalActivity.REQUEST_CODE);
        List<String> selectedImage = getArguments().getStringArrayList(PhotoSelectedFragment
                .SELECTED_PHOTO);
        if (requestCode == PublishRentalActivity.TO_PHOTO_SELECTED && selectedImage.size()>0) {
            toPhotoSelectedFragment((ArrayList<String>) selectedImage);
        }
        initToolBar();
        initRecyclerview();

        mPresenter.getPhotos();

        mAdapter.getSubject().subscribe(new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                mAdapter.getmSelectedItem().get((Integer) o);
            }

            @Override
            public void _onError(Throwable throwable) {
                Timber.e(throwable.getMessage());
            }
        });
        return mBinding.getRoot();
    }

    private void initToolBar() {
        mBinding.toolbar.setTitle("");
        ((AlbumActivity) getActivity()).setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_close);

        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private void initRecyclerview() {
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mBinding.recyclerView.addHeader(mTakePicBinding.getRoot());
    }

    @Override
    protected PhotoWallPresenter createPresent() {
        return mPresenter;
    }

    @Override
    public <T> LifecycleTransformer<T> getBindToLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showError(String error) {
    }

    @Override
    public void showLoadingDialog() {
        CustomDialog.createLoadingDialog(getContext()).show();
    }

    @Override
    public void hideLoadingDialog() {
        CustomDialog.dismissDialog();
    }

    public void takePic(View view) {
        if (mAdapter.getmSelectedItem().size() >= 9) {
            Toast.makeText(getContext(), "最多选择9张图片", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = FileUtils.createTempImage(getContext());
        mImageCapturePath = file.toString();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, ScanImageUtils.CHOOSE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(getContext(), "未拍摄有图片...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == ScanImageUtils.CHOOSE_CAPTURE) {
            File picture = new File(mImageCapturePath);
            if (picture.length() > 0) {
                Toast.makeText(getContext(), picture.toString(), Toast.LENGTH_SHORT).show();
                ArrayList<String> selectedImagePath = (ArrayList<String>) mAdapter.getmSelectedItem();
                selectedImagePath.add(mImageCapturePath);
                toPhotoSelectedFragment(selectedImagePath);
            }
        }
    }

    public void toPhotoSelectedFragment() {
        toPhotoSelectedFragment((ArrayList<String>) mAdapter.getmSelectedItem());
    }

    private void toPhotoSelectedFragment(ArrayList<String> selectedImagePath) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(PhotoSelectedFragment.SELECTED_PHOTO, selectedImagePath);
        //传送已选图片的路径给PhotoSelectedFragment
        photoSelectedFragment.setArguments(bundle);
        startForResult(photoSelectedFragment, PhotoSelectedFragment.FROM_PHOTOWALL_FRAGMENT);
    }

    public void switchAlbum(View view) {
        startForResult(photoFolderFragment, PhotoFolderFragment.FROM_PHOTOWALL_FRAGMENT);
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == PhotoFolderFragment.FROM_PHOTOWALL_FRAGMENT && resultCode ==
                TO_PHOTOFOLDER_FRAGMENT) {
            int selectedIndex = data.getInt(SELECTED_FOLDER_INDEX);
            ImageFolderBean bean = ScanImageUtils.getmImageFloderBeens().get(selectedIndex);
            File imageDir = new File(bean.getDir());
            List<String> imagePath = Arrays.asList(imageDir.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg"))
                        return true;
                    return false;
                }
            }));
            mAdapter.setData(imagePath);
            mAdapter.setmDirPath(imageDir.getAbsolutePath());
            mBinding.recyclerView.notifyDataSetChanged();
        } else if (requestCode == PhotoSelectedFragment.FROM_PHOTOWALL_FRAGMENT && resultCode ==
                TO_PHOTOSELECTED_FRAGMENT && data != null) {
            List<String> selectedImage = data.getStringArrayList(PhotoSelectedFragment.SELECTED_PHOTO);
            mAdapter.getmSelectedItem().clear();
            mAdapter.getmSelectedItem().addAll(selectedImage);
            mBinding.recyclerView.notifyDataSetChanged();
        }
    }

    @Override
    public void initRecyclerview(File maxImgDir, int imgCount, List<ImageFolderBean> beanList) {
        if (maxImgDir == null) {
            Toast.makeText(getContext(), "擦，一张图片没扫描到", Toast.LENGTH_SHORT).show();
            return;
        }
        mAdapter.setData(Arrays.asList(maxImgDir.list()));
        mAdapter.setmDirPath(maxImgDir.getAbsolutePath());
        photoFolderFragment.setmFolderBeens(beanList);

        List<String> selectedImage = (List<String>) getArguments().get(PhotoSelectedFragment.SELECTED_PHOTO);
        if (selectedImage != null) {
            mAdapter.getmSelectedItem().clear();
            mAdapter.getmSelectedItem().addAll(selectedImage);
            mBinding.btnFinish.setVisibility(View.VISIBLE);
        }
        mBinding.recyclerView.notifyDataSetChanged();
    }

}
