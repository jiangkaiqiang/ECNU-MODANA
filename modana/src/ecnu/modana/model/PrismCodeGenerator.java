package ecnu.modana.model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * Prism code generator (transforming EMF model to prism code)
 * @author cb
 */ 
public class PrismCodeGenerator {
	
	//the whole code string
	private StringBuilder codeStr = null;
	
	private ModelIO modelManager = null;
	
	public PrismCodeGenerator(String ecoreFile) {
		codeStr = new StringBuilder();
		modelManager = new ModelIO("default",ecoreFile,"");
	}
	
	/**
	 * generate code string and write it to file
	 * @param ModelFile XML model file
	 * @param fileName prism code file
	 */
	public void generatePrismFile(String ModelFile, String fileName) {
		FileWriter fw = null;
		try {
			System.out.println("generator code,ModelFile:"+ModelFile+" to:"+fileName);
			fw = new FileWriter(fileName, false);
			fw.write(generatePrismCode(ModelFile));
			//fw.write(generateCode(ModelFile));
			fw.flush();
		} catch (IOException e) {
			// TODO logger for e
			e.printStackTrace();
		}
		//close file
		if (fw != null) {
			try {
				fw.close();
			} catch (IOException e) {
				// TODO logger for e
				e.printStackTrace();
			}
		}
	}
	/**
	 * generate prism code string
	 * @param ModelFile XML model file
	 * @return code string
	 */
	private String generatePrismCode(String ModelFile) {
		EObject model = modelManager.LoadAModel(ModelFile);
		String lineSeparator = System.getProperty("line.separator");
		//write model type
		String mType=(String) modelManager.getEStructFeature(model, "type");
		codeStr.append(mType);
		codeStr.append(lineSeparator);
		
		//formulas
		List<Object> formulas = modelManager.getEStructListFeature(model, "formulas");
		for(Object obj:formulas)
		{
			EObject tEObject=(EObject)obj;
			codeStr.append("formula "+modelManager.getEStructFeature(tEObject, "name")+" = ");
			codeStr.append(modelManager.getEStructFeature(tEObject, "value")+lineSeparator);
		}
		List<Object> labels = modelManager.getEStructListFeature(model, "labels");
		for(Object obj:labels)
		{
			EObject tEObject=(EObject)obj;
			codeStr.append("label \""+modelManager.getEStructFeature(tEObject, "name")+"\" = ");
			codeStr.append(modelManager.getEStructFeature(tEObject, "value")+lineSeparator);
		}
		codeStr.append(lineSeparator);
		
		List<EObject> modules= (List<EObject>) modelManager.getEStructFeature(model, "modules");
		for(EObject module:modules)
		{
			codeStr.append("module "+modelManager.getEStructFeature(module, "name"));
			//codeStr.append(modelManager.getEStructFeature(model, "name"));
			codeStr.append(lineSeparator+lineSeparator);
			//collect state info
			//EObject module=((EList<EObject>)(modelManager.getEStructFeature(model, "modules"))).get(0);
			List<Object> states = modelManager.getEStructListFeature(module, "states");
			final class StateIndex { //inner class for collecting state info
				public int min;
				public int max;
				public String stateName;
				public StateIndex(int min, int max, String stateName) {
					this.min = min;
					this.max = max;
					this.stateName = stateName;
				}
			}
			ArrayList<StateIndex> stateIndexList = new ArrayList<StateIndex>();
			for (Object state: states) {
				int index = Integer.valueOf(modelManager.getEStructFeature((EObject)state, "index").toString());
				String name = modelManager.getEStructFeature((EObject)state, "name").toString();
				name=name.substring(0, name.length()-modelManager.getEStructFeature((EObject)state, "index").toString().length());
				int i = 0;
				for (i = 0; i < stateIndexList.size(); i++) {
					if (stateIndexList.get(i).stateName.equals(name)) { //name exists
						//update min or max index
						if (index < stateIndexList.get(i).min) {
							stateIndexList.get(i).min = index;
						} else if (index > stateIndexList.get(i).max) {
							stateIndexList.get(i).max = index;
						}
						break;
					}
				}
				if (i >= stateIndexList.size()) { //name doesn't exist, add new one
					stateIndexList.add(new StateIndex(index, index, name));
				}
			}		
			//write state var declaration
			for (StateIndex sIndex : stateIndexList) {
				codeStr.append(sIndex.stateName);
				codeStr.append(" : [");
				codeStr.append(sIndex.min);
				codeStr.append("..");
				codeStr.append(sIndex.max);
				codeStr.append("] init ");
				codeStr.append(sIndex.min);
				codeStr.append(";");
				codeStr.append(lineSeparator);
			}
			
			//collect transition info, source and syn(and condition)must be equal.
			List<Object> transitions = modelManager.getEStructListFeature(module, "transitions");
			Map<String, EObject> tMap=new HashMap<>();
			Map<String, ArrayList<EObject>> transMap = new HashMap<String, ArrayList<EObject>>();
			//Map<EObject, ArrayList<EObject>> transMap = new HashMap<EObject, ArrayList<EObject>>();
			List<StateIndex> varList = new ArrayList<StateIndex>();
			for (Object obj : transitions) {
				EObject transition = (EObject)obj;
				EObject source = (EObject)modelManager.getEStructFeature(transition, "source");
				String syn=(String) modelManager.getEStructFeature(transition, "syn");
				String condition=(String) modelManager.getEStructFeature(transition, "condition");
				String key=null;
				key=source.hashCode()+":"+syn+":"+condition;
//				if(!"ctmc".equals(mType))
//					key=source.hashCode()+":"+syn+":"+condition;
//				else
//					key=String.valueOf(source.hashCode());
				//find transition with same source
				if (transMap.get(key) == null) {
					ArrayList<EObject> newList = new ArrayList<EObject>();
					newList.add(transition);
					transMap.put(key, newList);
					tMap.put(key, source);
				} else {
					transMap.get(key).add(transition);
				}
	//			//collect variable assignment info
	//			List<Object> varAssigns = modelManager.getEStructListFeature(transition, "assigns");
	//			for (Object va : varAssigns) {
	//				String value = (String)modelManager.getEStructFeature((EObject)va, "index");
	//				String[] arrayStr = value.split("'=");
	//				int i = 0;
	//				for (i = 0; i < varList.size(); i++) {
	//					if (varList.get(i).stateName.equals(arrayStr[0])) { //name exists
	//						//update max (min value is 0 by default)
	//						if (Integer.valueOf(arrayStr[1]) > varList.get(i).max) {
	//							varList.get(i).max = Integer.valueOf(arrayStr[1]);
	//						}
	//						break;
	//					}
	//				}
	//				if (i >= varList.size()) { //name doesn't exist, add new one
	//					varList.add(new StateIndex(0, Integer.valueOf(arrayStr[1]), arrayStr[0]));
	//				}
	//			}
			}	
			//write other vars declaration
			for (StateIndex sIndex : varList) {
				codeStr.append(sIndex.stateName);
				codeStr.append(" : [");
				codeStr.append(sIndex.min);
				codeStr.append("..");
				codeStr.append(sIndex.max);
				codeStr.append("] init ");
				codeStr.append(sIndex.min);
				codeStr.append(";");
				codeStr.append(lineSeparator);
			}
			//write variable
			List<Object> Variables = modelManager.getEStructListFeature(model, "Variables");
			for(Object obj:Variables)
			{
				EObject tEObject=(EObject)obj;
				codeStr.append(modelManager.getEStructFeature(tEObject, "name")+" : ");
				codeStr.append(modelManager.getEStructFeature(tEObject, "value")+lineSeparator);
			}
			codeStr.append(lineSeparator);
			//write transition rule
			//Set<EObject> tempSet = transMap.keySet();
			Collection<String> tempSet =transMap.keySet();
			for (String tString : tempSet)
			{
				EObject eObj=tMap.get(tString);
				String syn,condition,prob,name,index;
				ArrayList<EObject> tempList = transMap.get(tString);
				if(tempList.size()==0) continue;
				syn=(String) modelManager.getEStructFeature(tempList.get(0), "syn");
				condition=(String) modelManager.getEStructFeature(tempList.get(0), "condition");
				codeStr.append("["+syn+"] ");
				name=(String) modelManager.getEStructFeature(eObj, "name");
				index=(String) modelManager.getEStructFeature(eObj, "index");
				codeStr.append(name.substring(0, name.length()-index.length()));
				codeStr.append("=");
				codeStr.append(index);
				if(""!=condition)
					codeStr.append(" & "+condition);
				codeStr.append(" -> ");
				for (int i = 0; i < tempList.size(); i++) 
				{
					if(i>=1&&"CTMC".equals(mType))
					{
						codeStr.append("["+syn+"] ");
						codeStr.append(name.substring(0, name.length()-index.length()));
						codeStr.append("=");
						codeStr.append(index);
						if(""!=condition)
							codeStr.append(" & "+condition);
						codeStr.append(" -> ");
					}
					//prob
					prob=(String) modelManager.getEStructFeature(tempList.get(i), "prob");
					if(""!=prob)
						codeStr.append(prob+" : ");
					codeStr.append("(");
					//target
					EObject target = (EObject)modelManager.getEStructFeature(tempList.get(i), "target");
					name=(String) modelManager.getEStructFeature(target, "name");
					index=(String) modelManager.getEStructFeature(target, "index");
					codeStr.append(name.substring(0,name.length()-index.length()));
					codeStr.append("'=");
					codeStr.append(modelManager.getEStructFeature(target, "index")+") ");
					String assign=(String) modelManager.getEStructFeature(tempList.get(i), "assigns");
					if(!assign.isEmpty())
					{	
						assign=assign.replaceAll("=", "\'=");
						codeStr.append(" & ("+assign+")");
					}
	//				//variable assignments
	//				List<Object> varAssignList = modelManager.getEStructListFeature(tempList.get(i), "assign");
	//				for (Object va : varAssignList) {
	//					codeStr.append(" & (");
	//					codeStr.append(modelManager.getEStructFeature((EObject)va, "index"));
	//					codeStr.append(")");
	//				}
					if("CTMC".equals(mType))
						codeStr.append(";"+lineSeparator);
					else {
						//'+' or ';'
						if ((i+1) >= tempList.size()) {
							codeStr.append(";");
							
						} else {
							codeStr.append(" + ");
						}
					}					
				}
				codeStr.append(lineSeparator);
			}
			//write end module
			codeStr.append(lineSeparator);
			codeStr.append("endmodule"+lineSeparator);
		}
		List<Object> moduleDefs = modelManager.getEStructListFeature(model, "ModuleDefs");
		for(Object obj:moduleDefs)
		{
			EObject tEObject=(EObject)obj;
			codeStr.append("module "+modelManager.getEStructFeature(tEObject, "name")+" = ");
			codeStr.append(modelManager.getEStructFeature(tEObject, "value")+lineSeparator);
		}
		return codeStr.toString();
	}
	private String generateCode(String ModelFile)
	{
		modelManager.LoadModel(ModelFile);
//		for(ArrayList<String> tArrayList:modelManager.loadRes)
//			System.err.println(tArrayList);
		String lineSeparator = System.getProperty("line.separator");
		int size=modelManager.loadRes.size();
		ArrayList<String> tList=modelManager.loadRes.get(size-1);
		codeStr.append(tList.get(2)+lineSeparator);
		codeStr.append("module "+tList.get(1)+lineSeparator);
		
		HashMap<String, Integer> statesMap=new HashMap<>();
		for(ArrayList<String> tArrayList:modelManager.loadRes)
			if("PrismState".equals(tArrayList.get(0)))
			{
				int end= tArrayList.get(1).length()-tArrayList.get(2).length();
				String name=tArrayList.get(1).substring(0,end);
				Integer tnew=Integer.valueOf(tArrayList.get(2));
				if(statesMap.containsKey(name))
				{
					if(tnew.compareTo(statesMap.get(name))>0)
						statesMap.put(name, tnew);
				}
				else statesMap.put(name, tnew);
			}
		//write state var declaration
		for (String stateName : statesMap.keySet()) {
			codeStr.append(stateName);
			codeStr.append(" : [0..");
			codeStr.append(statesMap.get(stateName));
			codeStr.append("] init 0;"+lineSeparator);
		}
		return codeStr.toString();
	}
//	private String generatePrismCode(String ModelFile) {
//		EObject model = modelManager.LoadAModel(ModelFile);
//		String lineSeparator = System.getProperty("line.separator");
//		//write model type
//		codeStr.append(modelManager.getEStructFeature(model, "type"));
//		codeStr.append(lineSeparator);
//		codeStr.append("module ");
//		codeStr.append(modelManager.getEStructFeature(model, "name"));
//		codeStr.append(lineSeparator);
//		//collect state info
//		List<Object> states = modelManager.getEStructListFeature(model, "PrismState");
//		final class StateIndex { //inner class for collecting state info
//			public int min;
//			public int max;
//			public String stateName;
//			public StateIndex(int min, int max, String stateName) {
//				this.min = min;
//				this.max = max;
//				this.stateName = stateName;
//			}
//		}
//		ArrayList<StateIndex> stateIndexList = new ArrayList<StateIndex>();
//		for (Object state: states) {
//			int index = Integer.valueOf(modelManager.getEStructFeature((EObject)state, "index").toString());
//			String name = modelManager.getEStructFeature((EObject)state, "name").toString();
//			int i = 0;
//			for (i = 0; i < stateIndexList.size(); i++) {
//				if (stateIndexList.get(i).stateName.equals(name)) { //name exists
//					//update min or max index
//					if (index < stateIndexList.get(i).min) {
//						stateIndexList.get(i).min = index;
//					} else if (index > stateIndexList.get(i).max) {
//						stateIndexList.get(i).max = index;
//					}
//					break;
//				}
//			}
//			if (i >= stateIndexList.size()) { //name doesn't exist, add new one
//				stateIndexList.add(new StateIndex(index, index, name));
//			}
//		}		
//		//write state var declaration
//		for (StateIndex sIndex : stateIndexList) {
//			codeStr.append(sIndex.stateName);
//			codeStr.append(" : [");
//			codeStr.append(sIndex.min);
//			codeStr.append("..");
//			codeStr.append(sIndex.max);
//			codeStr.append("] init ");
//			codeStr.append(sIndex.min);
//			codeStr.append(";");
//			codeStr.append(lineSeparator);
//		}
//		//collect transition info
//		List<Object> transitions = modelManager.getEStructListFeature(model, "transition");
//		Map<EObject, ArrayList<EObject>> transMap = new HashMap<EObject, ArrayList<EObject>>();
//		List<StateIndex> varList = new ArrayList<StateIndex>();
//		for (Object obj : transitions) {
//			EObject transition = (EObject)obj;
//			EObject source = (EObject)modelManager.getEStructFeature(transition, "source");
//			//find transition with same source
//			if (transMap.get(source) == null) {
//				ArrayList<EObject> newList = new ArrayList<EObject>();
//				newList.add(transition);
//				transMap.put(source, newList);
//			} else {
//				transMap.get(source).add(transition);
//			}
//			//collect variable assignment info
//			List<Object> varAssigns = modelManager.getEStructListFeature(transition, "assignments");
//			for (Object va : varAssigns) {
//				String value = (String)modelManager.getEStructFeature((EObject)va, "value");
//				String[] arrayStr = value.split("'=");
//				int i = 0;
//				for (i = 0; i < varList.size(); i++) {
//					if (varList.get(i).stateName.equals(arrayStr[0])) { //name exists
//						//update max (min value is 0 by default)
//						if (Integer.valueOf(arrayStr[1]) > varList.get(i).max) {
//							varList.get(i).max = Integer.valueOf(arrayStr[1]);
//						}
//						break;
//					}
//				}
//				if (i >= varList.size()) { //name doesn't exist, add new one
//					varList.add(new StateIndex(0, Integer.valueOf(arrayStr[1]), arrayStr[0]));
//				}
//			}
//		}	
//		//write other vars declaration
//		for (StateIndex sIndex : varList) {
//			codeStr.append(sIndex.stateName);
//			codeStr.append(" : [");
//			codeStr.append(sIndex.min);
//			codeStr.append("..");
//			codeStr.append(sIndex.max);
//			codeStr.append("] init ");
//			codeStr.append(sIndex.min);
//			codeStr.append(";");
//			codeStr.append(lineSeparator);
//		}
//		//write transition rule
//		Set<EObject> tempSet = transMap.keySet();
//		for (EObject eObj : tempSet) {
//			ArrayList<EObject> tempList = transMap.get(eObj);
//			codeStr.append("[] ");
//			codeStr.append(modelManager.getEStructFeature(eObj, "name"));
//			codeStr.append("=");
//			codeStr.append(modelManager.getEStructFeature(eObj, "index"));
//			codeStr.append(" -> ");
//			for (int i = 0; i < tempList.size(); i++) {
//				//prob
//				codeStr.append(modelManager.getEStructFeature(tempList.get(i), "prob"));
//				codeStr.append(" : (");
//				//target
//				EObject target = (EObject)modelManager.getEStructFeature(tempList.get(i), "target");
//				codeStr.append(modelManager.getEStructFeature(target, "name"));
//				codeStr.append("'=");
//				codeStr.append(modelManager.getEStructFeature(target, "index"));
//				codeStr.append(")");	
//				//variable assignments
//				List<Object> varAssignList = modelManager.getEStructListFeature(tempList.get(i), "assignments");
//				for (Object va : varAssignList) {
//					codeStr.append(" & (");
//					codeStr.append(modelManager.getEStructFeature((EObject)va, "value"));
//					codeStr.append(")");
//				}
//				//'+' or ';'
//				if ((i+1) >= tempList.size()) {
//					codeStr.append(";");
//					
//				} else {
//					codeStr.append(" + ");
//				}
//			}
//		}
//		//write end module
//		codeStr.append(lineSeparator);
//		codeStr.append("endmodule");
//		return codeStr.toString();
//	}
}
