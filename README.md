# dnd-7th-2-backend

- Figma: [Wireframe](https://www.figma.com/file/5Uuxwvyh5izdv6imJB1ePl/DND_2%EC%A1%B0-team?node-id=472%3A2), [UI](https://www.figma.com/file/5Uuxwvyh5izdv6imJB1ePl/DND_2%EC%A1%B0-team?node-id=561%3A222)
- [ERD](https://www.erdcloud.com/d/y8KTXX3dwiz93QE4b)

## 아키텍처

### 개발 서버

![image](https://user-images.githubusercontent.com/49931252/179384947-8739a65e-64f3-4e2a-80f4-d2d69bc72aec.png)

### 배포 서버

![image](https://user-images.githubusercontent.com/49931252/186856129-12d51755-7802-4927-828a-9cd684ad2ef1.png)

## 기술 스택

- Java 11
- Spring Boot, Spring Web, Spring Data Jpa(+Querydsl), Spring Security, Spring Rest Docs
- JUnit5, Mockito
- MySQL
- AWS EC2, RDS, SES, Route53, CodeDeploy(+S3)
- GitHub actions

## 환경변수

- `jdbc.url`
- `jdbc.username`
- `jdbc.password`
- `firebase.firebase_type`
- `firebase.project_id`
- `firebase.private_key_id`
- `firebase.private_key`
- `firebase.client_email`
- `firebase.client_id`
- `firebase.auth_uri`
- `firebase.token_uri`
- `firebase.auth_provider_x509_cert_url`
- `firebase.client_x509_cert_url`
- `jwt.secret_key`
- `jwt.access_token_validity`
- `jwt.refresh_token_validity`
- `aws.ses.access_key`
- `aws.ses.secret_key`