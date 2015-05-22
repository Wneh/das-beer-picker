var pg = require('pg');
var config = require('./../config/config');

/*
 * Inserts a beer vote into the votes table
 */
exports.insertVote = function(name, beerId, callback){
	pg.connect(config.DB_URL, function (err, client, done) {
		if(err){
			console.log("Error when connecting to the database");
			console.log(err);
			return callback(err, null);
		}

		client.query('INSERT into votes (beer_id, name) VALUES($1,$2)',[beerId, name], function (err, result){
			done();
			if(err){
				console.log("Error while inserting vote into database");
				console.log(err);
			} else {
				console.log("Vote inserted: " + "["+beerId+","+name+"]");
			}

			process.nextTick(function() {
				callback(err);
			});
		});
	});
};