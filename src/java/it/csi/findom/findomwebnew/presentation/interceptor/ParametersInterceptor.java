/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.presentation.interceptor;


import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.interceptor.NoParameters;
import com.opensymphony.xwork2.interceptor.ParameterNameAware;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.opensymphony.xwork2.util.TextParseUtil;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.reflection.ReflectionContextState;

/**
 * This interceptor gets all parameters from ActionContext.getParameters() and sets them on the value stack by calling ValueStack.setValue(String, Object), 
 * typically resulting in the values submitted in a form request being applied to an action in the value stack. 
 * Note that the parameter map must contain a String key and often containers a String[] for the value
 * 
 * @author 1427
 *
 */
public class ParametersInterceptor extends AbstractInterceptor {

	private static final Log LOGs = LogFactory.getLog(ParametersInterceptor.class);
	
	boolean ordered = false;
	Set<Pattern> acceptedParams = Collections.EMPTY_SET;
	Set<Pattern> excludeParams = Collections.EMPTY_SET;
	static boolean devMode = false;
	
	private String acceptedParamNames = "[\\p{Graph}&&[^,#:=]]*";
	private Pattern acceptedPattern = Pattern.compile(acceptedParamNames);
	
	
	@Override
	public String intercept(ActionInvocation arg0) throws Exception {
		return doIntercept(arg0);
	}

	public String doIntercept(ActionInvocation invocation) throws Exception {
//		System.out.println("[ParametersInterceptor::doIntercept] BEGIN");
		
	try {
		Object action = invocation.getAction();
		
		if (!(action instanceof NoParameters)) {
			
			ActionContext ac = invocation.getInvocationContext();
			final Map parameters = ac.getParameters();
//			System.out.println("[ParametersInterceptor::doIntercept]  Setting params " + getParameterLogMap(parameters));

			if (parameters != null) {

				final Map newParameters = new HashMap();
				newParameters.putAll(parameters);
				for (Iterator iterator = parameters.keySet().iterator(); iterator.hasNext();) {
					String key = (String) iterator.next();
					Object param = parameters.get(key);
//					System.out.println("[ParametersInterceptor::doIntercept]  key= " + key);
					normalizeParams(key, param, newParameters, invocation);
				}

				Map contextMap = ac.getContextMap();
				try {
					ReflectionContextState.setCreatingNullObjects(contextMap, true);
					ReflectionContextState.setDenyMethodExecution(contextMap, true);
					ReflectionContextState.setReportingConversionErrors(contextMap, true);

					ValueStack stack = ac.getValueStack();
					setParameters(action, stack, newParameters);
				} finally {
					ReflectionContextState.setCreatingNullObjects(contextMap, false);
					ReflectionContextState.setDenyMethodExecution(contextMap, false);
					ReflectionContextState.setReportingConversionErrors(contextMap, false);
				}
			}
		}
	  } catch (Exception e) {
//		  System.out.println("[ParametersInterceptor::Error");
		  e.printStackTrace();
		  throw e;
	  }
//		System.out.println("[ParametersInterceptor::doIntercept] END");
		return invocation.invoke();
		
	}
	
	private void normalizeParams(String key, Object param, Map parameters,	ActionInvocation invocation) {

		if (key.contains("?")) {
			parameters.remove(key);
			String newKey = key.substring(0, key.indexOf("?"));
			parameters.put(newKey, param);
			addMethodParams(key.substring(key.indexOf("?") + 1, key.length()),	parameters);
		}
		
	}
	
	private void addMethodParams(String substring, Map parameters) {
		StringTokenizer tok = new StringTokenizer(substring, "&");
		while (tok.hasMoreTokens()) {
			String tmp = tok.nextToken();
			parameters.put(tmp.substring(0, tmp.indexOf("=")),	tmp.substring(tmp.indexOf("=") + 1, tmp.length()));
		}

	}
	private String getParameterLogMap(Map parameters) {
		if (parameters == null) {
			return "NONE";
		}

		StringBuffer logEntry = new StringBuffer();
		for (Iterator paramIter = parameters.entrySet().iterator(); paramIter.hasNext();) {
			Map.Entry entry = (Map.Entry) paramIter.next();
			logEntry.append(String.valueOf(entry.getKey()));
			logEntry.append(" => ");
			if (entry.getValue() instanceof Object[]) {
				Object[] valueArray = (Object[]) entry.getValue();
				logEntry.append("[ ");
				for (int indexA = 0; indexA < (valueArray.length - 1); indexA++) {
					Object valueAtIndex = valueArray[indexA];
					logEntry.append(valueAtIndex);
					logEntry.append(String.valueOf(valueAtIndex));
					logEntry.append(", ");
				}
				logEntry.append(String.valueOf(valueArray[valueArray.length - 1]));
				logEntry.append(" ] ");
			} else {
				logEntry.append(String.valueOf(entry.getValue()));
			}
		}

		return logEntry.toString();
	}
	
	protected Comparator getOrderedComparator() {
		return rbCollator;
	}
	
	static final Comparator rbCollator = new Comparator() {
		public int compare(Object arg0, Object arg1) {
			String s1 = (String) arg0;
			String s2 = (String) arg1;
			int l1 = 0, l2 = 0;
			for (int i = s1.length() - 1; i >= 0; i--) {
				if (s1.charAt(i) == '.')
					l1++;
			}
			for (int i = s2.length() - 1; i >= 0; i--) {
				if (s2.charAt(i) == '.')
					l2++;
			}
			return l1 < l2 ? -1 : (l2 < l1 ? 1 : s1.compareTo(s2));
		}

		;
	};
	
	protected boolean acceptableName(String name) {
		if (isAccepted(name) && !isExcluded(name)) {
			return true;
		}
		return false;
	}
	
	protected boolean isAccepted(String paramName) {
		if (!this.acceptedParams.isEmpty()) {
			for (Pattern pattern : acceptedParams) {
				Matcher matcher = pattern.matcher(paramName);
				if (!matcher.matches()) {
					return false;
				}
			}
		}
		return acceptedPattern.matcher(paramName).matches();
	}

	protected boolean isExcluded(String paramName) {
//		System.out.println("[ParametersInterceptor::isExcluded] BEGIN, paramName="+paramName);
//		System.out.println("[ParametersInterceptor::isExcluded] excludeParams="+this.excludeParams);
		if (!this.excludeParams.isEmpty()) {
			for (Pattern pattern : excludeParams) {
				Matcher matcher = pattern.matcher(paramName);
				if (matcher.matches()) {
					return true;
				}
			}
		}
		return false;
	}
	
	protected void setParameters(Object action, ValueStack stack, final Map parameters) {
//		System.out.println("[ParametersInterceptor::setParameters] BEGIN");
		ParameterNameAware parameterNameAware = (action instanceof ParameterNameAware)
				? (ParameterNameAware) action
				: null;

		Map params = null;
		if (ordered) {
			params = new TreeMap(getOrderedComparator());
			params.putAll(parameters);
		} else {
			params = new TreeMap(parameters);
		}

		for (Iterator iterator = params.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String name = entry.getKey().toString();
//			System.out.println("[ParametersInterceptor::setParameters] name="+name);
			
			boolean acceptableName = acceptableName(name)
					&& (parameterNameAware == null || parameterNameAware
							.acceptableParameterName(name));

//			System.out.println("[ParametersInterceptor::setParameters] acceptableName="+acceptableName);
			
			if (acceptableName) {
				Object value = entry.getValue();
//				if(value!=null){
//					System.out.println("[ParametersInterceptor::setParameters] value=["+value.toString()+"]");
//				}else{
//					System.out.println("[ParametersInterceptor::setParameters] value=[null]");
//				}
				try {
					stack.setValue(name, value);
					
				} catch (RuntimeException e) { //NOSONAR  Reason:dubbia classificazione severity
					if (devMode) {
						String developerNotification = LocalizedTextUtil
								.findText(ParametersInterceptor.class,
										"devmode.notification", ActionContext.getContext().getLocale(),
										"Developer Notification:\n{0}",
										new Object[]{e.getMessage()});
//						System.out.println("[ParametersInterceptor::setParameters] developerNotification="+developerNotification);
						
						if (action instanceof ValidationAware) {
							((ValidationAware) action).addActionMessage(developerNotification);
						}
					} else {
						System.out.println("ParametersInterceptor - [setParameters]: Unexpected Exception caught setting '"
								+ name
								+ "' on '"
								+ action.getClass()
								+ ": "
								+ e.getMessage());
					}
				}
			}
		}
//		System.out.println("[ParametersInterceptor::setParameters] END");
	}
	
	protected Set getExcludeParamsSet() {
		return excludeParams;
	}
	public void setExcludeParams(String commaDelim) {
		Collection<String> excludePatterns = asCollection(commaDelim);
		if (excludePatterns != null) {
			excludeParams = new HashSet<Pattern>();
			for (String pattern : excludePatterns) {
				excludeParams.add(Pattern.compile(pattern));
			}
		}
	}
	private Collection asCollection(String commaDelim) {
		if (commaDelim == null || commaDelim.trim().length() == 0) {
			return null;
		}
		return TextParseUtil.commaDelimitedStringToSet(commaDelim);
	}
}
