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
string classpath ="-Djava.class.path=.;E:\\jtradesvn\\code\\bin;E:\\jtradesvn\\code\\commons-logging-1.0.4.jar;E:\\jtradesvn\\code\\log4j-1.2.15.jar;E:\\jtradesvn\\code\\net.wimpi.telnetd.jar";
   int retcode = startJavaVM(classpath);
   if(retcode !=0) {
      Print("Start JVM failed! Returned value is ",retcode);
   }
   
   int bars = iBars( NULL, PERIOD_D1);
   int synccount = syncData(PERIOD_D1,bars);
   Print("sync data PERIOD_D1,bars:",synccount);
   
   bars = iBars( NULL, PERIOD_H4);
   synccount = syncData(PERIOD_H4,bars);
   Print("sync data PERIOD_H4,bars:",synccount);
   
   
   bars = iBars( NULL, PERIOD_H1);
   synccount = syncData(PERIOD_H1,bars);
   Print("sync data PERIOD_H1,bars:",synccount);
   
   
   bars = iBars( NULL, PERIOD_M30);
   synccount = syncData(PERIOD_M30,bars);
   Print("sync data PERIOD_M30,bars:",synccount);
   
   bars = iBars( NULL, PERIOD_M15);
   synccount = syncData(PERIOD_M15,bars);
   Print("sync data PERIOD_M15,bars:",synccount);
   
   bars = iBars( NULL, PERIOD_M5);
   synccount = syncData(PERIOD_M5,bars);
   Print("sync data PERIOD_M5,bars:",synccount);
   
   bars = iBars( NULL, PERIOD_M1);
   synccount = syncData(PERIOD_M1,bars);
   Print("sync data PERIOD_M1,bars:",synccount);
   
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
 Print("do trade,cmd:" + cmd);
//----
   return(0);
  }
//+------------------------------------------------------------------+

int syncData(int timeframe,int lastBar) {
   int syncCount =0;
   

   if(lastBar != -1) {
   
      if (lastBar >1440) {
          lastBar = 1440;
      }
   
   
      for(int i=lastBar;i>=0;i--) {
      
            int time = iTime(NULL,timeframe,i);
            double closePrice = iClose(NULL,timeframe,i);
            double highPrice = iHigh(NULL,timeframe,i);
            double lowPrice = iLow(NULL,timeframe,i);
            double openPrice = iOpen(NULL,timeframe,i);
            int ret = doSyncData(time,openPrice,lowPrice,highPrice,closePrice);
            if(ret == 0) {
                syncCount++;
            } else {
                Print("doSyncData failed,ret:" + ret);
                return (-3);
            }
      }
   } else {
      return (-1);
   }
   
   

   lastSyncTime= iTime(NULL, timeframe, 0);
   
   return (syncCount);
}