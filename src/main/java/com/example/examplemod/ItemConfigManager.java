package com.example.examplemod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ItemConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = Paths.get("config/PlayerConfigData/item_data.json");
    private static final Type ITEM_DATA_LIST_TYPE = new TypeToken<List<ItemData>>(){}.getType();
    public static void saveItemData(String uniqueId, int damage, int strength, int attackSpeed, int critDmg, int critChance, int intelligence) {
        List<ItemData> items = loadAllItems();
        boolean itemExists = false;
        for (ItemData item : items) {
            if (item.getUniqueId().equals(uniqueId)) {
                item.setDamage(damage);
                item.setStrength(strength);
                item.setAttackSpeed(attackSpeed);
                item.setCritDmg(critDmg);
                item.setCritChance(critChance);
                item.setIntelligence(intelligence);
                itemExists = true;
                break;
            }
        }
        if (!itemExists) {
            ItemData newItem = new ItemData(uniqueId, damage, strength, attackSpeed, critDmg, critChance, intelligence);
            items.add(newItem);
        }
        try (FileWriter writer = new FileWriter(CONFIG_PATH.toFile())) {
            GSON.toJson(items, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static List<ItemData> loadAllItems() {
        if (!Files.exists(CONFIG_PATH)) {
            return new ArrayList<>();
        }
        try (FileReader reader = new FileReader(CONFIG_PATH.toFile())) {
            List<ItemData> items = GSON.fromJson(reader, ITEM_DATA_LIST_TYPE);
            return items != null ? items : new ArrayList<>(); // Return empty list if file is empty or malformed
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    public static ItemData loadItemData(String uniqueId) {
        List<ItemData> items = loadAllItems();
        for (ItemData item : items) {
            if (item.getUniqueId().equals(uniqueId)) {
                return item;
            }
        }
        return null;
    }
    public static class ItemData {
        private String uniqueId;
        private int damage;
        private int strength;
        private int attackSpeed;
        private int critDmg;
        private int critChance;
        private int intelligence;
        public ItemData(String uniqueId, int damage, int strength, int attackSpeed, int critDmg, int critChance, int intelligence) {
            this.uniqueId = uniqueId;
            this.damage = damage;
            this.strength = strength;
            this.attackSpeed = attackSpeed;
            this.critDmg = critDmg;
            this.critChance = critChance;
            this.intelligence = intelligence;
        }
        public String getUniqueId() {
            return uniqueId;
        }
        public int getDamage() {
            return damage;
        }
        public void setDamage(int damage) {
            this.damage = damage;
        }
        public int getStrength() {
            return strength;
        }
        public void setStrength(int strength) {
            this.strength = strength;
        }
        public int getAttackSpeed() {
            return attackSpeed;
        }
        public void setAttackSpeed(int attackSpeed) {
            this.attackSpeed = attackSpeed;
        }
        public int getCritDmg() {
            return critDmg;
        }
        public void setCritDmg(int critDmg) {
            this.critDmg = critDmg;
        }
        public int getCritChance() {
            return critChance;
        }
        public void setCritChance(int critChance) {
            this.critChance = critChance;
        }
        public int getIntelligence() {
            return intelligence;
        }
        public void setIntelligence(int intelligence) {
            this.intelligence = intelligence;
        }
    }
}
