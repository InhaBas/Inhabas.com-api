package com.inhabas.api.domain.club.usecase;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.board.domain.AlbumBoard;
import com.inhabas.api.domain.board.usecase.BoardSecurityChecker;
import com.inhabas.api.domain.club.dto.ClubActivityDetailDto;
import com.inhabas.api.domain.club.dto.ClubActivityDto;
import com.inhabas.api.domain.club.dto.SaveClubActivityDto;
import com.inhabas.api.domain.club.repository.ClubActivityRepository;
import com.inhabas.api.domain.file.domain.BoardFile;
import com.inhabas.api.domain.file.dto.FileDownloadDto;
import com.inhabas.api.domain.file.usecase.S3Service;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubActivityServiceImpl implements ClubActivityService {

    private final ClubActivityRepository clubActivityRepository;

    private final BoardSecurityChecker boardSecurityChecker;

    private final MemberRepository memberRepository;

    private final MenuRepository menuRepository;

    private final S3Service s3Service;

    private static final String CLUB_ACTIVITY_MENU_NAME = "동아리 활동";

    private static final String DIR_NAME = "clubActivity/";

    private static final Integer CLUB_ACTIVITY_MENU_ID = 2;

    @Override
    @Transactional(readOnly = true)
    public List<ClubActivityDto> getClubActivities() {

        List<AlbumBoard> clubActivityList = clubActivityRepository.findAll();

        return clubActivityList.stream().map(obj -> {
            String fileName = obj.getFiles().isEmpty() ? null : obj.getFiles().get(0).getName();
            String fileUrl = obj.getFiles().isEmpty() ? null : obj.getFiles().get(0).getUrl();
            return ClubActivityDto.builder()
                    .id(obj.getId())
                    .title(obj.getTitle())
                    .writerName(obj.getWriter().getName())
                    .dateCreated(obj.getDateCreated())
                    .dateUpdated(obj.getDateUpdated())
                    .thumbnail(new FileDownloadDto(fileName, fileUrl))
                    .build();
        }).collect(Collectors.toList());

    }

    @Override
    @Transactional
    public Long writeClubActivity(Long memberId, SaveClubActivityDto saveClubActivityDto) {

        boardSecurityChecker.checkMenuAccess(CLUB_ACTIVITY_MENU_ID, BoardSecurityChecker.CREATE_BOARD);
        Member writer = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        Menu menu = menuRepository.findByName_Value(CLUB_ACTIVITY_MENU_NAME);

        AlbumBoard board = AlbumBoard.builder()
                .menu(menu)
                .title(saveClubActivityDto.getTitle())
                .content(saveClubActivityDto.getContent())
                .build()
                .writtenBy(writer, AlbumBoard.class);

        saveClubActivityDto.getFiles().forEach(file -> {
            String url = s3Service.uploadS3File(file, generateRandomUrl());
            BoardFile boardFile = BoardFile.builder().name(file.getOriginalFilename()).url(url).board(board).build();
            board.addFile(boardFile);
        });

        return clubActivityRepository.save(board).getId();

    }

    @Override
    @Transactional(readOnly = true)
    public ClubActivityDetailDto getClubActivity(Long boardId) {

        boardSecurityChecker.checkMenuAccess(CLUB_ACTIVITY_MENU_ID, BoardSecurityChecker.READ_BOARD);
        AlbumBoard clubActivity = clubActivityRepository.findById(boardId).orElseThrow(NotFoundException::new);
        List<FileDownloadDto> fileDownloadDtoList = null;
        if (!clubActivity.getFiles().isEmpty()) {
            fileDownloadDtoList = clubActivity.getFiles().stream().map(obj ->
                FileDownloadDto.builder()
                        .name(obj.getName())
                        .url(obj.getUrl())
                        .build()
            ).collect(Collectors.toList());
        }

        return ClubActivityDetailDto.builder()
                .id(clubActivity.getId())
                .title(clubActivity.getTitle())
                .content(clubActivity.getContent())
                .writerName(clubActivity.getWriter().getName())
                .dateCreated(clubActivity.getDateCreated())
                .dateUpdated(clubActivity.getDateUpdated())
                .files(fileDownloadDtoList)
                .build();

    }

    @Override
    @Transactional
    public void updateClubActivity(Long boardId, SaveClubActivityDto saveClubActivityDto) {

        AlbumBoard clubActivity = clubActivityRepository.findById(boardId).orElseThrow(NotFoundException::new);
        List<BoardFile> updateFiles = new ArrayList<>();

        if (saveClubActivityDto.getFiles() != null) {
            try {
                clubActivity.updateText(saveClubActivityDto.getTitle(), saveClubActivityDto.getContent());

                updateFiles = saveClubActivityDto.getFiles().stream().map(file -> {
                    String url = s3Service.uploadS3File(file, generateRandomUrl());
                    return BoardFile.builder()
                            .name(file.getOriginalFilename())
                            .url(url)
                            .board(clubActivity)
                            .build();
                }).collect(Collectors.toList());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        clubActivity.updateFiles(updateFiles);

    }

    @Override
    @Transactional
    public void deleteClubActivity(Long boardId) {

        clubActivityRepository.deleteById(boardId);

    }

    private String generateRandomUrl() {
        return DIR_NAME + UUID.randomUUID();
    }

}
