# How to Contribute

## 기여하는 방법

### 1. 새로운 브렌치 만들기

IBAS Project를 로컬 환경에 설치하면 기본적으로 master / main 이라는 브렌치에서 시작합니다. 이 곳, master / main 브렌치에서 직접 개발하는 것이 아니라 새로운 브렌치를 만들어서 작업합니다.

- 터미널에서 새로운 브렌치를 만듭니다.

    ```jsx
    git checkout -b branchName
    ```

### 2. 새로운 브렌치에서 파일 수정하기

이제 새로운 브렌치에서 프로젝트를 수정합니다.

수정이 끝나면 다음의 과정을 수행하세요:

- git add .

    ```jsx
    git add .
    ```

  (수정사항들 git에 추가하는 명령어)

- git commit -m"커밋메세지"

    ```jsx
    git commit -m"커밋메세지"
    ```

   (git에 추가된 수정사항들을 묶어서 “커밋메세지”라는 이름으로 저장)

    > IBAS Project의 Commit convention은 다음과 같습니다.
    >
    >```jsx
    >[feature/#67] 대댓글 UI 추가
    >```
    >

- git push

    ```jsx
    git push origin branchName
    ```

    (“커밋메세지”라는 이름으로 저장된 수정사항들을 원격 저장소에 보내기)

### 3.PR올리기

수정후 push가 완료되면 IBAS Project로 들어갑니다(아래 링크 참고).

백엔드: [GitHub - InhaBas/Inhabas.com-api](https://github.com/InhaBas/Inhabas.com-api)

프론트엔드: [GitHub - InhaBas/Inhabas.com-front](https://github.com/InhaBas/inhabas.com-front)

git push가 제대로 이루어졌다면 IBAS  백엔드,프론트엔드 깃허브로 갔을 때 PR을 생성하라는 알림이 새로 생깁니다.

### 4.PR이 Merge되면 브렌치 삭제하기

PR이 성공적으로 오픈소스의 master 브렌치에 Merge되면 PR용으로 작업했던 브렌치를 삭제해야 합니다.

로컬환경에서 main 혹은 master 브렌치로 이동 후 오픈소스의 터미널에서

1. `git branch -D branchName` (로컬 브렌치 삭제)

2. `git push origin --delete branchName` (깃허브 브렌치 삭제)

입력해서 깔끔하게 브렌치를 삭제해야 합니다.

---

## 프로젝트 원본이랑 동기화 시키기- git pull

### 방법1

개발 작업을 하면서 프로젝트 원본이랑 충돌이 일어날 수 있기 때문에 프로젝트 원본에 수정사항이 생기면 주기적으로

```jsx
git pull upstream master
```

을 입력해서 프로젝트 원본이랑 동기화 시킵니다.

### 방법2(추천)

1. 깃허브에서 작업하는 브렌치로 이동

2. 프로젝트 위치의 터미널에서 `git pull origin 작업하는 브렌치이름` 입력

### 주의해야할 점!(다른 브렌치로 이동하는경우)

1. 한 브렌치에서 다른 브렌치로 이동하기 전에는 무조건 git commit을 해야 합니다.
2. 다른 브렌치로 이동하면 오른쪽 아래 생기는 Reload 버튼을 꼭 눌러줘야 합니다.(intelliJ 경우)

## PR 올릴 때 주의사항

PR을 제출할 때는 다음 가이드라인을 따라야 합니다:

- IBAS Style Convention을 참고하세요.
[STYLE-CONVENTION.md](STYLE-CONVENTION.md)
- 작성한 코드가 기존 코드 스타일을 준수하고 있는지 확인해야 합니다.

Intellij를 사용하는 경우 아래 포멧팅을 참고하세요:

- xml: [`ibas_intellij_formatting.xml`](
  /docs/back-end/ibas_intellij_formatting.xml).

Eclipse / VSCOde를 사용하는 경우 아래 포멧팅을 참고하세요:

- xml: [`eclipse_formatting.xml`](
  /docs/back-end/ibas_eclipse_formatting.xml).
