package com.example.roomie.SwaggerForm;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name="Main", description = "메인 화면 관련 API가 들어올 예정")
public interface MainControllerDocs {

    @Operation(summary = "데이터 반환", description = "리스트 데이터 반환")
    @Parameter(name = "", description = "파라미터 없음")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "400", description = "반환 실패")
    })
    List<String> getData();


    List<String> getMyPage();

    String getHomeDate();
}
