# ![img](https://user-images.githubusercontent.com/85453429/187358936-198c84d6-65de-4f31-972b-33bbc6443792.png) Look out

### 소개
>청각장애인을 위한 음성 기반 위험 안내 어플

- 라즈베리 파이를 활용하여 가정이나 실내 장소에 설치 가능한 기기를 만듭니다.
- 디바이스에 특정 음성이 인식되었을 경우 어플리케이션 서버에 감지된 키워드를 전달합니다.
- 어플에 등록한 디바이스에서 키워드 감지될 경우, 사용자는 스마트폰 통해 알림을 받을 수 있습니다.

### 주요 기능
> 디바이스
   *
   *
   *
  
> 어플
   * 비상 상황에 바로 119와 112로 전화를 걸 수 있는 기능
   * 비상 상황에 문자를 보낼 수 있는 기능(1. 자신이 설정한 기본 값으로 전송, 2. 직접 입력하여 전송)
   * 알림이 뜰 때 진동과 같이 울릴 수 있도록 설정하는 기능
   * 알림 로그(알림이 뜬 시간과 감지한 키워드)을 직접 확인하고 지울 수 있는 기능
   * 디바이스를 등록하고 삭제할 수 있는 기능

---

### 소개 영상

## [![YoutubeVideo]()

---

### 개발과 개발 환경

| Part         | Environment     | Remark(Version) |
| ------------ | --------------- | --------------- |
| App          | Android Studio  | 2021.2.1        |
| App Server   | Fire Base       | 7.6.15          |
| Device       | Raspbian        | 5.15(64-bit)    |

---

### Main library

| Name          | Remark                                                                              |
| ------------- | ----------------------------------------------------------------------------------- |
| WebRTC        | Seamless voice and data transmission between users                                  |
| WebGL(PixiJS) | Outputs animation instead of video, using the client's graphics processing hardware |
| WebAudioAPI   | Voice analysis and control for realizing realistic conversations without video      |
| Jest          | Unit and Integration Test of implemented components and classes                     |

---

### 시스템 다이어그램

#### 전체 시스템 구성

- Peer-to-peer connection using WebRTC is the most important part.
- Audio streams are exchanged through this P2P connection, and avatar-related data is also continuously transmitted to each other.
- Therefore, when a user connects for the first time, the P2P connection is established using the back signaling server.
- As the connection is completed, the transmitted audio stream is continuously played, and animation is drawn every frame using WebGL through the transmitted avatar information.
  ![image]()

#### 다중 연결 아키텍처

- So we aim for a project that anyone can service on their own server, that a number of P2P connections were established in **Full Mesh** in order to minimize the role of the back server.
- If we launch this service, the architecture may be restructured using **SFU**.
<img src="https://www.itrelease.com/wp-content/uploads/2021/06/Full-Mesh-Topology-1024x640.jpg" width="600" height="400">

#### 앱 화면
1. 스플래쉬 화면
<img src="https://user-images.githubusercontent.com/74593890/140017019-76c02218-0044-498d-a08e-10981f3b3ef5.png" width="200"> 

---
2. 메인 화면
<img src="https://user-images.githubusercontent.com/74593890/140028049-f3f2217f-ec49-40fe-ad9e-058d97e0c0ab.png" width="200"> 

---
3. 세팅 화면
<img src="https://user-images.githubusercontent.com/74593890/140028152-07059817-a7b0-44be-bdc3-03a1093f623d.png" width="200"> 

---
3-1. 문자 기본 값 화면
<img src="" width="200"> 

3-2. 알림 설정 화면
<img src="" width="200"> 

3-3. 디바이스 설정화면
<img src="" width="200"> 

3-3-1. 디바이스 추가 화면
<img src="" width="200"> 

3-4. 알림 기록 보는 화면
<img src="" width="200"> 

---
4-1. 알림 화면("불이야", "조심해" -> 119)
<img src="" width="200"> 

4-2. 알림 화면("도둑이야" -> 112)
<img src="" width="200"> 

---
5. 문자 전송 창
<img src="" width="200"> 

---


### 시작하기

in back folder

```
npm install
npm run start:dev
```

in front folder

```
npm install
npm run start
```

- You can change the backend address in **.env.development** in the front folder.

---

### 문서

- Information on various components, classes, and functions can be found [here](https://voicespaceunder5.github.io/VoiceSpaceDocs/docs/).

---

### License

- ?

---

### 팀 정보

| Name     | Email                                       | Role   | Major Part                                | Minor Part | Tech Stack                                   |
| -------- | ------------------------------------------- | ------ | ----------------------------------------- | ---------- | -------------------------------------------- |
| 김준영 | 메일주소 | 팀장 | 역할 | 역할 | 기술 스택 |
| 김언지 | ejkim0625@gmail.com   | 팀원 | 역할 | 역할 | 기술 스택 |
| 김지윤 | kwldbs1118@gmail.com  | 팀원 | 역할 | 역할 | 기술 스택 |
| 이채영 | pop98149814@gmail.com | 팀원 | 역할 | 역할 | 기술 스택 |
