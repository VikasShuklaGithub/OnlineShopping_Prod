 package com.vikas.onlineShopping.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class INConstants {
	
	public final static String IN="IN";
	
	public final static Map<String, String> mapOfINStates=new HashMap<String, String>()
			{
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				{
					put("MH","Maharashtra");
					put("PB","Punjab");
					put("UP","UttarPradesh");
					put("TN","Tamilnadu");
					put("KR","Kerala");
					put("KA","Karnatka");
					put("MP","Madhya Pradesh");
					put("AS","Assam");
					put("NL","Nagaland");
			
				}
			};
	public final static List<String> listOfINStatesCode=new ArrayList<String>(mapOfINStates.keySet());
	public final static List<String> listOfINStatesName=new ArrayList<String>(mapOfINStates.values());

}
