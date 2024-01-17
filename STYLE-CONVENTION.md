
# IBAS Project Style Convention

이 문서는 IBAS Project의 Style Convention에 대해 설명하는 문서입니다. IBAS Project는 코드 품질과 가독성을 유지하기 위해 일관된 스타일 규약을 따릅니다. 이러한 스타일 규약을 통해 프로젝트 참여자들이 명확하고 일관된 방식으로 코드를 작성할 수 있으며, 협업과 코드 유지 관리를 용이하게 할 수 있습니다.

## IBAS Style Convention 논의사항

IBAS Project의 주요한 Style Convention은 깃허브 Issue를 통해 논의되고 정해집니다. 프로젝트의 모든 멤버가 스타일 결정에 참여하고 의견을 제시할 수 있습니다.

- [import 시 * 와일드카드 사용여부 논의](https://github.com/InhaBas/Inhabas.com-api/issues/187)
- [PR Merge 방식에 대한 논의](https://github.com/InhaBas/Inhabas.com-api/issues/188)
- [JUnit / AssertJ 채택 논의](https://github.com/InhaBas/Inhabas.com-api/issues/165)

## Documentation style

IBAS Project의 기본 스타일은 [Google Developer Documentation Style Guide](https://developers.google.com/style)를 따르고 있습니다.

## Google Developer Document Style 적용

### Backend

#### 포멧 적용

1. 구글 스타일 깃허브에서 스타일 다운로드

    - IntelliJ: [intellij-java-google-style.xml](https://github.com/google/styleguide/blob/gh-pages/intellij-java-google-style.xml)

    - Eclipse: [eclipse-java-google-style.xml](https://github.com/google/styleguide/blob/gh-pages/eclipse-java-google-style.xml)

2. IDE에서 스타일 포멧 적용

#### 코드 스타일 교정

구글 코드 스타일에 맞게 코드 스타일 변경하는 명령어.

프로젝트 경로 터미널에서 아래 명령어 입력.

- 리눅스: `./gradlew spotlessApply`
- 윈도우: `.\gradlew spotlessApply`

### Frontend

prittier 이용해서 구글 스타일 컨벤션 적용.
