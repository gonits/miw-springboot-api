package com.gildedrose.server.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class is implementation for surgePrice interface. The
 * getSurgePriceForItem method records all the views(in form of timestamp) for
 * each items in a hashtable(thread safe). Then if all the timestamps for an
 * items fall under surgeduration of 1 hour and count of those timestamps is
 * greater than equal to 10 then surge is applied for that item.
 * 
 * @author Nitika Goel
 *
 */
public class SurgePriceImpl implements SurgePrice {

	private Duration surgeDuration;

	private int viewCounts;

	private double surgeIncrementPercentage;
	private ConcurrentHashMap<Long, CopyOnWriteArrayList<LocalDateTime>> itemViews = new ConcurrentHashMap<>();

	public SurgePriceImpl(Duration surgeDuration, int viewCounts, double surgeIncrementPercentage) {
		this.surgeDuration = surgeDuration;
		this.viewCounts = viewCounts;
		this.surgeIncrementPercentage = surgeIncrementPercentage;
	}

	@Override
	public int getSurgePriceForItem(Item item, boolean addViewCount) {
		Long id = item.getId();
		itemViews.putIfAbsent(id, new CopyOnWriteArrayList<>());
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
