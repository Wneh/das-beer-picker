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

exports.getPrefsByName = function(name, callback){
	pg.connect(config.DB_URL, function (err, client, done) {
		if(err){
			console.log("Error when connecting to the database");
			console.log(err);
			return callback(err, null);
		}

		client.query("SELECT votes.name, avg(beska) as beska, avg(sotma) as sotma, avg(fyllighet) AS fyllighet FROM votes, beer WHERE beer.beer_id = votes.beer_id AND votes.name = $1 GROUP by votes.name",[name], function (err, result){
			done();
			if(err){
				console.log("Error while selecting from database");
				console.log(err);
			} else {
				//Do nothing
			}

			process.nextTick(function() {
				callback(err, result.rows[0]);
			});
		});
	});
};