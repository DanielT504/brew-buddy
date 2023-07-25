const { HttpsError } = require("firebase-functions/v2/https");

const extractMetadata = (doc) => ({
  id: doc.id,
  imageUrl: doc.data().imageUrl,
  title: doc.data().title,
  city: doc.data().city,
  province: doc.data().province,
  price: doc.data().price,
  authorId: doc.data().authorId,
  tags: doc.data().tags,
});

exports.getMarketplaceItemsMetadataByQuery = (keywords, filters, db) => {
  let queryRef = db.collection("marketplace");
  console.log("keywords: ", keywords);
  if (keywords.length > 0) {
    queryRef = queryRef.where("keywords", "array-contains-any", keywords);
  }

  for (const [key, value] of Object.entries(filters)) {
    // if filter is not true, then don't need to filter it
    if (value) {
      queryRef = queryRef.where(key, "==", value);
    }
  }

  return queryRef.get().then((snapshot) => {
    return snapshot.docs.map((doc) => extractMetadata(doc));
  });
};

exports.getListingByAuthorId = async (userId, db) => {
  // Get all recipes from specified author ID.
  var items = [];

  if (!userId) {
    // Throwing an HttpsError so that the client gets the error details.
    throw new HttpsError("failed-precondition", "No user ID provided");
  }

  const author = await getUserById(userId);
  await db
    .collection("marketplace")
    .where("authorId", "==", userId)
    .get()
    .then((snapshot) => {
      snapshot.forEach((doc) => {
        const data = extractMetadata(doc);
        items.push({ ...data, author });
      });
    });
  return items;
};
exports.getMarketplaceItemsMetadata = (db) => {
  return db
    .collection("marketplace")
    .get()
    .then((snapshot) => {
      return snapshot.docs.map((doc) => extractMetadata(doc));
    });
};

exports.getMarketplaceItemById = (itemId, db) => {
  if (!itemId) {
    throw new HttpsError(
      "failed-precondition",
      "getMarketplaceItemById: No item ID provided"
    );
  }
  return db
    .collection("marketplace")
    .doc(itemId)
    .get()
    .then((doc) => {
      if (!doc) {
        throw new HttpsError(
          "failed-precondition",
          `No item with ID ${itemId} found`
        );
      }
      return {
        id: doc.id,
        ...doc.data(),
      };
    });
};
