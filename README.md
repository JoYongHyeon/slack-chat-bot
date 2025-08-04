# Plani Slack Bot (플랜아이 슬랙봇)

[![Java](https://img.shields.io/badge/Java-21-b07219.svg)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6db33f.svg)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-02303a.svg)](https://gradle.org)
[![Architecture](https://img.shields.io/badge/Architecture-Hexagonal-blueviolet)](https://alistair.cockburn.us/hexagonal-architecture/)

## 💬 시작하게 된 계기 (Motivation)

"매일 반복되는 자잘한 업무들, 슬랙에서 바로 처리할 순 없을까?" 

이 프로젝트는 위와 같은 단순한 생각에서 출발했습니다. 노션(Notion)에서 문서를 찾거나, 회의록을 요약하는 등의 일들을 슬랙을 떠나지 않고 곧바로 처리할 수 있는 봇을 만들고 싶었습니다. 

단순한 기능 구현을 넘어, 앞으로 계속해서 새로운 기능을 쉽게 추가하고 변경할 수 있는 **유연하고 견고한 구조**를 만드는 것을 핵심 목표로 삼았습니다.

## ✨ 주요 기능 (Features)

- **똑똑한 의도 분석:** 사용자의 메시지(`@Plani Bot 문서 찾아줘`)를 분석해서 원하는 작업을 정확히 찾아 수행합니다.
- **슬랙-노션 연동:** 슬랙을 벗어날 필요 없이, 멘션 한 번으로 노션 데이터베이스를 검색하고 결과를 공유합니다.
- **AI 요약 기능:** OpenAI API를 연동하여 긴 노션 페이지나 텍스트를 핵심만 요약해 줍니다.
- **플러그인 방식의 확장:** 새로운 기능(항공권 검색, 날씨 조회 등)이 필요할 때, 마치 플러그인을 끼우듯 쉽게 추가할 수 있도록 설계했습니다.

## 🛠️ 기술 스택 (Tech Stack)

- **Language:** Java 21
- **Framework:** Spring Boot 3.x
- **Build Tool:** Gradle 8.x
- **Architecture:** Hexagonal Architecture, Clean Architecture
- **External APIs:** Slack API, Notion API, OpenAI API
- **Async:** Spring `@Async`
- **Etc:** Lombok, Jackson

## 🚀 시작하기 (Getting Started)

### 1. 환경변수 설정

`.env` 또는 `application.yml` 파일에 아래와 같이 Slack, Notion, OpenAI 관련 API 키와 설정값을 입력해야 합니다.

```yaml
# application.yml
slack:
  bot-token: "xoxb-..."
  signing-secret: "..."
notion:
  api-key: "secret_..."
  database-id:
    document: "..."
    meeting: "..."
openai:
  api-key: "sk-..."
```

### 2. 애플리케이션 실행

```bash
./gradlew bootRun
```

## 🕹️ 사용 방법 (How to Use)

슬랙 채널에서 Plani Bot을 맨션하고 다음과 같이 메시지를 보내보세요.

- **문서 검색:** `@Plani Bot 문서: [검색할 키워드]`
- **회의록 요약:** `@Plani Bot 회의록 요약: [요약할 회의록 Notion 페이지 URL]`

---
