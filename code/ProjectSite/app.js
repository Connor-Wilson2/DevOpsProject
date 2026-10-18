//setup express to handle routing and rendering
var express = require('express');
var app = express();
app.set('view engine', 'ejs');

var features = {
    collection: {
        title: 'Collection catalog',
        summary: 'Keep a central catalog of the Warhammer 40,000 miniatures you own.',
        details: [
            'Create a record for each unit in your collection.',
            'Update a unit when your collection changes.',
            'Keep collection information together for easier army planning.'
        ]
    },
    factions: {
        title: 'Factions and units',
        summary: 'Organize collected units by faction so your armies are easy to browse.',
        details: [
            'Record the faction associated with each unit.',
            'Group units into faction-based collections.',
            'Review the units available for a selected faction.'
        ]
    },
    models: {
        title: 'Models and wargear',
        summary: 'Track unit sizes and the equipment options represented by individual models.',
        details: [
            'Store the number of models in each unit.',
            'Record wargear options for individual models.',
            'Review available equipment when planning a unit configuration.'
        ]
    },
    reports: {
        title: 'Collection reports',
        summary: 'See your collection grouped by faction and review available unit configurations.',
        details: [
            'Generate a view of units grouped by faction.',
            'See the configurations supported by models and wargear you have recorded.',
            'Use the report as a reference when preparing an army list.'
        ]
    },
    search: {
        title: 'Search and filtering',
        summary: 'Find relevant units in a growing collection without browsing every entry.',
        details: [
            'Search collection records by their unit information.',
            'Filter the collection to focus on a faction or subset of units.',
            'Use focused results to find units for army planning.'
        ]
    },
    data: {
        title: 'Data validation and storage',
        summary: 'Keep collection records consistent and available between sessions.',
        details: [
            'Validate collection information when it is entered or updated.',
            'Store collection data in a database.',
            'Load saved records again so the collection can be maintained over time.'
        ]
    }
};

//setup bodyparser to extra data from form posts
var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true })); 

//setup static public directory (to link images from view pages)
app.use(express.static('public'))

// route to index page
app.get('/', function(req, res) {
    res.render('page/index');
});

app.get('/documentation', function(req, res) {
    res.render('page/Documentaion', { features: features });
});

app.get('/documentation/quickstart', function(req, res) {
    res.render('page/Quickstart');
});

app.get('/documentation/features/:feature', function(req, res) {
    if (!Object.prototype.hasOwnProperty.call(features, req.params.feature)) {
        return res.status(404).render('page/not-found');
    }
    var feature = features[req.params.feature];
    res.render('page/Feature', { feature: feature });
});

// route to api call for checking a phone number
app.post('/api/check_phone_number', (req, res) => {
	var number = req.body.number
	if (isNaN(number)) {
		res.render('page/fail');
	} else if (number > 999999 && number < 10000000) {
		res.render('page/success');
	} else if (number > 999999999 && number < 10000000000) {
		res.render('page/success');
	} else { 
		res.render('page/fail');
	}
});

// route to tests page
app.get('/tests', function(req, res) {
	res.render('page/tests');
});

//start the web app
app.listen(8080, function () {
	console.log('App listening on port 8080!');
});
