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

package org.springdoc.webmvc.ui;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.core.SwaggerUiOAuthProperties;
import org.springdoc.ui.AbstractSwaggerIndexTransformer;

import org.springframework.core.io.Resource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.resource.ResourceTransformerChain;
import org.springframework.web.servlet.resource.TransformedResource;

/**
 * The type Swagger index transformer.
 * @author bnasslahsen
 */
public class SwaggerIndexPageTransformer extends AbstractSwaggerIndexTransformer implements SwaggerIndexTransformer {

	/**
	 * The Swagger welcome common.
	 */
	private final SwaggerWelcomeCommon swaggerWelcomeCommon;

	/**
	 * Instantiates a new Swagger index transformer.
	 * @param swaggerUiConfig the swagger ui config
	 * @param swaggerUiOAuthProperties the swagger ui o auth properties
	 * @param swaggerUiConfigParameters the swagger ui config parameters
	 * @param objectMapper the object mapper
	 * @param swaggerWelcomeCommon the swagger welcome common
	 */
	public SwaggerIndexPageTransformer(SwaggerUiConfigProperties swaggerUiConfig, SwaggerUiOAuthProperties swaggerUiOAuthProperties, SwaggerUiConfigParameters swaggerUiConfigParameters, ObjectMapper objectMapper, SwaggerWelcomeCommon swaggerWelcomeCommon) {
		super(swaggerUiConfig, swaggerUiOAuthProperties, swaggerUiConfigParameters, objectMapper);
		this.swaggerWelcomeCommon = swaggerWelcomeCommon;
	}

	@Override
	public Resource transform(HttpServletRequest request, Resource resource,
			ResourceTransformerChain transformerChain) throws IOException {
		if (swaggerUiConfigParameters.getConfigUrl() == null)
			swaggerWelcomeCommon.buildFromCurrentContextPath(request);

		final AntPathMatcher antPathMatcher = new AntPathMatcher();
		boolean isIndexFound = antPathMatcher.match("**/swagger-ui/**/index.html", resource.getURL().toString());

		if (isIndexFound) {
			String html = defaultTransformations(resource.getInputStream());
			return new TransformedResource(resource, html.getBytes());
		}
		else
			return resource;
	}

}