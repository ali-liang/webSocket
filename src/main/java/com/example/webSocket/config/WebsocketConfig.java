package com.example.webSocket.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import com.example.webSocket.ResultHandler;
@Configuration
public class WebsocketConfig {
	@Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}