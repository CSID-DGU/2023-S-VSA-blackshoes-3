/* eslint-disable react-native/no-inline-styles */
/* eslint-disable react/no-unstable-nested-components */
import React, {useState} from 'react';
import {createStackNavigator} from '@react-navigation/stack';
import {createDrawerNavigator} from '@react-navigation/drawer';
import Init from './pages/others/initialPage';
import ThemeSelect from './pages/others/themeSelect';
import Home from './pages/main/home';
import ThemeVideo from './pages/others/themeVideos';
import MyLog from './pages/main/myLog';
import Video from './pages/main/showVideos';
import MyVideo from './pages/main/myVideo';
import Play from './pages/others/playVideo';
import SignIn from './pages/signpages/signIn';
import SignUp from './pages/signpages/signUp';
import FindPw from './pages/signpages/findPw';
import Login from './pages/signpages/login';
import CheckInfo from './pages/mypages/userInfo';
import ThemeSelectEach from './pages/mypages/subscribeManage';
import MyCommentPage from './pages/mypages/comment';
import SearchedVideos from './pages/others/searchedVideos';
import NavigationContainer from '@react-navigation/native';
import NavigationBar from './components/tools/navigationBar';
import {
  HeaderTitleComponent,
  HeaderRightComponent,
  HeaderLeftComponent,
} from './components/tools/headerComponents';
import {View, StyleSheet, Text, TouchableOpacity} from 'react-native';
import {useNavigation} from '@react-navigation/native';
import Icon from 'react-native-vector-icons/Ionicons';
import Store from './storage/store';
import {deleteUserId} from './storage/actions';
import AsyncStorage from '@react-native-async-storage/async-storage';
import axios from 'axios';
import {SERVER_IP} from './config';

const Drawer = createDrawerNavigator();
const Stack = createStackNavigator();

export default function App() {
  console.log('count render');
  const [search, setSearch] = useState(false);
  const [value, setValue] = useState('videoName');
  const [searchText, setSearchText] = useState('');
  const navigation = useNavigation();
  const [sortModalVisible, setSortModalVisible] = useState(false);
  const [suggestions, setSuggestions] = useState([]);

  const items = [
    {label: 'Video', value: 'videoName'},
    {label: 'Seller', value: 'sellerName'},
    {label: 'Tag', value: 'tagName'},
    {label: 'GPT', value: 'gpt'},
  ];

  function CustomDrawer({drawerNavigation}) {
    return (
      <View style={styles.toggleMenuContainer}>
        <View style={styles.toggleMenuTop}>
          <Text style={styles.menuTitle}>My Page</Text>

          <TouchableOpacity
            style={styles.closeMenuButton}
            onPress={() => drawerNavigation.closeDrawer()}>
            <Icon name="close-outline" size={35} color={'black'} />
          </TouchableOpacity>
        </View>

        <TouchableOpacity
          style={styles.menuContentsContainer}
          onPress={() => navigation.navigate('CheckInfo')}>
          {/* <Icon name="person" size={35} color={'black'} /> */}
          <Text style={styles.menuContentsText}>회원 정보 조회</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.menuContentsContainer}
          onPress={() => navigation.navigate('ThemeSelectEach')}>
          {/* <Icon name="bookmark" size={35} color={'black'} /> */}
          <Text style={styles.menuContentsText}>구독 관리</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.menuContentsContainer}
          onPress={() => navigation.navigate('MyCommentPage')}>
          {/* <Icon name="chatbubble" size={35} color={'black'} /> */}
          <Text style={styles.menuContentsText}>내가 쓴 댓글</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.menuContentsContainer}
          onPress={async () => {
            const refreshToken = Store.getState().REFRESH;

            console.log(refreshToken);
            try {
              await axios.post(`${SERVER_IP}user-service/logout`, {
                refreshToken: refreshToken,
              });
              await AsyncStorage.removeItem('email');
              await AsyncStorage.removeItem('pass');
              Store.dispatch(deleteUserId());
              navigation.navigate('SignIn');
            } catch (e) {
              console.log(e);
            }
          }}>
          {/* <Icon name="log-out" size={35} color={'black'} /> */}
          <Text style={styles.menuContentsText}>로그 아웃</Text>
        </TouchableOpacity>
      </View>
    );
  }

  return (
    <Drawer.Navigator
      style={styles.fontStyle}
      screenOptions={{
        drawerPosition: 'right',
        headerShown: false,
        drawerStyle: {
          width: '100%',
          backgroundColor: '#F5F5F5',
        },
      }}
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
            screenOptions={{
              animationEnabled: false,
              headerMode: 'float',
              headerTitleAlign: 'center',
              headerLeft: () => (
                <HeaderLeftComponent
                  search={search}
                  setSortModalVisible={setSortModalVisible}
                  sortModalVisible={sortModalVisible}
                  setValue={setValue}
                  items={items}
                  value={value}
                />
              ),

              headerTitle: () => (
                <HeaderTitleComponent
                  value={value}
                  search={search}
                  setSearch={setSearch}
                  searchText={searchText}
                  setSearchText={setSearchText}
                  suggestions={suggestions}
                  setSuggestions={setSuggestions}
                />
              ),
              headerRight: () => (
                <HeaderRightComponent
                  search={search}
                  setSearch={setSearch}
                  setSuggestions={setSuggestions}
                />
              ),
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
            <Stack.Screen name="ShowVideo" component={Video} />
            <Stack.Screen name="My" component={MyVideo} />
            <Stack.Screen name="Log" component={MyLog} />
            <Stack.Screen name="SearchedVideos" component={SearchedVideos} />
            <Stack.Screen name="CheckInfo" component={CheckInfo} />
            <Stack.Screen name="ThemeSelectEach" component={ThemeSelectEach} />
            <Stack.Screen name="MyCommentPage" component={MyCommentPage} />

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
  );
}

const styles = StyleSheet.create({
  toggleMenuContainer: {
    flex: 1,
    alignItems: 'center',
  },
  toggleMenuTop: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    width: '100%',
    paddingVertical: 15,
    backgroundColor: 'white',
    paddingHorizontal: 16,
    borderBottomWidth: 1,
    borderBottomColor: '#D9D9D9',

    borderTopWidth: 1,
  },
  menuTitle: {
    fontSize: 25,
    fontWeight: 'bold',
    letterSpacing: 3,
    color: 'black',
  },
  menuContentsContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 30,
    width: '100%',
    paddingLeft: 35,
    paddingVertical: 25,
    borderBottomWidth: 0.4,
    borderStyle: 'dotted',
  },
  menuContentsText: {
    fontSize: 23,
    fontWeight: 'bold',
  },
});
