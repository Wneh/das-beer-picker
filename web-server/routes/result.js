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
			console.log(err);
			res.status(500).send(err);
		} else {
			res.send(result);
		}
	});
};

exports.resultPage = function(req,res){
	resultModel.haveData(function (err, data){
		if(data){
			res.send("No result yet, check back later");
		} else {
			res.sendFile(path.resolve(__dirname +'/../public/result.html'));
		}
	});
};