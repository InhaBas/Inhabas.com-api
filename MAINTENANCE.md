# IBAS 백엔드 유지보수 매뉴얼

---

## 목차

1. [기술 스택](#1-기술-스택)
2. [모듈 구조](#2-모듈-구조)
3. [개발 환경 설정](#3-개발-환경-설정)
4. [빌드 및 실행](#4-빌드-및-실행)
5. [설정 관리 (Spring Cloud Config)](#5-설정-관리-spring-cloud-config)
6. [배포 구조 (CI/CD)](#6-배포-구조-cicd)
7. [도메인 구조](#7-도메인-구조)
8. [인증 및 권한 체계](#8-인증-및-권한-체계)
9. [코드 스타일](#9-코드-스타일)
10. [테스트](#10-테스트)
11. [주요 GitHub Secrets](#11-주요-github-secrets)
12. [모니터링](#12-모니터링)
13. [자주 겪는 문제와 해결법](#13-자주-겪는-문제와-해결법)

---

## 1. 기술 스택

| 구분 | 기술 |
|------|------|
| Language | Java 17 |
| Framework | Spring Boot 3.5.7 |
| ORM | Spring Data JPA, QueryDSL 5.1.0 |
| Security | Spring Security, OAuth2 Client, JWT (jjwt 0.11.2) |
| Database | MariaDB / MySQL (운영), H2 (테스트) |
| Cloud | AWS S3 (파일 저장), AWS SES (이메일 발송) |
| Config | Spring Cloud Config 2025.0.0 |
| Build | Gradle (Groovy DSL) |
| Container | Docker, Docker Compose |
| CI/CD | GitHub Actions |
| API Docs | Swagger / SpringDoc OpenAPI 2.7.0 |
| Code Format | Spotless (Google Java Format) |

---

## 2. 모듈 구조

```
api/                        ← 루트 프로젝트 (Gradle multi-module)
├── module-auth/            ← OAuth2 인증, JWT, 회원 엔티티, JPA 공통 설정
├── module-fileStorage/     ← AWS S3 파일 스토리지
└── resource-server/        ← 실제 API 엔드포인트 (메인 애플리케이션)
```

### 의존 관계

```
resource-server
    ├── module-auth
    └── module-fileStorage
```

### 각 모듈 역할

**`module-auth`**
- JPA 공통 설정 (`JpaConfig`: Auditing, QueryDSL `JPAQueryFactory`) 및 JPA/DB 의존성 제공
- `Member` 엔티티 및 관련 Value Object (StudentId, Name, Phone, Email 등)
- OAuth2 소셜 로그인 처리 (Google, Naver, Kakao)
- JWT 토큰 발급/검증/갱신
- 역할(Role) 계층 정의 및 권한 처리
- `bootJar`는 비활성화, `jar`만 생성 (라이브러리 모듈)

**`module-fileStorage`**
- AWS S3 파일 업로드/삭제 서비스 (`S3Service`, `S3ServiceImpl`)
- `bootJar`는 비활성화, `jar`만 생성

**`resource-server`**
- 메인 애플리케이션 진입점 (`ApiApplication`)
- 모든 REST API 컨트롤러 (`web/` 패키지)
- 도메인 비즈니스 로직 (`domain/` 패키지)
- 보안 설정 (`WebSecurityConfig`)
- Swagger, Actuator 설정

### `resource-server` 내부 패키지 구조

```
com.inhabas.api/
├── config/             ← WebSecurityConfig, SwaggerConfig, WebConfig 등
├── domain/
│   ├── board/          ← 게시판 공통
│   ├── budget/         ← 회계 내역, 예산지원 신청
│   ├── club/           ← 동아리 활동, 연혁
│   ├── comment/        ← 댓글, 대댓글
│   ├── contest/        ← 공모전 게시판
│   ├── file/           ← 첨부파일
│   ├── lecture/        ← 강의/스터디
│   ├── member/         ← 회원 관리 서비스
│   ├── menu/           ← 메뉴 구조
│   ├── myInfo/         ← 내 정보 / 이름 변경 요청
│   ├── normalBoard/    ← 일반 게시판 (공지, 자유, 안내 등)
│   ├── policy/         ← 동아리 정책 문서
│   ├── project/        ← 프로젝트 게시판
│   ├── questionnaire/  ← 가입 지원서 질문
│   ├── scholarship/    ← 장학회 게시판, 연혁
│   ├── signUp/         ← 회원가입 프로세스
│   └── signUpSchedule/ ← 가입 신청 기간 관리
├── global/
│   ├── dto/            ← 공통 DTO (페이지네이션 등)
│   └── util/           ← 파일 분류, 문자열 유틸
└── web/
    ├── argumentResolver/   ← @Authenticated (현재 로그인 회원 주입)
    ├── converter/          ← Path/Query 파라미터 타입 변환
    ├── interceptor/        ← 회원가입 단계 검증 인터셉터
    └── *Controller.java    ← 각 도메인 컨트롤러
```

각 도메인 패키지는 다음 패턴을 따릅니다.

```
{domain}/
├── domain/        ← 엔티티, Value Object, Enum
├── dto/           ← Request/Response DTO
├── repository/    ← Spring Data JPA Repository, QueryDSL Custom
└── usecase/       ← Service 인터페이스 + Impl
```

---

## 3. 개발 환경 설정

### 사전 요구사항

- JDK 17
- IntelliJ IDEA (권장 IDE)
- Git

### 코드 스타일 설정 (IntelliJ)

1. [intellij-java-google-style.xml](https://github.com/google/styleguide/blob/gh-pages/intellij-java-google-style.xml) 다운로드
2. IntelliJ → Settings → Editor → Code Style → Java → Import Scheme

### 로컬 실행 방법

`application-local.yml`(Cloud Config에서 관리)을 별도로 구성하거나 Cloud Config 서버를 로컬에 띄운 후:

```bash
./gradlew :resource-server:bootRun --args='--spring.profiles.active=local'
```

> **주의**: 실제 설정값은 Spring Cloud Config 서버에서 관리합니다. 팀원에게 Cloud Config 접근 방법을 확인하세요.

---

## 4. 빌드 및 실행

### 빌드

```bash
# 전체 빌드 + 테스트
./gradlew clean build

# 테스트 제외 빌드 (배포 시 사용)
./gradlew build -x test

# 코드 포맷 자동 적용
./gradlew spotlessApply

# 코드 포맷 검사만 (CI에서 사용)
./gradlew spotlessCheck
```

### QueryDSL Q클래스 생성 정리

```bash
# resource-server의 생성된 Q클래스 삭제
./gradlew :resource-server:cleanGeneratedDir
```

### JAR 위치

빌드 후 실행 가능한 JAR:
```
resource-server/build/libs/resource-server-0.0.1-SNAPSHOT.jar
```

---

## 5. 설정 관리 (Spring Cloud Config)

이 프로젝트는 **Spring Cloud Config**를 통해 모든 환경별 설정을 외부에서 관리합니다.

### Config 서버 연결

`resource-server/src/main/resources/bootstrap.yml`이 Config 서버 주소를 결정합니다.

| 프로파일 | Config 서버 주소 |
|---------|----------------|
| `dev1`, `dev2`, `prod1`, `prod2` | `http://172.18.0.6:8888` (Docker 내부 네트워크) |
| `local` | `http://localhost:8888` |

### 실행 프로파일 종류

| 프로파일 | 용도 | 포트 |
|---------|------|------|
| `local` | 로컬 개발 | 8080 |
| `dev1` | 개발 서버 블루 | 8082 |
| `dev2` | 개발 서버 그린 | 8083 |
| `prod1` | 운영 서버 블루 | 8080 |
| `prod2` | 운영 서버 그린 | 8081 |
| `test` | 단위 테스트 (H2) | - |
| `integration_test` | 통합 테스트 (H2) | - |

---

## 6. 배포 구조 (CI/CD)

### GitHub Actions 워크플로우

| 파일 | 트리거 | 역할 |
|------|--------|------|
| `spotless-check.yml` | 모든 push / PR | 코드 포맷 검사 |
| `gradle-build.yml` | PR 오픈/재오픈/동기화 | 빌드 + 전체 테스트 |
| `deploy-dev.yml` | `master` 브랜치 push | 개발 서버 자동 배포 |
| `depoly-production.yml` | `release-*` 브랜치 push | 운영 서버 자동 배포 |

### 배포 흐름

```
PR 작성 → gradle-build + spotless-check (자동) → 리뷰 → master merge
    → deploy-dev 자동 실행
        1. Gradle 빌드 (-x test)
        2. Docker 이미지 빌드 & Docker Hub push (linux/arm64)
        3. docker-compose.yml을 EC2 개발 서버로 SCP 전송
        4. EC2에서 deploy.sh 실행 (블루/그린 전환)

release-* 브랜치 push → deploy-production 자동 실행
    1. Gradle 빌드 (테스트 포함)
    2. Docker 이미지 빌드 & Docker Hub push (linux/arm64)
    3. docker-compose.yml을 EC2 운영 서버로 SCP 전송
    4. EC2에서 deploy.sh 실행 (블루/그린 전환)
```

### Docker Compose 구성

```yaml
services:
  config:       # Spring Cloud Config 서버 (8888)
  prod1:        # 운영 블루 (8080)
  prod2:        # 운영 그린 (8081)
  dev1:         # 개발 블루 (8082)
  dev2:         # 개발 그린 (8083)

networks:
  ibas-network: # external - 서버에 미리 생성되어 있어야 함
    ipv4_address:
      config:  172.18.0.6
      prod1:   172.18.0.2
      prod2:   172.18.0.3
      dev1:    172.18.0.4
      dev2:    172.18.0.5
```

> Docker Hub 이미지: `whtmdgus56/ibas-config:{dev|prod|config}`
> 이미지 아키텍처: `linux/arm64` (EC2 인스턴스가 ARM 기반)

### 운영 서버 릴리즈 절차

1. `master`에서 `release-*` 브랜치 생성
2. 해당 브랜치로 push하면 `depoly-production.yml` 자동 실행
3. 배포 완료 후 브랜치 삭제 또는 유지

---

## 7. 도메인 구조

### 도메인 목록

| 도메인 | Controller | 설명 |
|--------|-----------|------|
| `normalBoard` | `NormalBoardController` | 공지, 자유게시판 등 일반 게시판 |
| `projectBoard` | `ProjectBoardController` | 프로젝트 소개 게시판 |
| `contestBoard` | `ContestBoardController` | 공모전 게시판 |
| `scholarship` | `ScholarshipController` | 장학회 게시판 |
| `scholarshipHistory` | `ScholarshipHistoryController` | 장학회 연혁 |
| `comment` | `CommentController` | 댓글/대댓글 |
| `member` | `MemberController` | 회원 관리 (승인, 역할 변경) |
| `myInfo` | `MyInfoController` | 내 정보 조회/수정, 이름 변경 요청 |
| `budget` | `BudgetHistoryController`, `BudgetApplicationController` | 회계 내역, 예산지원 신청 |
| `lecture` | `LectureController` | 강의/스터디 |
| `signUp` | `SignUpController` | 회원가입 프로세스 |
| `signUpSchedule` | `SignUpScheduleController` | 가입 신청 기간 설정 |
| `club` | `ClubActivityController`, `ClubHistoryController` | 동아리 활동, 연혁 |
| `menu` | `MenuController` | 메뉴 목록 |
| `policy` | `PolicyTermController` | 동아리 정책 문서 |
| `file` | `FileController` | 파일 다운로드/삭제 |

### 공통 패턴

**서비스 계층**: 인터페이스 + Impl 패턴으로 분리되어 있습니다.
```java
// 인터페이스
public interface NormalBoardService { ... }

// 구현체
public class NormalBoardServiceImpl implements NormalBoardService { ... }
```

**현재 로그인 회원 주입**: `@Authenticated` 어노테이션으로 컨트롤러 파라미터에 현재 회원 ID 주입.
```java
@GetMapping("/myInfo")
public ResponseEntity<?> getMyInfo(@Authenticated Long memberId) { ... }
```

**페이지네이션 응답**: `PagedResponseDto<T>` 또는 `PagedPinnedResponseDto<T>` 사용.

**BaseEntity**: 생성/수정 시각 자동 기록 (JPA Auditing).

---

## 8. 인증 및 권한 체계

### OAuth2 소셜 로그인

지원 제공자: **Google, Naver, Kakao**

로그인 흐름:
```
클라이언트 → /oauth2/authorization/{provider}
    → OAuth2 공급자 인증
    → /login/oauth2/code/{provider} (콜백)
    → CustomOAuth2UserService (회원 조회/생성)
    → Oauth2AuthenticationSuccessHandler
    → JWT 토큰 발급 → 프론트엔드 리다이렉트
```

### JWT 토큰

- Access Token / Refresh Token 이중 구조
- 갱신: `POST /token/reissue`
- 설정값(`jwt.secretKey`)은 Cloud Config에서 관리

### 역할(Role) 계층

권한은 수직적 계층 구조입니다. 상위 역할은 하위 역할의 모든 권한을 포함합니다.

```
ADMIN
  └── CHIEF (회장)
        └── VICE_CHIEF (부회장)
              └── EXECUTIVES (회장단)
                    └── SECRETARY (총무)
                          └── BASIC (활동 일반회원)
                                └── DEACTIVATED (비활동/졸업생)
                                      └── NOT_APPROVED (승인 대기)
                                            └── SIGNING_UP (가입 진행 중)
                                                  └── ANONYMOUS (미로그인/익명)
```

| 역할 | 설명    |
|------|-------|
| `ADMIN` | 사이트 관리자 (최고 권한) |
| `CHIEF` | 동아리 회장 |
| `VICE_CHIEF` | 동아리 부회장 |
| `EXECUTIVES` | 운영진   |
| `SECRETARY` | 총무    |
| `BASIC` | 활동 일반회원 |
| `DEACTIVATED` | 비활동회원 |
| `NOT_APPROVED` | 가입했으나 아직 승인 전 |
| `SIGNING_UP` | OAuth2 인증 후 회원가입 진행 중 |
| `ANONYMOUS` | 익명 (미로그인) |

> `ANONYMOUS`는 회원가입을 시도할 수 있는 유일한 권한입니다. 상위 권한으로는 회원가입 불가.

### 주요 엔드포인트 권한 요약

| 엔드포인트 | 최소 권한 |
|-----------|-----------|
| `GET /policy/**`, `/club/histories`, `/signUp/schedule` 등 | 인증 불필요 |
| `/signUp/**` | `SIGNING_UP` |
| `/lecture/**`, `/budget/**` | `DEACTIVATED` |
| `/members/**`, `/member/**` | `SECRETARY` 또는 `EXECUTIVES` |
| `PUT /signUp/schedule`, `PUT /policy/**` | `CHIEF` 또는 `VICE_CHIEF` |
| 그 외 대부분 | `ANONYMOUS` (로그인 필요) |

### 회원 유형(MemberType)

| 값 | 설명 |
|----|------|
| `UNDERGRADUATE` | 학부생 |
| `BACHELOR` | 대학원생 |
| `GRADUATED` | 학사 졸업 |
| `PROFESSOR` | 교수 |
| `OTHER` | 기타 |

---


## 9. 코드 스타일

[Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)를 따릅니다.

```bash
# 포맷 자동 적용 (PR 전 반드시 실행)
./gradlew spotlessApply
```



> CI에서 `spotlessCheck`가 실패하면 PR Merge가 불가능합니다. 반드시 `spotlessApply` 후 push하세요.

---

## 10. 테스트

### 테스트 프로파일

| 어노테이션/프로파일 | 설명 |
|--------------------|------|
| `@NoSecureWebMvcTest` | 시큐리티 제외 MVC 테스트 |
| `@DefaultDataJpaTest` | H2 기반 JPA 테스트 |
| `@CustomSpringBootTest` | 전체 컨텍스트 통합 테스트 |

### 인증 모킹

테스트에서 로그인 회원 시뮬레이션:
- `@WithMockJwtAuthenticationToken`: JWT 기반 인증 모킹
- `@WithMockCustomOAuth2Account`: OAuth2 기반 인증 모킹

### 테스트 데이터베이스

테스트는 H2 인메모리 DB (MySQL 호환 모드)를 사용합니다.
```yaml
datasource:
  url: jdbc:h2:mem:testdb;MODE=MYSQL;NON_KEYWORDS=USER;DATABASE_TO_UPPER=false
```

### 테스트 실행

```bash
# 전체 테스트
./gradlew test

# 특정 모듈만 테스트
./gradlew :resource-server:test
./gradlew :module-auth:test
```

---

## 11. 주요 GitHub Secrets

다음 Secrets가 GitHub 리포지토리에 등록되어야 CI/CD가 정상 작동합니다.

| Secret 키 | 설명 |
|-----------|------|
| `AWS_USER_ACCESS_KEY` | AWS IAM Access Key |
| `AWS_USER_SECRET_KEY` | AWS IAM Secret Key |
| `AWS_SECURITY_GROUP_ID` | EC2 보안 그룹 ID (GitHub Actions IP 임시 허용용) |
| `DOCKERHUB_USERNAME` | Docker Hub 사용자명 |
| `DOCKERHUB_TOKEN` | Docker Hub Access Token |
| `DOCKERHUB_STORAGE` | Docker Hub 이미지 이름 (예: `whtmdgus56/ibas-config`) |
| `IBAS_DEV_HOST` | 개발 서버 EC2 호스트 |
| `IBAS_DEV_USERNAME` | 개발 서버 SSH 사용자 |
| `IBAS_DEV_SSH_KEY` | 개발 서버 SSH 개인키 |
| `IBAS_DEV_PASSWORD` | 개발 서버 SSH 키 패스프레이즈 |
| `IBAS_DEV_DEPLOY_PATH` | 개발 서버 배포 경로 |
| `IBAS_PROD_HOST` | 운영 서버 EC2 호스트 |
| `IBAS_PROD_USERNAME` | 운영 서버 SSH 사용자 |
| `IBAS_PROD_SSH_KEY` | 운영 서버 SSH 개인키 |
| `IBAS_PROD_PASSWORD` | 운영 서버 SSH 키 패스프레이즈 |
| `IBAS_PROD_DEPLOY_PATH` | 운영 서버 배포 경로 |

---

## 12. API 문서

- **Swagger UI**: `/swagger-ui/index.html`

API 컨텍스트 경로: `/api` (예: `https://www.inhabas.com/api/...`)

---

## 13. 자주 겪는 문제와 해결법

### Q. 빌드 시 QueryDSL Q클래스 관련 오류가 발생합니다.

Gradle annotationProcessor로 Q클래스가 생성됩니다. 오류 발생 시:

```bash
./gradlew :resource-server:cleanGeneratedDir
./gradlew clean build
```

IntelliJ에서는 `Build > Rebuild Project`를 실행하세요.




### Q. 새 도메인(기능) 추가 시 어디부터 시작해야 하나요?

1. `resource-server/src/main/java/com/inhabas/api/domain/{domain}/` 패키지 생성
2. 엔티티 (`domain/` 하위) 작성 → H2 DDL 자동 생성 확인 (테스트)
3. Repository 작성 (`repository/`)
4. Service 인터페이스 + Impl 작성 (`usecase/`)
5. Controller 작성 (`web/` 하위)
6. `WebSecurityConfig`에 권한 규칙 추가 (필요 시)
7. 테스트 작성 후 `spotlessApply`


---

## 참고 문서

- [README.md](README.md) — 프로젝트 소개, 아키텍처 다이어그램, ERD
- [CONTRIBUTING.md](CONTRIBUTING.md) — 브랜치/PR 제출 방법
- [COMMITER-INSTRUCTION.md](COMMITER-INSTRUCTION.md) — PR Merge 절차
- [STYLE-CONVENTION.md](STYLE-CONVENTION.md) — 코드 스타일 가이드
- [Notion 개발 문서](https://sparkly-lunge-241.notion.site/IBAS-049505480e5f4bebbb01bfc9b1e9c3c0) — 상세 비즈니스 로직 문서
