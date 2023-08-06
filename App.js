/* eslint-disable react/no-unstable-nested-components */
import React from 'react';
import {Image} from 'react-native';
import {NavigationContainer} from '@react-navigation/native';
import {createStackNavigator} from '@react-navigation/stack';
import Init from './pages/init';
import ThemeSelect from './pages/themeSelect';
import TopbarIcons from './components/topBarIcons';
import Home from './pages/home';
import ThemeVideo from './pages/themeVideo';
import MyLog from './pages/myLog';
import Video from './pages/video';
import MyVideo from './pages/myVideo';
import Play from './pages/play';
import SignIn from './pages/sign/signIn';
import SignUp from './pages/sign/signUp';
import FindPw from './pages/sign/findPw';
import Login from './pages/sign/login';

const Stack = createStackNavigator();
export default function App() {
  return (
    <NavigationContainer>
      <Stack.Navigator
        initialRouteName="First"
        screenOptions={{
          headerMode: 'float',
          headerTitleAlign: 'center',
          headerLeft: () => null,
          headerTitle: () => <Image source={require('./assets/logo.png')} />,
          headerRight: () => <TopbarIcons />,
          headerStyle: {
            height: 60,
            borderBottomWidth: 1,
            shadowColor: '#000',
            shadowOffset: {
              width: 0,
              height: 2,
            },
            shadowOpacity: 0.5,
            shadowRadius: 2,
            elevation: 1,
          },
        }}>
        <Stack.Screen
          name="First"
          component={Init}
          options={{headerShown: false}}
        />
        <Stack.Screen
          name="ThemeSelect"
          component={ThemeSelect}
          options={{
            headerRight: () => <></>,
          }}
        />
        <Stack.Screen name="Home" component={Home} />
        <Stack.Screen name="ThemeVideo" component={ThemeVideo} />
        <Stack.Screen name="Video" component={Video} />
        <Stack.Screen name="My" component={MyVideo} />
        <Stack.Screen name="Log" component={MyLog} />
        <Stack.Screen
          name="Play"
          component={Play}
          options={{headerShown: false}}
        />
        <Stack.Screen
          name="SignIn"
          component={SignIn}
          options={{
            headerRight: () => <></>,
          }}
        />
        <Stack.Screen
          name="SignUp"
          component={SignUp}
          options={{
            headerRight: () => <></>,
          }}
        />
        <Stack.Screen
          name="Login"
          component={Login}
          options={{
            headerRight: () => <></>,
          }}
        />
        <Stack.Screen
          name="FindPw"
          component={FindPw}
          options={{
            headerRight: () => <></>,
          }}
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
