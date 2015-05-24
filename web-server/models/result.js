var config = require('./../config/config');
var pg = require('pg');

/*
 * Returns a list of all beer in the database
 */
exports.getResultByType = function(type,callback){
	pg.connect(config.DB_URL, function (err, client, done) {
		if(err){
			console.log("Error when connecting to the database");
			console.log(err);
			return callback(err, null);
		}
		
		//Run the select query
		client.query('SELECT x,y,z FROM results WHERE type = $1',[type], function (err, result) {
			done();
			if(err){
				console.log("Error while running select query");
				console.log(err);
				return callback(err, null);
			}
			callback(null, result.rows);
		});
	});
};

/*
 *	Returns callback(null, true) if thers is atleast
 * 	1 row in the reuslts table
 */
exports.haveData = function(callback){
	pg.connect(config.DB_URL, function (err, client, done) {
		if(err){
			console.log("Error when connecting to the database");
			console.log(err);
			return callback(err, null);
		}
		
		//Run the select query
		client.query('select * from results limit 1', function (err, result) {
			done();
			if(err){
				console.log("Error while running select query");
				console.log(err);
				return callback(null, false);
			}
			if(result.rows !== undefined && result.rows.length > 0){
				callback(null, true);
			} else {
				callback(null,false);
			}
		});
	});
};
