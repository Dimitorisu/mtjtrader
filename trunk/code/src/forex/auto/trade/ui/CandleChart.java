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
        //�������趨
		
		int cout = xyplot.getDomainAxisCount();
		System.out.println("count:="+ cout);
		
		
        ValueAxis valueaxis = xyplot.getDomainAxis();  
        //�Զ��������������ݷ�Χ  
//        valueaxis.setAutoRange(false);
//        valueaxis.setAutoTickUnitSelection(false);
        //valueaxis.setRange(1.63, 1.64);
        //������̶����ݷ�Χ 30s  
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
		
		 final CandlestickRenderer candlestickRender=new CandlestickRenderer(2);//����K��ͼ�Ļ�ͼ������������Ϊfinal������Ҫ�������ڲ��������õ�
	     candlestickRender.setUseOutlinePaint(true); //�����Ƿ�ʹ���Զ���ı߿��ߣ������Դ��ı߿��ߵ���ɫ�������й���Ʊ�г���ϰ��
	     candlestickRender.setAutoWidthMethod(CandlestickRenderer.WIDTHMETHOD_AVERAGE);//������ζ�K��ͼ�Ŀ�Ƚ����趨
	     candlestickRender.setAutoWidthGap(0.001);//���ø���K��ͼ֮��ļ��
	     candlestickRender.setUpPaint(Color.RED);//���ù�Ʊ���ǵ�K��ͼ��ɫ
	     candlestickRender.setDownPaint(Color.GREEN);//���ù�Ʊ�µ���K��ͼ��ɫ

	     DateAxis x1Axis=new DateAxis();//����x�ᣬҲ����ʱ����
	    // x1Axis.setAutoRange(false);//���ò������Զ�����ʱ�䷶Χ
//	     try{
//	      x1Axis.setRange(dateFormat.parse("2007-08-20"),dateFormat.parse("2007-09- 29"));//����ʱ�䷶Χ��ע��ʱ������ֵҪ�����е�ʱ�����ֵҪ��һ��
//	     }catch(Exception e){
//	      e.printStackTrace();
//	     }
//	     x1Axis.setTimeline(SegmentedTimeline.newMondayThroughFridayTimeline());// ����ʱ������ʾ�Ĺ���������������������������������Щû�н��׵�����(�ܶ��˶���֪���д˷���)��ʹͼ�ο���ȥ����
	     x1Axis.setAutoTickUnitSelection(false);//���ò������Զ�ѡ��̶�ֵ
	     x1Axis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);//���ñ�ǵ�λ��
	     x1Axis.setStandardTickUnits(DateAxis.createStandardDateTickUnits());//���ñ�׼��ʱ��̶ȵ�λ
	     x1Axis.setTickUnit(new DateTickUnit(DateTickUnitType.MINUTE,10));//����ʱ��̶ȵļ����һ������Ϊ��λ
	     x1Axis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd HH:mm"));//������ʾʱ��ĸ�ʽ
	     NumberAxis y1Axis=new NumberAxis();//�趨y�ᣬ����������
	     y1Axis.setAutoRange(false);//����ʹ���Զ��趨��Χ
	     double minValue=1.6300 ;
		double highValue=1.6400;
		y1Axis.setRange(minValue, highValue);//�趨y��ֵ�ķ�Χ�������ֵҪ��һЩ�������ֵҪ��һЩ������ͼ�ο�����������Щ
	     y1Axis.setTickUnit(new NumberTickUnit(0.0005));//���ÿ̶���ʾ���ܶ�
	     XYPlot plot1=new XYPlot(dataset,x1Axis,y1Axis,candlestickRender);//���û�ͼ�������
	  
//	     XYBarRenderer xyBarRender=new XYBarRenderer(){
//	     private static final long serialVersionUID = 1L;//Ϊ�˱�����־�����Ϣ�����趨��ֵ
//	     public Paint getItemPaint(int i, int j){//�����ڲ������������յĳɽ�������ͼ����ɫ��K��ͼ����ɫ����һ��
//	       if(dataset.getCloseValue(i,j)>dataset.getOpenValue(i,j)){//���̼۸��ڿ��̼ۣ���Ʊ���ǣ�ѡ�ù�Ʊ���ǵ���ɫ
//	        return candlestickRender.getUpPaint();
//	       }else{
//	        return candlestickRender.getDownPaint();
//	       }
//	     }};
//	     xyBarRender.setMargin(0.005);//��������ͼ֮��ļ��
//	     NumberAxis y2Axis=new NumberAxis();//����Y�ᣬΪ��ֵ,��������ã��ο������y������
//	     y2Axis.setAutoRange(false);
//	     y2Axis.setRange(min2Value*0.9, high2Value*1.1);
//	     y2Axis.setTickUnit(new NumberTickUnit((high2Value*1.1-min2Value*0.9)/4));
//	     XYPlot plot2=new XYPlot(timeSeriesCollection,null,y2Axis,xyBarRender);//�����ڶ�����ͼ���������Ҫ��ʱ�� x����Ϊ��nullֵ����ΪҪ���һ����ͼ���������x��
//	     CombinedDomainXYPlot combineddomainxyplot = new CombinedDomainXYPlot(x1Axis);//����һ��ǡ��������ͼ�����������x��Ϊ������
//	     combineddomainxyplot.add(plot1, 2);//���ͼ��������󣬺���������Ǽ�������������Ӧ��ռ�ݶ�������2/3
//	        combineddomainxyplot.add(plot2, 1);//���ͼ��������󣬺���������Ǽ�������������Ӧ��ռ�ݶ�������1/3
//	        combineddomainxyplot.setGap(10);//��������ͼ���������֮��ļ���ռ�
	        JFreeChart chart = new JFreeChart("�й���ͨ", JFreeChart.DEFAULT_TITLE_FONT, plot1, false);
//	        ChartFrame frame = new ChartFrame("�й���ͨ��Ʊ", chart);
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
