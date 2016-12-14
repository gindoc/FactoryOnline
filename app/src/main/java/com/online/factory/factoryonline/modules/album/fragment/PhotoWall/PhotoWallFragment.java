package com.online.factory.factoryonline.modules.album.fragment.PhotoWall;

import android.Manifest;
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
import com.online.factory.factoryonline.base.PermissionCallback;
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
import com.online.factory.factoryonline.utils.StatusBarUtils;
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
        PhotoWallContract.View{
    public static final int WRITE_PERMISSION_REQUEST_CODE = 198;
    public static final int READ_PERMISSION_REQUEST_CODE = 199;
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

    private ArrayList<String> uploadedImageKeys = new ArrayList<>();        // 已上传的imagekey
    private List<String> orderedImageKeys = new ArrayList<>();               // 排序好的imagekey，包含已上传的imagekey和准备上传图片的imagekey

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
        List<String> uploadedImage = getArguments().getStringArrayList(PhotoSelectedFragment
                .UPLOADED_PHOTO);
        uploadedImageKeys = getArguments().getStringArrayList(PhotoSelectedFragment.IMAGE_KEYS);
        if (uploadedImageKeys != null) {                            // 如果有传已上传图片的imagekey过来，则加入到排序好的imagekeys中
            orderedImageKeys.addAll(uploadedImageKeys);
        }
        if (requestCode == PublishRentalActivity.TO_PHOTO_SELECTED && uploadedImage.size() > 0) {
            toPhotoSelectedFragment((ArrayList<String>) uploadedImage);
        }

        initToolBar();
        initRecyclerview();

        checkPermission(READ_PERMISSION_REQUEST_CODE);

        mAdapter.getSubject().subscribe(new RxSubscriber() {
            @Override
            public void _onNext(Object o) {
                String imageKey = uploadedImageKeys.get((Integer) o);           // 删除已上传的image时，已上传的imagekeys是排序好的（在上传完毕时排序好）
                mPresenter.deleteImage(imageKey);
            }

            @Override
            public void _onError(Throwable throwable) {
                Timber.e(throwable.getMessage());
            }
        });
        return mBinding.getRoot();
    }

    private void initToolBar() {
        StatusBarUtils.from((Activity) getContext())
                //沉浸状态栏
                .setTransparentStatusbar(true)
                //白底黑字状态栏
                .setLightStatusBar(true)
                //设置toolbar,actionbar等view
                .setActionbarView(mBinding.toolbar)
                .process();
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
        checkPermission(WRITE_PERMISSION_REQUEST_CODE);
    }

    private void toCameraPage() {
        if (mAdapter.getUploadedItem().size() + mAdapter.getReadyToUpload().size() >= 9) {
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
        if (requestCode == READ_PERMISSION_REQUEST_CODE) {
            mPresenter.getPhotos();
        } else if (requestCode == WRITE_PERMISSION_REQUEST_CODE) {
            Toast.makeText(getContext(), "读写权限已打开，可以开始拍照了", Toast.LENGTH_SHORT).show();
            mPresenter.getPhotos();
        } else if (requestCode == ScanImageUtils.CHOOSE_CAPTURE) {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(getContext(), "未拍摄有图片...", Toast.LENGTH_SHORT).show();
                return;
            }
            File picture = new File(mImageCapturePath);
            if (picture.length() > 0) {
                Toast.makeText(getContext(), picture.toString(), Toast.LENGTH_SHORT).show();
                ArrayList<String> readyToUpload = (ArrayList<String>) mAdapter.getReadyToUpload();
                readyToUpload.add(mImageCapturePath);
                mPresenter.uploadImage(readyToUpload);
            }
        }
    }

    public void toPhotoSelectedFragment() {
        mPresenter.uploadImage(mAdapter.getReadyToUpload());
    }

    public void toPhotoSelectedFragment(ArrayList<String> selectedImagePath) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(PhotoSelectedFragment.UPLOADED_PHOTO, selectedImagePath);
        bundle.putStringArrayList(PhotoSelectedFragment.IMAGE_KEYS, uploadedImageKeys);
        photoSelectedFragment.setArguments(bundle);                              // 传送已选图片的路径给PhotoSelectedFragment
        startForResult(photoSelectedFragment, PhotoSelectedFragment.FROM_PHOTOWALL_FRAGMENT);
    }

    /**
     * 每次上传图片前，都将图片的imagekey加入到排序好的imagekeys中
     * @param imageKey    准备上传的图片的imagekey
     */
    @Override
    public void addImageKeyToOrderedImageKeys(String imageKey) {
        if (orderedImageKeys == null) {
            orderedImageKeys = new ArrayList<>();
        }
        orderedImageKeys.add(imageKey);
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
            List<String> selectedImage = data.getStringArrayList(PhotoSelectedFragment.UPLOADED_PHOTO);
            mAdapter.getUploadedItem().clear();
            mAdapter.getUploadedItem().addAll(selectedImage);
            mAdapter.getReadyToUpload().clear();
            mBinding.recyclerView.notifyDataSetChanged();
            uploadedImageKeys = data.getStringArrayList(PhotoSelectedFragment.IMAGE_KEYS);
            if (uploadedImageKeys != null) {
                orderedImageKeys.clear();
                orderedImageKeys.addAll(uploadedImageKeys);
            }
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

        List<String> selectedImage = (List<String>) getArguments().get(PhotoSelectedFragment.UPLOADED_PHOTO);
        if (selectedImage != null) {
            mAdapter.getUploadedItem().clear();
            mAdapter.getUploadedItem().addAll(selectedImage);
            mBinding.btnFinish.setVisibility(View.VISIBLE);
        }
        mBinding.recyclerView.notifyDataSetChanged();
    }

    /**
     * 每次删完图片后都将图片从已上传的图片列表中移除，并将图片的imagekey从“已上传图片的imagekeys”和“排序好的imagekeys”中移除
     * @param imageKey
     */
    @Override
    public void removeUploadedImage(String imageKey) {
        mAdapter.getUploadedItem().remove(uploadedImageKeys.indexOf(imageKey));
        uploadedImageKeys.remove(imageKey);
        orderedImageKeys.remove(imageKey);
        if (mAdapter.getUploadedItem().size() + mAdapter.getReadyToUpload().size() > 0) {
            mBinding.btnFinish.setVisibility(View.VISIBLE);
        }else {
            mBinding.btnFinish.setVisibility(View.GONE);
        }
    }

    /**
     * 每次上传图片完后，都将图片的imagekey添加到“已上传图片的imagekeys”，并判断是否上传完毕
     * 判断的依据是：“已上传图片的imagekeys”的大小是否等于“准备上传图片的列表”大小+“已上传图片的列表”大小
     * 如果上传完毕，则做两件事：
     * 1、将“准备上传图片的列表”添加到“已上传图片的列表”
     * 2、将“已上传图片的imagekeys”清空，并将“排序好的imagekeys”添加到“已上传图片的imagekeys”，这样“已上传图片的imagekeys”就是排序好的了
     * @param imageKey
     */
    @Override
    public void isToPhotoSlectedPage(String imageKey) {
        if (uploadedImageKeys == null) {
            uploadedImageKeys = new ArrayList<>();
        }
        if (imageKey != null) {
            uploadedImageKeys.add(imageKey);
        }
        if (uploadedImageKeys.size() == mAdapter.getReadyToUpload().size() + mAdapter.getUploadedItem().size()) {
            hideLoadingDialog();
            mAdapter.getUploadedItem().addAll(mAdapter.getReadyToUpload());
            uploadedImageKeys.clear();
            uploadedImageKeys.addAll(orderedImageKeys);
            toPhotoSelectedFragment((ArrayList<String>) mAdapter.getUploadedItem());
        }
    }

    private void checkPermission(final int permissionType) {
        performCodeWithPermission(getString(R.string.permission_storage_rationale), permissionType,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionCallback() {
                    @Override
                    public void hasPermission() {
                        if (permissionType == READ_PERMISSION_REQUEST_CODE) {
                            mPresenter.getPhotos();
                        } else if (permissionType == WRITE_PERMISSION_REQUEST_CODE) {
                            toCameraPage();
                        }
                    }

                    @Override
                    public void noPermission(Boolean hasPermanentlyDenied) {
                        if (hasPermanentlyDenied) {
                            alertAppSetPermission(getString(R.string.permission_storage_deny_again), permissionType);
                        }
                    }
                });

    }
}
