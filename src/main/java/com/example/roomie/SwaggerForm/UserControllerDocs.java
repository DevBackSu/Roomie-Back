package com.example.roomie.SwaggerForm;

import com.example.roomie.DTO.UserPageDTO;
import com.example.roomie.SwaggerForm.Response.ErrorResponse;
import com.example.roomie.SwaggerForm.Response.UserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@Tag(name="User", description = "사용자 관련 API 모음 (사용자 정보 호출 | 관심 친구 등록/해제 | 등등)")
public interface UserControllerDocs {

    @Operation(summary = "사용자 정보 호출", description = "사용자가 후 메인 페이지를 호출할 때 보이는 사용자 정보값을 반환한다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공",
                    content = {@Content(
                            schema = @Schema(implementation = UserPageDTO.class),
                            examples = {@ExampleObject(name="사용자 정보 호출",
                                    value = """
                            {
                                "user" : {
                                    "userId" : 1,
                                    "email" : "sample@gmail.com",
                                    "nickname" : "sample",
                                    "gender" : 남,
                                    "mainAnimal" : 2,
                                    "birthDate" : "nnnn-nn-nn",
                                    "school" : "학교",
                                    "local" : "지역",
                                    "imgUrl" : 3,
                                    "socialType" : "GOOGLE",
                                    "role" : "USER"
                                }
                            }
                            """, description = "1 : 종달새 / 2 : 부엉이 | imgUrl은 번호에 따라 프론트에서 다른 이미지를 반환함")}
                    )}
            ),
            @ApiResponse(responseCode = "200", description = "실패 처리",
                    content = {@Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {@ExampleObject(name = "실패",
                                    value = """
                            {
                                "Invalid access token" : {
                                    "success" : "false",
                                    "message" : "Invalid access token",
                                }
                                "User Not found" : {
                                    "success" : "false",
                                    "message" : "User not found",
                            }
                            """, description = "오류가 아닌 실패 시 반환값")}
                    )}
            ),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR",
                    content = {@Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {@ExampleObject(name = "Exception 오류",
                                    value = """
                            {
                                "success" : "false",
                                "error" : "error message"
                            }
                            """, description = "Exception 발생 시 반환")}
                    )}
            )
    })
    public ResponseEntity<Map<String, Object>> findUser(@RequestHeader("Authorization") String authHeader);
}
