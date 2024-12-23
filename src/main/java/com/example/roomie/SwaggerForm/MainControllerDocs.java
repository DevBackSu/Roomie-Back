package com.example.roomie.SwaggerForm;

import com.example.roomie.DTO.RankDTO;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

@Tag(name = "Main", description = "메인 화면 관련 API가 들어올 예정")
public interface MainControllerDocs {

//    @Operation(summary = "사용자 데이터 반환", description = "User 데이터 반환")
//    @Parameter(name = "", description = "파라미터 없음")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "반환 성공"),
//            @ApiResponse(responseCode = "400", description = "반환 실패")
//    })
//    UserDTO getUser();
//
//    // 친구 관계의 사용자 3명 정도를 반환
//    public Map<String, UserDTO> getOther();

    // 메인 화면 중 종달새/올빼미 통계값 반환
    public Map<String, Integer> getStatistics();

    // 메인 화면 중 특징 순위 반환
    public Map<String, RankDTO> getCrank();

    // 메인 화면 중 지역 순위 반환
    public Map<String, RankDTO> getLrank();

}
