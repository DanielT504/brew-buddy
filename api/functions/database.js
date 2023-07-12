const mysql = require("mysql");

class DB {
  constructor(username, password) {
    const socket = "/cloudsql/brew-buddy-ece452:us-central1:main";
    this.conn = mysql.createConnection({
      socketPath: socket,
      user: username,
      password: password,
      database: "dev",
    });
  }

  getRecipeById = async () => {
    this.conn.connect();

    var res = {};
    this.conn.query(
      "SELECT * FROM recipe_metadata",
      (error, results, fields) => {
        if (error) throw error;
        console.log(results);
        res = results;
      }
    );

    this.conn.end();
    return res;
  };
}

module.exports = DB;
