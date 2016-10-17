package com.online.factory.factoryonline.modules.baidumap;

import android.support.annotation.NonNull;

import com.baidu.mapapi.clusterutil.clustering.Cluster;
import com.baidu.mapapi.clusterutil.clustering.ClusterItem;
import com.baidu.mapapi.clusterutil.clustering.algo.Algorithm;
import com.baidu.mapapi.clusterutil.projection.Bounds;
import com.baidu.mapapi.clusterutil.projection.Point;
import com.baidu.mapapi.clusterutil.projection.SphericalMercatorProjection;
import com.baidu.mapapi.clusterutil.quadtree.PointQuadTree;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by louiszgm-pc on 2016/10/15.
 */

public class MyAlgorithm<T extends ClusterItem> implements Algorithm<T> {
    public static  int MAX_DISTANCE_AT_ZOOM = 100; // essentially 100 dp.

    /**
     * Any modifications should be synchronized on mQuadTree.
     */
    private final Collection<QuadItem<T>> mItems = new ArrayList<QuadItem<T>>();

    /**
     * Any modifications should be synchronized on mQuadTree.
     */
    private final PointQuadTree<QuadItem<T>> mQuadTree = new PointQuadTree<QuadItem<T>>(0, 1, 0, 1);

    private static final SphericalMercatorProjection PROJECTION = new SphericalMercatorProjection(1);

    @Override
    public void addItem(T item) {
        final QuadItem<T> quadItem = new QuadItem<T>(item);
        synchronized (mQuadTree) {
            mItems.add(quadItem);
            mQuadTree.add(quadItem);
        }
    }

    @Override
    public void addItems(Collection<T> items) {
        for (T item : items) {
            addItem(item);
        }
    }

    @Override
    public void clearItems() {
        synchronized (mQuadTree) {
            mItems.clear();
            mQuadTree.clear();
        }
    }

    @Override
    public void removeItem(T item) {
        // TODO: delegate QuadItem#hashCode and QuadItem#equals to its item.
        throw new UnsupportedOperationException("NonHierarchicalDistanceBasedAlgorithm.remove not implemented");
    }

    /**
     *  cluster算法核心
     * @param zoom map的级别
     * @return
     */
    @Override
    public Set<? extends Cluster<T>> getClusters(double zoom) {
         Set<Cluster<T>> results = null;
        if(zoom < 11){
            //显示市级聚集点
            results = getClustersByZoomLevel(zoom);
        }else {
            results = getClustersByDistrict();
        }
// else if(zoom >= 11 && zoom < 13){
//            //显示区级，镇级 聚集点
//            results = getClustersByDistrict();
//        }else if(zoom >= 13){
//            //显示地方 街道聚集点
//
//        }
        return results;
    }
    @NonNull
    private Set<Cluster<T>> getClustersByDistrict(){
        Set<Cluster<T>> results = new HashSet<Cluster<T>>();
        final Set<QuadItem<T>> visitedCandidates = new HashSet<QuadItem<T>>();
        for(QuadItem<T> candidate:mItems){
            if (visitedCandidates.contains(candidate)) {
                // Candidate is already part of another cluster.
                continue;
            }
            int compareId = ((BaiduMapActivity.MyItem1)candidate.mClusterItem).getDistrictId();
            String description = ((BaiduMapActivity.MyItem1)candidate.mClusterItem).getDescription();
            com.baidu.mapapi.clusterutil.clustering.algo.StaticCluster<T> cluster =
                    new com.baidu.mapapi.clusterutil.clustering.algo
                            .StaticCluster<T>(candidate.mClusterItem.getPosition());
            cluster.setmDescription(description);
            results.add(cluster);
            for(QuadItem<T> clusterItem:mItems){
                int id = ((BaiduMapActivity.MyItem1)clusterItem.mClusterItem).getDistrictId();
                if(compareId == id){
                    cluster.add(clusterItem.mClusterItem);
                    visitedCandidates.add(clusterItem);
                }
            }
        }
        return results;
    }
    @NonNull
    private Set<Cluster<T>> getClustersByZoomLevel(double zoom) {
        final int discreteZoom = (int) zoom;

        final double zoomSpecificSpan = MAX_DISTANCE_AT_ZOOM / Math.pow(2, discreteZoom) / 256;

        final Set<QuadItem<T>> visitedCandidates = new HashSet<QuadItem<T>>();
        final Set<Cluster<T>> results = new HashSet<Cluster<T>>();
        final Map<QuadItem<T>, Double> distanceToCluster = new HashMap<QuadItem<T>, Double>();
        final Map<QuadItem<T>, com.baidu.mapapi.clusterutil.clustering.algo.StaticCluster<T>> itemToCluster =
                new HashMap<QuadItem<T>, com.baidu.mapapi.clusterutil.clustering.algo.StaticCluster<T>>();

        synchronized (mQuadTree) {
            for (QuadItem<T> candidate : mItems) {
                if (visitedCandidates.contains(candidate)) {
                    // Candidate is already part of another cluster.
                    continue;
                }

                Bounds searchBounds = createBoundsFromSpan(candidate.getPoint(), zoomSpecificSpan);
                Collection<QuadItem<T>> clusterItems;
                // search 某边界范围内的clusterItems
                clusterItems = mQuadTree.search(searchBounds);
                if (clusterItems.size() == 1) {
                    // Only the current marker is in range. Just add the single item to the results.
                    results.add(candidate);
                    visitedCandidates.add(candidate);
                    distanceToCluster.put(candidate, 0d);
                    continue;
                }
                com.baidu.mapapi.clusterutil.clustering.algo.StaticCluster<T> cluster =
                        new com.baidu.mapapi.clusterutil.clustering.algo
                                .StaticCluster<T>(candidate.mClusterItem.getPosition());
                results.add(cluster);

                for (QuadItem<T> clusterItem : clusterItems) {
                    Double existingDistance = distanceToCluster.get(clusterItem);
                    double distance = distanceSquared(clusterItem.getPoint(), candidate.getPoint());
                    if (existingDistance != null) {
                        // Item already belongs to another cluster. Check if it's closer to this cluster.
                        if (existingDistance < distance) {
                            continue;
                        }
                        // Move item to the closer cluster.
                        itemToCluster.get(clusterItem).remove(clusterItem.mClusterItem);
                    }
                    distanceToCluster.put(clusterItem, distance);
                    cluster.add(clusterItem.mClusterItem);
                    itemToCluster.put(clusterItem, cluster);
                }
                visitedCandidates.addAll(clusterItems);
            }
        }
        return results;
    }

    @Override
    public Collection<T> getItems() {
        final List<T> items = new ArrayList<T>();
        synchronized (mQuadTree) {
            for (QuadItem<T> quadItem : mItems) {
                items.add(quadItem.mClusterItem);
            }
        }
        return items;
    }

    private double distanceSquared(Point a, Point b) {
        return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
    }

    private Bounds createBoundsFromSpan(Point p, double span) {
        // TODO: Use a span that takes into account the visual size of the marker, not just its
        // LatLng.
        double halfSpan = span / 2;
        return new Bounds(
                p.x - halfSpan, p.x + halfSpan,
                p.y - halfSpan, p.y + halfSpan);
    }

    private static class QuadItem<T extends ClusterItem> implements PointQuadTree.Item, Cluster<T> {
        private final T mClusterItem;
        private final Point mPoint;
        private final LatLng mPosition;
        private Set<T> singletonSet;

        private QuadItem(T item) {
            mClusterItem = item;
            mPosition = item.getPosition();
            mPoint = PROJECTION.toPoint(mPosition);
            singletonSet = Collections.singleton(mClusterItem);
        }

        @Override
        public Point getPoint() {
            return mPoint;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Override
        public Set<T> getItems() {
            return singletonSet;
        }

        @Override
        public int getSize() {
            return 1;
        }
    }
}
