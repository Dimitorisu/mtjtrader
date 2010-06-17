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