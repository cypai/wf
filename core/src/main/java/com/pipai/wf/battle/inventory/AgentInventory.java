package com.pipai.wf.battle.inventory;

import java.util.Objects;
import java.util.function.Function;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pipai.wf.item.Item;
import com.pipai.wf.item.armor.Armor;
import com.pipai.wf.misc.DeepCopyable;
import com.pipai.wf.misc.DeepCopyableAsNew;

// TODO: Think about NoArmor issues
public class AgentInventory implements DeepCopyable, DeepCopyableAsNew {

	private Item[] items;
	private int itemsAmount;
	private Armor equippedArmor;
	private int equippedSlot;

	public AgentInventory(int slots) {
		items = new Item[slots];
	}

	@JsonCreator
	public AgentInventory(@JsonProperty("slots") int slots, @JsonProperty("items") Item[] items, @JsonProperty("equippedSlot") int equippedSlot) {
		if (slots != items.length) {
			throw new IllegalArgumentException("Size of inventory does not match item array size");
		}
		if (equippedSlot > slots) {
			throw new IllegalArgumentException("Specified equipped slot cannot be larger than the number of slots");
		}
		if (!(items[equippedSlot] instanceof Armor)) {
			throw new IllegalArgumentException("Equipped slot does not contain an Armor");
		}
		this.items = new Item[slots];
		for (int i = 0; i < slots; i++) {
			Item item = items[i];
			this.items[i] = item;
			if (item != null) {
				itemsAmount += 1;
			}
		}
		equippedArmor = (Armor) this.items[equippedSlot];
		this.equippedSlot = equippedSlot;
	}

	public int getSlots() {
		return items.length;
	}

	public Item[] getItems() {
		return items;
	}

	@JsonIgnore
	public boolean isFull() {
		return itemsAmount == items.length;
	}

	public Item getItem(int slot) {
		validateSlotArgument(slot);
		return items[slot - 1];
	}

	public Item swapItem(Item item, int slot) {
		Item removedItem = getItem(slot);
		setItem(item, slot);
		return removedItem;
	}

	public String getItemName(int slot) {
		Item item = getItem(slot);
		return item == null ? "None" : item.getName();
	}

	public void setItem(Item item, int slot) {
		validateSlotArgument(slot);
		items[slot - 1] = item;
	}

	public void removeItem(int slot) {
		validateSlotArgument(slot);
		items[slot - 1] = null;
	}

	public boolean contains(Item item) {
		for (int i = 1; i <= getSlots(); i++) {
			if (Objects.equals(getItem(i), item)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the slot for the item, or 0 if inventory does not contain the item.
	 */
	public int getSlotFor(Item item) {
		for (int i = 1; i <= getSlots(); i++) {
			if (getItem(i).equals(item)) {
				return i;
			}
		}
		return 0;
	}

	@JsonIgnore
	public boolean isEquippingArmor() {
		return equippedArmor != null;
	}

	@JsonIgnore
	public Armor getEquippedArmor() {
		return equippedArmor;
	}

	public int getEquippedSlot() {
		return equippedSlot;
	}

	public void equipArmor(int slot) {
		Item item = getItem(slot);
		if (item instanceof Armor) {
			equippedArmor = (Armor) item;
			equippedSlot = slot;
		} else {
			throw new IllegalArgumentException("Slot does not contain armor");
		}
	}

	public void unequipArmor() {
		equippedArmor = null;
	}

	private void validateSlotArgument(int slot) {
		if (slot > getSlots()) {
			throw new IllegalArgumentException("Slot argument " + slot + " was larger than the number of slots");
		}
		if (slot <= 0) {
			throw new IllegalArgumentException("Slot argument " + slot + " cannot be <= 0");
		}
	}

	@Override
	public AgentInventory deepCopyAsNew() {
		return copyByStrategy(item -> item == null ? null : item.copyAsNew());
	}

	@Override
	public AgentInventory deepCopy() {
		return copyByStrategy(item -> item == null ? null : item.copy());
	}

	private AgentInventory copyByStrategy(Function<Item, Item> strategy) {
		AgentInventory copy = new AgentInventory(getSlots());
		for (int i = 0; i < items.length; i++) {
			copy.items[i] = strategy.apply(items[i]);
		}
		copy.itemsAmount = itemsAmount;
		copy.equippedSlot = equippedSlot;
		if (isEquippingArmor()) {
			copy.equippedArmor = (Armor) copy.items[equippedSlot];
		}
		return copy;
	}

}
