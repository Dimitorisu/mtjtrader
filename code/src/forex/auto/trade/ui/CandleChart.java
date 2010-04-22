package forex.auto.trade.ui;

import java.awt.Color;
import java.awt.Paint;
import java.text.SimpleDateFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SegmentedTimeline;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import forex.auto.trade.core.TimeSerise;

public class CandleChart extends ApplicationFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CandleChart(final String title) {
		super(title);

	}

	public void init(TimeSerise ts) {
		final DefaultHighLowDataset dataset = ForexDatasetFactory
				.createHighLowDataset(ts);
		final JFreeChart chart = createChart(dataset);
		//chart.getXYPlot().setOrientation(PlotOrientation.VERTICAL);
		XYPlot xyplot = chart.getXYPlot();  
        //纵坐标设定
		
		int cout = xyplot.getDomainAxisCount();
		System.out.println("count:="+ cout);
		
		
        ValueAxis valueaxis = xyplot.getDomainAxis();  
        //自动设置数据轴数据范围  
//        valueaxis.setAutoRange(false);
//        valueaxis.setAutoTickUnitSelection(false);
        //valueaxis.setRange(1.63, 1.64);
        //数据轴固定数据范围 30s  
        //valueaxis.setFixedAutoRange(30000D);  
  
    
		
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(1600, 270));
		setContentPane(chartPanel);
	}

	/**
	 * Creates a chart.
	 * 
	 * @param dataset
	 *            the dataset.
	 * 
	 * @return The dataset.
	 */
	private JFreeChart createChart(final DefaultHighLowDataset dataset) {
		
		 final CandlestickRenderer candlestickRender=new CandlestickRenderer(2);//设置K线图的画图器，必须申明为final，后面要在匿名内部类里面用到
	     candlestickRender.setUseOutlinePaint(true); //设置是否使用自定义的边框线，程序自带的边框线的颜色不符合中国股票市场的习惯
	     candlestickRender.setAutoWidthMethod(CandlestickRenderer.WIDTHMETHOD_AVERAGE);//设置如何对K线图的宽度进行设定
	     candlestickRender.setAutoWidthGap(0.001);//设置各个K线图之间的间隔
	     candlestickRender.setUpPaint(Color.RED);//设置股票上涨的K线图颜色
	     candlestickRender.setDownPaint(Color.GREEN);//设置股票下跌的K线图颜色

	     DateAxis x1Axis=new DateAxis();//设置x轴，也就是时间轴
	    // x1Axis.setAutoRange(false);//设置不采用自动设置时间范围
//	     try{
//	      x1Axis.setRange(dateFormat.parse("2007-08-20"),dateFormat.parse("2007-09- 29"));//设置时间范围，注意时间的最大值要比已有的时间最大值要多一天
//	     }catch(Exception e){
//	      e.printStackTrace();
//	     }
//	     x1Axis.setTimeline(SegmentedTimeline.newMondayThroughFridayTimeline());// 设置时间线显示的规则，用这个方法就摒除掉了周六和周日这些没有交易的日期(很多人都不知道有此方法)，使图形看上去连续
	     x1Axis.setAutoTickUnitSelection(false);//设置不采用自动选择刻度值
	     x1Axis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);//设置标记的位置
	     x1Axis.setStandardTickUnits(DateAxis.createStandardDateTickUnits());//设置标准的时间刻度单位
	     x1Axis.setTickUnit(new DateTickUnit(DateTickUnitType.MINUTE,10));//设置时间刻度的间隔，一般以周为单位
	     x1Axis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd HH:mm"));//设置显示时间的格式
	     NumberAxis y1Axis=new NumberAxis();//设定y轴，就是数字轴
	     y1Axis.setAutoRange(false);//不不使用自动设定范围
	     double minValue=1.6300 ;
		double highValue=1.6400;
		y1Axis.setRange(minValue, highValue);//设定y轴值的范围，比最低值要低一些，比最大值要大一些，这样图形看起来会美观些
	     y1Axis.setTickUnit(new NumberTickUnit(0.0005));//设置刻度显示的密度
	     XYPlot plot1=new XYPlot(dataset,x1Axis,y1Axis,candlestickRender);//设置画图区域对象
	  
//	     XYBarRenderer xyBarRender=new XYBarRenderer(){
//	     private static final long serialVersionUID = 1L;//为了避免出现警告消息，特设定此值
//	     public Paint getItemPaint(int i, int j){//匿名内部类用来处理当日的成交量柱形图的颜色与K线图的颜色保持一致
//	       if(dataset.getCloseValue(i,j)>dataset.getOpenValue(i,j)){//收盘价高于开盘价，股票上涨，选用股票上涨的颜色
//	        return candlestickRender.getUpPaint();
//	       }else{
//	        return candlestickRender.getDownPaint();
//	       }
//	     }};
//	     xyBarRender.setMargin(0.005);//设置柱形图之间的间隔
//	     NumberAxis y2Axis=new NumberAxis();//设置Y轴，为数值,后面的设置，参考上面的y轴设置
//	     y2Axis.setAutoRange(false);
//	     y2Axis.setRange(min2Value*0.9, high2Value*1.1);
//	     y2Axis.setTickUnit(new NumberTickUnit((high2Value*1.1-min2Value*0.9)/4));
//	     XYPlot plot2=new XYPlot(timeSeriesCollection,null,y2Axis,xyBarRender);//建立第二个画图区域对象，主要此时的 x轴设为了null值，因为要与第一个画图区域对象共享x轴
//	     CombinedDomainXYPlot combineddomainxyplot = new CombinedDomainXYPlot(x1Axis);//建立一个恰当的联合图形区域对象，以x轴为共享轴
//	     combineddomainxyplot.add(plot1, 2);//添加图形区域对象，后面的数字是计算这个区域对象应该占据多大的区域2/3
//	        combineddomainxyplot.add(plot2, 1);//添加图形区域对象，后面的数字是计算这个区域对象应该占据多大的区域1/3
//	        combineddomainxyplot.setGap(10);//设置两个图形区域对象之间的间隔空间
	        JFreeChart chart = new JFreeChart("中国联通", JFreeChart.DEFAULT_TITLE_FONT, plot1, false);
//	        ChartFrame frame = new ChartFrame("中国联通股票", chart);
//	     frame.pack();
//	     frame.setVisible(true);
		
		
		
//		final JFreeChart chart = ChartFactory.createCandlestickChart(
//				"Candlestick Demo", "Time", "Value", dataset, true);
		return chart;
	}

	public static void display(TimeSerise ts) {
		final CandleChart candleWindows = new CandleChart("Candlestick Demo");
		candleWindows.init(ts);
		candleWindows.pack();
		RefineryUtilities.centerFrameOnScreen(candleWindows);
		candleWindows.setVisible(true);
	}

}
