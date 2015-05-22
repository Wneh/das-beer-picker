var async = require('async');
var voteModel = require('./../models/vote');

exports.vote = function(req,res){


	var userName = req.body.userName;

	/*
	 * Each named form input is stored as a key value pair.
	 * If the user has checked a checkbox then the name of 
	 * that id will be present with a empty value.
	 * 
	 * The name field will also be present so just make
	 * and exception for that and treat all other
	 * keys like a beer that the user liked.
	 */
	async.forEach(Object.keys(req.body), function (item, callback){

		if(item === 'userName'){
			//Do nothing
			return callback();
		}

		/*
		 * Insert the vote into the database
		 * and hide any errors for the user.
		 */
		voteModel.insertVote(userName, item, function (err){
			process.nextTick(function() {   
				if(err){
					console.log(err);
				}
				callback(err);
    		});
		});
	},
	function (err) {
		if(err){
			console.log(err);
			res.status(500).send(err);
		} else {
			res.status(200).send("Vote success!");
		}
	});
};