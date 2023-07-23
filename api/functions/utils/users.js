const { HttpsError } = require("firebase-functions/v2/https");
const { DEFAULT_AVATAR_URL, DEFAULT_BANNER_URL } = require("./constants.js");
const PLACEHOLDER_USER = {
  username: "unknown",
  bannerUrl: DEFAULT_BANNER_URL,
  avatarUrl: DEFAULT_AVATAR_URL,
  email: "unknownuser@email.com",
};

exports.getUserById = (userId, db) => {
  if (!userId) {
    throw new HttpsError("failed-precondition", "No user ID provided");
  }
  return db
    .collection("users")
    .doc(userId)
    .get()
    .then((doc) => {
      if (!doc) {
        throw new HttpsError(
          "failed-precondition",
          `No user with ID ${userId} found`
        );
      }
      return {
        ...PLACEHOLDER_USER,
        id: doc.id,
        ...doc.data(),
      };
    });
};

exports.updatePinnedRecipes = (userId, recipes, db) => {
  if (!userId) {
    throw new HttpsError(
      "failed-precondition",
      "updatePinnedRecipes: No user ID provided"
    );
  }
  return db.collection("users").doc(userId).update({ pinnedRecipes: recipes });
};

exports.getUserPreferences = (userId, db) => {
  if (!userId) {
    throw new HttpsError(
      "failed-precondition",
      "getUserPreferences: No user ID provided"
    );
  }
  return db
    .collection("user_preferences")
    .doc(userId)
    .get()
    .then((doc) => {
      if (!doc) {
        throw new HttpsError(
          "failed-precondition",
          `[getUserPreferences]: No user with ID ${userId} found`
        );
      }
      return {
        id: doc.id,
        ...doc.data(),
      };
    });
};
