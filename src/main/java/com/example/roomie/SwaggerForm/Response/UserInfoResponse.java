package com.example.roomie.SwaggerForm.Response;

import com.example.roomie.DTO.UserPageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserInfoResponse {
    @Schema(description = "성공 여부", example = "true")
    private String success;
    @Schema(description = "사용자의 User 테이블 데이터")
    private UserPageDTO userPageDTO;
    @Schema(description = "사용자의 특성 리스트")
    private List<String> list; // 특성 리스트
    @Schema(description = "사용자의 자기소개")
    private String self; // 자기소개
}
