/* eslint-disable react/react-in-jsx-scope */
import {View, StyleSheet} from 'react-native';
import {TouchableOpacity} from 'react-native-gesture-handler';
import {useNavigation} from '@react-navigation/native';
import Icon from 'react-native-vector-icons/Ionicons';

export default function TopbarIcons() {
  const navigation = useNavigation();
  return (
    <View style={styles.itemContainer}>
      <TouchableOpacity
        style={styles.button}
        onPress={() => navigation.navigate('설정')}>
        <Icon
          style={styles.icon}
          name="search-outline"
          size={30}
          color={'black'}
        />
      </TouchableOpacity>
      <TouchableOpacity
        style={styles.button}
        onPress={() => navigation.navigate('설정')}>
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

const styles = StyleSheet.create({
  itemContainer: {
    position: 'absolute',
    flexDirection: 'row',
    paddingHorizontal: 5,
  },
  icon: {
    marginRight: 10,
  },
});
