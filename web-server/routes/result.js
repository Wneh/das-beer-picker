var resultModel = require('./../models/result');
var async = require('async');
var path = require('path');

/*
 * Maps the result of the datapoint into one object
 * with the format:
 *
 *		{
 *			'all': [{1,2,3},...],
 *			'top': ...,
 *			'div': ...
 * 		}
 */
exports.getResult = function(req,res){
	var resultTypes = ['all','top','div'];

	var result = {};
	async.each(resultTypes,function (type, callback){
		resultModel.getResultByType(type, function (err, rows){
			if(err){
				return callback(err);
			}
			result[type] = rows;
			callback(null);
		});
	},
	function (err){
		if(err){
			res.send({'all':[],'top':[],'div':[]});
		} else {
			res.send(result);
		}
	});
};

/*
 * Serve the result.html file if there
 * is result data in the database, otherwise
 * send a msg to come back later.
 */
exports.resultPage = function(req,res){
	resultModel.haveData(function (err, data){
		if(data === true){
			res.sendFile(path.resolve(__dirname +'/../public/result.html'));
		} else {
			res.render("noResult");
		}
	});
};