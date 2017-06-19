package com.example.s164403.foodstr.database;

import com.example.s164403.foodstr.database.Model.Ingredient;
import com.example.s164403.foodstr.database.Model.PrimaryUnit;
import com.example.s164403.foodstr.database.Model.Recipe;
import com.example.s164403.foodstr.database.Model.RecipeIngredientRelation;
import com.example.s164403.foodstr.database.Model.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Morbi95 on 14-Jun-17.
 */

public class DummyData {
    public static List<Ingredient> dummyIngredients(){
        ArrayList<Ingredient> res = new ArrayList<>();
        res.add(new Ingredient("Onion", PrimaryUnit.amt));
        res.add(new Ingredient("Garlic Bulb", PrimaryUnit.amt));
        res.add(new Ingredient("Chicken Leg", PrimaryUnit.amt));
        res.add(new Ingredient("Olive Oil", PrimaryUnit.tbsp));
        res.add(new Ingredient("Parsnip", PrimaryUnit.amt));
        res.add(new Ingredient("Carrot", PrimaryUnit.amt));
        res.add(new Ingredient("Sweet Potato", PrimaryUnit.amt));
        res.add(new Ingredient("Chicken Stock", PrimaryUnit.ml));
        res.add(new Ingredient("Pasta", PrimaryUnit.gram));
        res.add(new Ingredient("parmasan Cheese", PrimaryUnit.gram));
        res.add(new Ingredient("spaghetti", PrimaryUnit.gram));
        res.add(new Ingredient("Sugar", PrimaryUnit.tsp));

        return res;
    }

    public static List<Task> dummyRecipeTasks() {
        List<Task> dummyTasks = new LinkedList<>();

        //public Task(long id,
        //          long recipeId,
        //          String name,
        //          int duration,
        //          boolean requireAttention,
        //          boolean cariesOnHot,
        //          List<Long> preRequisiteIds)
        dummyTasks.add(new Task(1, 1, "Prepare ingredients", 10, true, false, new ArrayList<Long>(),"Finely chop onion, carrots, and celery sticks. Chop the pancetta into cubes, and chop the chicken liver while removing fat and sinew.");
        dummyTasks.add(new Task(2, 1, "Vegetables", 12, false, true, Arrays.asList((long)1),"Heat 1 1/2 tbsp oil in a large pan or flameproof casserole dish over a low-medium heat. Add the onion, celery and carrots with a pinch of salt. Cook for 10 mins, stirring occasionally, until softened but not coloured. Transfer to a plate using a slotted spoon.");
        dummyTasks.add(new Task(3, 1, "Meat", 10, false, true, Arrays.asList((long)1),"Pour the remaining oil into the pan, increase the heat and tip in the pancetta. Cook for 3-4 mins until golden. Add the mince and chicken livers, and cook for a further 5 mins until browned, breaking down the mince with the back of a wooden spoon.");
        dummyTasks.add(new Task(4, 1, "Bolognese", 10, false, true, Arrays.asList((long)2,(long)3), "Return the vegetables to the pan and add the bay leaf and tomato purée. Cook for a 1 min more and mix well. Pour in the wine and reduce by half. Add the stock and passata with some seasoning and bring to the boil. Reduce the heat to medium and let the sauce bubble away for 35-40 mins, stirring occasionally, until reduced by half and you are left with a thick ragu.");
        dummyTasks.add(new Task(5, 1, "Spaghetti", 15, false, true, new ArrayList<Long>(), "Bring a large saucepan of salted water to the boil 15 mins before the sauce is ready. Drop in the pasta, cook following pack instructions until al dente, then drain.";
        dummyTasks.add(new Task(6, 1, "Mix", 5, true, true, Arrays.asList((long)4,(long)5), "To finish the sauce, stir in the milk and season to taste. Tip the pasta onto a plate and top with the Bolognese. Serve with the Parmesan.";

        dummyTasks.add(new Task(7, 2, "Prepare ingredients", 10, true, false, new ArrayList<Long>(), "Chop onion, separate the garlic into cloves, chop sweet potatoes, parsnips and carrots into chunks"));


        return dummyTasks;
    }

    public static List<RecipeIngredientRelation> dummyRI(){
        Ingredient oil = new Ingredient("oil", PrimaryUnit.tbsp);
        Ingredient onion = new Ingredient("onion", PrimaryUnit.amt);
        Ingredient celeryStick = new Ingredient("Celery Sticks", PrimaryUnit.tbsp);
        Ingredient carrots = new Ingredient("carrots", PrimaryUnit.amt);
        Ingredient pancetta = new Ingredient("pancetta", PrimaryUnit.gram);
        Ingredient beefMince = new Ingredient("Beef Mince", PrimaryUnit.gram);
        Ingredient chickenLiver = new Ingredient("Chicken Liver", PrimaryUnit.gram);
        Ingredient bayLeaves = new Ingredient("Bay Leaves", PrimaryUnit.amt);
        Ingredient tomatoPuree = new Ingredient("Tomato Puree", PrimaryUnit.tbsp);
        Ingredient whiteWine = new Ingredient("White Wine", PrimaryUnit.ml);
        Ingredient chickenStock = new Ingredient("Chicken Stock", PrimaryUnit.ml);
        Ingredient passata = new Ingredient("Passata", PrimaryUnit.ml);
        Ingredient spaghetti = new Ingredient("Spaghetti", PrimaryUnit.gram);
        Ingredient milk = new Ingredient("Milk", PrimaryUnit.ml);
        Ingredient parmesan = new Ingredient("Parmesan", PrimaryUnit.gram);
        Ingredient garlicBulb = new Ingredient("Garlic Bubl", PrimaryUnit.amt);
        Ingredient chicken = new Ingredient("Free-Range Chicken", PrimaryUnit.gram);
        Ingredient oliveOil = new Ingredient("Olive Oil", PrimaryUnit.tbsp);
        Ingredient rosemarySprings = new Ingredient("rosemarySprings", PrimaryUnit.pack);
        Ingredient parsnip = new Ingredient("parsnip", PrimaryUnit.amt);
        Ingredient sweetPotato = new Ingredient("Sweet Potato", PrimaryUnit.amt);
        Ingredient plainFlour = new Ingredient("Plain Flour", PrimaryUnit.gram);
        Ingredient savoyCabbage = new Ingredient("Savoy Cabbage", PrimaryUnit.amt);
        Ingredient potato = new Ingredient("Potato", PrimaryUnit.gram);
        Ingredient chocolateBourbonBiscuits = new Ingredient("Chocolate Bourbon Biscuits", PrimaryUnit.gram);
        Ingredient butter = new Ingredient("Butter", PrimaryUnit.gram);
        Ingredient cream = new Ingredient("Cream", PrimaryUnit.gram);
        Ingredient sugar = new Ingredient("Sugar", PrimaryUnit.gram);
        Ingredient cocoaPowder = new Ingredient("Cocoa Powder", PrimaryUnit.tbsp);
        Ingredient vanilaPod = new Ingredient("Vanilla Pod", PrimaryUnit.amt);
        Ingredient strongBlackCoffee = new Ingredient("Strong Black Coffee", PrimaryUnit.ml);
        Ingredient eggs = new Ingredient("Eggs", PrimaryUnit.amt);
        Ingredient sourCream = new Ingredient("Sour Cream", PrimaryUnit.ml);
        Ingredient chocholate = new Ingredient("Chocolate", PrimaryUnit.gram);
        Ingredient doubleCream = new Ingredient("Double Cream", PrimaryUnit.ml);
        Ingredient fruitCord = new Ingredient("Passion Fruit Cord", PrimaryUnit.gram);
        Ingredient cremeEggs = new Ingredient("Creme Eggs", PrimaryUnit.tbsp);
        Ingredient starAnise = new Ingredient("Star Anise", PrimaryUnit.amt);
        Ingredient juniperBerries = new Ingredient("Juniper Berries", PrimaryUnit.amt);
        Ingredient driedThyme = new Ingredient("Dried Thyme", PrimaryUnit.tsp);
        Ingredient rapeseedOil = new Ingredient("oil", PrimaryUnit.tbsp);
        Ingredient vensionBoneRack = new Ingredient("Bone Racks of Vension", PrimaryUnit.amt);
        Ingredient bannanaShallot = new Ingredient("Banana Shallot", PrimaryUnit.amt);
        Ingredient sloeGin = new Ingredient("Sloe Gin", PrimaryUnit.ml);
        Ingredient blackberry = new Ingredient("Blackberries", PrimaryUnit.gram);

        Ingredient sausages = new Ingredient("Sausages", PrimaryUnit.amt);
        Ingredient ketchup = new Ingredient("Ketchup", PrimaryUnit.ml);


        //https://www.bbcgoodfood.com/recipes/classic-spaghetti-bolognese
        Recipe classicSpeghettiBolognese = new Recipe("Classic spaghetti Bolognese","https://www.bbcgoodfood.com/sites/default/files/styles/recipe/public/recipe/recipe-image/2016/07/classic-spaghetti-bolognese.jpg?itok=-2HhQATY", "This classic recipe stays as true to the Italian way as possible - no garlic, white wine instead of red and a little milk splashed in at the end. A great source of iron and folate");
        HashMap<Ingredient, Double> classicSpeghettiBologneseIngredients = new HashMap<>();
        classicSpeghettiBologneseIngredients.put(oil, 0.5);
        classicSpeghettiBologneseIngredients.put(onion, 0.25);
        classicSpeghettiBologneseIngredients.put(celeryStick, 0.5);
        classicSpeghettiBologneseIngredients.put(carrots, 0.5);
        classicSpeghettiBologneseIngredients.put(pancetta, 12.5);
        classicSpeghettiBologneseIngredients.put(beefMince, 100.0);
        classicSpeghettiBologneseIngredients.put(chickenLiver, 37.5);
        classicSpeghettiBologneseIngredients.put(bayLeaves, 0.25);
        classicSpeghettiBologneseIngredients.put(tomatoPuree, 1.0);
        classicSpeghettiBologneseIngredients.put(whiteWine, 37.5);
        classicSpeghettiBologneseIngredients.put(chickenStock, 125.0);
        classicSpeghettiBologneseIngredients.put(passata, 75.0);
        classicSpeghettiBologneseIngredients.put(spaghetti, 125.0);
        classicSpeghettiBologneseIngredients.put(milk, 12.5);
        classicSpeghettiBologneseIngredients.put(parmesan, 5.0);
        RecipeIngredientRelation ri1 = new RecipeIngredientRelation(classicSpeghettiBolognese,
                classicSpeghettiBologneseIngredients);

        Recipe roastChicken = new Recipe("Roast chicken with garlic & rosemary root veg", "https://www.bbcgoodfood.com/sites/default/files/styles/recipe/public/recipe/recipe-image/2017/06/roast-chicken-with-rosemary-and-garlic-root-veg.jpg?itok=D3VZPh-J", "Get four of your five-a-day in a comforting roast chicken dinner served with Savoy cabbage. It’s easy to prepare and makes an ideal weekend feast" );
        HashMap<Ingredient, Double> roastChickenIngredients = new HashMap<>();
        roastChickenIngredients.put(onion, 0.25);
        roastChickenIngredients.put(garlicBulb, 0.25);
        roastChickenIngredients.put(chicken, 375.0);
        roastChickenIngredients.put(oliveOil, 1.25);
        roastChickenIngredients.put(rosemarySprings, 0.25);
        roastChickenIngredients.put(parsnip, 1.0);
        roastChickenIngredients.put(carrots, 1.25);
        roastChickenIngredients.put(sweetPotato, 0.5);
        roastChickenIngredients.put(plainFlour, 0.25);
        roastChickenIngredients.put(chickenStock, 125.0);
        roastChickenIngredients.put(savoyCabbage, 0.125);
        roastChickenIngredients.put(potato, 2.0);
        RecipeIngredientRelation ri2 = new RecipeIngredientRelation(roastChicken,
                roastChickenIngredients);

        //https://www.bbcgoodfood.com/recipes/double-chocolate-easter-egg-cheesecake
        Recipe easterEggCake = new Recipe("Double chocolate Easter egg cheesecake", "https://www.bbcgoodfood.com/sites/default/files/styles/recipe/public/recipe/recipe-image/2016/03/creme-egg-cheesecake.jpg?itok=mS2YN2Y7", "What's better than a chocolate cheesecake? A double chocolate Easter egg cheesecake with a crunchy bourbon biscuit base and topped with creme eggs");
        HashMap<Ingredient, Double> easterEggCakeIngredients = new HashMap<>();
        easterEggCakeIngredients.put(chocolateBourbonBiscuits, 25.0);
        easterEggCakeIngredients.put(butter, 10.0);
        easterEggCakeIngredients.put(cream, 84.0);
        easterEggCakeIngredients.put(sugar, 20.0);
        easterEggCakeIngredients.put(cocoaPowder, 0.4);
        easterEggCakeIngredients.put(vanilaPod, 0.1);
        easterEggCakeIngredients.put(strongBlackCoffee, 0.3);
        easterEggCakeIngredients.put(eggs, 0.3);
        easterEggCakeIngredients.put(sourCream, 30.0);
        easterEggCakeIngredients.put(chocholate, 2.5);
        easterEggCakeIngredients.put(doubleCream, 20.0);
        easterEggCakeIngredients.put(fruitCord, 10.0);
        easterEggCakeIngredients.put(cremeEggs, 0.8);

        RecipeIngredientRelation ri3 =  new RecipeIngredientRelation(easterEggCake, easterEggCakeIngredients);

        //https://www.bbcgoodfood.com/recipes/rack-venison-roasted-carrots-forager-sauce
        Recipe rackOfVension = new Recipe("Rack of venison, roasted carrots & forager sauce", "https://www.bbcgoodfood.com/sites/default/files/styles/recipe/public/recipe/recipe-image/2016/08/rack-of-venison-roasted-carrots-and-forager-sauce.jpg?itok=CSepkVnd", "This dinner party roast has a touch of autumn about it with the star anise, roasted carrots and blackberries in the sauce");
        HashMap<Ingredient, Double> rackOfVensionIngredient = new HashMap<>();
        rackOfVensionIngredient.put(carrots, 1.34);
        rackOfVensionIngredient.put(starAnise, 0.67);
        rackOfVensionIngredient.put(butter, 0.16);
        rackOfVensionIngredient.put(juniperBerries, 2.0);
        rackOfVensionIngredient.put(driedThyme, 0.16);
        rackOfVensionIngredient.put(rapeseedOil, 0.75);
        rackOfVensionIngredient.put(vensionBoneRack, 2.0);
        rackOfVensionIngredient.put(bannanaShallot, 0.16);
        rackOfVensionIngredient.put(chickenStock, 166.67);
        rackOfVensionIngredient.put(sloeGin, 0.83);
        rackOfVensionIngredient.put(blackberry, 25.0);
        RecipeIngredientRelation ri4 = new RecipeIngredientRelation(rackOfVension, rackOfVensionIngredient);

        Recipe noddlesAndSausages = new Recipe("Noddles with danish sausages", "https://www.maduniverset.dk/images/ABCD0003.JPG", "Easy to make. Just cock the noddles and fry the sausages. Then mix and eat with ketchup" );
        HashMap<Ingredient, Double> noddlesAndSausagesIngredients = new HashMap<>();
        noddlesAndSausagesIngredients.put(sausages, 2.0);
        noddlesAndSausagesIngredients.put(spaghetti, 200.0);
        noddlesAndSausagesIngredients.put(ketchup, 10.0);
        RecipeIngredientRelation ri5 = new RecipeIngredientRelation(noddlesAndSausages, noddlesAndSausagesIngredients);

        List<RecipeIngredientRelation> ri = new ArrayList<>();
        ri.add(ri1);
        ri.add(ri2);
        ri.add(ri3);
        ri.add(ri4);
        ri.add(ri5);
        return ri;

    }


}
