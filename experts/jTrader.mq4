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
   
   int bars = iBars( NULL, PERIOD_D1);
   syncData(PERIOD_D1,bars);
   
   bars = iBars( NULL, PERIOD_H4);
   syncData(PERIOD_H4,bars);
   
   bars = iBars( NULL, PERIOD_H1);
   syncData(PERIOD_H1,bars);
   
   bars = iBars( NULL, PERIOD_M30);
   syncData(PERIOD_M30,bars);
   
   bars = iBars( NULL, PERIOD_M15);
   syncData(PERIOD_M15,bars);
   
   bars = iBars( NULL, PERIOD_M5);
   syncData(PERIOD_M5,bars);
   
   bars = iBars( NULL, PERIOD_M1);
   syncData(PERIOD_M1,bars);
   
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
  
   int lastBar = iBarShift(NULL,PERIOD_M1,lastSyncTime,true);
   syncData(PERIOD_M1,lastBar);
   
   
//----
   int cmd = doTrade(Ask,Bid);

//----
   return(0);
  }
//+------------------------------------------------------------------+

int syncData(int timeframe,int lastBar) {

   if(lastBar != -1) {
   
      for(int i=lastBar;i<=0;i++) {
            datetime time = iTime(NULL,timeframe,i);
            double closePrice = iClose(NULL,timeframe,i);
            double highPrice = iHigh(NULL,timeframe,i);
            double lowPrice = iLow(NULL,timeframe,i);
            double openPrice = iOpen(NULL,timeframe,i);
         doSyncData(TimeSeconds(time),openPrice,lowPrice,highPrice,closePrice);
      }
   } else {
      return -1;
   }
   
   
   int now = iTime(NULL, timeframe, 0);
   lastSyncTime= now;
   
   return 0;
}