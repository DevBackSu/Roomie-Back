package com.example.roomie.SwaggerForm;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

@Tag(name="Chatting", description = "채팅 관련 API")
public interface ChatControllerDocs {

    @Operation(summary = "데이터 반환", description = "Map 데이터 반환")
    @Parameter(name = "user_id", description = "사용자의 채팅 리스트 조회를 위한 사용자 id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "400", description = "반환 실패")
    })
     Map<String, Object> chattingList();
}
