package com.inhabas.api.domain.club.repository;

import static com.inhabas.api.domain.board.domain.QAlbumBoard.albumBoard;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.inhabas.api.domain.club.dto.ClubActivityDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class ClubActivityRepositoryImpl implements ClubActivityRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<ClubActivityDto> findAllAndSearch(String search) {
    return queryFactory
        .select(
            Projections.constructor(
                ClubActivityDto.class,
                albumBoard.id,
                albumBoard.title.value,
                albumBoard.writer.id,
                albumBoard.writer.name.value,
                albumBoard.dateCreated,
                albumBoard.dateUpdated))
        .from(albumBoard)
        .where(likeTitle(search).or(likeContent(search)).or(likeWriterName(search)))
        .orderBy(albumBoard.dateCreated.desc())
        .fetch();
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
