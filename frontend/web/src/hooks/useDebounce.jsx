import { useState, useEffect } from "react";

export const useDebounce = (value, delay) => {
  // value와 delay를 인자로 받는다.
  const [debounceValue, setDebounceValue] = useState(value);

  useEffect(() => {
    // 업데이트 된 value를 delay 이후에 설정한다.
    const handler = setTimeout(() => {
      setDebounceValue(value);
    }, delay);

    // timeout이 설정되기 전에 이전 timeout을 clear한다.
    return () => {
      clearTimeout(handler);
    };
    // value나 delay가 업데이트 될 때마다 useEffect를 재실행한다.
  }, [value, delay]);

  // 최종적으로 debounce된 value를 반환한다.
  return debounceValue;
};
