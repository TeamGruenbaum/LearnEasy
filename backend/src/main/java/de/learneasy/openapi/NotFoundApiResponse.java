package de.learneasy.openapi;

import de.learneasy.datatransferobjects.ErrorDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApiResponse(
    responseCode = "404",
    content = { @Content(schema = @Schema(implementation = ErrorDTO.class)) }
)
public @interface NotFoundApiResponse { }