/**  
 * Project Name:Android_Car_Example  
 * File Name:LocationTask.java  
 * Package Name:com.amap.api.car.example  
 * Date:2015年4月3日上午9:27:45  
 *  
 */

package com.amap.api.car.example;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

/**
 * ClassName:LocationTask <br/>
 * Function: 简单封装了定位请求，可以进行单次定位和多次定位，注意的是在app结束或定位结束时注意销毁定位 <br/>
 * Date: 2015年4月3日 上午9:27:45 <br/>
 * 
 * @author yiyi.qi
 * @version
 * @since JDK 1.6
 * @see
 */
public class LocationTask implements AMapLocationListener,
		OnLocationGetListener {

	private LocationManagerProxy mLocationManagerProxy;

	private static LocationTask mLocationTask;

	private Context mContext;

	private OnLocationGetListener mOnLocationGetlisGetListener;

	private RegeocodeTask mRegecodeTask;

	private LocationTask(Context context) {
		mLocationManagerProxy = LocationManagerProxy.getInstance(context);
		mRegecodeTask = new RegeocodeTask(context);
		mRegecodeTask.setOnLocationGetListener(this);
		mContext = context;
	}

	public void setOnLocationGetListener(
			OnLocationGetListener onGetLocationListener) {
		mOnLocationGetlisGetListener = onGetLocationListener;
	}

	public static LocationTask getInstance(Context context) {
		if (mLocationTask == null) {
			mLocationTask = new LocationTask(context);
		}
		return mLocationTask;
	}

	/**  
	 * 开启单次定位
	 */
	public void startSingleLocate() {
		mLocationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, -1, 10, this);
	}

	/**  
	 * 开启多次定位
	 */
	public void startLocate() {
		mLocationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, 5 * 1000, 10, this);
	}

	/**  
	 * 结束定位，可以跟多次定位配合使用
	 */
	public void stopLocate() {
		mLocationManagerProxy.removeUpdates(this);

	}

	/**  
	 * 销毁定位资源
	 */
	public void onDestroy() {
		mLocationManagerProxy.removeUpdates(this);
		mLocationManagerProxy.destroy();
	}

	@Override
	public void onLocationChanged(Location arg0) {

		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String arg0) {

		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {

		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (amapLocation != null && amapLocation.getAMapException() != null
				&& amapLocation.getAMapException().getErrorCode() == 0) {
			PositionEntity entity = new PositionEntity();
			entity.latitue = amapLocation.getLatitude();
			entity.longitude = amapLocation.getLongitude();

			if (!TextUtils.isEmpty(amapLocation.getAddress())) {
				entity.address = amapLocation.getAddress();
			}
			mOnLocationGetlisGetListener.onLocationGet(entity);

		}

	}

	@Override
	public void onLocationGet(PositionEntity entity) {

		// TODO Auto-generated method stub

	}

	@Override
	public void onRegecodeGet(PositionEntity entity) {

		// TODO Auto-generated method stub

	}

}
