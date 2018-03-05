/**
 * 
 */
package com.k2.MetaModel;

import com.k2.Util.StringUtil;

/**
 * MetaModelErrors are unchecked errors thrown by the MetaModel in response to conditions from which it is not expected that an application
 * using the meta model would be able to recover. This is most typically the case during configuration of the meta model where unexpected 
 * conditions are generally fatal.
 * 
 * @author Simon Emmott
 *
 */
public class MetaModelError extends Error {

	private static final long serialVersionUID = -2877328727563978131L;

	/**
	 * Create a new MetaModelError for the given message replacing all instances of '{}' in the message with the StringUtil.toString(Object)
	 * value of the given object replacements
	 * 
	 * @param message	The message template for the MetaModelError
	 * @param replacements	An object array of replacements to replaces instances of '{}' with in the given message template
	 */
	public MetaModelError(String message, Object ...replacements) {
		super(StringUtil.replaceAll(message, "{}", replacements));
	}

	/**
	 * Create a new MetaModelError for the given throwable cause
	 * 
	 * @param cause	The throwable cause giving rise to this Error
	 */
	public MetaModelError(Throwable cause) {
		super(cause);
	}

	/**
	 * Create a new MetaModelError for the given message and throwable clause replacing all instances of '{}' in the message with the 
	 * StringUtil.toString(Object) value of the given object replacements
	 * 
	 * @param message	The message template for the MetaModelError 
	 * @param cause		The throwable cause giving rise to this Error
	 * @param replacements	An object array of replacements to replaces instances of '{}' with in the given message template
	 */
	public MetaModelError(String message, Throwable cause, Object ...replacements) {
		super(StringUtil.replaceAll(message, "{}", replacements), cause);
	}

}
