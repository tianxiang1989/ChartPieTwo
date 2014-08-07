package com.example.chartpie;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * 饼图view 
 * 2014年8月7日
 */
public class PieChartView extends View {
	/**控件宽*/
	private int scrWidth;
	/**控件高*/
	private int scrHeight;
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
	RectF rectCircle;
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
	/**外边框画笔*/
	Paint paintBorder;
	/**外边框*/
	RectF rectOut;
	/**图例的画笔*/
	Paint legendPaint;
	/**图例的X坐标*/
	float positionLegendX;
	/**图例的Y坐标*/
	float positionLegendY;
	/**图例间距*/
	float perLegendHeight;
	/**图例和图例文字高度*/
	float strHeight;
	/**是否开始draw的标识：true开始，flase不开始*/
	private boolean drawFlag = false;

	public PieChartView(Context context) {
		super(context);
	}

	public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public PieChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	* 传入比例，名称，颜色的list集合
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
		//获取给控件的宽高
		scrHeight = this.getHeight();
		scrWidth = this.getWidth();
		// 扇形画笔初始化
		paintArc = new Paint();
		paintArc.setAntiAlias(true);
		// 扇形上百分比文字画笔初始化
		paintLabel = new Paint();
		paintLabel.setColor(Color.WHITE);// 文字白色
		paintLabel.setTextSize(24);// 文字大小
		paintLabel.setAntiAlias(true);// 消除锯齿
		// 外边框画笔初始化
		paintBorder = new Paint();
		paintBorder.setColor(Color.BLACK);
		paintBorder.setStyle(Paint.Style.STROKE);
		paintBorder.setStrokeWidth(2f);
		// 图例画笔初始化
		legendPaint = new Paint();
		legendPaint.setColor(Color.BLACK);
		legendPaint.setTextSize(30f); // 字体大小
		legendPaint.setAntiAlias(true);// 消除锯齿
		legendPaint.setStyle(Paint.Style.FILL);//填充
		// 初始化圆数据
		cirX = scrWidth / 3;
		cirY = scrHeight / 2;
		radius = scrHeight / 3;
		// 初始化圆边距数据
		arcLeft = cirX - radius;
		arcTop = cirY - radius;
		arcRight = cirX + radius;
		arcBottom = cirY + radius;
		rectCircle = new RectF(arcLeft, arcTop, arcRight, arcBottom);// 圆的外切正方形
		// 外边框
		rectOut = new RectF(0, 0, scrWidth, scrHeight);
		//图例部分数据初始化
		strHeight = getMaxHeight();// 获取图例最大高度
		positionLegendX = (getWidth() - arcRight) * 0.25F + arcRight;
		perLegendHeight = (2F * radius - strHeight) / ((perList).size() - 1);
		positionLegendY = arcTop;// 设置图例起始高度
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (drawFlag) {
			init();
			canvas.drawRect(rectOut, paintBorder);// 画外边框
			float percentage = 0.0f;// 百分比
			float currPer = 180.0f;// 起始画圆的角度
			for (int i = 0; i < perList.size(); i++) {
				percentage = 360 * (perList.get(i) / 100);// 将百分比转换为饼图显示角度
				percentage = (float) (Math.round(percentage * 100)) / 100;// 精确到小数点后两位
				paintArc.setColor(rgbList.get(i));// 分配颜色
				canvas.drawArc(rectCircle, currPer, percentage, true, paintArc);// 按比例画饼图
				// 计算百分比标签
				pieBean = calcArcEndPointXY(cirX, cirY, radius * 0.7F, currPer
						+ percentage / 2);
				String drawTextString = Float.toString(perList.get(i)) + "%";// 图像上的文字
				float textwidth = paintLabel.measureText(drawTextString);// 文字宽度
				// 画文字百分比
				canvas.drawText(drawTextString, pieBean.getPosX() - textwidth
						* 0.5F, pieBean.getPosY(), paintLabel);
				// 下次画弧的起始角度
				currPer += percentage;
				// 图例部分
				String legendText = nameList.get(i);// 图例文字
				legendPaint.setColor(rgbList.get(i));
				//图例正方形
				Rect rectLegend = new Rect(Math.round(positionLegendX),
						Math.round(positionLegendY), Math.round(positionLegendX
								+ strHeight), Math.round(positionLegendY
								+ strHeight));
				canvas.drawRect(rectLegend, legendPaint);
				legendPaint.setColor(Color.BLACK);//图例文字黑
				canvas.drawText(legendText, positionLegendX + strHeight
						+ changeDp(2), positionLegendY - changeDp(1)
						+ strHeight, legendPaint);
				positionLegendY += perLegendHeight;//图例起始高度下移
			}
		}
	}

	/**
	 * dp转化pix像素--工具方法
	 * @param dp dp的值
	 * @return pix的值
	 */
	private int changeDp(int dp) {
		int pix = Math.round(TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, dp, getResources()
						.getDisplayMetrics()));
		return pix;
	}

	/**
	 * 获取list中最大的高度 [图例用]
	 * @return 最大高度
	 */
	public float getMaxHeight() {
		float maxHeight = 0;
		for (String str : nameList) {
			Rect rect = new Rect();
			// 返回包围整个字符串的最小的一个Rect区域 [为了取到文字高度]
			legendPaint.getTextBounds(str, 0, 1, rect);
			float curHeight = rect.height();
			if (curHeight > maxHeight) {
				maxHeight = curHeight;
			}
		}
		return maxHeight;
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
