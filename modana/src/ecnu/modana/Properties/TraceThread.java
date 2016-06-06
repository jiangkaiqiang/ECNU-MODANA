package ecnu.modana.Properties;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.ptolemy.fmi.FMIModelDescription;

public class TraceThread 
{
	String prismModelPath,prismType;
	double endTime,stepSize;
	int method;
	int threadCnt=Runtime.getRuntime().availableProcessors();
	FMIModelDescription fmiModelDescription=null;
	//List<MyThread> threads=new ArrayList<>();
	ExecutorService pool;
	public TraceThread(String prismModelPath,String prismType,FMIModelDescription fmiModelDescription,double endTime,double stepSize,int method)
	{
		this.prismModelPath=prismModelPath;
		this.prismType=prismType;
		this.fmiModelDescription=fmiModelDescription;
		this.endTime=endTime;
		this.stepSize=stepSize;
		this.method=method;
		pool = Executors.newFixedThreadPool(threadCnt);
	}
	private List<Trace> traceList=new ArrayList<Trace>();
	private synchronized void AddTrace(Trace trace)
	{
		traceList.add(trace);
	}
	public Trace GetTrace()
	{
		if(traceList.size()<threadCnt) 
		{
			pool = Executors.newFixedThreadPool(threadCnt);
			for(int i=0;i<threadCnt;i++)
			{
				pool.execute(new MyThread());
			}
			pool.shutdown();
		}
		//while(!pool.isTerminated()) ;
		while(traceList.size()==0) ;
		synchronized(MyThread.class)
		{
		    Trace trace=traceList.get(0);
			traceList.remove(0);
			return trace;
		}
	}
	class MyThread implements Runnable
	{
		@Override
		public void run()
		{
            AddTrace(Trace.CosimulationTrace(prismModelPath, prismType,fmiModelDescription, endTime, stepSize, method));
		}
	}
}
