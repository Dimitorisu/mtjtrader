//+------------------------------------------------------------------+
//|                                                      jTrader.mq4 |
//|                       Copyright ?2009, MetaQuotes Software Corp. |
//|                                        http://www.metaquotes.net |
//+------------------------------------------------------------------+
#property copyright "Copyright ?2009, MetaQuotes Software Corp."
#property link      "http://www.metaquotes.net"
#include <libjtrader.mqh>
//+------------------------------------------------------------------+
//| expert initialization function                                   |
//+------------------------------------------------------------------+

datetime lastSyncTime=0;


int init()
  {
//----
   int retcode = startJavaVM();
   if(retcode !=0) {
      Print("Start JVM failed! Returned value is ",retcode);
   }
//----
   return(0);
  }
//+------------------------------------------------------------------+
//| expert deinitialization function                                 |
//+------------------------------------------------------------------+
int deinit()
  {
//----
   cleanupVM(0);
//----
   return(0);
  }
//+------------------------------------------------------------------+
//| expert start function                                            |
//+------------------------------------------------------------------+
int start()
  {
  
   
//----
   int cmd = doTrade(1.2);
	while (cmd != 0) {
		cmd = doTrade(1.2);
	}
//----
   return(0);
  }
//+------------------------------------------------------------------+

int syncData(int timeframe) {

  
   int lastBar = iBarShift(NULL,timeframe,lastSyncTime,true);
   if(lastBar !=0) {
      for(int i=lastBar;i<=0;i++) {
            datetime time = iTime(NULL,timeframe,i);
            double closePrice = iClose(NULL,timeframe,i);
            double highPrice = iHigh(NULL,timeframe,i);
            double lowPrice = iLow(NULL,timeframe,i);
            double openPrice = iOpen(NULL,timeframe,i);
         doSyncData(TimeSeconds(time),openPrice,lowPrice,highPrice,closePrice);
      }
   
   }
   
   int now = iTime(NULL, PERIOD_M1, 0);
   lastSyncTime= now;
}