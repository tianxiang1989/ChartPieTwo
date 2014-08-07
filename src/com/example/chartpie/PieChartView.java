package com.example.chartpie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * 饼图view 
 * 2014年8月7日
 */
public class PieChartView extends View {
	private int ScrWidth, ScrHeight;
	/**饼图的比例*/
	private List<Float> perList = new ArrayList<Float>();
	/**饼图的文字*/
	private List<String> nameList = new ArrayList<String>();

	/**RGB颜色数组*/
	private List<Integer> rgbList = new ArrayList<Integer>();
	/**画弧的画笔*/
	Paint paintArc;
	/**文字的画笔*/
	Paint paintLabel;
	/**圆心x*/
	float cirX;
	/**圆心y*/
	float cirY;
	/**圆半径*/
	float radius;
	/**圆的外切正方形*/
	RectF arcRF0;
	/**用于计算画图和文字的偏移量*/
	private PieBean pieBean = new PieBean(0, 0);
	/**圆距屏幕的左距离*/
	float arcLeft;
	/**圆距屏幕的上距离*/
	float arcTop;
	/**圆距屏幕的右距离*/
	float arcRight;
	/**圆距屏幕的下距离*/
	float arcBottom;

	private boolean drawFlag = false;

	public PieChartView(Context context) {
		super(context);
		init();
	}

	public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public PieChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	* 设置比例，颜色
	* @param arrPer 存比例的list
	* @param nameList 存名字的list
	* @param arrColorRgb 存颜色的list
	*/
	public void setData(List<Float> perList, List<String> nameList,
			List<Integer> rgbList) {
		this.perList = perList;
		this.nameList = nameList;
		this.rgbList = rgbList;
		drawFlag = true;
	}

	/**
	 * 初始化
	 */
	private void init() {
		// 屏幕信息
		DisplayMetrics dm = getResources().getDisplayMetrics();
		ScrHeight = dm.heightPixels;
		ScrWidth = dm.widthPixels;
		// 图像画笔初始化
		paintArc = new Paint();
		paintLabel = new Paint();
		// 文字画笔初始化
		paintLabel.setColor(Color.WHITE);// 文字白色
		paintLabel.setTextSize(18);// 文字大小

		paintArc.setAntiAlias(true);
		paintLabel.setAntiAlias(true);

		cirX = ScrWidth / 2;
		cirY = ScrHeight / 3;
		radius = ScrHeight / 5;

		arcLeft = cirX - radius;
		arcTop = cirY - radius;
		arcRight = cirX + radius;
		arcBottom = cirY + radius;
		arcRF0 = new RectF(arcLeft, arcTop, arcRight, arcBottom);// 为了画圆
	}

	public void onDraw(Canvas canvas) {
		if (drawFlag) {
			// 画布背景
			canvas.drawColor(Color.WHITE);
			float percentage = 0.0f;// 百分比
			float currPer = 180.0f;// 起始画圆的角度
			for (int i = 0; i < perList.size(); i++) {
				percentage = 360 * (perList.get(i) / 100);// 将百分比转换为饼图显示角度
				percentage = (float) (Math.round(percentage * 100)) / 100;// 精确到小数点后两位
				paintArc.setColor(rgbList.get(i));// 分配颜色
				canvas.drawArc(arcRF0, currPer, percentage, true, paintArc);// 按比例画饼图
				// 计算百分比标签
				pieBean = calcArcEndPointXY(cirX, cirY, radius * 0.7F, currPer
						+ percentage / 2);
				String drawTextString = nameList.get(i) + ":"
						+ Float.toString(perList.get(i)) + "%";
				// 文字宽度
				float textwidth = paintLabel.measureText(drawTextString);
				// 画文字百分比
				canvas.drawText(drawTextString, pieBean.getPosX() - textwidth
						* 0.5F, pieBean.getPosY(), paintLabel);
				// 下次的起始角度
				currPer += percentage;
			}
		}
	}

	/**
	 * 依圆心坐标，半径，扇形角度，计算出扇形终射线与圆弧交叉点的xy坐标 
	 * 
	 * @param cirX 圆心x
	 * @param cirY 圆心y
	 * @param radius 圆心偏移量
	 * @param cirAngle 圆弧平分线的角度
	 */
	public PieBean calcArcEndPointXY(float cirX, float cirY, float radius,
			float cirAngle) {
		// 将角度转换为弧度
		float arcAngle = (float) (Math.PI * cirAngle / 180.0);
		float posX = cirX + (float) (Math.cos(arcAngle)) * radius;
		float posY = cirY + (float) (Math.sin(arcAngle)) * radius;
		PieBean pb = new PieBean(posX, posY);
		return pb;
	}

}
