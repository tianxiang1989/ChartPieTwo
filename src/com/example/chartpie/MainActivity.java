package com.example.chartpie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;

import com.example.chartpieTwo.R;

/**
 * 程序入口
 * 2014年8月7日
 */
public class MainActivity extends Activity {
	/**自定义的扇形图*/
	PieChartView pieChartView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		pieChartView = (PieChartView) findViewById(R.id.pieView);
		/**百分比数据*/
		List<Float> perList = new ArrayList<Float>(Arrays.asList(20F, 30F, 15F,
				20F,15F));
		/**名字数据*/
		List<String> nameList = new ArrayList<String>(Arrays.asList("桌子","铅笔","钢琴","水杯","橡皮"));
		/**RGB颜色数据*/
		List<Integer> rgbList = new ArrayList<Integer>(Arrays.asList(
				0xFF444444, 0xFFCCCCCC, 0xFFFF0000, 0xFF0000FF,0xFF888888));
		pieChartView.setData(perList,nameList, rgbList);//给view传数据
		pieChartView.invalidate(); //刷新页面
	}
}
