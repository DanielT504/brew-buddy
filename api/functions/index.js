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
const { getUserById } = require("./users.js");
const { initializeApp } = require("firebase-admin/app");
const { getFirestore } = require("firebase-admin/firestore");
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
  var recipe = await db
    .collection("recipes")
    .doc(recipeId)
    .get()
    .then((doc) => {
      if (!doc) {
        throw new HttpsError(
          "failed-precondition",
          `No recipe with ID ${recipeId} found`
        );
      }
      return {
        id: doc.id,
        ...doc.data(),
      };
    });

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

exports.getRecipesMetadata = onCall(async ({ data }, context) => {
  var recipes = [];

  await db
    .collection("recipes")
    .get()
    .then((snapshot) => {
      snapshot.forEach((doc) => {
        const metadata = {
          id: doc.id,
          bannerUrl: doc.data().bannerUrl,
          title: doc.data().title,
        };
        recipes.push(metadata);
      });
    });

  return recipes;
});

exports.createRecipe = onRequest(async ({ body }, response) => {
  console.log(body);
  const { recipes, users } = body;
  recipes.forEach((recipe) => {
    db.collection("recipes").doc().set(recipe);
  });

  users.forEach((user) => {
    db.collection("users").doc(user["uid"]).set(user);
  });
  // const res = await db.collection("recipes").doc().set(body);
  response.status(200).send();
});

const functions = require('firebase-functions');
const admin = require('firebase-admin');

exports.notifyUserOnNewItem = functions.firestore
  .document('saved_stores/{userId}')
  .onUpdate((change, context) => {
    // Get the updated data from the snapshot
    const updatedData = change.after.data();
    const previousData = change.before.data();

    // Compare the previous items array with the updated items array
    const previousItems = previousData.items;
    const updatedItems = updatedData.items;

    if (!previousItems || !updatedItems) {
      // Items array does not exist, nothing to compare, exit
      return null;
    }

    if (previousItems.length === updatedItems.length) {
      // The array length is the same, no new item added, exit
      return null;
    }

    // Find the new item added to the array
    const newItem = updatedItems.filter(item => !previousItems.includes(item))[0];

    if (!newItem) {
      // New item not found, exit
      return null;
    }

    // Get the user ID from the context (document ID is the user ID)
    const userId = context.params.userId;

    // Create a notification payload
    const payload = {
      notification: {
        title: 'New Item Added!',
        body: `A new item "${newItem}" is added to your saved stores.`,
        click_action: 'MAIN_ACTIVITY' // Adjust this to the activity you want to open when the notification is clicked
      }
    };

    // Send the notification to the user
    return admin.messaging().sendToDevice(userId, payload);
  });

