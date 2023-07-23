const { HttpsError } = require("firebase-functions/v2/https");

exports.getMarketplaceItems = (db) => {
  return db
    .collection("marketplace")
    .get()
    .then((snapshot) => {
      return snapshot.docs.map((doc) => {
        return {
          id: doc.id,
          ...doc.data(),
        };
      });
    });
};