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
const { onDocumentCreated } = require("firebase-functions/v2/firestore");

const { initializeApp } = require("firebase-admin/app");
const { getFirestore } = require("firebase-admin/firestore");
initializeApp();
const db = getFirestore();

exports.getRecipesByAuthor = onRequest(async ({ query }, response) => {
  // Get all recipes from specified author ID.
  var recipes = [];
  const { authorId } = query;
  if (authorId) {
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
  }
  response.send(recipes);
});

exports.createRecipe = onRequest(async ({ query, body }, response) => {
  var recipes = [];
  const { authorId } = query;

  if (authorId) {
    await db
      .collection("recipes")
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
  }
  response.send(recipes);
});

// exports.getRecipeById = onRequest(async (request, response) => {
//   const { recipeId } = request.query;
//   console.log(request);
//   if (recipeId) {
//     db.collection("recipes")
//       .doc(recipeId)
//       .get()
//       .then((doc) => {
//         if (!doc) {
//           response.status(404).send({});
//           return;
//         }
//         const data = {
//           id: doc.id,
//           author: doc.data().author,
//           ingredients: doc.data().ingredients,
//           steps: doc.data().steps,
//           bannerUrl: doc.data().bannerUrl,
//           description: doc.data().description,
//           title: doc.data().title,
//         };
//         response.status(200).json({ data });
//       });
//   }
// });

exports.getRecipeById = onCall((data, context) => {
  console.log("GET RECIPE BY ID");
  const { recipeId } = data;
  log("getRecipeById Request: ", data);
  console.log("getRecipeById Request: ", data);

  if (!recipeId) {
    // Throwing an HttpsError so that the client gets the error details.
    throw new HttpsError("failed-precondition", "No recipe ID provided");
  }
  return db
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
        author: doc.data().author,
        ingredients: doc.data().ingredients,
        steps: doc.data().steps,
        bannerUrl: doc.data().bannerUrl,
        description: doc.data().description,
        title: doc.data().title,
      };
    });
});

exports.getRecipes = onRequest(async ({ query }, response) => {
  var recipes = [];

  await db
    .collection("recipes")
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

  response.send(recipes);
});

exports.getRecipesMetadata = onRequest(async ({ query }, response) => {
  var recipes = [];

  await db
    .collection("recipes")
    .get()
    .then((snapshot) => {
      snapshot.forEach((doc) => {
        const metadata = {
          id: doc.id,
          author: doc.data().author,
          tags: doc.data().tags,
          thumbnail: doc.data().thumbnail,
          title: doc.data().title,
        };
        recipes.push(metadata);
      });
    });

  response.status(200).json({ data: recipes });
});
