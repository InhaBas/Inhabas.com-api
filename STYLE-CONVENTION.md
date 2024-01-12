
# IBAS Project Style Convention

이 문서는 IBAS Project의 Style Convention에 대해 설명하는 문서입니다. IBAS Project는 코드 품질과 가독성을 유지하기 위해 일관된 스타일 규약을 따릅니다. 이러한 스타일 규약을 정함으로써 프로젝트 참여자들이 명확하고 일관된 방식으로 코드를 작성할 수 있으며, 협업과 코드 유지 관리를 용이하게 할 수 있습니다.

## IBAS 코드 스타일 포멧팅 사용하기

### IntelliJ 전용 IBAS JAVA 코드 스타일 포멧

- xml: [`ibas_intellij_formatting.xml`](
  /docs/back-end/ibas_intellij_formatting.xml).

### Eclipse 전용 IBAS  JAVA 코드 스타일 포멧

- xml: [`ibas_eclipse_formatting.xml`](
  /docs/back-end/ibas_eclipse_formatting.xml).

### IntelliJ에서 사용하기

IntelliJ IDEA에서 IBAS JAVA 코드 스타일 포맷팅을 사용하려면 다음과 같은 단계를 따르면 됩니다:

1. IntelliJ IDEA를 열고 프로젝트를 로드합니다.
2. File > Settings > Editor > Code Style로 이동합니다.
3. 오른쪽 상단의 설정 톱니바퀴 아이콘을 클릭한 다음, Import Scheme > IntelliJ IDEA code style XML을 선택합니다.
4. IBAS_intellij_formatting.xml 파일을 찾아서 선택하고, 'OK'를 클릭합니다.

### Eclipse에서 사용하기

Eclipse에서 IBAS JAVA 코드 스타일 포맷팅을 사용하려면 다음과 같은 단계를 따르면 됩니다:

1. Eclipse를 열고 프로젝트를 로드합니다.
2. Window > Preferences > Java > Code Style > Formatter로 이동합니다.
3. 'Import' 버튼을 클릭합니다.
4. 다운로드한 eclipse_formatting.xml 파일을 찾아서 선택하고, 'Open'을 클릭합니다.
5. 새로운 포맷팅 프로필이 리스트에 나타납니다. 이 프로필을 선택하고 'OK' 또는 'Apply and Close'를 클릭합니다.

### VSCode에서 사용하기

VSCode에서 IBAS JAVA 코드 스타일 포맷팅을 사용하려면 다음과 같은 단계를 따르면 됩니다:

1. VSCode를 열고 Java 프로젝트를 로드합니다.
2. Java Extension Pack이 설치되어 있지 않다면, VSCode 마켓플레이스에서 설치합니다.
3. 프로젝트의 루트 디렉토리에 .settings 폴더를 생성합니다 (이미 존재한다면 생략).
4. .settings 폴더 내에 org.eclipse.jdt.core.prefs 파일을 생성합니다.
5. eclipse_formatting.xml 파일의 내용을 org.eclipse.jdt.core.prefs 파일에 복사하거나, 이 파일을 직접 .settings 폴더에 배치합니다.
6. VSCode 설정에서 Java 포맷팅을 Eclipse 설정으로 지정합니다. settings.json 파일에 다음과 같은 설정을 추가합니다:

```json
"java.format.settings.url": "file:///<path_to_your_project>/.settings/org.eclipse.jdt.core.prefs",
"java.format.settings.profile": "IBAS Java Backend Style"
```

위 경로에서 <path_to_your_project>는 실제 프로젝트의 경로로 대체해야 합니다.

### IBAS Style Convention 논의사항

IBAS Project의 주요한 Style Convention은 깃허브 Issue를 통해 논의되고 정해집니다. 프로젝트의 모든 멤버가 스타일 결정에 참여하고 의견을 제시할 수 있습니다.

- [import 시 * 와일드카드 사용여부 논의](https://github.com/InhaBas/Inhabas.com-api/issues/187)
- [PR Merge 방식에 대한 논의](https://github.com/InhaBas/Inhabas.com-api/issues/188)
- [JUnit / AssertJ 채택 논의](https://github.com/InhaBas/Inhabas.com-api/issues/165)

### Documentation style

IBAS Project의 기본 스타일은 [Google Developer Documentation Style Guide](https://developers.google.com/style)를 따르고 있습니다.
