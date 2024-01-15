package com.inhabas.api.web;

import com.inhabas.api.auth.domain.error.ErrorResponse;
import com.inhabas.api.domain.club.dto.ClubActivityDetailDto;
import com.inhabas.api.domain.club.dto.ClubActivityDto;
import com.inhabas.api.domain.club.dto.SaveClubActivityDto;
import com.inhabas.api.domain.club.usecase.ClubActivityService;
import com.inhabas.api.global.dto.PageInfoDto;
import com.inhabas.api.global.dto.PagedMemberResponseDto;
import com.inhabas.api.global.util.PageUtil;
import com.inhabas.api.web.argumentResolver.Authenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "동아리 활동",
        description = "동아리 활동 관련 API")
@RestController
@RequiredArgsConstructor
public class ClubActivityController {

    private final ClubActivityService clubActivityService;


    @Operation(summary = "동아리 활동 목록 조회",
            description = "동아리 활동 목록 조회 (썸네일은 첫 사진 첨부파일)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(
                    schema = @Schema(implementation = PagedMemberResponseDto.class))}),
    })
    @SecurityRequirements(value = {})
    @GetMapping("/club/activities")
    public ResponseEntity<PagedMemberResponseDto<ClubActivityDto>> getClubActivities(
            @Parameter(description = "페이지", example = "0") @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "페이지당 개수", example = "10") @RequestParam(name = "size", defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);
        List<ClubActivityDto> allDtos = clubActivityService.getClubActivities();
        List<ClubActivityDto> pagedDtos = PageUtil.getPagedDtoList(pageable, allDtos);

        PageImpl<ClubActivityDto> ClubActivityDtoPage =
                new PageImpl<>(pagedDtos, pageable, allDtos.size());
        PageInfoDto pageInfoDto = new PageInfoDto(ClubActivityDtoPage);

        return ResponseEntity.ok(new PagedMemberResponseDto<>(pageInfoDto, pagedDtos));

    }

    @Operation(summary = "동아리 활동 글 생성",
            description = "동아리 활동 글 생성 (운영진 이상)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "'Location' 헤더에 생성된 리소스의 URI 가 포함됩니다."),
            @ApiResponse(responseCode = "400 ", description = "입력값이 없거나, 타입이 유효하지 않습니다.", content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 400, \"code\": \"G003\", \"message\": \"입력값이 없거나, 타입이 유효하지 않습니다.\"}"
                    )
            ))
    })
    @PostMapping(path = "/club/activity", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> writeClubActivity(@Authenticated Long memberId,
                                                             @RequestPart("title") String title,
                                                             @RequestPart("content") String content,
                                                             @RequestPart("files") List<MultipartFile> files) {

        SaveClubActivityDto saveClubActivityDto = new SaveClubActivityDto(title, content, files);

        Long newClubActivityId = clubActivityService.writeClubActivity(memberId, saveClubActivityDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/club/activity/{boardId}")
                .buildAndExpand(newClubActivityId)
                .toUri();

        return ResponseEntity.created(location).build();

    }

    @Operation(summary = "동아리 활동 글 단일 조회",
            description = "동아리 활동 글 단일 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400 ", description = "입력값이 없거나, 타입이 유효하지 않습니다.", content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 400, \"code\": \"G003\", \"message\": \"입력값이 없거나, 타입이 유효하지 않습니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "404", description = "데이터가 존재하지 않습니다.", content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"code\": \"G004\", \"message\": \"데이터가 존재하지 않습니다.\"}"
                    )
            ))
    })
    @SecurityRequirements(value = {})
    @GetMapping("/club/activity/{boardId}")
    public ResponseEntity<ClubActivityDetailDto> writeClubActivity(@PathVariable Long boardId) {

        ClubActivityDetailDto clubActivityDetailDto = clubActivityService.getClubActivity(boardId);

        return ResponseEntity.ok(clubActivityDetailDto);

    }

    @Operation(summary = "동아리 활동 글 수정",
            description = "동아리 활동 글 수정 (작성자, 회장만)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400 ", description = "입력값이 없거나, 타입이 유효하지 않습니다.", content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 400, \"code\": \"G003\", \"message\": \"입력값이 없거나, 타입이 유효하지 않습니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "404", description = "데이터가 존재하지 않습니다.", content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"code\": \"G004\", \"message\": \"데이터가 존재하지 않습니다.\"}"
                    )
            ))
    })
    @PutMapping(path = "/club/activity/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("@boardSecurityChecker.writerOnly(#boardId) or hasRole('VICE_CHIEF')")
    public ResponseEntity<ClubActivityDto> updateClubActivity(@Authenticated Long memberId,
                                                             @PathVariable Long boardId,
                                                             @RequestPart("title") String title,
                                                             @RequestPart("content") String content,
                                                             @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        SaveClubActivityDto saveClubActivityDto = new SaveClubActivityDto(title, content, files);
        clubActivityService.updateClubActivity(boardId, saveClubActivityDto);

        return ResponseEntity.noContent().build();

    }

    @Operation(summary = "동아리 활동 글 삭제",
            description = "동아리 활동 글 삭제 (작성자, 회장만)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400 ", description = "입력값이 없거나, 타입이 유효하지 않습니다.", content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 400, \"code\": \"G003\", \"message\": \"입력값이 없거나, 타입이 유효하지 않습니다.\"}"
                    )
            )),
            @ApiResponse(responseCode = "404", description = "데이터가 존재하지 않습니다.", content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"status\": 404, \"code\": \"G004\", \"message\": \"데이터가 존재하지 않습니다.\"}"
                    )
            ))
    })
    @DeleteMapping("/club/activity/{boardId}")
    @PreAuthorize("@boardSecurityChecker.writerOnly(#boardId) or hasRole('VICE_CHIEF')")
    public ResponseEntity<ClubActivityDto> deleteClubActivity(@Authenticated Long memberId,
                                                              @PathVariable Long boardId) {

        clubActivityService.deleteClubActivity(boardId);

        return ResponseEntity.noContent().build();

    }

}
