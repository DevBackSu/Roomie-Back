package com.example.roomie.SwaggerForm;

import com.example.roomie.DTO.RankDTO;
import com.example.roomie.SwaggerForm.Response.UserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Tag(name = "Main", description = "메인 화면 관련 API가 들어올 예정")
public interface MainControllerDocs {

    /**
     * @Operation : API가 어떤 역할을 하는지 명시함 (summary : 역할 / description : 설명)
     * @RequestBody : PUT / POST 메소드 api의 request body를 설명함
     * @ApiResponse : 보통 GET 메소드 api의 response가 어떻게 오는 설명함
     */

    @Operation(summary = "메인 화면 종달새/올빼미 통계값 반환", description = "메인 화면에서 종달새/올빼미 통계 데이터를 반환합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "반환 성공",
            content = {@Content(
                schema = @Schema(implementation = UserInfoResponse.class),
                    examples = {@ExampleObject(name="통계값 반환",
                        value = """
                            {
                                "1" : "퍼센트값",
                                "2" : "퍼센트값"
                            }
                            """, description = "1 : 종달새 / 2 : 부엉이")}
            )}
        ),
        @ApiResponse(responseCode = "422", description = "UNPROCESSABLE_ENTITY",
            content = {@Content(
                schema = @Schema(implementation = UserInfoResponse.class),
                    examples = {@ExampleObject(name = "DB 오류",
                        value = """
                            {
                                "1" : "0",
                                "2" : "0"
                            }
                            """, description = "DB 초기화로 값이 제대로 반환되지 않음")}
            )}
        ),
        @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR",
            content = {@Content(
                examples = {@ExampleObject(name = "Exception 오류",
                    value = """
                        {
                            "error" : "error message"
                        }
                        """, description = "전체 개수가 0일 경우 반환됨")}
            )}
        )
    })
    ResponseEntity<Map<String, Object>> getStatistics();

    // 메인 화면 중 특징 순위 반환
    @Operation(summary = "메인 화면 특징 순위 반환", description = "메인 화면에 특징 순위값을 반환합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "반환 성공",
            content = {@Content(
                examples = {@ExampleObject(name = "특징값 반환",
                value = """
                        "rank" : ["첫번째 특징", "두번째 특징", ...]
                        """, description = "DB에서 조회한 최대 5개의 특징 순위를 반환함")}
            )}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR",
                content = {@Content(
                    examples = {@ExampleObject(name = "Exception 오류",
                        value = """
                        {
                            "error" : "error message"
                        }
                        """)}
                )}
            )
    })
    public ResponseEntity<Map<String, Object>> getCrank();

    @Operation(summary = "메인 화면 지역 순위 반환", description = "메인 화면에 지역 순위값을 반환합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "반환 성공",
            content = {@Content(
                examples = {@ExampleObject(name = "지역값 반환",
                value = """
                        "rank" : ["첫번째 지역", "두번째 지역", ...]
                        """, description = "DB에서 조회한 최대 5개의 지역 순위를 반환함")}
            )}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR",
                content = {@Content(
                    examples = {@ExampleObject(name = "Exception 오류",
                       value = """
                        {
                            "error" : "error message"
                        }
                        """)}
                )}
            )
    })
    public ResponseEntity<Map<String, Object>> getLrank();
}
