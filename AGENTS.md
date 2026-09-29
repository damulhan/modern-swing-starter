# AGENTS.md

## 프로젝트 개요
이 프로젝트는 **Java Swing + JGoodies (Forms & Binding) + FlatLaf**를 조합하여 현대적인 룩앤필과 견고한 아키텍처(Presentation Model 패턴)를 갖춘 데스크톱 GUI 애플리케이션의 모범 사례를 구현한 레퍼런스 샘플입니다.

웹이나 최신 선언형 UI 프레임워크(Compose, Electron, Flutter 등)를 일체 사용하지 않고, 오직 표준 Java 데스크톱 기술 스택과 검증된 라이브러리만으로 완성도 높은 비즈니스 앱을 구축하는 실전 예제입니다.

---

## 핵심 기술 제약 및 규칙 (Strict Constraints)
AI 에이전트 및 기여자는 본 프로젝트를 유지보수하거나 기능을 추가할 때 다음 제약을 엄격히 준수해야 합니다.

1. **허용된 핵심 기술 스택**
   - **Java** (`java.desktop` 모듈 기반 Swing)
   - **FlatLaf**: 모던 룩앤필(Light/Dark 런타임 테마 전환 및 컴포넌트 스타일링)
   - **JGoodies Forms**: `FormLayout`, `DefaultFormBuilder`를 통한 정밀한 그리드 레이아웃
   - **JGoodies Binding**: `PresentationModel`, `ValueModel`, `BasicComponentFactory`를 통한 엔티티-UI 간 양방향 바인딩
   - **빌드 도구**: Gradle (Shadow 플러그인 포함) 및 Maven

2. **사용 금지 기술 (Forbidden Technologies)**
   - JavaFX, SWT, Compose Desktop
   - Kotlin, Scala 등 JVM 대체 언어 (순수 Java만 사용)
   - Spring, Spring Boot 등의 무거운 엔터프라이즈 프레임워크
   - Electron, CEF, WebView, Tauri 등 웹 기반 런타임
   - Lombok (POJO/JavaBeans 스펙의 순수 Getter/Setter 및 `PropertyChangeSupport` 명시 구현 유지)

3. **Swing EDT (Event Dispatch Thread) 준수**
   - 모든 UI 생성, 갱신, 테마 변경은 반드시 `SwingUtilities.invokeLater()`를 통해 EDT에서 실행되어야 합니다.
   - 시간이 걸리는 I/O나 비즈니스 처리는 EDT를 블로킹하지 않도록 백그라운드 스레드(`SwingWorker` 등)를 고려합니다.

---

## 아키텍처 및 디렉토리 구조
프로젝트는 **Presentation Model 패턴**을 철저히 분리하여 구현되어 있습니다.

```
src/main/java/com/example/swingapp/
├── Application.java               # 메인 엔트리포인트 (FlatLaf 초기화, MainFrame 실행)
├── model/                         # 도메인 모델 (PropertyChangeSupport 기반 JavaBeans)
│   ├── Customer.java              # 고객 엔티티 (속성 변경 알림 지원)
│   └── Order.java                 # 주문 엔티티
├── service/                       # 비즈니스 로직 및 인메모리 저장소
│   └── CustomerService.java       # 고객 및 주문 데이터 CRUD, 대시보드 메트릭 집계
├── presentation/                  # UI 바인딩 및 뷰 상태 관리 (JGoodies PresentationModel)
│   ├── CustomerPresentationModel.java # 폼 입력값 검증, 변경 추적, 모델 동기화
│   └── DashboardPresentationModel.java# 대시보드 통계 요약 상태 관리
└── view/                          # 순수 Swing 컴포넌트 및 화면 레이아웃
    ├── MainFrame.java             # 사이드바 네비게이션, CardLayout 화면 전환, 상태바
    ├── DashboardPanel.java        # 카드형 KPI 위젯 및 최근 주문 테이블
    ├── CustomerPanel.java         # 마스터-디테일 테이블 및 JGoodies Form 에디터
    └── SettingsPanel.java         # FlatLaf 런타임 라이트/다크 테마 스위처
```

---

## 에이전트 작업 지침 (Instructions for Agents)

### 1. 새 화면(패널) 추가 시 절차
1. `model/`에 필요한 도메인 엔티티 정의 (`com.jgoodies.binding.beans.Model` 상속 또는 `PropertyChangeSupport` 연동).
2. `presentation/`에 해당 화면을 전담할 `PresentationModel` 작성 (데이터 상태 및 비즈니스 커맨드 캡슐화).
3. `view/`에 `JPanel`을 상속하는 뷰 클래스 구현:
   - 복잡한 폼 레이아웃은 `com.jgoodies.forms.layout.FormLayout` 사용.
   - 입력 필드는 `BasicComponentFactory.createTextField(presentationModel.getModel("propertyName"))` 형태로 바인딩.
   - 버튼 스타일 등은 `putClientProperty("FlatLaf.styleClass", ...)` 적극 활용.
4. `MainFrame.java`의 `CardLayout` 및 사이드바 네비게이션에 신규 패널 등록.

### 2. 컴포넌트 스타일링 규칙
- 직접적인 픽셀 하드코딩 대신 FlatLaf의 클라이언트 프로퍼티(`FlatLaf.styleClass`, `JTextField.placeholderText`, `JButton.buttonType` 등)를 사용합니다.
- 색상을 직접 `new Color(...)`로 고정하지 않고 `UIManager.getColor(...)`를 활용하여 라이트/다크 테마 전환 시 자연스럽게 대응되도록 합니다.

### 3. 빌드 및 검증
- 수정 후 빌드 검증: `.\gradlew.bat test` 또는 `.\gradlew.bat check`
- 실행 검증: `.\gradlew.bat run`
