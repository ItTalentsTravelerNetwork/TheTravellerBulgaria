package com.springframework.comparators;

import java.util.Comparator;

import com.springframework.model.PlaceToSleep;

public class ComparatorByNamePlacesToSleep implements Comparator<PlaceToSleep> {

	@Override
	public int compare(PlaceToSleep o1, PlaceToSleep o2) {
		return o1.getName().compareTo(o2.getName());
	}

}
