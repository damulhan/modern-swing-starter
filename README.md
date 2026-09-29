# Modern Swing Starter (`modern-swing-starter`)
> **Java Swing + JGoodies (Forms & Binding) + FlatLaf**  
> 모던 감각의 룩앤필과 견고한 Presentation Model 패턴으로 완성한 Java 순수 데스크톱 보일러플레이트/스타터 키트

---

## 💡 프로젝트의 의미와 의의

웹 기술(Electron, WebView)이나 최신 모바일/크로스플랫폼 프레임워크(Flutter, Compose Desktop)가 주류가 된 지금, **"왜 굳이 순수 Java Swing인가?"**에 대한 모범적인 답을 제시하는 프로젝트입니다.

1. **가볍고 독립적인 네이티브 앱 (Low Overhead)**
   - Chromium 기반 브라우저를 띄우지 않아 시작 시 메모리 점유율이 50~100MB 안팎으로 매우 가볍습니다.
   - OS 기본 JVM 그래픽 파이프라인(DirectX/OpenGL/GDI)을 직접 활용하여 반응 속도가 즉각적입니다.
2. **20년 검증된 불변의 런타임 안정성**
   - 사내 관리자 툴, 제조/물류 현장 설비 PC, 폐쇄망 ERP 등에서 프레임워크 유행에 휘둘리지 않고 10년 뒤에도 동일하게 동작하는 절대적인 신뢰성을 가집니다.
3. **FlatLaf를 통한 모던 룩앤필 혁신**
   - 흔히 "촌스럽고 구식"이라 평가받던 Swing UI를 IntelliJ IDEA 수준의 깔끔한 모던 디자인(라이트/다크 테마 런타임 전환)으로 재탄생시켰습니다.
4. **Presentation Model 패턴의 정석 적용**
   - 단순 이벤트 리스너 지옥을 벗어나 `JGoodies Binding`을 통해 비즈니스 로직-도메인 모델-UI 컴포넌트 간 양방향 데이터 바인딩을 구조화했습니다.

---

## 🛠️ 기술 스택 (Tech Stack)

| 구분 | 기술 / 라이브러리 | 버전 / 비고 |
| :--- | :--- | :--- |
| **Language** | Java | 21+ (`java.desktop`) |
| **UI Framework** | Pure Java Swing | 표준 API (`JFrame`, `JPanel`, `JTable`, `CardLayout`) |
| **Look & Feel** | [FlatLaf](https://www.formdev.com/flatlaf/) | 3.5.4 (다크/라이트 테마 즉시 전환) |
| **Layout Manager** | [JGoodies Forms](http://www.jgoodies.com/) | 1.9.0 (`FormLayout`, 정밀한 그리드 정렬) |
| **Data Binding** | [JGoodies Binding](http://www.jgoodies.com/) | 2.13.0 (`PresentationModel`, `ValueModel`) |
| **Build Tool** | Gradle | 8.10 (Shadow Fat-JAR 플러그인 포함) & Maven |

---

## 📱 주요 화면 및 기능

* **📊 대시보드 (Dashboard)**
  - 총 고객 수, 활성 고객 수, 총 주문 건수, 누적 매출 등 핵심 KPI 카드 위젯
  - 최근 주문 내역 실시간 요약 테이블
* **👥 고객 관리 (Customers)**
  - **마스터-디테일 패턴**: 고객 목록 검색(`JTable`) 및 선택 시 우측 편집 폼 즉시 동기화
  - **JGoodies 양방향 바인딩**: 이름, 이메일, 전화번호, 활성 상태 변경 시 도메인 모델 실시간 연동
  - 신규 등록, 저장, 삭제 기능 및 상태바 메시지 피드백
* **⚙ 설정 (Settings)**
  - FlatLaf 테마 실시간 토글 (Light Mode / Dark Mode)
  - 애플리케이션 환경 정보(Java 버전, 런타임 등) 표시

---

## 📂 프로젝트 구조

```
swingtest/
 ├── AGENTS.md                   # AI 에이전트 및 기여자를 위한 기술 제약/가이드 문서
 ├── README.md                   # 프로젝트 소개 및 매뉴얼
 ├── prompt.txt                  # 초기 아키텍처 및 구현 제약 명세서
 ├── build.gradle                # Gradle 빌드 스크립트 (Shadow 플러그인)
 ├── run.cmd                     # 원클릭 실행 배치 스크립트
 └── src/main/java/com/example/swingapp/
      ├── Application.java               # 진입점 (FlatLaf 설정 및 MainFrame 실행)
      ├── model/
      │    ├── Customer.java             # PropertyChangeSupport 기반 고객 도메인 모델
      │    └── Order.java                # 주문 엔티티
      ├── service/
      │    └── CustomerService.java      # 비즈니스 로직 및 인메모리 샘플 데이터 저장소
      ├── presentation/
      │    ├── CustomerPresentationModel.java # 고객 데이터 바인딩/검증 PresentationModel
      │    └── DashboardPresentationModel.java# 대시보드 통계 집계 상태 모델
      └── view/
           ├── MainFrame.java            # 사이드바 네비게이션, CardLayout 화면 전환
           ├── DashboardPanel.java       # KPI 메트릭 카드 및 주문 요약 뷰
           ├── CustomerPanel.java        # JTable 마스터 + JGoodies Form 에디터
           └── SettingsPanel.java        # FlatLaf 런타임 다크/라이트 테마 변경 패널
```

---

## 🚀 실행 및 빌드 방법

### 1) 원클릭 실행 (Windows)
루트 경로의 `run.cmd`를 더블클릭하거나 콘솔에서 실행합니다.
```cmd
run.cmd
```

### 2) Gradle 명령어로 실행
```powershell
# 개발 환경에서 즉시 실행
.\gradlew.bat run

# 모든 의존성이 포함된 단독 실행 Fat JAR 생성 (build/libs/ 에 생성)
.\gradlew.bat shadowJar

# 빌드된 JAR 실행
java -jar build\libs\modern-swing-starter-1.0.0.jar
```

---

## 📦 독립 실행형 .exe 패키징 (`jpackage`)

JDK에 내장된 `jpackage`를 사용하면 사용자 PC에 JRE가 설치되어 있지 않아도 실행되는 완전한 독립형 Windows 패키지를 생성할 수 있습니다.

```powershell
$env:JAVA_HOME = "C:\Users\damul\.jdks\jbr-21.0.11"

& "$env:JAVA_HOME\bin\jpackage.exe" `
  --type app-image `
  --name "ModernSwingApp" `
  --input "build\libs" `
  --main-jar "modern-swing-starter-1.0.0.jar" `
  --main-class "com.example.swingapp.Application" `
  --dest "dist" `
  --java-options "-Xmx512m -Dfile.encoding=UTF-8"
```
* 실행 결과물: `dist\ModernSwingApp\ModernSwingApp.exe`
