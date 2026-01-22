package com.validacpf.config;

import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class FeignConfig {

	@Bean
	public Encoder feignEncoder() {
		final var jacksonConverter = new MappingJackson2HttpMessageConverter();
		final var messageConverters = new HttpMessageConverters(jacksonConverter);
		final ObjectFactory<HttpMessageConverters> messageConvertersFactory = () -> messageConverters;
		return new SpringEncoder(messageConvertersFactory);
	}

	@Bean
	public Decoder feignDecoder() {
		final var jacksonConverter = new MappingJackson2HttpMessageConverter();
		final var messageConverters = new HttpMessageConverters(jacksonConverter);
		final ObjectFactory<HttpMessageConverters> messageConvertersFactory = () -> messageConverters;
		return new ResponseEntityDecoder(new SpringDecoder(messageConvertersFactory));
	}
}
