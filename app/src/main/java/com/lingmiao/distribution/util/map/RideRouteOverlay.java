package com.lingmiao.distribution.util.map;

import android.content.Context;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.RidePath;
import com.amap.api.services.route.RideStep;

import java.util.ArrayList;
import java.util.List;

/**
 * 骑行路线图层类。在高德地图API里，如果要显示步行路线规划，可以用此类来创建骑行路线图层。如不满足需求，也可以自己创建自定义的骑行路线图层。
 * @since V3.5.0
 */
public class RideRouteOverlay extends RouteOverlay {

	private PolylineOptions mPolylineOptions;
	
	private BitmapDescriptor rideStationDescriptor= null;

	private RidePath ridePath;
	/**
	 * 通过此构造函数创建骑行路线图层。
	 * @param context 当前activity。
	 * @param amap 地图对象。
	 * @param path 骑行路线规划的一个方案。详见搜索服务模块的路径查询包（com.amap.api.services.route）中的类 <strong><a href="../../../../../../Search/com/amap/api/services/route/WalkStep.html" title="com.amap.api.services.route中的类">WalkStep</a></strong>。
	 * @param start 起点。详见搜索服务模块的核心基础包（com.amap.api.services.core）中的类<strong><a href="../../../../../../Search/com/amap/api/services/core/LatLonPoint.html" title="com.amap.api.services.core中的类">LatLonPoint</a></strong>。
	 * @param end 终点。详见搜索服务模块的核心基础包（com.amap.api.services.core）中的类<strong><a href="../../../../../../Search/com/amap/api/services/core/LatLonPoint.html" title="com.amap.api.services.core中的类">LatLonPoint</a></strong>。
	 * @since V3.5.0
	 */
	public RideRouteOverlay(Context context, AMap amap, RidePath path,
                            LatLonPoint start, LatLonPoint end) {
		super(context);
		this.mAMap = amap;
		this.ridePath = path;
		startPoint = new LatLng(start.getLatitude(), start.getLongitude());
		endPoint =  new LatLng(end.getLatitude(), end.getLongitude());
	}
	/**
	 * 添加骑行路线到地图中。
	 * @since V3.5.0
	 */
	public void addToMap() {
		initPolylineOptions();
		try {
			List<RideStep> ridePaths = ridePath.getSteps();
			mPolylineOptions.add(startPoint);
			for (int i = 0; i < ridePaths.size(); i++) {
				RideStep rideStep = ridePaths.get(i);
				addRidePolyLines(rideStep);
			}
			mPolylineOptions.add(endPoint);
			addStartAndEndMarker();
			
			showPolyline();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}


	/**
	 * @param rideStep
	 */
	private void addRidePolyLines(RideStep rideStep) {
		ArrayList<LatLng> lineShapes = new ArrayList<LatLng>();
		for (LatLonPoint point : rideStep.getPolyline()) {
			LatLng latLngTemp =  new LatLng(point.getLatitude(), point.getLongitude());
			lineShapes.add(latLngTemp);
		}
		mPolylineOptions.addAll(lineShapes);
	}

	 /**
     * 初始化线段属性
     */
    private void initPolylineOptions() {
        mPolylineOptions = null;
        mPolylineOptions = new PolylineOptions();
        mPolylineOptions.color(getRideColor()).width(getRouteWidth());
    }
	 private void showPolyline() {
	        addPolyLine(mPolylineOptions);
	    }
}
