This is a new [**React Native**](https://reactnative.dev) project, bootstrapped using [`@react-native-community/cli`](https://github.com/react-native-community/cli).

![header](https://capsule-render.vercel.app/api?type=waving&color=1DAE86&height=250&section=header&text=산학연계%20프로젝트%20우수상&fontSize=60&animation=fadeIn&fontAlignY=32&desc=2023%20DonggukUniversity%20Web%20Developer%20%20Traning%20Course&descAlignY=51&descAlign=70)

# **Tour Video Commerce Platform *WANDERLUST***

<table>
<tr>
<td align="center">
  
### Front-End

<img src="https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=white">
<img src="https://img.shields.io/badge/React_Native-61DAFB?style=for-the-badge&logo=react&logoColor=white">
</td>
<td align="center">
  
### Back-End

<img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=mongodb&logoColor=white">
<img src="https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white">
</td>

<td>

### UX·UI

<img src="https://img.shields.io/badge/Figma-ae4dff?style=for-the-badge&logo=figma&logoColor=white">
</td>
</tr>
</table>


> Role : Mobile Application

<hr/>

## Related Organization: Mobile App Cooperative


- **Corporate Requiremens**
  ```
  - Cloud Web Content Service
    - Development of user authentication features
    - Development of content registration management features
    - Development of content streaming features

  ```

- **Why, A Travel-specialized Video Commerce Platform?**
  ```
   - Sharp increase in travel demand after the COVID-19 pandemic
   - Rapid growth in the video commerce industry
   - Increase in collaborations between YouTubers and travel companies
   - Poor advertising sales performance compared to video views

  ```

<hr/>

- **제안**
>  1. AI를 활용한 추천 여행 영상 제공
>  2. 사용 기록 기반 개인 추천 영상 제공
>  3. 구매 성사 기반의 광고 비즈니스 모델


- **해결 방안**
>  1. 영상 인코딩 및 CDN
>     - AWS CloudFront, S3
>     - FFMPEG, Video.js(React Native) 라이브러리
>  2. 사용자를 위한 개인화 영상 제공
>     - GPT API 활용, 자연어 기반 추천 영상 제공
>     - TF-IDF 알고리즘 활용 개인화 영상 제공
>  3. 판매자를 위한 통계 제공
>     - Chart.js 라이브러리 활용 데이터 시각화
>     - Redis 활용 통계 등록 중복 방지 구현
>  4. UX 향상을 위한 성능 개선
>     - 영상 인코딩 서버 분리 배포
>     - SockJS Stomp JS 활용 동영상 진행도 시각화

---


### System Structure
> ![WANDERLUST 시스템 구조도](https://github.com/dgu-web-t3-blackshoe/travel-v-commerce-web/assets/102159721/7709a13e-3c18-4e01-8801-4e7a6b10d392)


<hr/>

### 배포 링크
> http://www.roberniro-projects.xyz/

### 유튜브 시연
> https://www.youtube.com/watch?v=EyDKypFDwgE

### 발표 자료
> https://drive.google.com/drive/folders/1bAkz9T9r50oekLjm5VMDQYYh0wHFJowW
# Getting Started

>**Note**: Make sure you have completed the [React Native - Environment Setup](https://reactnative.dev/docs/environment-setup) instructions till "Creating a new application" step, before proceeding.

## Step 1: Start the Metro Server

First, you will need to start **Metro**, the JavaScript _bundler_ that ships _with_ React Native.

To start Metro, run the following command from the _root_ of your React Native project:

```bash
# using npm
npm start

# OR using Yarn
yarn start
```

## Step 2: Start your Application

Let Metro Bundler run in its _own_ terminal. Open a _new_ terminal from the _root_ of your React Native project. Run the following command to start your _Android_ or _iOS_ app:

### For Android

```bash
# using npm
npm run android

# OR using Yarn
yarn android
```

### For iOS

```bash
# using npm
npm run ios

# OR using Yarn
yarn ios
```

If everything is set up _correctly_, you should see your new app running in your _Android Emulator_ or _iOS Simulator_ shortly provided you have set up your emulator/simulator correctly.

This is one way to run your app — you can also run it directly from within Android Studio and Xcode respectively.

## Step 3: Modifying your App

Now that you have successfully run the app, let's modify it.

1. Open `App.tsx` in your text editor of choice and edit some lines.
2. For **Android**: Press the <kbd>R</kbd> key twice or select **"Reload"** from the **Developer Menu** (<kbd>Ctrl</kbd> + <kbd>M</kbd> (on Window and Linux) or <kbd>Cmd ⌘</kbd> + <kbd>M</kbd> (on macOS)) to see your changes!

   For **iOS**: Hit <kbd>Cmd ⌘</kbd> + <kbd>R</kbd> in your iOS Simulator to reload the app and see your changes!

## Congratulations! :tada:

You've successfully run and modified your React Native App. :partying_face:

### Now what?

- If you want to add this new React Native code to an existing application, check out the [Integration guide](https://reactnative.dev/docs/integration-with-existing-apps).
- If you're curious to learn more about React Native, check out the [Introduction to React Native](https://reactnative.dev/docs/getting-started).

# Troubleshooting

If you can't get this to work, see the [Troubleshooting](https://reactnative.dev/docs/troubleshooting) page.

# Learn More

To learn more about React Native, take a look at the following resources:

- [React Native Website](https://reactnative.dev) - learn more about React Native.
- [Getting Started](https://reactnative.dev/docs/environment-setup) - an **overview** of React Native and how setup your environment.
- [Learn the Basics](https://reactnative.dev/docs/getting-started) - a **guided tour** of the React Native **basics**.
- [Blog](https://reactnative.dev/blog) - read the latest official React Native **Blog** posts.
- [`@facebook/react-native`](https://github.com/facebook/react-native) - the Open Source; GitHub **repository** for React Native.
