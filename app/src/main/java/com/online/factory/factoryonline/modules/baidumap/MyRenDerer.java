package com.online.factory.factoryonline.modules.baidumap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.ViewGroup;


import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudRgcInfo;
import com.baidu.mapapi.cloud.CloudRgcResult;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.clusterutil.clustering.Cluster;
import com.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.clusterutil.clustering.view.DefaultClusterRenderer;
import com.baidu.mapapi.clusterutil.ui.IconGenerator;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.online.factory.factoryonline.R;

import java.util.List;

import timber.log.Timber;

/**
 * Created by louiszgm-pc on 2016/10/14.
 */

public class MyRenDerer extends DefaultClusterRenderer {
    IconGenerator mIconGenerator;
    private  float mDensity;
    private ShapeDrawable mColoredCircleBackground;
    private Marker mMaker;
    public MyRenDerer(Context context, BaiduMap map, ClusterManager clusterManager) {
        super(context, map, clusterManager);
        mDensity = context.getResources().getDisplayMetrics().density;
        mIconGenerator = new IconGenerator(context);
        mIconGenerator.setContentView(makeSquareTextView(context));
        mIconGenerator.setTextAppearance(R.style.ClusterIcon_TextAppearance);
        mIconGenerator.setBackground(makeClusterBackground());

        CloudListener listen = new CloudListener() {
            @Override
            public void onGetSearchResult(CloudSearchResult result, int error) {

            }
            @Override
            public void onGetDetailSearchResult(DetailSearchResult result, int error) {
            }
            @Override
            public void onGetCloudRgcResult(CloudRgcResult result, int error) {
                //获取云反地理编码检索结果
                if(result.status == 0){
                    List<CloudRgcResult.PoiInfo> datas = result.pois;
                    String name = datas.get(0).name;
                        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(mIconGenerator.makeIcon(name));
                        mMaker.setIcon(descriptor);

                }
                CloudManager.getInstance().destroy();
            }
        };
        CloudManager.getInstance().init(listen);
    }

    private void loadPoi(double latitude , double longitude){
        CloudRgcInfo info = new CloudRgcInfo();
        info.geoTableId = 154773;
        String locationString = latitude+","+longitude;
        info.location = locationString;
        CloudManager.getInstance().rgcSearch(info);
    }
    @Override
    protected void onBeforeClusterRendered(Cluster cluster, MarkerOptions markerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions);
//        loadPoi(cluster.getPosition().latitude,cluster.getPosition().longitude);
    }

    @Override
    protected void onClusterRendered(Cluster cluster, Marker marker) {
        super.onClusterRendered(cluster, marker);
        mMaker = marker;
        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(mIconGenerator.makeIcon("东莞市"+cluster.getSize()));
        mMaker.setIcon(descriptor);
    }

    private LayerDrawable makeClusterBackground() {
        mColoredCircleBackground = new ShapeDrawable(new OvalShape());
        ShapeDrawable outline = new ShapeDrawable(new OvalShape());
        outline.getPaint().setColor(0x80ffffff); // Transparent white.
        LayerDrawable background = new LayerDrawable(new Drawable[]{outline, mColoredCircleBackground});
        int strokeWidth = (int) (mDensity * 3);
        background.setLayerInset(1, strokeWidth, strokeWidth, strokeWidth, strokeWidth);
        return background;
    }

    private com.baidu.mapapi.clusterutil.ui.SquareTextView makeSquareTextView(Context context) {
        com.baidu.mapapi.clusterutil.ui.SquareTextView squareTextView =
                new com.baidu.mapapi.clusterutil.ui.SquareTextView(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        squareTextView.setLayoutParams(layoutParams);
        squareTextView.setId(R.id.text);
        int twelveDpi = (int) (12 * mDensity);
        squareTextView.setPadding(twelveDpi, twelveDpi, twelveDpi, twelveDpi);
        return squareTextView;
    }
}
