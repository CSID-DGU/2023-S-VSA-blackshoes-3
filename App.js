/* eslint-disable react-native/no-inline-styles */
/* eslint-disable react/no-unstable-nested-components */
import React, {useState} from 'react';
import {NavigationContainer} from '@react-navigation/native';
import {createStackNavigator} from '@react-navigation/stack';
import {createDrawerNavigator} from '@react-navigation/drawer';
import Init from './pages/init';
import ThemeSelect from './pages/themeSelect';
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
import CheckInfo from './pages/myPage/checkInfo';
import {
  View,
  StyleSheet,
  Image,
  Text,
  TouchableOpacity,
  TextInput,
  Modal,
} from 'react-native';
import {useNavigation} from '@react-navigation/native';
import Icon from 'react-native-vector-icons/Ionicons';
import Store from './storage/store';
import {Provider} from 'react-redux';

const Drawer = createDrawerNavigator();
const Stack = createStackNavigator();

export default function App() {
  const [search, setSearch] = useState(false);
  const [value, setValue] = useState('recent');
  const [sortModalVisible, setSortModalVisible] = useState(false);

  const items = [
    {label: '최신순', value: 'recent'},
    {label: '조회순', value: 'views'},
    {label: '좋아요순', value: 'likes'},
  ];

  const getLabelForValue = val => {
    const item = items.find(i => i.value === val);
    return item ? item.label : val;
  };
  function HeaderRightComponent() {
    const navigation = useNavigation();

    if (search) {
      return (
        <View style={styles.itemContainer}>
          <TouchableOpacity
            style={styles.button}
            onPress={() => setSearch(false)}>
            <Icon style={styles.icon} name="close" size={30} color={'black'} />
          </TouchableOpacity>
        </View>
      );
    }

    return (
      <View style={styles.itemContainer}>
        <TouchableOpacity
          style={styles.button}
          onPress={() => setSearch(!search)}>
          <Icon
            style={styles.icon}
            name="search-outline"
            size={30}
            color={'black'}
          />
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.button}
          onPress={() => navigation.openDrawer()}>
          <Icon
            style={styles.icon}
            name="person-circle-outline"
            size={30}
            color={'black'}
          />
        </TouchableOpacity>
      </View>
    );
  }

  const screenOptions = {
    headerMode: 'float',
    headerTitleAlign: 'center',
    headerLeft: () =>
      search ? (
        <TouchableOpacity
          style={styles.dropDownButton}
          onPress={() => setSortModalVisible(true)}>
          <Text style={styles.dropDownFont}>{getLabelForValue(value)}</Text>
        </TouchableOpacity>
      ) : null,

    headerTitle: () =>
      search ? (
        <TextInput placeholder="검색" style={styles.searchBar} />
      ) : (
        <Image source={require('./assets/logo.png')} />
      ),
    headerRight: () => <HeaderRightComponent />,
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
  };

  function CustomDrawer({drawerNavigation}) {
    const navigation = useNavigation();
    return (
      <View style={{flex: 1, alignItems: 'center', justifyContent: 'center'}}>
        <Text>Drawer Menu</Text>

        <TouchableOpacity
          style={styles.closeMenuButton}
          onPress={() => drawerNavigation.closeDrawer()}>
          <Text>Close Menu</Text>
          <Icon name="close-outline" size={30} color={'black'} />
        </TouchableOpacity>

        <TouchableOpacity onPress={() => navigation.navigate('CheckInfo')}>
          <Text>CheckInfo</Text>
        </TouchableOpacity>
      </View>
    );
  }

  return (
    <Provider store={Store}>
      <NavigationContainer>
        <Modal
          animationType="slide"
          transparent={true}
          visible={sortModalVisible}
          onRequestClose={() => {
            setSortModalVisible(!sortModalVisible);
          }}>
          <View
            style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
            <View
              style={{
                width: 300,
                height: 200,
                backgroundColor: 'white',
                borderRadius: 20,
                padding: 20,
              }}>
              {items.map(item => (
                <TouchableOpacity
                  key={item.value}
                  onPress={() => {
                    setValue(item.value);
                    setSortModalVisible(false);
                  }}>
                  <Text>{item.label}</Text>
                </TouchableOpacity>
              ))}
            </View>
          </View>
        </Modal>
        <Drawer.Navigator
          screenOptions={{
            drawerPosition: 'right',
            headerShown: false,
          }}
          drawerWidth="70%"
          initialRouteName="MainStack"
          drawerContent={props => (
            <CustomDrawer drawerNavigation={props.navigation} />
          )}>
          <Drawer.Screen
            name="MainStack"
            options={{title: 'Main', headerShown: false}}>
            {() => (
              <Stack.Navigator
                initialRouteName="First"
                screenOptions={screenOptions}>
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
                <Stack.Screen name="CheckInfo" component={CheckInfo} />

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
            )}
          </Drawer.Screen>
        </Drawer.Navigator>
      </NavigationContainer>
    </Provider>
  );
}

const styles = StyleSheet.create({
  itemContainer: {
    flexDirection: 'row',
    paddingHorizontal: 5,
  },
  icon: {
    marginRight: 8,
  },
  searchBar: {
    position: 'relative',
    left: 15,
    backgroundColor: '#EBEBEB',
    width: 250,
    borderRadius: 10,
    height: 40,
    paddingHorizontal: 10,
  },
  dropDownButton: {
    width: 60,
    alignItems: 'center',
    marginLeft: 10,
    height: 39,
    justifyContent: 'center',
    borderRadius: 10,
    borderWidth: 0.5,
  },
  dropDownFont: {
    fontWeight: '600',
    letterSpacing: 0.5,
  },
});
