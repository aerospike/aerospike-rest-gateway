/*
 * Copyright 2019 Aerospike, Inc.
 *
 * Portions may be licensed to Aerospike, Inc. under one or more contributor
 * license agreements WHICH ARE COMPATIBLE WITH THE APACHE LICENSE, VERSION 2.0.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.aerospike.restclient.config;

import com.aerospike.restclient.domain.RestClientError;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Autowired private TypeResolver typeResolver;

	@Autowired private BuildProperties buildProperties;

	private ApiInfo apiInfo() {
		ApiInfo info = new ApiInfoBuilder()
				.version(buildProperties.getVersion())
				.title("Aerospike REST Client")
				.description("REST Interface for Aerospike Database.")
				.contact(new Contact("Aerospike, Inc.",
						"https://www.aerospike.com", ""))
				.build();
		return info;
	}

	@Bean
	public Docket api() {

		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(Predicates.not(PathSelectors.regex("/error*"))) // We don't want the base error controller listed.
				.build()
				.useDefaultResponseMessages(false)
				.globalResponseMessage(RequestMethod.GET, default500Error())
				.globalResponseMessage(RequestMethod.POST, default500Error())
				.globalResponseMessage(RequestMethod.PATCH, default500Error())
				.globalResponseMessage(RequestMethod.DELETE, default500Error())
				.globalResponseMessage(RequestMethod.HEAD, default500Error())
				.globalResponseMessage(RequestMethod.PUT, default500Error())
				.additionalModels(typeResolver.resolve(RestClientError.class))
				.alternateTypeRules(alternateRules());
	}

	private AlternateTypeRule[] alternateRules() {
		return new AlternateTypeRule[] {
				AlternateTypeRules.newRule(
						typeResolver.resolve(List.class, typeResolver.resolve(Map.class, String.class, Object.class)),
						typeResolver.resolve(List.class, Object.class))
		};
	}

	private List<springfox.documentation.service.ResponseMessage> default500Error() {
		return Arrays.asList(
				new ResponseMessageBuilder()
				.code(500)
				.responseModel(new ModelRef("RestClientError"))
				.message("The REST Client encountered an error processing the request")
				.build());
	}
}
