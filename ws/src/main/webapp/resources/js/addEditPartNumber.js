jQuery(function($) {
	//typeahead.js
	//example taken from plugin's page at: https://twitter.github.io/typeahead.js/examples/
	var substringMatcher = function(strs) {
		return function findMatches(q, cb) {
			var matches, substringRegex;

			// an array that will be populated with substring matches
			matches = [];

			// regex used to determine if a string contains the substring `q`
			substrRegex = new RegExp(q, 'i');

			// iterate through the pool of strings and for any string that
			// contains the substring `q`, add it to the `matches` array
			$.each(strs, function(i, str) {
				if (substrRegex.test(str)) {
					// the typeahead jQuery plugin expects suggestions to a
					// JavaScript object, refer to typeahead docs for more info
					matches.push({
						value : str
					});
				}
			});

			cb(matches);
		}
	}

	
	$('input.typeahead').typeahead({
		hint : true,
		highlight : true,
		minLength : 1
	}, {
		name : 'states',
		displayKey : 'value',
		source : substringMatcher([ 'HAHA', 'HIHI' ])
	});
	

});