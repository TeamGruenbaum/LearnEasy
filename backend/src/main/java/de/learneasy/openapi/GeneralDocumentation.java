package de.learneasy.openapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    servers = { @Server(url = "http://localhost:8080") },
    info = @Info(
        title = "LearnEasy",
        version = "v0.1.0"
    )
)
public class GeneralDocumentation { }
