This is a [**React Native**](https://reactnative.dev) project, bootstrapped using [`@react-native-community/cli`](https://github.com/react-native-community/cli).

![header](https://capsule-render.vercel.app/api?type=waving&color=1DAE86&height=240&section=header&text=University-Industry%20Collaboration%20Project&fontSize=50&animation=fadeIn&fontAlignY=28&desc=2023%20Dongguk%20University%20Web%20Developer%20Training%20Course&descAlignY=50&descAlign=60)

> Get Excellence Award

# **A Travel-specialized Video Commerce Platform *WANDERLUST***

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

- **Proposal**
>  1. Offering travel video recommendations using AI
>  2. Providing personalized video recommendations based on user history
>  3. Advertising business model based on completed purchases


- **Solutions**
>  1. Video encoding and CDN
>     - AWS CloudFront, S3
>     - FFMPEG, Video.js(React Native) library
>  2. Personalized video offerings for users
>     - Utilizing GPT API, providing recommendation videos based on natural language
>     - Providing personalized videos using TF-IDF algorithm
>  3. Providing statistics for sellers
>     - Data visualization using Chart.js library
>     - Implementation of duplicate statistics registration prevention using Redis
>  4. Performance improvements for enhanced UX
>     - Separate deployment for video encoding servers
>     - Video progress visualization using SockJS Stomp JS

---


### System Structure
> ![WANDERLUST 시스템 구조도](https://github.com/dgu-web-t3-blackshoe/travel-v-commerce-web/assets/102159721/7709a13e-3c18-4e01-8801-4e7a6b10d392)


<hr/>

### Deployment Link
> http://www.roberniro-projects.xyz/

### Youtube
> https://www.youtube.com/watch?v=EyDKypFDwgE

### Presentation
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
