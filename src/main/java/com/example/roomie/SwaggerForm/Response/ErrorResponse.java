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
    @Schema(description = "400 - 요청 오류 / 401 - 인증 실패 / 500 - 서버 내부 오류")
    private String message;
}
