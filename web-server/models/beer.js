var config = require('./../config/config');
var pg = require('pg');

/*
 * Returns a list of all beer in the database
 */
exports.getAllBeer = function(callback){
	pg.connect(config.DB_URL, function (err, client, done) {
		if(err){
			console.log("Error when connecting to the database");
			console.log(err);
			return callback(err, null);
		}
		
		//Run the select query
		client.query('SELECT * FROM beer', function (err, result) {
			if(err){
				console.log("Error while running select query");
				console.log(err);
				return callback(err, null);
			}

			callback(null, result.rows);
		});
	});
};
