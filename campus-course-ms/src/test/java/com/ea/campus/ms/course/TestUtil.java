package com.ea.campus.ms.course;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.List;

public class TestUtil {
	
	public static <T> void checkListEquality(List<T> list1, List<T> list2) {
		then(list1).isNotNull();
		then(list2).isNotNull();
		then(list1.size()).isEqualTo(list2.size());

		for (int i = 0; i < list1.size(); i++) {
			then(list1.get(i)).isEqualTo(list2.get(i));
		}
	}

}
