/*
 *
 *  * Copyright 2019-2020 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package test.org.springdoc.api.app172;

import org.junit.jupiter.api.Test;
import org.springdoc.core.Constants;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import test.org.springdoc.api.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = { "springdoc.show-actuator=true",
		"management.endpoints.web.exposure.include = tenant" })
public class SpringDocApp172Test extends AbstractSpringDocTest {

	@SpringBootApplication
	static class SpringDocTestApp {
		@Bean
		public GroupedOpenApi actuatorApi(OpenApiCustomiser actuatorOpenApiCustomiser, OperationCustomizer actuatorCustomizer) {
			return GroupedOpenApi.builder()
					.group("sample-group")
					.packagesToScan("test.org.springdoc.api.app172")
					.addOpenApiCustomiser(actuatorOpenApiCustomiser)
					.addOperationCustomizer(actuatorCustomizer)
					.pathsToExclude("/health/*")
					.build();
		}
	}

	@Test
	public void testApp() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/sample-group"))
				.andExpect(jsonPath("$.openapi", is("3.0.1")))
				.andExpect(status().isOk())
				.andExpect(content().json(getContent("results/app172.json"), true));
	}

}