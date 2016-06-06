package ecnu.modana.Properties;
import static java.lang.Math.abs;
import static java.lang.Math.pow;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ptolemy.fmi.FMIModelDescription;
import org.ptolemy.fmi.FMUFile;

import ecnu.modana.FmiDriver.CoSimulation;
import ecnu.modana.FmiDriver.PrismClient;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sun.util.resources.OpenListResourceBundle;
/**
 * @author JKQ
 *
 * 2015年11月29日下午3:40:01
 */
public class PropertiesManager {
	public ObservableList<Properties> propertiesObervableList=FXCollections.observableArrayList();
	public  int n = 0; // number of traces drawn so far
	public  int x = 0;// number of traces satisfying PBLTL so far
	public List<String> trace;
	static MathContext mc = new MathContext(10);
	public Random random = new Random();
	
	public void verifier(Properties property,int method) throws IOException {
//		System.out.println(property.getProperties().getValue());
		double a = 1.0;
		double b = 1.0;
		double k = 0.01;// 半区间大小
		double c = 0.8;// 区间覆盖系数
		double r = 0.0;
		double p = 0.0;
		double t0 = 0.0;
		double t1 = 0.0; 
		// 生成一个trace
		System.out.println("开始");
//		Trace tra = new Trace();
//       	tra.GetTrace("./11.pm");
		
//		String prismModelPath="./fmu.pm";
//		String fmuPath="./MyBouncingBall.fmu";		
		
		String prismModelPath="./CTMC.pm";
		String fmuPath="./ctmcBouncingBall.fmu";
		
		FMIModelDescription fmiModelDescription=FMUFile.parseFMUFile(fmuPath);
		PrismClient prismClient=PrismClient.getInstance();
		prismClient.OpenModel(prismModelPath);
		String prismType=prismClient.modelType;
		prismClient.Close();
		double stepSize=0.005;
		Trace tra=null;
		long startTime=new Date().getTime();
		tra=Trace.CosimulationTrace(prismModelPath,prismType,fmiModelDescription, 5.5, stepSize,method);

		if(tra.GetStateNumber()<1) return;
//		System.err.println(tra.names);
//		if(tra.GetStateNumber()>0)
//		{
//			long j=tra.GetStateNumber();
//			System.err.println(j);
//			for(int i=0;i<j;i++)
//				if(0!=Double.valueOf(tra.GetVar(i, "s").toString()))
//				System.err.print(tra.GetVar(i, "s")+" ");
//			System.err.println();
//			return;
//		}
		//System.err.println(tra.GetAllState());

//		System.err.println(tra.GetStateNumber());
		
//		TraceThread traceThread=new TraceThread(prismModelPath,prismType,fmiModelDescription, 5.5, 0.005,method);
//		tra=traceThread.GetTrace();
		//if(method==1) return;
		//System.err.println(tra.GetAllState());
		
		n = n + 1;// 生成了一次trace，所以n+1
//		System.out.println(checkTrace(tra,property.getProperties().getValue())+"============验证是否正确");
		if (checkTrace(tra,property.getProperties().getValue()))// 满足BLTL
			x = x + 1;
//		if(random.nextInt(6) == 1)
//		{
//			x = x + 1;
//		}
		p = (x + a) / (n + a + b);// 后验平均值
		t0 = p - k;
		t1 = p + k;
		if (t1 > 1) {
			t1 = 1;
			t0 = 1 - 2 * k;
		} else if (t0 < 0) {
			t0 = 0;
			t1 = 2 * k;
		}
//		double db = 1.0*pow(10, 20);
		// 计算p属于(t0,t1)的后验概率
//		r = (DefiniteIntegral(0, t1, x + a, n - x + b) - DefiniteIntegral(0,
//				t0, x + a, n - x + b))
//				/ DefiniteIntegral(0, 1, x + a, n - x + b);
		r = (DefiniteIntegral(0, t1, x + a, n - x + b).add(DefiniteIntegral(0, t0, x + a, n - x + b).negate())).
				divide(DefiniteIntegral(0, 1, x + a, n - x + b), mc).doubleValue();
		System.out.println("r等于" + r);
		while (r < c) {
//			System.out.println(checkTrace(tra,property.getProperties().getValue())+"============验证是否正确");
			// 生成一个trace
//			tra.GetTrace("./11.pm");
			tra=Trace.CosimulationTrace(prismModelPath,prismType,fmiModelDescription, 5.5, stepSize,method);
//			System.err.println("last h,v:"+tra.GetVar((int)tra.GetStateNumber()-1, "h")+","+tra.GetVar((int)tra.GetStateNumber()-1, "in_v"));
			
			//tra=traceThread.GetTrace();
			n = n + 1;// 生成了一次trace，所以n+1
			if (checkTrace(tra,property.getProperties().getValue()))// 满足BLTL
			{
				x = x + 1;
			}
//			if(random.nextInt(6) == 1)
//			{
//				x = x + 1;
//			}
			p = (x + a) / (n + a + b);// 后验平均值
			t0 = p - k;
			t1 = p + k;
			if (t1 > 1) {
				t1 = 1;
				t0 = 1 - 2 * k;
			} else if (t0 < 0) {
				t0 = 0;
				t1 = 2 * k;
			}
//			System.out.println(t0+","+t1+","+x+","+n);
			// 计算p属于(t0,t1)的后验概率
//			r = (DefiniteIntegral(0, t1, x + a, n - x + b) - DefiniteIntegral(
//					0, t0, x + a, n - x + b))
//					/ DefiniteIntegral(0, 1, x + a, n - x + b);
//			BigDecimal r1 = DefiniteIntegral(0, t1, x + a, n - x + b);
//			BigDecimal r2 = DefiniteIntegral(0, t0, x + a, n - x + b);
//			BigDecimal r3 = DefiniteIntegral(0, 1, x + a, n - x + b);
			r = (DefiniteIntegral(0, t1, x + a, n - x + b).add(DefiniteIntegral(0, t0, x + a, n - x + b).negate()))
					.divide(DefiniteIntegral(0, 1, x + a, n - x + b), mc).doubleValue();
			if (n % 50 == 0) {
//				System.out.print(DefiniteIntegral(0, 1, x + a, n - x + b)+"========DefiniteIntegral");
				System.out.print("n= "+n+", ");
				System.out.println("r= " + r);
			}
		}
		System.out.println("model:"+fmuPath+",prop:"+property.getProperties().get()+"method:"+method);
		System.out.println("最后的结果是：k:"+k+",c:"+c+",t0:" + t0 + ",t1:" + t1 + ",p:" + p+",r:"+r+",c:"+c);
	    System.err.println("time consuming:"+(new Date().getTime()-startTime)+",Traces:"+n+",stepSize:"+stepSize);
	}
	
	/* Properties 定义：
	 * 定义格式为:
	 * P=? [ F s==7&d==6 ]
       R=? [ F s>=7&d==6 ]
       P=? [ F<=10 s<=7&d==6 ]
       P=? [ F<10 s1<7&d==6 ]
       P=? [ G<10 fhs!=7|d==6 ]
       P=? [ G<=10 s>7|d<6 ]
       P=?[G<=10 s>7|d<6]
	 * 以P=? R=? 字母为开头，表示得到计算的概率或Reward 之后为[]符号，在[和]左右两边可以有空格，也可以不空格
	 * 在[]之间，首先是以F或G为开头的表达式，表示发生为Future或Global,<N表示time或step限制；之后为函数表达式，在以F或G为开头的表达式与函数表达式之间需要有空格
	 * 表示验证的变量表示的性质，包括（<,>,<=,>=,==,!=）,与java语法一致，变量的定义为符号，字母，下划线。在各个表达式之间以|或&连接，表示析取和合取关系
	 * */
	public static boolean checkTrace(Trace trace,String property) {
		//get all state
		 List<String> values = trace.getValues();
		 //match whole property
		 Pattern pattern = Pattern.compile("([PR]=\\?)\\s?\\[\\s?([FG]<?=?0?.?[0-9]*)\\s([a-zA-EH-OQS-Z_0-9]\\w*[><!=]=?-?[0-9]?.?[0-9]*( [&|] [a-zA-EH-OQS-Z_0-9]\\w*[><!=]=?-?[0-9]?.?[0-9]*)*)\\s? ]");
	     Matcher matcher = pattern.matcher(property);
	     //match and find like PR=? and send value to  frStringBuffer
	     Pattern prPattern = Pattern.compile("([PR]=\\?)"); 
	     Matcher prMatcher = prPattern.matcher(property);
	     StringBuffer prStringBuffer = new StringBuffer();
	     //match and find like FG<=3 and send value to  fgStringBuffer
	     Pattern fgPattern = Pattern.compile("[FG]<?=?(0?.?[0-9]*)");
	     Matcher fgMatcher = fgPattern.matcher(property);
	     StringBuffer fgStringBuffer = new StringBuffer();
	     //match and find like $or| and send value to opList
	     Pattern opPattern = Pattern.compile("[&|]");
	     Matcher opMatcher = opPattern.matcher(property);
	     List<String> opList = new ArrayList<String>();
	     //match and find like s!=5 and send value to exVList exOList exNList
	     Pattern exPattern = Pattern.compile("([a-zA-EH-OQS-Z_0-9]\\w*)([><!=]=?)(-?[01]?.?[0-9]*)");
	     Matcher exMatcher = exPattern.matcher(property);
	     List<String> exVList = new ArrayList<String>();
	     List<String> exOList = new ArrayList<String>();
	     List<String> exNList = new ArrayList<String>();
	     //match and find number in like FG<=3 and send value to step
	     Pattern fgNPattern = Pattern.compile("([FG]<?=?)(0?.?[0-9]*)");
		 if (matcher.matches()) {
			while (prMatcher.find()) {
				prStringBuffer.append(prMatcher.group());
			}
			while (fgMatcher.find()) {
				fgStringBuffer.append(fgMatcher.group());
			}
			Matcher fgNMatcher = fgNPattern.matcher(fgStringBuffer.toString());
		    double fgN = 0.0;
			while (opMatcher.find()) {
				opList.add(opMatcher.group());
			}
			while (exMatcher.find()) {
				exVList.add(exMatcher.group(1));
				exOList.add(exMatcher.group(2));
				exNList.add(exMatcher.group(3));
			}	
			if (prStringBuffer.toString().startsWith("P")) {
				if (fgStringBuffer.toString().startsWith("F")) {
					
//					if (fgStringBuffer.toString().length()<=1) {
//					 System.out.println(exVList+"============变量");
//					 System.out.println(exOList+"============操作符");
//					 System.out.println(exNList+"============变量值");
				
					 List<Boolean> flag = null;
					     	for (int i = 0; i < values.size(); i++) {
					     		flag = new ArrayList<>();
					     		for (int j = 0; j < exVList.size(); j++) {
//					     		 System.out.println(trace.GetVar(i, exVList.get(j))+"============打印属性值"+exVList.get(j));
								 String op =  exOList.get(j);
								 if (op.equals("==")) {
									 if (Double.valueOf(exNList.get(j))==(double)trace.GetVar(i, exVList.get(j))) {
											flag.add(true);
									 }
									 else{
										 flag.add(false);
									 }
								 } 
								 else if (op.equals("<")) {
									if ((double)trace.GetVar(i, exVList.get(j))<Double.valueOf(exNList.get(j))) {
										flag.add(true);
									}
									else{
										flag.add(false);
									 }
								 }
								 else if (op.equals("<=")) {
										if ((double)trace.GetVar(i, exVList.get(j))<=Double.valueOf(exNList.get(j))) {
											flag.add(true);
										}
										else{
											flag.add(false);
										 }
									}
								 else if(op.equals(">")){
									 if ((double)trace.GetVar(i, exVList.get(j))>Double.valueOf(exNList.get(j))) {
										 flag.add(true);
										}
									 else{
										 flag.add(false);
									 }
								  }
								 else if(op.equals(">=")){
									 if ((double)trace.GetVar(i, exVList.get(j))>=Double.valueOf(exNList.get(j))) {
										 flag.add(true);
										}
									 else{
										 flag.add(false);
									 }
								  }
								 else if(op.equals("!=")){
									 if ((double)trace.GetVar(i, exVList.get(j))!=Double.valueOf(exNList.get(j))) {
										 flag.add(true);
										}
									 else{
										 flag.add(false);
									 }
								  } 
								 else {
									
								  }
//								 System.out.println(flag);
							  }			     	
					     		int count = 0;
					     		for (int k = 0; k < flag.size(); k++) {
									if (flag.get(k)==true) {
										count++;
									}
								if (opList.get(0).equals("&")) {
					     		  if (count==flag.size()) {
									return true;
								  }
					     		  else{
					     			
					     		  }
								}
								else if (opList.get(0).equals("|")) {
									 if (count>=1) {
											return true;
										  }
							     		  else{
							     			
							     	}
								}
							}
					     }
					     		
//						}
//					else if(fgStringBuffer.toString().length()>2){
//						while (fgNMatcher.find()) {
//							fgN = Double.valueOf(fgNMatcher.group(2));
////							System.out.println(fgStringBuffer.toString());
//							System.out.println("输出F的bound"+fgN);
//						}
//						
//					  }
				 }
					else if(fgStringBuffer.toString().startsWith("G")) {
//						if (fgStringBuffer.toString().length()<=1) {
						 List<Boolean> flag = null;

					     	for (int i = 0; i < values.size(); i++) {
					     		flag = new ArrayList<>();
					     		for (int j = 0; j < exVList.size(); j++) {
//					     		 System.out.println(trace.GetVar(i, exVList.get(j))+"============打印属性值"+exVList.get(j));
								 String op =  exOList.get(j);
								 if (op.equals("==")) {
									 if (Double.valueOf(exNList.get(j))==(double)trace.GetVar(i, exVList.get(j))) {
											flag.add(true);
									 }
									 else{
										 flag.add(false);
									 }
								 } 
								 else if (op.equals("<")) {
									if ((double)trace.GetVar(i, exVList.get(j))<Double.valueOf(exNList.get(j))) {
										flag.add(true);
									}
									else{
										flag.add(false);
									 }
								 }
								 else if (op.equals("<=")) {
										if ((double)trace.GetVar(i, exVList.get(j))<=Double.valueOf(exNList.get(j))) {
											flag.add(true);
										}
										else{
											flag.add(false);
										 }
									}
								 else if(op.equals(">")){
									 if ((double)trace.GetVar(i, exVList.get(j))>Double.valueOf(exNList.get(j))) {
										 flag.add(true);
										}
									 else{
										 flag.add(false);
									 }
								  }
								 else if(op.equals(">=")){
									 if ((double)trace.GetVar(i, exVList.get(j))>=Double.valueOf(exNList.get(j))) {
										 flag.add(true);
										}
									 else{
										 flag.add(false);
									 }
								  }
								 else if(op.equals("!=")){
									 if ((double)trace.GetVar(i, exVList.get(j))!=Double.valueOf(exNList.get(j))) {
										 flag.add(true);
										}
									 else{
										 flag.add(false);
									 }
								  } 
								 else {
									
								  }
//								 System.out.println(flag);
							  }			     	
					     		int count = 0;
					     		for (int k = 0; k < flag.size(); k++) {
									if (flag.get(k)==true) {
										count++;
									}
								if (opList.get(0).equals("&")) {
					     		  if (count!=flag.size()) {
									return false;
								  }
					     		  else{
					     			return true;
					     		  }
								}
								else if (opList.get(0).equals("|")) {
//									 System.out.println(opList+"============&or|");
									 if (count==0) {
											return false;
										  }
							     		  else{	
							     			 return true;
							     	}
								}
							}
					     }
//						else if(fgStringBuffer.toString().length()>2){
//							while (fgNMatcher.find()) {
//								fgN = Double.valueOf(fgNMatcher.group(2));
////								System.out.println(fgStringBuffer.toString());
//								System.out.println("输出F的bound"+fgN);
//							}
//							
//						  }
					}
				}
			else {
				System.out.println("Reward Algorithm");
			}
//       	     System.out.println(fgStringBuffer.toString());
//	       	 System.out.println(opList);
//			 System.out.println(exVList);
//			 System.out.println(exOList);
//			 System.out.println(exNList);
		 }
	     else
	    	 System.out.println("expresion syntax error");
		 return false;
	}
	
	
	public static BigDecimal DefiniteIntegral(double x0, double xn,double alpha, double beta)
	{
		int n =10000;  //100000  ini
		//h:步长
//		double h = abs(xn-x0)/n;
//		double sum = 0;
		
		BigDecimal sum = new BigDecimal(0.0,mc);
		BigDecimal h = new BigDecimal(abs(xn-x0)/n,mc);
		
		for (double xi =x0;xi<xn;xi=xi+h.doubleValue())
		{
			sum = sum.add(BetaDistribution(xi+h.doubleValue(),alpha,beta).multiply(h, mc), mc);
//			sum += BetaDistribution(xi+h,alpha,beta)*h;
		}
		return sum;		
	}
	
	public static BigDecimal BetaDistribution(double x, double alpha, double beta )
	{
//		System.out.println(x+","+(1-x)+","+(alpha-1)+","+(beta-1));
		
		BigDecimal b1 = new BigDecimal(x,mc);
		BigDecimal b2 = new BigDecimal(1-x, mc);
		BigDecimal p1 = b1.pow((int)(alpha-1),mc);
		BigDecimal p2 = b2.pow((int)(beta-1),mc);
		BigDecimal f = p1.multiply(p2,mc);
		return f;
//		double p1 = pow(x,alpha-1);
////		System.out.println(p1);
//		double p2 = pow(1-x,beta-1);
////		System.out.println(p2);
//		double f = (p1*p2);
//		return f;
	}
}
