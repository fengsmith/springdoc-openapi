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

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springdoc.core.ActuatorProvider;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SpringDocConfiguration;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.core.SwaggerUiOAuthProperties;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ConditionalOnManagementPort;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import static org.springdoc.core.Constants.SPRINGDOC_SWAGGER_UI_ENABLED;
import static org.springdoc.core.Constants.SPRINGDOC_USE_MANAGEMENT_PORT;
import static org.springdoc.core.Constants.SPRINGDOC_USE_ROOT_PATH;


/**
 * The type Swagger config.
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
@ConditionalOnBean(SpringDocConfiguration.class)
public class SwaggerConfig {

	/**
	 * Swagger welcome swagger welcome web mvc.
	 *
	 * @param swaggerUiConfig the swagger ui config
	 * @param springDocConfigProperties the spring doc config properties
	 * @param swaggerUiConfigParameters the swagger ui config parameters
	 * @param applicationContext the application context
	 * @return the swagger welcome web mvc
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(name = SPRINGDOC_USE_MANAGEMENT_PORT, havingValue = "false", matchIfMissing = true)
	SwaggerWelcomeWebMvc swaggerWelcome(SwaggerUiConfigProperties swaggerUiConfig, SpringDocConfigProperties springDocConfigProperties, SwaggerUiConfigParameters swaggerUiConfigParameters, ApplicationContext applicationContext) {
		return new SwaggerWelcomeWebMvc(swaggerUiConfig, springDocConfigProperties,swaggerUiConfigParameters, applicationContext);
	}

	/**
	 * Swagger config resource swagger config resource.
	 *
	 * @param swaggerWelcomeCommon the swagger welcome common
	 * @return the swagger config resource
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(name = SPRINGDOC_USE_MANAGEMENT_PORT, havingValue = "false", matchIfMissing = true)
	SwaggerConfigResource swaggerConfigResource(SwaggerWelcomeCommon swaggerWelcomeCommon){
		return new SwaggerConfigResource(swaggerWelcomeCommon);
	}

	/**
	 * Swagger ui home swagger ui home.
	 *
	 * @return the swagger ui home
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(name = SPRINGDOC_USE_ROOT_PATH, havingValue = "true")
	SwaggerUiHome swaggerUiHome(){
		return new SwaggerUiHome();
	}

	/**
	 * Index page transformer swagger index transformer.
	 *
	 * @param swaggerUiConfig the swagger ui config
	 * @param swaggerUiOAuthProperties the swagger ui o auth properties
	 * @param swaggerUiConfigParameters the swagger ui config parameters
	 * @param objectMapper the object mapper
	 * @param swaggerWelcomeCommon the swagger welcome common
	 * @return the swagger index transformer
	 */
	@Bean
	@ConditionalOnMissingBean
	SwaggerIndexTransformer indexPageTransformer(SwaggerUiConfigProperties swaggerUiConfig, SwaggerUiOAuthProperties swaggerUiOAuthProperties,  SwaggerUiConfigParameters swaggerUiConfigParameters, ObjectMapper objectMapper,SwaggerWelcomeCommon swaggerWelcomeCommon) {
		return new SwaggerIndexPageTransformer(swaggerUiConfig, swaggerUiOAuthProperties,swaggerUiConfigParameters, objectMapper, swaggerWelcomeCommon);
	}

	/**
	 * Swagger web mvc configurer swagger web mvc configurer.
	 *
	 * @param swaggerUiConfigParameters the swagger ui calculated config
	 * @param swaggerIndexTransformer the swagger index transformer
	 * @return the swagger web mvc configurer
	 */
	@Bean
	@ConditionalOnMissingBean
	SwaggerWebMvcConfigurer swaggerWebMvcConfigurer(SwaggerUiConfigParameters swaggerUiConfigParameters, SwaggerIndexTransformer swaggerIndexTransformer, Optional<ActuatorProvider> actuatorProvider) {
		return new SwaggerWebMvcConfigurer(swaggerUiConfigParameters, swaggerIndexTransformer, actuatorProvider);
	}

	/**
	 * Swagger ui config parameters swagger ui config parameters.
	 *
	 * @param swaggerUiConfig the swagger ui config
	 * @return the swagger ui config parameters
	 */
	@Bean
	@ConditionalOnMissingBean
	SwaggerUiConfigParameters swaggerUiConfigParameters(SwaggerUiConfigProperties swaggerUiConfig) {
		return new SwaggerUiConfigParameters(swaggerUiConfig);
	}

	@ConditionalOnProperty(SPRINGDOC_USE_MANAGEMENT_PORT)
	@ConditionalOnClass(WebMvcEndpointHandlerMapping.class)
	@ConditionalOnManagementPort(ManagementPortType.DIFFERENT)
	static class SwaggerActuatorWelcomeConfiguration {

		@Bean
		@ConditionalOnMissingBean
		SwaggerWelcomeActuator swaggerActuatorWelcome(SwaggerUiConfigProperties swaggerUiConfig, SpringDocConfigProperties springDocConfigProperties, SwaggerUiConfigParameters swaggerUiConfigParameters, WebEndpointProperties webEndpointProperties) {
			return new SwaggerWelcomeActuator(swaggerUiConfig, springDocConfigProperties, swaggerUiConfigParameters, webEndpointProperties);
		}
	}
}
