# Plani Slack Bot (플랜아이 슬랙봇)

[![Java](https://img.shields.io/badge/Java-21-b07219.svg)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6db33f.svg)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-02303a.svg)](https://gradle.org)
[![Architecture](https://img.shields.io/badge/Architecture-Hexagonal-blueviolet)](https://alistair.cockburn.us/hexagonal-architecture/)

## 💬 시작하게 된 계기 (Motivation)

사내의 주요 문서, 회의록 등 모든 정보는 노션을 통해 관리되고 있습니다. 하지만 방대한 정보가 계층형 구조로 흩어져 있어, 원하는 정보를 빠르게 찾기란 쉽지 않았습니다. 오랜 시간 근무한 직원들조차 문서의 위치를 헷갈려 하는 경우가 많았습니다.

**Plani Slack Bot**은 이러한 정보 검색의 비효율을 해결하기 위해 탄생했습니다. 슬랙 채널에서 봇을 맨션하고 질문하는 것만으로, 복잡한 노션 데이터베이스를 헤맬 필요 없이 원하는 정보를 바로 찾고 요약까지 받아볼 수 있습니다. 현재는 저희 팀의 업무 효율 향상을 목표로 개발되었습니다.

## ✨ 주요 기능 (Features)

Plani Slack Bot은 사용자의 자연어 질문을 분석하여 핵심 의도(Intent)를 파악하고, 그에 맞는 정보를 노션에서 찾아 제공합니다.

- **AI 기반 의도 분석 및 정보 검색:** 사용자의 질문을 아래와 같은 네 가지 의도로 분류하여 처리합니다.
    - **회의 (meeting):** '회의', '회의록', '미팅', 'agenda', 특정 회의실 이름(이비자, 산토리니 등)이 포함된 질문에 응답합니다.
        - *예시: `@Plani Bot 7월 28일 이비자 회의록 찾아줘`*
    - **문서 (document):** '문서', '파일', '자료', '가이드' 등 문서 관련 키워드가 포함된 질문에 응답합니다.
        - *예시: `@Plani Bot MSA 전환 가이드 문서 보여줘`*
    - **직원 (member):** '멤버', '사람', '조직도', '연락처', '이메일' 등 인물 관련 키워드가 포함된 질문에 응답합니다.
        - *예시: `@Plani Bot 신동호 님 소속이 어디야?`*
    - **휴가 (vacation):** '휴가', '연차', '휴무' 등 휴가 관련 키워드가 포함된 질문에 응답합니다.
        - *예시: `@Plani Bot 오늘 휴가자 누구야?`*
- **노션 페이지 AI 요약:** 검색 결과로 제공된 노션 페이지에 대해, 버튼 클릭 한 번으로 AI가 핵심 내용을 요약해주는 기능을 제공합니다.
- **실시간 슬랙 결과 공유:** 모든 검색 결과는 질문이 올라온 슬랙 채널에 즉시 공유되어, 팀원들과 빠르게 정보를 확인하고 협업할 수 있습니다.

## 🛠️ 기술 스택 (Tech Stack)

- **Language:** Java 21
- **Framework:** Spring Boot 3.x
- **Build Tool:** Gradle 8.x
- **Architecture:** Hexagonal Architecture
- **External APIs:** Slack API, Notion API, OpenAI API
- **Async:** Spring `@Async`
- **Etc:** Lombok, Jackson

## 🚀 시작하기 (Getting Started)

### 1. 환경변수 설정

`application.yml` 파일에 아래와 같이 Slack, Notion, OpenAI 관련 API 키와 설정값을 입력해야 합니다.

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
    member: "..."
    vacation: "..."
openai:
  api-key: "sk-..."
```

### 2. 애플리케이션 실행

```bash
./gradlew bootRun
```

## 🕹️ 사용 방법 (How to Use)

슬랙 채널에서 **@Plani Bot**을 맨션하고 자연스럽게 질문해보세요.

- `@Plani Bot 7월 전체 회의록 보여줘`
- `@Plani Bot DevOps팀 멤버 목록 알려줘`
- `@Plani Bot 사내 전화번호부`
- `@Plani Bot 지난주 산토리니 회의 참석자 누구야?`
- `@Plani Bot 8월 휴가자 현황`

---
