package ecnu.modana.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EAttributeImpl;
import org.eclipse.emf.ecore.impl.EReferenceImpl;
import org.eclipse.emf.ecore.impl.EStoreEObjectImpl.EStoreImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;

import com.sun.swing.internal.plaf.synth.resources.synth_es;

/**
 * Modana model framework manager
 * you need to ensure each EClass owns a distinct name
 * (for creating, updating, querying, deleting EMF-based models)
 * @author cb
 */
public class ModelIO {
	EPackage thisEPackage = null;
	EFactory thisEFactory = null;
	EObject model;
	Map<String, EObject>eobjectMap=new HashMap<>();
	private String modelName="PrismModel";
	private String modelEClassName="";
	private char seperater='#';
	/**
	 * 
	 * @param ecoreFile ecore filePath
	 * @param modelEClassName EClassName of model
	 * @param modelName name of this model,must not change again
	 */
	public ModelIO(String modelName,String ecoreFile,String modelEClassName)
	{
		try
		{
			loadEcore(ecoreFile);
			model=createEObject(modelEClassName);
			AddEStructFeature(model, "name", modelName);
			eobjectMap.put(modelName, model);
			this.modelEClassName=modelEClassName;
			this.modelName=modelName;
			
//			EClass eClass= ((EClass)thisEPackage.getEClassifier("PrismTransition"));
//			System.out.println(eClass.getEStructuralFeature("source"));			
//			setProperty("model", "name1","dtmc");
//			newEClass("PrismRewards", "reward0");
//			setReference("model", "PrismRewards", "reward0");
//			System.err.println(model.eClass().getName());
			
			//loadEPackage();
			//saveEcore("/.prism.ecore");
		} catch (Exception e)
		{
			System.err.println(e.getMessage());
		}		
	}
	public boolean NewEClass(String eclassName,String objName)
	{
		EObject object=createEObject(eclassName);
		if(object==null) return false;
		if(!eobjectMap.containsKey(objName))
		    eobjectMap.put(objName, object);
		else
		   return false;
		return true;
	}
	/**
	 * set all property of a EClass,not contain reference
	 * @param eobj
	 * @param propertyValue
	 * @return
	 */
	public boolean SetProperty(String objName,String...propertyValue) {
		EObject obj=eobjectMap.get(objName);
		if(null==obj) return false;
		EList<EStructuralFeature> eList=((EClass)obj.eClass()).getEStructuralFeatures();
		int elistCnt=GetEclassPropertyCount(eList);
		int k=0;
		if(propertyValue.length!=elistCnt)
		{
			System.err.println("淇濆瓨鍑洪敊,鏁扮洰涓嶄竴锛�"+objName+" "+propertyValue);
			return false;
		}
		for(int i=0,j=0;i<eList.size();i++)
			if(eList.get(i) instanceof EAttributeImpl) //锟斤拷锟斤拷锟皆ｏ拷 EReferenceImpl 锟斤拷锟斤拷锟斤拷
				obj.eSet(eList.get(i), propertyValue[j++]);
//		for(int i=0;i<propertyValue.length;i++)
//			obj.eSet(eList.get(i), propertyValue[i]);
		return true;
	}
	public void ChangeModelName(String newName)
	{
		AddEStructFeature(model, "name", newName);
		eobjectMap.remove(getEStructFeature(model, "name"));
		eobjectMap.put(newName, model);
		this.modelName=newName;
	}
	/**
	 * add a Rrfrence to a EClass
	 * @param to
	 * @param propertyName
	 * @param ref
	 * @return
	 */
	public boolean SetReference(String to,String propertyName,String refName) {
		EObject obj=eobjectMap.get(to),refObj=eobjectMap.get(refName);
		//if(null==obj||null==refObj) return false;
		if(obj==null||refObj==null)
		{
			String t= obj==null?to:refName;
			System.err.println("SetReference error:"+propertyName+",object instance:"+t+" do not exist");
			return false;
		}
		AddEStructFeature(obj, propertyName, refObj);
		return true;
	}
	
	public boolean LoadModel(String filePath)
	{
		try
		{
			model=LoadAModel(filePath);
			this.modelName=(String) getEStructFeature(model, "name");
			System.err.println(modelName);
			eobjectMap.clear();
			//AddEclassObj(model,eobjectMap);
			loadRes=new ArrayList<ArrayList<String>>();
			GetAllData(model,eobjectMap);
//			System.err.println("ha");
//			for(ArrayList<String> tArrayList:loadRes)
//				System.err.println(tArrayList);
		} catch (Exception e)
		{
			return false;
		}
		return true;
	}
	ArrayList<ArrayList<String>> loadRes;
	public ArrayList<ArrayList<String>> GetAllData(EObject obj,Map<String,EObject> map)
	{
		ArrayList<String> tArrayList;
		String objName=null;
		objName=(String) getEStructFeature(obj, "name");
		if(null==objName||map.containsKey(objName))//EClass 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
			return loadRes;
		EList<EStructuralFeature> eList=((EClass)obj.eClass()).getEStructuralFeatures();
		List<Object>tEList;
		for(int i=0;i<eList.size();i++)
		{
			//System.out.println(eList.get(i));
			if(eList.get(i) instanceof EReferenceImpl) //锟斤拷锟斤拷锟皆ｏ拷 EReferenceImpl 锟斤拷锟斤拷锟斤拷
			{
				tEList=getEStructListFeature(obj, eList.get(i).getName());
				for(Object object:tEList)
					GetAllData((EObject) object, map);
			}
		}
		
		tArrayList=new ArrayList<String>();
		tArrayList.add(obj.eClass().getName());
		for(EStructuralFeature eStructuralFeature:eList)
			if(eStructuralFeature instanceof EAttributeImpl)
				tArrayList.add((String) obj.eGet(eStructuralFeature));
		for(int i=0;i<eList.size();i++)
		{
			//System.out.println(eList.get(i));
			if(eList.get(i) instanceof EReferenceImpl) //锟斤拷锟斤拷锟皆ｏ拷 EReferenceImpl 锟斤拷锟斤拷锟斤拷
			{
				tEList=getEStructListFeature(obj, eList.get(i).getName());
				for(Object object:tEList)
					tArrayList.add(getEStructFeature((EObject) object, "name").toString()+seperater);
					//AddEclassObj((EObject) object, map);
			}
		}
		loadRes.add(tArrayList);
		map.put(objName, obj);
		return loadRes;
	}
	private boolean AddEclassObj(EObject obj,Map<String,EObject> map)
	{
		String objName=null;
		objName=(String) getEStructFeature(obj, "name");
		if(null!=objName&&!map.containsKey(objName))
			map.put(objName, obj);
		else //EClass 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
			return false;
		EList<EStructuralFeature> eList=((EClass)obj.eClass()).getEStructuralFeatures();
		List<Object>tEList;
		for(int i=0;i<eList.size();i++)
		{
			//System.out.println(eList.get(i));
			if(eList.get(i) instanceof EReferenceImpl) //锟斤拷锟斤拷锟皆ｏ拷 EReferenceImpl 锟斤拷锟斤拷锟斤拷
			{
				tEList=getEStructListFeature(obj, eList.get(i).getName());
				for(Object object:tEList)
					AddEclassObj((EObject) object, map);
			}
		}
		return true;
	}
	public ArrayList<String>GetProperty(String objName)
	{
		ArrayList<String> res=new ArrayList<>();
		EObject obj=eobjectMap.get(objName);
		if(null==obj) return res;
		EList<EStructuralFeature> eList=((EClass)obj.eClass()).getEStructuralFeatures();
		for(int i=0;i<eList.size();i++)
		{
			//System.out.println(eList.get(i));
			if(eList.get(i) instanceof EAttributeImpl) //锟斤拷锟斤拷锟皆ｏ拷 EReferenceImpl 锟斤拷锟斤拷锟斤拷
				res.add(obj.eGet(eList.get(i)).toString());
		}
		return res;
	}
	
	//锟斤拷锟铰讹拷锟斤拷支锟斤拷
	public EObject createEObject(String eClassName) {
		try
		{
			return thisEFactory.create((EClass)thisEPackage.getEClassifier(eClassName));
		} catch (Exception e)
		{
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void AddEStructFeature(EObject eObj, String eStructFeatureName, Object value) {
		EStructuralFeature eFeature = ((EClass)thisEPackage
			.getEClassifier(eObj.eClass().getName())).getEStructuralFeature(eStructFeatureName);
		if(eFeature==null)
		{
			System.err.println(eObj.eClass().getName()+" Do not contain EStructFeature: "+eStructFeatureName);
			return;
		}
		if (eFeature.getUpperBound() > 1 
			|| eFeature.getUpperBound() == EStructuralFeature.UNBOUNDED_MULTIPLICITY) {
			((List<Object>)eObj.eGet(eFeature)).add(value);
		} else {
			eObj.eSet(eFeature, value);
		}
	}
	
	public Object getEStructFeature(EObject eObj, String eStructFeatureName) {
		EStructuralFeature eFeature = ((EClass)thisEPackage
			.getEClassifier(eObj.eClass().getName())).getEStructuralFeature(eStructFeatureName);
		return eObj.eGet(eFeature);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getEStructListFeature(EObject eObj, String eStructFeatureName) {
		EStructuralFeature eFeature = ((EClass)thisEPackage
			.getEClassifier(eObj.eClass().getName())).getEStructuralFeature(eStructFeatureName);
		if (eFeature.getUpperBound() > 1 
			|| eFeature.getUpperBound() == EStructuralFeature.UNBOUNDED_MULTIPLICITY) {
			return (List<Object>)eObj.eGet(eFeature);
		} else {
			List<Object> retList = new ArrayList<Object>();
			retList.add(eObj.eGet(eFeature));
			return retList;
		}
	}
	
	public EObject LoadAModel(String fileName) {
		try {
			//load file
			//fileName="./11.xml";
			ResourceSet resourceSet = new ResourceSetImpl();
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xml", new XMIResourceFactoryImpl()); //note new XMLResourceFactoryImpl()
			resourceSet.getPackageRegistry().put(thisEPackage.getNsURI(), thisEPackage);
			//Resource resource = resourceSet.getResource(URI.createURI(fileName), true);
			Resource resource = resourceSet.getResource(URI.createFileURI(fileName), true); //note createURI
			((XMLResource)resource).setEncoding("UTF-8");
			return resource.getContents().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * save a Model
	 * @param fileName
	 * @param modelEObj
	 */
	public void save(String fileName, EObject modelEObj) {
		//save file
//		ResourceSet resourceSet = new ResourceSetImpl();
//		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
//			.put("*", new XMLResourceFactoryImpl());
//		Resource resource = resourceSet.createResource(URI.createURI(fileName));
		
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xml", new XMIResourceFactoryImpl());
		Resource resource = resourceSet.createResource(URI.createFileURI(fileName));//"d:/temp/company.xml"));
		((XMLResource)resource).setEncoding("UTF-8");
		resource.getContents().add(modelEObj);
		try {
			resource.save(null);
			DeleteAllElements();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void SaveModel(String fileName)
	{
//		System.err.println(fileName);
//		eobjectMap.clear();
//		//AddEclassObj(model,eobjectMap);
//		loadRes=new ArrayList<ArrayList<String>>();
//		GetAllData(model,eobjectMap);
//		for(ArrayList<String> tArrayList:loadRes)
//			System.err.println(tArrayList);
		save(fileName, model);
	}
	public void DeleteAllElements()
	{
		eobjectMap.clear();
		model=createEObject(modelEClassName);
		AddEStructFeature(model, "name", modelName);
		eobjectMap.put(modelName, model);
		eobjectMap.put(this.modelName, model);
	}
	
	public void loadEcore(String fileName) {
		//load ecore file
//		ResourceSet metaResourceSet = new ResourceSetImpl();
//		metaResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
//			    "ecore", new  XMLResourceFactoryImpl());
//		Resource metaResource = metaResourceSet.getResource(URI.createURI(fileName), true);
//		((XMLResource)metaResource).setEncoding("UTF-8");
//		thisEPackage = (EPackage)metaResource.getContents().get(0);
//		thisEFactory = thisEPackage.getEFactoryInstance();
		
		EcoreFactory ecoreFactory = EcoreFactory.eINSTANCE;
		EcorePackage ecorePackage = EcorePackage.eINSTANCE;
		ResourceSet resourceSet = new ResourceSetImpl();
		//注锟斤拷XML锟斤拷源锟斤拷锟斤拷
	    resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new XMIResourceFactoryImpl());
	    URI fileURI =URI.createFileURI(new File(fileName).getAbsolutePath());
		Resource poResource = resourceSet.getResource(fileURI, true);
		thisEPackage = (EPackage)poResource.getContents().get(0);
		thisEFactory = thisEPackage.getEFactoryInstance();
		//System.err.println("ok");
	}
	
	public void saveEcore(String fileName) {
		if (thisEFactory == null) {
			return;
		}
		//save ecore file
		ResourceSet metaResourceSet = new ResourceSetImpl();
		metaResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
		    "ecore", new  XMLResourceFactoryImpl());
		Resource metaResource = metaResourceSet.createResource(URI.createURI(fileName));
		((XMLResource)metaResource).setEncoding("UTF-8");
		metaResource.getContents().add(thisEPackage);
		try {
			metaResource.save(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	private int GetEclassPropertyCount(EList<EStructuralFeature> eList)
	{
		if(null==eList) return 0;
		int res=0;
		for(int i=0;i<eList.size();i++)
		{
			if(eList.get(i) instanceof EAttributeImpl) //锟斤拷锟斤拷锟皆ｏ拷 EReferenceImpl 锟斤拷锟斤拷锟斤拷
				res++;
		}
		return res;
	}
}
