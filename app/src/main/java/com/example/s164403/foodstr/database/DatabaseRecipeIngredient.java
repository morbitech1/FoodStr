package com.example.s164403.foodstr.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.s164403.foodstr.database.Model.Ingredient;
import com.example.s164403.foodstr.database.Model.Recipe;
import com.example.s164403.foodstr.database.Model.RecipeIngredientRelation;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by s164403 on 6/14/2017.
 */

public class DatabaseRecipeIngredient extends DatabaseTableDefinition{
    public static final String NAME = "RecipeIngredientRelationship";
    public static final String COL1 = "recipe_id";
    public static final String COL2 = "ingredient_id";
    public static final String COL3 = "factor";

    public DatabaseRecipeIngredient(){}
    public DatabaseRecipeIngredient(SQLiteDatabase db) {
        this.db=db;
    }

    @Override
    public String getCreateQuery() {
        return  "CREATE TABLE IF NOT EXISTS " + NAME + " (" +
                COL1 + " INTEGER NOT NULL REFERENCES " + DatabaseRecipe.NAME + "("+DatabaseRecipe.COL1 + "),"+
                COL2 + " INTEGER NOT NULL REFERENCES " + DatabaseIngredient.NAME + "("+DatabaseIngredient.COL1 + "),"+
                COL3 + " INTEGER," +
                "PRIMARY KEY (" + COL1 + "," + COL2 + ")"+ ")";
    }

    @Override
    public String getDropQuery() {
        return "DROP TABLE IF EXISTS " + NAME;
    }


    public void addRelation(RecipeIngredientRelation rir){
        DatabaseRecipe dbRecipe = new DatabaseRecipe(db);
        DatabaseIngredient dbIngredient = new DatabaseIngredient(db);
        long recipeId = dbRecipe.getId(db, rir.recipe.name);
        if(recipeId < 0)
            recipeId = dbRecipe.addRecipe(rir.recipe);
        rir.recipe.id = recipeId;

        for(Ingredient ingredient : rir.ingredients.keySet()){
            dbIngredient.addIngredient(ingredient);
            if(!hasEntry(rir.recipe.id, ingredient.id)) {
                ContentValues cv = new ContentValues();
                cv.put(COL1, rir.recipe.id);
                cv.put(COL2, ingredient.id);
                cv.put(COL3, rir.ingredients.get(ingredient));
                db.insert(NAME, null, cv);
            }
        }
    }

    public Map<Recipe, Double> searchRecipesByScore(int numOfPeopleInt, int limit, String filter) {
        final String finalColName = "RepScore";
        Double numOfPeople = (double)numOfPeopleInt;
        if (filter == null) {
            filter = "";
        }
        boolean filtered = !"".equals(filter);
        Map<Recipe, Double> res = new LinkedHashMap<>();
        String query = " SELECT * FROM " +
                "(" +
                " SELECT "+ DatabaseRecipeIngredient.COL1 +", AVG(Score) as " + finalColName + " FROM" +
                "(" +
                " SELECT " + DatabaseRecipeIngredient.COL1 + ", case " +
                "when Score > 100 then 100 " +
                "when Score < 0 then 100 " +
                "else Score " +
                "end as Score FROM (" +
                " SELECT "+ DatabaseRecipeIngredient.NAME +"."+ DatabaseRecipeIngredient.COL1 +"," +
                DatabaseRecipeIngredient.NAME +"."+ DatabaseRecipeIngredient.COL2 +"," +
                " ifnull(100.0*("+ LocalDatabaseFridge.NAME + "." + LocalDatabaseFridge.COL2 +")/(" + DatabaseRecipeIngredient.NAME +"."+ DatabaseRecipeIngredient.COL3 + "*?), 0) as Score" +
                " FROM " + DatabaseRecipeIngredient.NAME +
                " LEFT OUTER JOIN "+ LocalDatabaseFridge.NAME +" on " + LocalDatabaseFridge.NAME + "." + LocalDatabaseFridge.COL1 + " = " + DatabaseRecipeIngredient.NAME + "." + DatabaseRecipeIngredient.COL2 +
                ")" +
                ")" +
                " GROUP by " + DatabaseRecipeIngredient.COL1 + " ORDER BY " + finalColName + " DESC LIMIT ?" +
                ") JOIN " + DatabaseRecipe.NAME + " on "+ DatabaseRecipe.NAME +"."+ DatabaseRecipe.COL1 +" = " + DatabaseRecipeIngredient.COL1 +
                (filtered ? " WHERE " + DatabaseRecipe.NAME + "." + DatabaseRecipe.COL2 + " LIKE ?" : "");

        String[] params = filtered ? new String[] {numOfPeople.toString(), Integer.toString(limit), "%" + filter + "%" } : new String[] {numOfPeople.toString(), Integer.toString(limit) } ;

        Cursor cursor = db.rawQuery(query, params);
        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe(cursor);
                res.put(recipe, cursor.getDouble(cursor.getColumnIndex(finalColName)));
            } while(cursor.moveToNext());
        }
        cursor.close();
        return res;
    }

    public RecipeIngredientRelation getRecipeIngredientRelation(long recipeId){
        Cursor cursor = db.rawQuery("SELECT * FROM " + NAME + " WHERE "+ COL1 + "=" +recipeId, null);
        Recipe recipe = new DatabaseRecipe(db).getRecipe(recipeId);
        DatabaseIngredient ingredientDB = new DatabaseIngredient(db);
        Map<Ingredient, Double> ingredientDoubleMap = new HashMap<>();
        if(cursor.moveToFirst()){
            do {
                long ingredientId = cursor.getLong(cursor.getColumnIndex(COL2));
                double factor = cursor.getDouble(cursor.getColumnIndex(COL3));
                ingredientDoubleMap.put(ingredientDB.getIngredient(ingredientId), factor);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return new RecipeIngredientRelation(recipe, ingredientDoubleMap);
    }

    public boolean hasEntry(long recipeID, long ingredientID){
        Cursor cursor = db.rawQuery("SELECT * FROM "+ NAME + " WHERE " + COL1 + "="+recipeID + " AND " + COL2 + "="+ingredientID, null);
        boolean check = cursor.moveToFirst();
        cursor.close();
        return check;
    }
}
