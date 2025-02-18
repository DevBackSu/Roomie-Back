package com.example.roomie.SwaggerForm.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {
    @Schema(description = "성공 여부", example = "false")
    private String success;
    @Schema(description = "오류 내용")
    private String message;
}
