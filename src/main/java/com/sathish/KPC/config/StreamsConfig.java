package com.sathish.KPC.config;

import com.sathish.KPC.messaging.streams.Stream;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

@EnableBinding(Stream.class)
@Configuration
public class StreamsConfig {
}