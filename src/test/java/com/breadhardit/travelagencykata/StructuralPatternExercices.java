package com.breadhardit.travelagencykata;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class StructuralPatternExercices {
    /*
     * We have a calories-counter API. The users can ask how much calories food have.
     * User can ask for a list of items. And this item's can be dishes or ingredients.
     * A dish has a list of ingredients, and each ingredient has calories.
     * Te number of calories to return in a query is the summary of the calories of
     * the elements queried
     */

    public interface ObjectWithCalories {
        Long getCalories();
    }

    @Value
    public static class Food implements ObjectWithCalories{
        String name;
        Long caloresPer100g;
        Long weight;
        public Long getCalories() {
            return caloresPer100g * weight;
        }
    }

    @Value
    public static class Dish implements ObjectWithCalories{
        String name;
        List<Food> foodList;
        public void addIngredient(Food food) {
            this.foodList.add(food);
        }
        // Returns the calories of the dish as the sum of calories of each Food
        public Long getCalories() {
            return this.foodList.stream().collect(Collectors.summarizingLong(Food::getCalories)).getSum();
        }
    }

    public static class Menu implements ObjectWithCalories{
        List<ObjectWithCalories> meals;
        Long totalCalories = 0L;
        public Menu(List<ObjectWithCalories> meals) {
            this.meals = meals;
        }
        public Long getCalories(){
            for (int i = 0; i < meals.size(); i++) {
                totalCalories += meals.get(i).getCalories();
            }
            return totalCalories;
        }
    }

    @Test
    public void testCalories() {
        Food potato = new Food("POTATO", 80L, 300L);
        Food bread = new Food("BREAD", 320L, 100L);
        Food tomato = new Food("TOMATO", 85L, 50L);
        Food burger = new Food("BURGER", 340L, 120L);
        Food lettuce = new Food("LETTUCE", 16L, 120L);
        Food apple = new Food("APPLE", 34L, 180L);
        Food ketchup = new Food("KETCHUP", 180L, 15L);
        Food beer = new Food("BEER", 80L, 330L);
        Dish completeBuger = new Dish("COMPLETE BURGER", List.of(potato, bread, burger));
        Dish greenSalad = new Dish("GREEN SALAD", List.of(lettuce, tomato));
        Menu menu = new Menu(List.of(greenSalad, completeBuger, ketchup, apple));

        log.info(menu.getCalories().toString());
    }
}
