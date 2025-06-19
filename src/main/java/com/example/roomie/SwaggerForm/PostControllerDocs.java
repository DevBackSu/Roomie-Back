package com.example.roomie.SwaggerForm;

import com.example.roomie.SwaggerForm.Response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Tag(name="Post", description = "게시판 API")
public interface PostControllerDocs {
    @Operation(summary = "게시글 목록 조회", description = "제목, 내용 일부, 등록일이 포함된 게시글 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    ResponseEntity<Map<String, Object>> getPostList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "게시글 상세 조회", description = "postCheckId를 통해 게시글 상세 내용을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "게시글을 찾을 수 없음"),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = {@Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )}),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = {@Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )})
    })
    ResponseEntity<Map<String, Object>> getPostDetail(
            @PathVariable Long postCheckId,
            @RequestHeader("Authorization") String authHeader
    );
}
