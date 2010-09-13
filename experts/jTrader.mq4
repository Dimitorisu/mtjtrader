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
int MagicNumber =87109493;
double lotMM = 0.1;

int init()
  {

//----
string classpath ="-Djava.class.path=.;E:\\jtradesvn\\code\\bin;E:\\jtradesvn\\code\\commons-logging-1.0.4.jar;E:\\jtradesvn\\code\\log4j-1.2.15.jar;E:\\jtradesvn\\code\\net.wimpi.telnetd.jar";
   int retcode = startJavaVM(classpath);
   if(retcode !=0) {
      Print("Start JVM failed! Returned value is ",retcode);
   }
   
 Sleep(5000);
   
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
   Print("Clear JVM!");
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
   
   
   // Check if any open positions
   HandleOpenPositions();  
   
//----


   string cmd = doTrade(Ask,Bid);
   while(StringLen(cmd)>0 ) {
   string actionId = StringSubstr(cmd,0,1);
      if ( actionId =="B" )
   {
        OpenBuyOrder(cmd);      
   } else if ( actionId =="S")
   {
       OpenSellOrder(cmd);
      
   } else if(actionId=="C") 
   {
		   CloseOrder(cmd);
		   
	}else if(actionId=="T") 
	{	   
		  	ModifyOrder(cmd);
		
	}
	       cmd = doTrade(Ask,Bid);
   }
   
// Print("do trade,cmd:" + cmd);
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




//+------------------------------------------------------------------+
//| OpenBuyOrder                                                     |
//| If Stop Loss or TakeProfit are used the values are calculated    |
//| for each trade
//| cmd=B1.31112|1.31111|1.32111|2342341                                                   |
//+------------------------------------------------------------------+
void OpenBuyOrder(string cmd)
{
   int err,ticket;
      
  int p1 = StringFind(cmd,"|",2);
  int p2 = StringFind(cmd,"|",p1+1);
  int p3 = StringFind(cmd,"|",p2+1);
  string pString1 = StringSubstr(cmd,1,p1-1);
  string pString2 = StringSubstr(cmd,p1+1,p2-p1-1);
  string pString3 = StringSubstr(cmd,p2+1,p3-p2-1);
  string pString4 = StringSubstr(cmd,p3+1);
  double myPrice= StrToDouble(pString1);
  double myStopLoss= StrToDouble(pString2);
  double myTakeProfit= StrToDouble(pString3);
  int myMagicNumber = StrToInteger(pString4);
  
   ticket=OrderSend(Symbol(),OP_BUY,lotMM,myPrice,3,myStopLoss,myTakeProfit,NULL,myMagicNumber,0,NULL); 

   if(ticket<=0)
   {
      err = GetLastError();
      Print("Error opening BUY order: (" + err + ") ask:" + Ask + ",myprice:"+myPrice+",cmd:" + cmd ); 
   }
}



//+------------------------------------------------------------------+
//| OpenSellOrder                                                    |
//| If Stop Loss or TakeProfit are used the values are calculated    |
//| for each trade
//| cmd=S1.31112|1.31111|1.32111|2342341                                                      |
//+------------------------------------------------------------------+
void OpenSellOrder(string cmd)
{
   int err, ticket;
   
   int p1 = StringFind(cmd,"|",2);
  int p2 = StringFind(cmd,"|",p1+1);
  int p3 = StringFind(cmd,"|",p2+1);
  string pString1 = StringSubstr(cmd,1,p1-1);
  string pString2 = StringSubstr(cmd,p1+1,p2-p1-1);
  string pString3 = StringSubstr(cmd,p2+1,p3-p2-1);
  string pString4 = StringSubstr(cmd,p3+1);
  double myPrice= StrToDouble(pString1);
  double myStopLoss= StrToDouble(pString2);
  double myTakeProfit= StrToDouble(pString3);
  int myMagicNumber = StrToInteger(pString4);
  
  
  
   ticket=OrderSend(Symbol(),OP_SELL,lotMM,myPrice,0,myStopLoss,myTakeProfit,NULL,myMagicNumber,0,NULL); 

   if(ticket<=0)
   {
      err = GetLastError();
      Print("Error opening Sell order : (" + err + ") " ); 
   }
}

//+------------------------------------------------------------------+
//| Handle Open Positions                                            |
//| Check if any open positions need to be closed or modified        |
//+------------------------------------------------------------------+
int HandleOpenPositions() {
	int cnt;
	bool YesClose;
	double pt;

	for (cnt = OrdersTotal() - 1; cnt >= 0; cnt--) {
		OrderSelect(cnt, SELECT_BY_POS, MODE_TRADES);
		if (OrderSymbol() != Symbol())
			continue;
		if (OrderMagicNumber() != MagicNumber)
			continue;

      string feedback = doSyncOrder(OrderTicket(),OrderType(), OrderLots(), OrderOpenPrice(),
				OrderStopLoss(), OrderTakeProfit());
					
	}
}

//+------------------------------------------------------------------+

//+------------------------------------------------------------------+
//| Close Open Position Controls                                     |
//|  Try to close position 3 times                                   |
//+------------------------------------------------------------------+
void CloseOrder(string feedback)
{
   int CloseCnt, err;
   //feedback="C2345678|0.1|1.3533";
   int p1 = StringFind(feedback,"|",1);
   int p2 = StringFind(feedback,"|",p1+1);

   string pString1 = StringSubstr(feedback,1,p1-1);
   string pString2 = StringSubstr(feedback,p1+1,p2-p1-1);
   string pString3 = StringSubstr(feedback,p2+1);
   int ticket = StrToInteger(pString1);
   double numLots = StrToDouble(pString2);
   double close_price = StrToDouble(pString3);

   // try to close 3 Times
      
    CloseCnt = 0;
    while (CloseCnt < 3)
    {
       if (OrderClose(ticket,numLots,close_price,0,Violet))
       {
         CloseCnt = 3;
       }
       else
       {
         err=GetLastError();
         Print(CloseCnt," Error closing order : (", err , ") " );
         if (err > 0) CloseCnt++;
       }
    }
}

//+------------------------------------------------------------------+
//| Modify Open Position Controls                                    |
//|  Try to modify position 3 times                                  |
//+------------------------------------------------------------------+
void ModifyOrder(string feedback)
{
    int CloseCnt, err;
    //feedback="T2345678|1.3455|1.3533|1.3211";
		   int p1 = StringFind(feedback,"|",1);
         int p2 = StringFind(feedback,"|",p1+1);
         int p3 = StringFind(feedback,"|",p2+1);
         
         string pString1 = StringSubstr(feedback,1,p1-1);
         string pString2 = StringSubstr(feedback,p1+1,p2-p1-1);
         string pString3 = StringSubstr(feedback,p2+1,p3-p2-1);
         string pString4 = StringSubstr(feedback,p3+1);
         int ord_ticket = StrToInteger(pString1);
         double op = StrToDouble(pString2);
         double sl = StrToDouble(pString3);
		   double tp = StrToDouble(pString4);
		   
    
    CloseCnt=0;
    while (CloseCnt < 3)
    {
       if (OrderModify(ord_ticket,op,sl,tp,0,Aqua))
       {
         CloseCnt = 3;
       }
       else
       {
          err=GetLastError();
          Print(CloseCnt," Error modifying order : (", err , ") " );
         if (err>0) CloseCnt++;
       }
    }
}


