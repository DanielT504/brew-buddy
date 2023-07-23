exports.calculateSimilarityScore = (recipe, userPreferences) => {
    let score = 0
    for (const tag of recipe.tags) {
        if(userPreferences[tag] == true) {
            score += 1
        }
    }
    return score
}
