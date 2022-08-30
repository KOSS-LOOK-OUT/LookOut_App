# ![img](https://user-images.githubusercontent.com/85453429/187358936-198c84d6-65de-4f31-972b-33bbc6443792.png) Look out

### 소개
> **시각장애인**을 위한~...

- ~을 할 수 있고...
- ~을 할 수 있고... 

### 주요 기능

   * 전화~
   * 문자~
   * 디바이스 등록~
   * 알림 로그~

---

### 소개 영상

## [![YoutubeVideo](https://img.youtube.com/vi/ufxFfA7_ntU/maxresdefault.jpg)](https://youtu.be/ufxFfA7_ntU)

---

### 개발과 개발 환경

| Part                       | Environment | Remark(Version) |
| -------------------------- | ----------- | --------------- |
| FrontEnd                   | React       | 17.0.2          |
| BackEnd                    | NestJS      | 7.6.15          |
| WebServer                  | Nginx       | 1.14.2          |
| Publishing Server Hardware | AWS         | EC2             |

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

#### Overall system configuration

- Peer-to-peer connection using WebRTC is the most important part.
- Audio streams are exchanged through this P2P connection, and avatar-related data is also continuously transmitted to each other.
- Therefore, when a user connects for the first time, the P2P connection is established using the back signaling server.
- As the connection is completed, the transmitted audio stream is continuously played, and animation is drawn every frame using WebGL through the transmitted avatar information.
  ![image](https://user-images.githubusercontent.com/74593890/139775166-f036b4c0-1584-4ecd-9444-5a1788fec82c.png)

#### Multiple peer connection architecture

- So we aim for a project that anyone can service on their own server, that a number of P2P connections were established in **Full Mesh** in order to minimize the role of the back server.
- If we launch this service, the architecture may be restructured using **SFU**.
<img src="https://www.itrelease.com/wp-content/uploads/2021/06/Full-Mesh-Topology-1024x640.jpg" width="600" height="400">

#### 앱 화면
1. 메인 화면
<img src="https://user-images.githubusercontent.com/74593890/140017019-76c02218-0044-498d-a08e-10981f3b3ef5.png" width="200"> 

---
2. 세팅 화면
<img src="https://user-images.githubusercontent.com/74593890/140028049-f3f2217f-ec49-40fe-ad9e-058d97e0c0ab.png" width="200"> 

---
3. 알림 화면
<img src="https://user-images.githubusercontent.com/74593890/140028152-07059817-a7b0-44be-bdc3-03a1093f623d.png" width="200"> 

---

### 주요 기능

- 쭈르륵~...
- 기능 적기~ ...

### you can try it [**right here**](https://giggleforest.com)

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

- [MIT](https://github.com/VoiceSpaceUnder5/VoiceSpace/blob/master/LICENSE)

---

### 팀 정보

| Name     | Email                                       | Role   | Major Part                                | Minor Part | Tech Stack                                   |
| -------- | ------------------------------------------- | ------ | ----------------------------------------- | ---------- | -------------------------------------------- |
| 김준영 | 메일주소 | 팀장 | 역할 | 역할 | 기술 스택 |
| 김언지 | ejkim0625@gmail.com | 팀원 | 역할 | 역할 | 기술 스택 |
| 김지윤 | 메일주소 | 팀원 | 역할 | 역할 | 기술 스택 |
| 이채영 | 메일주소 | 팀원 | 역할 | 역할 | 기술 스택 |
