var async = require('async');

exports.vote = function(req,res){
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
    	console.log(item); // print the key
    	callback(); // tell async that the iterator has completed

	},
	function (err) {
    	console.log('iterating done');
    	res.json(req.body);
	});
};