package com.k2.MetaModel.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import com.k2.MetaModel.MetaModelError;
import com.k2.MetaModel.annotations.MetaParameter;
import com.k2.MetaModel.annotations.MetaServiceMethod;
import com.k2.Util.tuple.Pair;

public class ServiceMethodInvoker {

	private Object serviceObject;
	private Method serviceMethod;
	private String alias;
	
	public ServiceMethodInvoker(Object serviceObject, Method serviceMethod) {
		this.serviceObject = serviceObject;
		this.serviceMethod = serviceMethod;
		this.alias = serviceMethod.getAnnotation(MetaServiceMethod.class).value();
	}
	
	
	public Parameter[] getParameters() {
		return serviceMethod.getParameters();
	}
	
	public Class<?> getReturnType() { return serviceMethod.getReturnType(); }
	
	public Class<?> getParameterType(String alias) {
		for (Parameter p : getParameters()) {
			if (p.getAnnotation(MetaParameter.class).value().equals(alias))
				return p.getType();
		}
		throw new MetaModelError("The service method does not require a parameter named {}", alias);
	}
	
	@SuppressWarnings("unchecked")
	public Object invoke(Pair<String, Object> ... parameterValues) throws Throwable {
		
		Object[] values = new Object[getParameters().length];
		
		if (parameterValues.length != values.length)
			throw new MetaModelError("Incorrect number of arguments invoking service method {} recieved {} expected {}", alias, parameterValues.length, values.length);
		
		for (Parameter p : getParameters()) {
			boolean found = false;
			for (int i=0; i<values.length; i++) {
				Pair<String, Object> parameter = parameterValues[i];
				if (p.getAnnotation(MetaParameter.class).value().equals(parameter.a)) {
					if (p.getType().isAssignableFrom(parameter.b.getClass())) {
						values[i] = parameter.b;
						found = true;
						break;
					}
					throw new MetaModelError("The recieved value for the parameter named {} was not of the correct type. Recieved {} expected {}", 
							p.getAnnotation(MetaParameter.class).value(), parameter.b.getClass().getName(), p.getType().getName());
				}
			}
			if ( ! found)
				throw new MetaModelError("No value supplied for the {} parameter when invoking service method {}", p.getAnnotation(MetaParameter.class).value(), alias);
		}
		
		try {
			return serviceMethod.invoke(serviceObject, values);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new MetaModelError("Unable to invoke service method {}::{} with alias {}", serviceMethod.getDeclaringClass().getName(), serviceMethod.getName(), alias );
		} catch (InvocationTargetException e) {
			throw e.getCause();
		}
		
	}
 
}
