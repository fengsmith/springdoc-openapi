/*
 *
 *  *
 *  *  * Copyright 2019-2020 the original author or authors.
 *  *  *
 *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  * you may not use this file except in compliance with the License.
 *  *  * You may obtain a copy of the License at
 *  *  *
 *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  * See the License for the specific language governing permissions and
 *  *  * limitations under the License.
 *  *
 *
 */

package org.springdoc.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * The interface Javadoc provider.
 * @author bnasslashen
 */
public interface JavadocProvider {

	/**
	 * Gets method description.
	 *
	 * @param method the method
	 * @return the method description
	 */
	String getMethodJavadocDescription(Method method);

	/**
	 * Gets method javadoc return.
	 *
	 * @param method the method
	 * @return the method javadoc return
	 */
	String getMethodJavadocReturn(Method method);

	/**
	 * Gets param javadoc.
	 *
	 * @param method the method
	 * @param name the name
	 * @return the param javadoc
	 */
	String getParamJavadoc(Method method, String name);

	String getFieldJavadoc(Field field);
}

