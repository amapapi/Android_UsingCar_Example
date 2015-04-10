/**  
 * Project Name:Android_Car_Example  
 * File Name:DestinationActivity.java  
 * Package Name:com.amap.api.car.example  
 * Date:2015年4月3日上午10:52:03  
 *  
*/  
  
package com.amap.api.car.example;  

 

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


/**  
 * ClassName:DestinationActivity <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2015年4月3日 上午10:52:03 <br/>  
 * @author   yiyi.qi  
 * @version    
 * @since    JDK 1.6  
 * @see        
 */
public class DestinationActivity extends Activity implements OnClickListener,TextWatcher,OnItemClickListener{
	
	private ListView mRecommendList;

	private ImageView mBack_Image;
	
	private TextView mSearchText;
	
	private EditText mDestinaionText;
	
	private RecomandAdapter mRecomandAdapter;
	
	private RouteTask mRouteTask;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_destination);
		mRecommendList=(ListView) findViewById(R.id.recommend_list);
		mBack_Image=(ImageView) findViewById(R.id.destination_back);
		mBack_Image.setOnClickListener(this);
		
		mSearchText=(TextView) findViewById(R.id.destination_search);	
		mSearchText.setOnClickListener(this);
		
		mDestinaionText=(EditText) findViewById(R.id.destination_edittext);
		mDestinaionText.addTextChangedListener(this);
		mRecomandAdapter=new RecomandAdapter(getApplicationContext());
		mRecommendList.setAdapter(mRecomandAdapter);
		mRecommendList.setOnItemClickListener(this);
		
		mRouteTask=RouteTask.getInstance(getApplicationContext());
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		  
		// TODO Auto-generated method stub  
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		  
		// TODO Auto-generated method stub  
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
		InputTipTask.getInstance(getApplicationContext(), mRecomandAdapter).searchTips(s.toString(),
				RouteTask.getInstance(getApplicationContext()).getStartPoint().city);
		
 
	}

	@Override
	public void onClick(View v) {
		  
	 switch(v.getId()){
	 case R.id.destination_back:
		 Intent intent =new Intent(DestinationActivity.this, MainActivity.class);
		 intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		 startActivity(intent);
		 finish();
		 break;
	 case R.id.destination_search:
		 PoiSearchTask poiSearchTask=new PoiSearchTask(getApplicationContext(), mRecomandAdapter);
		 poiSearchTask.search(mDestinaionText.getText().toString(),RouteTask.getInstance(getApplicationContext()).getStartPoint().city);
		 break;
	 }
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long id) {

		PositionEntity entity = (PositionEntity) mRecomandAdapter.getItem(position);
		if (entity.latitue == 0 && entity.longitude == 0) {
			 PoiSearchTask poiSearchTask=new PoiSearchTask(getApplicationContext(), mRecomandAdapter);
			 poiSearchTask.search(entity.address,RouteTask.getInstance(getApplicationContext()).getStartPoint().city);
			
		} else {
			mRouteTask.setEndPoint(entity);
			mRouteTask.search();
			Intent intent = new Intent(DestinationActivity.this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish(); 
		}
	}
	
}
  
