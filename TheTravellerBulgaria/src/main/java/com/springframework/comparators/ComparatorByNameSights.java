package com.springframework.comparators;

import java.util.Comparator;

import com.springframework.model.Sight;

public class ComparatorByNameSights implements Comparator<Sight> {

	@Override
	public int compare(Sight o1, Sight o2) {
		// TODO Auto-generated method stub
		return o1.getName().compareTo(o2.getName());
	}

}