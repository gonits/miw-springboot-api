package com.gildedrose.server.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Hashtable;
import java.util.Vector;

public class SurgePriceImpl implements SurgePrice {

	private Duration surgeDuration;

	private int viewCounts;

	private double surgeIncrementPercentage;
	private Hashtable<Long, Vector<LocalDateTime>> itemViews = new Hashtable<>();

	public SurgePriceImpl(Duration surgeDuration, int viewCounts, double surgeIncrementPercentage) {
		this.surgeDuration = surgeDuration;
		this.viewCounts = viewCounts;
		this.surgeIncrementPercentage = surgeIncrementPercentage;
	}

	@Override
	public int getSurgePriceForItem(Item item, boolean addViewCount) {
		Long id = item.getId();
		itemViews.putIfAbsent(id, new Vector<>());
		LocalDateTime currentTime = LocalDateTime.now();
		LocalDateTime surgeDurationWindow = currentTime.minus(surgeDuration);
		itemViews.get(id).removeIf(view -> view.isBefore(surgeDurationWindow));
		if (addViewCount)
			itemViews.get(id).add(LocalDateTime.now());
		if (itemViews.get(id).size() >= viewCounts) {
			return (int) Math.round(item.getPrice() * (1 + surgeIncrementPercentage));
		}
		return item.getPrice();
	}

}
