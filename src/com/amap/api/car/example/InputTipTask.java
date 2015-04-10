/**  
 * Project Name:Android_Car_Example  
 * File Name:InputTipTask.java  
 * Package Name:com.amap.api.car.example  
 * Date:2015年4月7日上午10:42:41  
 *  
*/  
  
package com.amap.api.car.example;  

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.BaseAdapter;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.Tip;

/**  
 * ClassName:InputTipTask <br/>  
 * Function: 简单封装了Inputtips的搜索服务，将其余提示的adapter进行数据绑定  
 * Date:     2015年4月7日 上午10:42:41 <br/>  
 * @author   yiyi.qi  
 * @version    
 * @since    JDK 1.6  
 * @see        
 */
public class InputTipTask implements InputtipsListener{

	private static InputTipTask mInputTipTask;
	
	private Inputtips mInputTips;
	
	private RecomandAdapter mAdapter;
	
	public static InputTipTask getInstance(Context context,RecomandAdapter adapter){
		if(mInputTipTask==null){
			mInputTipTask=new InputTipTask(context);
		}
		//单例情况，多次进入DestinationActivity传进来的RecomandAdapter对象会不是同一个
		mInputTipTask.setRecommandAdapter(adapter);
		return mInputTipTask;
	}
	
	public void setRecommandAdapter(RecomandAdapter adapter){
		mAdapter=adapter;
	}
	private InputTipTask(Context context ){
		mInputTips=new Inputtips(context, this);
	 
	
	}
	
	public void searchTips(String keyWord,String city){
		try {
			mInputTips.requestInputtips(keyWord, city);
		} catch (AMapException e) {
			e.printStackTrace();  
			
		}
	}
	@Override
	public void onGetInputtips(List<Tip> tips, int resultCode) {
		  
		if(resultCode==0&&tips!=null){
			ArrayList<PositionEntity>positions=new ArrayList<PositionEntity>();
			for(Tip tip:tips){
				positions.add(new PositionEntity(0, 0, tip.getName(),tip.getAdcode()));
			}
			mAdapter.setPositionEntities(positions);	 
			mAdapter.notifyDataSetChanged();
		}
		//TODO 可以根据app自身需求对查询错误情况进行相应的提示或者逻辑处理
	}

}
  
