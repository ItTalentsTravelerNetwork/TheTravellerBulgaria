package com.springframework.comparators;

import java.util.Comparator;

import com.springframework.model.Activity;

public class ComparatorByNameActivities implements Comparator<Activity> {

	@Override
	public int compare(Activity o1, Activity o2) {
		// TODO Auto-generated method stub
		return o1.getName().compareTo(o2.getName());
	}

}