package com.inhabas.api.domain.club.repository;

import static com.inhabas.api.domain.board.domain.QAlbumBoard.albumBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import com.inhabas.api.domain.board.domain.AlbumBoard;
import com.inhabas.api.domain.club.dto.ClubActivityDto;
import com.inhabas.api.global.util.ClassifiedFiles;
import com.inhabas.api.global.util.ClassifyFiles;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class ClubActivityRepositoryImpl implements ClubActivityRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<ClubActivityDto> findAllAndSearch(String search) {

    // 필터 조건 설정
    BooleanExpression target = likeTitle(search).or(likeContent(search)).or(likeWriterName(search));

    // 정렬 조건 설정
    OrderSpecifier<?> order = albumBoard.dateCreated.desc();

    List<AlbumBoard> boards =
        queryFactory.selectFrom(albumBoard).where(target).orderBy(order).fetch();

    return boards.stream()
        .map(
            board -> {
              ClassifiedFiles classifiedFiles =
                  ClassifyFiles.classifyFiles(new ArrayList<>(board.getFiles()));
              return ClubActivityDto.builder()
                  .id(board.getId())
                  .title(board.getTitle())
                  .writerId(board.getWriter().getId())
                  .writerName(board.getWriter().getName())
                  .dateCreated(board.getDateCreated())
                  .dateUpdated(board.getDateUpdated())
                  .thumbnail(classifiedFiles.getThumbnail()) // 첫 번째 이미지 파일을 썸네일로 선택
                  .build();
            })
        .collect(Collectors.toList());
  }

  private BooleanExpression likeTitle(String search) {
    return albumBoard.title.value.like("%" + search + "%");
  }

  private BooleanExpression likeContent(String search) {
    return albumBoard.content.value.like("%" + search + "%");
  }

  private BooleanExpression likeWriterName(String search) {
    return albumBoard.writer.name.value.like("%" + search + "%");
  }
}
