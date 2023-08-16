import React from 'react';
import {Text} from 'react-native';
import {AppRegistry} from 'react-native';
import App from './App';
import {name as appName} from './app.json';
import {enableScreens} from 'react-native-screens';
import {NavigationContainer} from '@react-navigation/native';
import Store from './storage/store';
import {Provider} from 'react-redux';
import NavigationBar from './components/tools/navigationBar';

enableScreens();

function RootComponent() {
  return (
    <Provider store={Store}>
      <NavigationContainer>
        <App />
      </NavigationContainer>
    </Provider>
  );
}

AppRegistry.registerComponent(appName, () => RootComponent);
