package com.example.roomie.SwaggerForm;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name="User", description = "사용자 관련 API 모음 (소셜 로그인/로그아웃 | 관심 친구 등록/해제 | 등등")
public interface UserControllerDocs {

    @Operation(summary = "사용자 데이터 반환", description = "리스트 데이터 반환")
    @Parameter(name = "", description = "파라미터 없음")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "400", description = "반환 실패")
    })
    List<String> getUser();


}
