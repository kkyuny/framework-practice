## GitHub 레포지토리 병합 가이드
패스트캠퍼스 나만의 MVC 프레임워크 만들기 강의를 듣고 여러개의 레포지토리가 생성되어 여러 개의 GitHub 레포지토리를 하나의 레포로 병합하는 작업을 진행하였고 진행한 방법을 소개합니다.

### 1. GitHub에서 레포지토리 생성
- 이동할 레포지토리를 우선 생성한다. ex) frame-practice

### 2. 생성한 메인 레포 클론
``` bash
git clone https://github.com/kkyuny/framework-practice.git
cd framework-practice
```

### 3. 초기 커밋 (subtree 쓰려면 커밋 1개는 필요함)
``` bash
echo "# framework-practice" > README.md
git add README.md
git commit -m "Initial commit"
```

### 4. 병합할 레포 추가 (예: mvc-practice)
``` bash
git remote add mvc-practice https://github.com/kkyuny/mvc-practice.git
git fetch mvc-practice
```
- 진입한 framework-practice 디렉토리에서 추가할 레포를 입력한다.
  
### 5. mvc-practice를 mvc-practice/ 폴더로 병합 (히스토리 압축)
``` bash
git subtree add --prefix=mvc-practice mvc-practice main --squash
```
- `git subtree add` 명령은 자동으로 커밋을 생성해서 따로 git commit을 할 필요가 없다.
- --squash를 빼면 커밋 히스토리 전부 유지됨
- `commit` VS `subtree add`

| 명령어            | 설명                                                  |
|-------------------|-------------------------------------------------------|
| `git commit`      | 현재 로컬 작업 디렉토리의 변경사항을 커밋                          |
| `git subtree add` | **다른 레포지토리의 특정 브랜치를 하위 디렉토리로 병합 + 자동 커밋** |


### 6. 원격 저장소 푸시
``` bash
git push origin main
```
