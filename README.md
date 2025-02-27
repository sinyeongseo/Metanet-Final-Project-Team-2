# 2024~2025 메타넷 최종 프로젝트 | [4조] Classpick
<p align="center"><img width="300" alt="logo-title-light" src="https://github.com/user-attachments/assets/8587b8ab-9cde-4e54-9154-4962521d1b9c"></p>

## 💭 프로젝트 개요
<img width="1920" alt="프로젝트 개요 및 목적" src="https://github.com/user-attachments/assets/9e50bcec-712f-461f-9c21-4a268936d76f" />
저희 서비스는 사용자가 원하는 실시간 강의를 조회하고 예약할 수 있는 기능과,
강사가 강의를 등록할 때 zoom 강의실을 자동으로 생성하고 수강생 입퇴장 데이터로 출결을 관리하는 실시간 강의 운영에 최적화된 서비스입니다.


## 🛠️ 개발환경 및 시스템 구성도
<img width="1920" alt="개발환경" src="https://github.com/user-attachments/assets/fc0fe71f-436a-4501-8f64-193e51ecc1d5" />
백엔드는 spring boot 3.4.1버전을 사용했으며, 프론트는 vue.js 3버전을 사용했습니다. 또한 많은 라이브러리나 외부 api를 통해 시스템의 완성도를 극대화하려고 노력했습니다.<br/>


<img width="1920" alt="시스템 구성도" src="https://github.com/user-attachments/assets/68a23b75-e8cf-435a-9de9-d07036ccbf33" />
Amazon AWS EC2 에서 jenkins 기반 CI/CD 로 docker 컨테이너화 된 nginx 뷰 기반 프론트와, tomcat 스프링 부트를 자동 배포하고 있습니다. 추가적으로 redis를 연결하여 캐싱기능을 사용할 수 있도록 하였습니다. DB와 스토리지는 RDS를 이용 mysql 과 이미지 및 파일 저장을 위해 S3를 이용했습니다. 보안은 Route 53을 통해 도메인을 연결하고 lets encrypt 를 통해 https 를 구현 했습니다. 추가적으로 cert bot 을 이용하여 자동 인증서 발급을 하게 하여, 보안성을 유지 할 수 있도록 하였습니다. 그리고 nginx 에게 리버스 프록시를 설정하여 서버에 부하를 줄이고 서비스 성능을 최적화 하는 구조로 설계하는데 집중했습니다.


## 🎯 기술 요구사항 이행 보고
<img width="1920" alt="기술 요구사항" src="https://github.com/user-attachments/assets/42efad26-fb30-4bac-ae90-851b7bb7966f" />
총 15개의 요구사항 완료


## 👩🏻‍💻 RnR(Role and Responsibility)
<img width="1920" alt="RnR" src="https://github.com/user-attachments/assets/86d5dbae-1d01-4395-9807-be1f96bee373" />
