package com.amap.api.car.example;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.car.example.RouteTask.OnRouteCalculateListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnCameraChangeListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

public class MainActivity extends Activity implements OnCameraChangeListener,
		OnMapLoadedListener, OnLocationGetListener, OnClickListener,
		OnRouteCalculateListener {

	private MapView mMapView;

	private AMap mAmap;

	private TextView mAddressTextView;

	private Button mDestinationButton;

	private Marker mPositionMark;

	private LatLng mStartPosition;

	private RegeocodeTask mRegeocodeTask;

	private LinearLayout mDestinationContainer;


	private TextView mRouteCostText;

	private TextView mDesitinationText;

	private LocationTask mLocationTask;

	private ImageView mLocationImage;

	private LinearLayout mFromToContainer;
	
	private Button mCancelButton;

	private boolean mIsFirst = true;

	public interface OnGetLocationListener {
		public void getLocation(String locationAddress);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init(savedInstanceState);
		mLocationTask = LocationTask.getInstance(getApplicationContext());
		mLocationTask.setOnLocationGetListener(this);

	}

	private void init(Bundle savedInstanceState) {
		mAddressTextView = (TextView) findViewById(R.id.address_text);
		mDestinationButton = (Button) findViewById(R.id.destination_button);
		mDestinationButton.setOnClickListener(this);
		mMapView = (MapView) findViewById(R.id.map);
		mMapView.onCreate(savedInstanceState);
		mAmap = mMapView.getMap();
		mAmap.getUiSettings().setZoomControlsEnabled(false);
		mAmap.setOnMapLoadedListener(this);
		mAmap.setOnCameraChangeListener(this);
		mRegeocodeTask = new RegeocodeTask(getApplicationContext());
		mDestinationContainer = (LinearLayout) findViewById(R.id.destination_container);
		mRouteCostText = (TextView) findViewById(R.id.routecost_text);
		mDesitinationText = (TextView) findViewById(R.id.destination_text);
		mDesitinationText.setOnClickListener(this);
		mLocationImage = (ImageView) findViewById(R.id.location_image);
		mLocationImage.setOnClickListener(this);
		mFromToContainer = (LinearLayout) findViewById(R.id.fromto_container);
		mCancelButton=(Button) findViewById(R.id.cancel_button);
		RouteTask.getInstance(getApplicationContext())
				.addRouteCalculateListener(this);

	}

	private void hideView() {

		mFromToContainer.setVisibility(View.GONE);
		mDestinationButton.setVisibility(View.GONE);
		mCancelButton.setVisibility(View.GONE);
	}

	private void showView() {
		mFromToContainer.setVisibility(View.VISIBLE);
		mDestinationButton.setVisibility(View.VISIBLE);
		mCancelButton.setVisibility(View.VISIBLE);
	}

	@Override
	public void onCameraChange(CameraPosition arg0) {
		hideView();
	}

	@Override
	public void onCameraChangeFinish(CameraPosition cameraPosition) {
		showView();
		mStartPosition = cameraPosition.target;
		mRegeocodeTask.setOnLocationGetListener(this);
		mRegeocodeTask
				.search(mStartPosition.latitude, mStartPosition.longitude);
		if (mIsFirst) {
			Utils.addEmulateData(mAmap, mStartPosition);
			mPositionMark.setToTop();
			mIsFirst = false;
		}
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
		mLocationTask.onDestroy();
	}

	@Override
	public void onMapLoaded() {

		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.setFlat(true);
		markerOptions.anchor(0.5f, 0.5f);
		markerOptions.position(new LatLng(0, 0));
		markerOptions.icon(
				BitmapDescriptorFactory.fromBitmap(
				BitmapFactory.decodeResource(getResources(), R.drawable.icon_loaction_start)
			)	
		);
		mPositionMark = mAmap.addMarker(markerOptions);

		mPositionMark.setPositionByPixels(mMapView.getWidth() / 2,
				mMapView.getHeight() / 2);
		mLocationTask.startSingleLocate();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.destination_button:
			Intent intent = new Intent(this, DestinationActivity.class);
			startActivity(intent);
			break;
		case R.id.location_image:
			mLocationTask.startSingleLocate();
			break;
		case R.id.destination_text:
			Intent destinationIntent = new Intent(this,
					DestinationActivity.class);
			startActivity(destinationIntent);
			break;
		}
	}

	@Override
	public void onLocationGet(PositionEntity entity) {
		// todo 这里在网络定位时可以减少一个逆地理编码
		mAddressTextView.setText(entity.address);
		RouteTask.getInstance(getApplicationContext()).setStartPoint(entity);

		mStartPosition = new LatLng(entity.latitue, entity.longitude);
		CameraUpdate cameraUpate = CameraUpdateFactory.newLatLngZoom(
				mStartPosition, mAmap.getCameraPosition().zoom);
		mAmap.animateCamera(cameraUpate);

	}

	@Override
	public void onRegecodeGet(PositionEntity entity) {
		mAddressTextView.setText(entity.address);
		entity.latitue = mStartPosition.latitude;
		entity.longitude = mStartPosition.longitude;
		RouteTask.getInstance(getApplicationContext()).setStartPoint(entity);
		RouteTask.getInstance(getApplicationContext()).search();
	}

	@Override
	public void onRouteCalculate(float cost,float distance,int duration) {
		mDestinationContainer.setVisibility(View.VISIBLE);
	
		mRouteCostText.setVisibility(View.VISIBLE);
		mDesitinationText.setText(RouteTask
				.getInstance(getApplicationContext()).getEndPoint().address);
		mRouteCostText.setText(
				String.format("预估费用%.2f元，距离%.1fkm,用时%d分", cost,distance,duration)
				 );
		mDestinationButton.setText("我要用车");
		mCancelButton.setVisibility(View.VISIBLE);
		mDestinationButton.setOnClickListener(null);
	}
}
