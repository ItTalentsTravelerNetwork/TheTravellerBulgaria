package com.springframework.comparators;

import java.util.Comparator;

import com.springframework.model.PlaceToEat;

public class ComparatorByNamePlacesToEat implements Comparator<PlaceToEat> {

	@Override
	public int compare(PlaceToEat o1, PlaceToEat o2) {
		// TODO Auto-generated method stub
		return o1.getName().compareTo(o2.getName());
	}

}
