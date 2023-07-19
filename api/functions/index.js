/**
 * Import function triggers from their respective submodules:
 *
 * const {onCall} = require("firebase-functions/v2/https");
 * const {onDocumentWritten} = require("firebase-functions/v2/firestore");
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

// The Cloud Functions for Firebase SDK to create Cloud Functions and triggers.
// const { logger } = require("firebase-functions");
const {
  log,
  info,
  debug,
  warn,
  error,
  write,
} = require("firebase-functions/logger");

const {
  onCall,
  HttpsError,
  onRequest,
} = require("firebase-functions/v2/https");
const { initializeApp } = require("firebase-admin/app");
const { getFirestore } = require("firebase-admin/firestore");

const {
  getRecipeById,
  getRecipes,
  getRecipesMetadata,
  getRecipeMetadataById,
} = require("./utils/recipes.js");
const { getUserById, updatePinnedRecipes } = require("./utils/users.js");

initializeApp();
const db = getFirestore();

exports.getRecipesByAuthor = onCall(async ({ data }, context) => {
  // Get all recipes from specified author ID.
  var recipes = [];
  const { authorId } = data;

  if (!authorId) {
    // Throwing an HttpsError so that the client gets the error details.
    throw new HttpsError("failed-precondition", "No author ID provided");
  }
  await db
    .collection("recipes")
    .where("authorId", "==", authorId)
    .get()
    .then((snapshot) => {
      snapshot.forEach((doc) => {
        const data = {
          id: doc.id,
          author: doc.data().author,
          ingredients: doc.data().ingredients,
        };
        recipes.push(data);
      });
    });
  return recipes;
});

exports.getRecipeById = onCall(async ({ data }, context) => {
  console.log("GET RECIPE BY ID");
  const { recipeId } = data;
  log("getRecipeById Request: ", data);
  console.log("getRecipeById Request: ", data);

  if (!recipeId) {
    // Throwing an HttpsError so that the client gets the error details.
    throw new HttpsError("failed-precondition", "No recipe ID provided");
  }
  var recipe = await getRecipeById(recipeId, db);

  var author = null;
  try {
    author = await getUserById(recipe.authorId, db);
  } catch (e) {
    author = null;
  }

  console.log("Author of GetRecipeById: ", author);
  return {
    ...recipe,
    author,
  };
});

exports.getUserById = onCall(async ({ data }, context) => {
  const { userId } = data;
  log("getUserById Request: ", data);
  log("getUserById userId: ", userId);

  if (userId === undefined) {
    // Throwing an HttpsError so that the client gets the error details.
    throw new HttpsError("failed-precondition", "No user ID provided");
  }
  return await getUserById(userId, db);
});

exports.pinRecipe = onCall(async ({ data }, context) => {
  const { userId, recipeId } = data;
  log("pinRecipe Request: ", data);

  if (userId === undefined || userecipeIdrId === undefined) {
    // Throwing an HttpsError so that the client gets the error details.
    throw new HttpsError(
      "failed-precondition",
      "pinRecipe: No user or recipe ID provided"
    );
  }
  const user = await getUserById(userId, db);
  const pinnedRecipes = user.pinnedRecipes || [];
  if (pinnedRecipes.includes(recipeId)) {
    // the recipe is already pinned, so unpin it
    updatePinnedRecipes(
      userId,
      pinnedRecipes.filter((id) => id !== recipeId),
      db
    );
  } else {
    updatePinnedRecipes(userId, [...pinnedRecipes, recipeId], db);
  }
});

exports.getRecipesMetadata = onCall(async ({ data }, context) => {
  const metadatas = await getRecipesMetadata(db);

  return metadatas;
});

exports.getPopularRecipes = onCall(async ({ data }, context) => {
  const recipes = await getRecipesMetadata(db);

  const popularRecipes = recipes.sort((a, b) => a.likes > b.likes);
  console.log("Popular Recipes: ", popularRecipes);
  return popularRecipes.slice(0, 10);
});
