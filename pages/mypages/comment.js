/* eslint-disable react-hooks/exhaustive-deps */
import React, {useEffect, useState} from 'react';
import {
  ScrollView,
  TouchableOpacity,
  View,
  Text,
  StyleSheet,
} from 'react-native';
import {useSelector} from 'react-redux';
import axiosInstance from '../../utils/axiosInstance';

export default function MyCommentPage({navigation, route}) {
  const userId = useSelector(state => state.USER);
  const [commentContents, setCommentContents] = useState(null);

  useEffect(() => {
    getUserComment();
  }, []);

  const getUserComment = async () => {
    try {
      console.log('userId in getUserComment : ', userId);
      const response = await axiosInstance.get(
        `comment-service/comments/user?userId=${userId}&page=0&size=10`,
      );

      setCommentContents(response.data.payload);
    } catch (e) {
      console.log(e);
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>내가 쓴 댓글</Text>
      <ScrollView style={styles.scrollContainer}>
        <View>
          {commentContents &&
            commentContents.comments.map((e, i) => {
              return (
                <TouchableOpacity key={i}>
                  <Text>{e.createdAt.split('T')[0]}</Text>
                  <Text>{e.content}</Text>
                </TouchableOpacity>
              );
            })}
        </View>
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
});
