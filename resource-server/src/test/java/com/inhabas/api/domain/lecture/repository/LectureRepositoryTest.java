package com.inhabas.api.domain.lecture.repository;

import static com.inhabas.api.domain.member.domain.entity.MemberTest.basicMember1;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.testAnnotataion.DefaultDataJpaTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DefaultDataJpaTest
public class LectureRepositoryTest {

  @Autowired private TestEntityManager em;

  @Autowired private LectureRepository repository;

  private Member chief;

  @BeforeEach
  public void setUp() {
    chief = em.persist(basicMember1());
  }

  //    @DisplayName("강의글 단일 조회")
  //    @Test
  //    public void getTest() {
  //
  //        //given
  //        Lecture entity = Lecture.builder()
  //                .title("절권도 배우기")
  //                .chief(chief.getId())
  //                .applyDeadline(LocalDateTime.of(9011, 1, 1, 1, 1, 1))
  //                .curriculumDetails("1주차: 빅데이터에 기반한 공격패턴분석<br> 2주차: ...")
  //                .daysOfWeek("월 금")
  //                .introduction("호신술을 배워보자")
  //                .method(1)
  //                .participantsLimits(30)
  //                .place("6호관 옥상")
  //                .build();
  //        entity = repository.save(entity);
  //
  //        //when
  //        Integer id = (Integer) ReflectionTestUtils.getField(entity, "id");
  //        LectureDetailDto detailDto = repository.getDetails(id)
  //                .orElse(null);
  //
  //        //then
  //        assertThat(detailDto).isNotNull();
  //        assertThat(detailDto.getChief().getId()).isEqualTo(12171234);
  //        assertThat(detailDto.getChief().getMajor()).isEqualTo("건축공학과");
  //        assertThat(detailDto.getChief().getName()).isEqualTo("유동현");
  //    }

  //    @DisplayName("강의글 목록 조회")
  //    @Test
  //    public void getListTest() {
  //
  //        ArrayList<Lecture> lectures = new ArrayList<>();
  //
  //        for (int i = 0; i < 4; i++) {
  //            Lecture lecture = Lecture.builder()
  //                    .title("절권도 배우기" + i)
  //                    .chief(chief.getId())
  //                    .applyDeadline(LocalDateTime.of(9011, 1, 1, 1, 1, 1))
  //                    .curriculumDetails("1주차: 빅데이터에 기반한 공격패턴분석<br> 2주차: ...")
  //                    .daysOfWeek("월 금")
  //                    .introduction("호신술을 배워보자")
  //                    .method(1)
  //                    .participantsLimits(30)
  //                    .place("6호관 옥상")
  //                    .build();
  //            ReflectionTestUtils.setField(lecture, "status", LectureStatus.values()[i]);
  //            lectures.add(lecture);
  //        }
  //        repository.saveAll(lectures);
  //
  //        //when
  //        Page<LectureListDto> page = repository.getList(PageRequest.of(0, 6));
  //
  //        //then
  //        assertThat(page.getTotalElements()).isEqualTo(2);
  //        assertThat(page.getNumberOfElements()).isEqualTo(2);
  //        assertThat(page.getTotalPages()).isEqualTo(1);
  //        assertThat(page.getContent().get(0)).extracting("title").isEqualTo("절권도 배우기3");
  //    }
}
