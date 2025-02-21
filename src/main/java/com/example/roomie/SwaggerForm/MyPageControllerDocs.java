package com.example.roomie.SwaggerForm;

import com.example.roomie.DTO.UserOtherDTO;
import com.example.roomie.DTO.UserPageDTO;
import com.example.roomie.SwaggerForm.Response.ErrorResponse;
import com.example.roomie.SwaggerForm.Response.UserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@Tag(name = "MyPage", description = "마이페이지 API")
public interface MyPageControllerDocs {

    @Operation(summary = "마이페이지에 필요한 데이터 반환", description = "프로필 이미지, 이름, 성별, 생년월, 가입한 소셜 네트워크, 학교, 지역, 특성, 자기소개가 반환됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공",
                content = {@Content(
                        schema = @Schema(implementation = UserInfoResponse.class)
                )}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = {@Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )})
    })
    ResponseEntity<Map<String, Object>> getUserInfo(@RequestHeader("Authorization") String authHeader);

    @Operation(summary = "마이페이지에서 사용자 정보 수정", description = "프로필 이미지, 이름, 성별, 생년월, 학교, 지역, 이메일, 메인 동물을 수정할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공",
                    content = {@Content(
                            schema = @Schema(implementation = UserPageDTO.class)
                    )}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = {@Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )})
    })
    ResponseEntity<Map<String, Object>> updateUserInfo(@RequestHeader("Authorization") String authHeader, @RequestBody UserPageDTO userPageDTO);

    @Operation(summary = "마이페이지에 사용자의 특성과 자기소개 수정", description = "특성, 자기소개를 수정할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공",
                    content = {@Content(
                            schema = @Schema(implementation = UserOtherDTO.class)
                    )}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = {@Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )})
    })
    ResponseEntity<Map<String, Object>> updateUserOtherInfo(@RequestHeader("Authorization") String authHeader, @RequestBody UserOtherDTO userOtherDTO);

}
