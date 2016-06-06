package ecnu.modana.Properties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ptolemy.fmi.FMIModelDescription;
import org.ptolemy.fmi.FMUFile;

import ecnu.modana.FmiDriver.CoSimulation;
import ecnu.modana.FmiDriver.PrismClient;
import ecnu.modana.model.ModelManager;

/**
 * @author JKQ
 *
 * 2015年11月29日下午3:41:21
 */
public class Trace implements Runnable
{
	//public List<String> names=new ArrayList<String>();
	//PrismClient prismClient=PrismClient.getInstance();
	public HashMap<String, Integer> namesMap=new HashMap<>();
	private List<String>values=new ArrayList<>();
//	public void GetTrace(String prismModelPath)
//	{
//		prismClient.StartServer();
//		prismClient.Start("127.0.0.1", 40000);
//		String nameString=prismClient.OpenModel(prismModelPath);
//		String[] tStrings=nameString.split(",");
//		namesMap=new HashMap<>();
//		for(int i=0;i<tStrings.length;i++)
//			namesMap.put(tStrings[i], i);
//		String preState=null,curState="";
//		try {
//			while (!curState.equals(preState)) {
//				preState=curState;
//				curState=prismClient.DoStep(false);
//				values.add(curState);
//			}
////			System.err.println("------"+values);
//			prismClient.Close();
//			//prismClient.EndServer();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public long GetStateNumber()
	{
		return values.size();
	}
	public String names="";
	public void AddNames(String allName)
	{
		String[] tStrings=allName.split(",");
		namesMap=new HashMap<>();
		for(int i=0;i<tStrings.length;i++)
			if(!namesMap.containsKey(tStrings[i]))
			   namesMap.put(tStrings[i], i);
		this.names=allName;
	}
	public static Trace CosimulationTrace(String prismModelPath,String prismType,FMIModelDescription fmiModelDescription,double endTime,double stepSize,int method)
	{
		CoSimulation coSimulation=new CoSimulation("127.0.0.1", 40000);
		if(method==0)
			return coSimulation.simulateTrace(prismModelPath, prismType,fmiModelDescription, endTime, stepSize, false, ',', "./1.xml");
		//else if(method==1)
			return coSimulation.simulateTradition(prismModelPath, prismType,fmiModelDescription, endTime, stepSize, false, ',', "./1.xml");
	}
	public List<String> getValues(){
		return values;
	}
	public HashMap<String, Integer> getNameMap(){
		return namesMap;
	}
	public void AddState(String state)
	{
		values.add(state);
	}
	public String GetState(int curIndex)
	{
		if(curIndex<values.size())
			return values.get(curIndex);
		return null;
	}
	public Number GetVar(int curIndex,String varName)
	{
		String tres = null;
		if(!namesMap.containsKey(varName))
			return null;
		int index=namesMap.get(varName);
		if(curIndex<values.size())
		{
			String state=values.get(curIndex);
			tres=state.split(",")[index];	
		}
		return Double.valueOf(tres);
		
	}
	public List<String> GetAllState()
	{
		return values;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
